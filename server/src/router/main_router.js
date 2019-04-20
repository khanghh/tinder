import express from 'express'
import bodyParser from 'body-parser'
import crypto from 'crypto'
import config from '../config'
import cryptoUtils from '../utils/cryptoUtils'
import createUserRepo from '../repository/UserRepository'
import createLogger from '../utils/createLogger'
import MailComposer from '../mail/MailComposer'

export default function(mysqlClient, mailTransporter) {
  const userRepo = createUserRepo(mysqlClient)
  const logger = createLogger('MainRouter')
  const router = express.Router()

  const asyncMiddleware = fn => (req, res) => {
    Promise.resolve(fn(req, res)).catch(err => logger.error(err.message))
  }

  function gen_nonce() {
    return (Math.random() * 1e9) >>> 0
  }

  router.get('/register', (req, res) => {
    const error = req.query.error || null
    if (!req.session.nonce) {
      req.session.nonce = gen_nonce()
    }
    const nonce = req.session.nonce
    res.render('register', { nonce, error })
  })

  router.post(
    '/register',
    bodyParser.urlencoded({ extended: false }),
    asyncMiddleware(async (req, res) => {
      const nonce = req.body.nonce
      if (!nonce || nonce != req.session.nonce) {
        res.redirect('/register?error=1')
      } else {
        const re_email = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
        const name = req.body.name
        const email = re_email.test(req.body.email) ? req.body.email : null
        const password = req.body.password
        const gender = req.body.gender

        if (email) {
          const exist_user = await userRepo.getUserByEmail(email)
          if (exist_user) {
            res.redirect('/register?error=2')
          } else if (!(gender === 'male' || gender === 'female')) {
            res.redirect('/register?error=3')
          } else {
            req.session.nonce = null
            const passwordSalt = crypto.randomBytes(16).toString('hex')
            const salt_round = 100
            const hashedPassword = crypto.pbkdf2Sync(password, passwordSalt, salt_round, 16, 'sha256').toString('hex')
            const activateToken = cryptoUtils.encrypt(email, config.cryptoKey)
            const mail = await MailComposer.compose_activate_user(
              'Tinder<bf3t02@gmail.com>',
              email,
              name,
              activateToken
            )
            await mailTransporter.sendMail(mail)
            const result = await userRepo.addUser(name, email, hashedPassword + '.' + passwordSalt, gender)
            if (result && result.insertId) {
              res.redirect('/login')
            } else {
              res.status(500).send({ message: 'Internal server error' })
            }
          }
        } else {
          res.redirect('/register?error=4')
        }
      }
    })
  )

  router.get(
    '/activate_user',
    asyncMiddleware(async (req, res) => {
      const token = req.query.token
      const email = cryptoUtils.decrypt(token, config.cryptoKey)
      const re_email = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
      if (re_email.test(email)) {
        const result = await userRepo.activateUser(email)
        if (result.changedRows) {
          return res.send({ message: 'Account activated.' })
        }
      }
      res.status(400).send({ message: 'Bad request.' })
    })
  )

  return router
}
