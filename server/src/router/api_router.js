import express from 'express'
import bodyParser from 'body-parser'
import cache from 'memory-cache'
import crypto from 'crypto'
import config from '../config'
import cryptoUtils from '../utils/cryptoUtils'
import createLogger from '../utils/createLogger'
import MailComposer from '../mail/MailComposer'
import createUserRepo from '../repository/UserRepository'
import createMessageRepo from '../repository/MessageRepositoy'

export default function(mysqlClient, mailTransporter) {
  const logger = createLogger('ApiRouter')
  const userRepo = createUserRepo(mysqlClient)
  const messageRepo = createMessageRepo(mysqlClient)
  const router = express.Router()

  const asyncMiddleware = fn => (req, res) => {
    Promise.resolve(fn(req, res)).catch(err => logger.error(err.message))
  }

  router.use(bodyParser.json())
  router.use(bodyParser.urlencoded({ extended: false }))

  router.get('/user_info', (req, res) => {
    res.set('Content-Type', 'application/json')
    const user_id = req.query.user_id
    userRepo.getUserByUserId(user_id).then(user => {
      res.send(JSON.stringify(user))
    })
  })

  router.get('/message', (req, res) => {
    res.set('Content-Type', 'application/json')
    const conversation_id = req.query.conversation_id
    messageRepo.getMessages(conversation_id).then(message => {
      res.send(JSON.stringify(message))
    })
  })

  router.get('/get_nonce', (req, res) => {
    const nonce = crypto.randomBytes(16).toString('hex')
    cache.put(`nonce_${req.connection.remoteAddress}`, nonce, 5 * 60 * 1000)
    res.set('Content-Type', 'application/json')
    res.send({ nonce })
  })

  router.post(
    '/register',
    asyncMiddleware(async (req, res) => {
      res.set('Content-Type', 'application/json')
      const nonce = req.body.nonce
      if (!nonce || nonce != cache.get(`nonce_${req.connection.remoteAddress}`)) {
        res.status(403)
        res.send({ message: 'Forbidden.' })
      } else {
        const re_email = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
        const name = req.body.name
        const email = re_email.test(req.body.email) ? req.body.email : null
        const password = req.body.password
        const gender = req.body.gender

        if (email) {
          const exist_user = await userRepo.getUserByEmail(email)
          if (exist_user) {
            res.status(409)
            res.send({ message: 'This email is already registered.' })
          } else if (!(gender === 'male' || gender === 'female')) {
            res.status(400)
            res.send({ message: 'Please enter a valid gender.' })
          } else {
            cache.del(`nonce_${req.connection.remoteAddress}`)
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
            const result = await userRepo.addUser(name, email, hashedPassword, passwordSalt, gender)
            if (result && result.insertId) {
              res.send({ message: 'Registeration successful. Check your email to activate your account.' })
            } else {
              res.status(500)
              res.send({ message: 'Internal server error.' })
            }
          }
        } else {
          res.send({ message: 'Please enter a valid email address.' })
        }
      }
    })
  )

  return router
}
