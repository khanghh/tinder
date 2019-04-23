import express from 'express'
import bodyParser from 'body-parser'
import cache from 'memory-cache'
import crypto from 'crypto'
import jwt from 'jsonwebtoken'
import config from '../config'
import cryptoUtils from '../utils/cryptoUtils'
import createLogger from '../utils/createLogger'
import MailComposer from '../mail/MailComposer'
import createUserRepo from '../repository/UserRepository'
import createMessageRepo from '../repository/MessageRepositoy'
import jwtVerifyMiddleware from '../middleware/jwtVerifyMiddleware'

export default function(mysqlClient, mailTransporter) {
  const logger = createLogger('ApiRouter')
  const userRepo = createUserRepo(mysqlClient)
  const messageRepo = createMessageRepo(mysqlClient)
  const router = express.Router()

  const userProperties = ['id', 'name', 'email', 'phone', 'gender', 'age', 'description']

  const asyncMiddleware = fn => (req, res) => {
    Promise.resolve(fn(req, res)).catch(err => logger.error(err.message + 'at ' + err.stack))
  }

  router.use(bodyParser.json())
  router.use(bodyParser.urlencoded({ extended: false }))

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
        res.status(403).send({ message: 'Forbidden.' })
      } else {
        const re_email = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
        const name = req.body.name
        const email = re_email.test(req.body.email) ? req.body.email : null
        const password = req.body.password
        const gender = req.body.gender
        const age = parseInt(req.body.age)

        if (email) {
          const exist_user = await userRepo.getUserByEmail(email)
          if (exist_user) {
            res.status(409).send({ message: 'This email is already registered.' })
          } else if (!(gender === 'male' || gender === 'female')) {
            res.status(400).send({ message: 'Please enter a valid gender.' })
          } else if (age < 1 || age > 100) {
            res.status(400).send({ message: 'Please enter a valid age.' })
          } else {
            cache.del(`nonce_${req.connection.remoteAddress}`)
            const passwordSalt = crypto.randomBytes(16).toString('hex')
            const hashedPassword = crypto.pbkdf2Sync(password, passwordSalt, 1000, 16, 'sha256').toString('hex')
            const activateToken = cryptoUtils.encrypt(email, config.cryptoKey)
            const mail = await MailComposer.compose_activate_user(
              'Tinder<bf3t02@gmail.com>',
              email,
              name,
              activateToken
            )
            const result = await userRepo.addUser(name, email, hashedPassword + '.' + passwordSalt, gender)
            if (result && result.insertId) {
              await mailTransporter.sendMail(mail)
              res.send({ message: 'Registeration successful. Check your email to activate your account.' })
            } else {
              res.status(500).send({ message: 'Internal server error.' })
            }
          }
        } else {
          res.status(400).send({ message: 'Please enter a valid email address.' })
        }
      }
    })
  )

  router.post(
    '/login',
    asyncMiddleware(async (req, res) => {
      const nonce = req.body.nonce
      res.set('Content-Type', 'application/json')
      if (!nonce || nonce != cache.get(`nonce_${req.connection.remoteAddress}`)) {
        res.status(403).send({ message: 'Forbidden.' })
      } else {
        const re_email = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
        const email = re_email.test(req.body.email) ? req.body.email : null
        if (email) {
          const exist_user = await userRepo.getUserByEmail(email)
          if (exist_user) {
            if (exist_user.is_active) {
              if (!exist_user.is_banned) {
                const clientPassword = req.body.password
                const password = exist_user.password.split('.')
                const hashedPassword = password[0]
                const passwordSalt = password[1]
                const clientHashedPassword = crypto
                  .pbkdf2Sync(clientPassword, passwordSalt, 1000, 16, 'sha256')
                  .toString('hex')
                if (hashedPassword == clientHashedPassword) {
                  const token = jwt.sign({ user_id: exist_user.id }, config.jwtSecret, { expiresIn: config.jwtMaxAge })
                  const data = {}
                  userProperties.forEach(key => (data[key] = exist_user[key]))
                  res.send({ message: 'Login successfully.', authToken: token, user: data })
                } else {
                  res.send({ message: 'Login failed. Check your email and password and try again.' })
                }
              } else {
                res.status(403).send({
                  message: `Your account has been temporarily suspended because of ${exist_user.ban_reason}`
                })
              }
            } else {
              res.status(403).send({ message: 'Check your email to activate your account.' })
            }
          } else {
            res.status(404).send({ message: 'Account not found.' })
          }
        }
      }
    })
  )

  router.get('/user_info', jwtVerifyMiddleware, (req, res) => {
    res.set('Content-Type', 'application/json')
    const re_id = /^\d+$/
    const user_id = re_id.test(req.query.user_id) ? req.query.user_id : null
    if (user_id) {
      userRepo.getUserByUserId(user_id).then(user => {
        if (user) {
          const data = {}
          userProperties.forEach(key => (data[key] = user[key]))
          res.send(JSON.stringify(data))
        } else {
          res.status(404).send({ message: 'User not found.' })
        }
      })
    } else {
      res.status(400).send({ message: 'Invalid user_id provided.' })
    }
  })

  router.get('/message', jwtVerifyMiddleware, (req, res) => {
    res.set('Content-Type', 'application/json')
    const conversation_id = req.query.conversation_id
    messageRepo.getMessages(conversation_id).then(message => {
      res.send(JSON.stringify(message))
    })
  })

  return router
}
