import express from 'express'
import bodyParser from 'body-parser'
import cache from 'memory-cache'
import crypto from 'crypto'
import jwt from 'jsonwebtoken'
import config from '../config'
import cryptoUtils from '../utils/cryptoUtils'
import createLogger from '../utils/createLogger'
import mailComposer from '../mail/MailComposer'
import createUserRepo from '../repository/UserRepository'
import createMessageRepo from '../repository/MessageRepositoy'
import jwtVerifyMiddleware from '../middleware/jwtVerifyMiddleware'
import ClientManager from '../socket/ClientManager'

export default function(mysqlClient, mailTransporter) {
  const logger = createLogger('ApiRouter')
  const userRepo = createUserRepo(mysqlClient)
  const messageRepo = createMessageRepo(mysqlClient)
  const clientManager = ClientManager.getInstance()
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
            const mail = await mailComposer.compose_activate_user(
              'Tinder<bf3t02@gmail.com>',
              email,
              name,
              activateToken
            )
            const result = await userRepo.addUser(name, email, hashedPassword + '.' + passwordSalt, gender, age)
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
      res.set('Content-Type', 'application/json')
      const re_email = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i
      const email = re_email.test(req.body.email) ? req.body.email : null
      if (email) {
        const exist_user = await userRepo.getUserByEmail(email)
        if (exist_user) {
          if (exist_user.is_activate) {
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
                res.status(401).send({ message: 'Login failed. Check your email and password and try again.' })
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

  router.get('/messages', (req, res) => {
    res.set('Content-Type', 'application/json')
    const conversation_id = req.query.conversation_id
    const base_time = req.query.base_time
    if (parseInt(conversation_id) > 0) {
      if (parseInt(base_time) > 0) {
        messageRepo.getMessages(conversation_id, base_time, 15).then(data => {
          res.send(JSON.stringify({ conversation_id, messages: data }))
        })
      } else {
        messageRepo.getMessages(conversation_id, new Date().getTime(), 15).then(data => {
          res.send(JSON.stringify({ conversation_id, messages: data }))
        })
      }
    } else {
      res.status(400).send('Invalid parameters.')
    }
  })

  router.post('/settings', jwtVerifyMiddleware, (req, res) => {
    const re_phone = /(\+(?:\d{2})|0)\d{9,10}/
    const re_gender = /^male$|^female$/
    const name = req.body.name
    const gender = re_gender.test(req.body.gender) ? req.body.gender : null
    const age = parseInt(req.body.age)
    const phone = re_phone.test(req.body.phone) ? req.body.phone : null
    const description = req.body.description
    const max_distance = parseInt(req.body.max_distance)
    const min_age = parseInt(req.body.min_age)
    const max_age = parseInt(req.body.max_age)
    const user_id = req.user_id

    res.set('Content-Type', 'application/json')
    if (req.body.phone && isNaN(phone)) {
      return res.status(400).send({ message: 'Invalid phone number.' })
    }
    if (req.body.age && isNaN(age)) {
      return res.status(400).send({ message: 'Invalid age.' })
    }
    if (req.body.min_age && isNaN(min_age)) {
      return res.status(400).send({ message: 'Invalid min age.' })
    }
    if (req.body.max_age && isNaN(max_age)) {
      return res.status(400).send({ message: 'Invalid max age.' })
    }
    if (req.body.max_distance && isNaN(max_distance)) {
      return res.status(400).send({ message: 'Invalid max distance.' })
    }
    if (req.body.gender && !gender) {
      return res.status(400).send({ message: 'Invalid gender.' })
    }
    return userRepo
      .updateUserSetting(user_id, name, gender, age, phone, description, max_distance, min_age, max_age)
      .then(result => {
        if (result.changedRows > 0) {
          res.send({ message: 'Settings saved.' })
        } else {
          res.status(500).send({ message: 'Internal Server Error' })
        }
      })
  })

  router.get('/statistic', (req, res) => {
    res.send({ online: clientManager.getCount() })
  })

  return router
}
