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
import createConvRepo from '../repository/ConversationRepository'
import jwtVerifyMiddleware from '../middleware/jwtVerifyMiddleware'
import ClientManager from '../socket/ClientManager'

export default function(mysqlClient, mailTransporter) {
  const logger = createLogger('ApiRouter')
  const userRepo = createUserRepo(mysqlClient)
  const messageRepo = createMessageRepo(mysqlClient)
  const convRepo = createConvRepo(mysqlClient)
  const clientManager = ClientManager.getInstance()
  const router = express.Router()

  const userProperties = ['id', 'name', 'email', 'phone', 'gender', 'age', 'workplace', 'city', 'description']

  const asyncMiddleware = fn => (req, res) => {
    Promise.resolve(fn(req, res)).catch(err => {
      logger.error(err.message + 'at ' + err.stack)
      res.set('Content-Type', 'application/json')
      res.status(500).send({ message: 'Internal server error.' })
    })
  }

  const omitProperties = exist_user => {
    const data = {}
    userProperties.forEach(key => {
      if (key == 'gender' || key == 'swipe_gender') {
        data.key = exist_user[key] ? 'male' : 'female'
      } else {
        data[key] = exist_user[key]
      }
    })
    return data
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
      const clientPassword = req.body.password
      if (email && clientPassword) {
        const exist_user = await userRepo.getUserByEmail(email)
        if (exist_user) {
          if (exist_user.is_activate) {
            if (!exist_user.is_banned) {
              const password = exist_user.password.split('.')
              const hashedPassword = password[0]
              const passwordSalt = password[1]
              const clientHashedPassword = crypto
                .pbkdf2Sync(clientPassword, passwordSalt, 1000, 16, 'sha256')
                .toString('hex')
              if (hashedPassword == clientHashedPassword) {
                const token = jwt.sign({ user_id: exist_user.id }, config.jwtSecret, { expiresIn: config.jwtMaxAge })
                const data = omitProperties(exist_user)
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
      } else {
        res.status(401).send({ message: 'Please provide email and password.' })
      }
    })
  )

  router.get('/user_info', jwtVerifyMiddleware, (req, res) => {
    res.set('Content-Type', 'application/json')
    const user_id = parseInt(req.query.user_id)
    if (user_id) {
      userRepo.getUserByUserId(user_id).then(user => {
        if (user) {
          const data = omitProperties(user)
          res.send(JSON.stringify(data))
        } else {
          res.status(404).send({ message: 'User not found.' })
        }
      })
    } else {
      res.status(400).send({ message: 'Invalid user_id provided.' })
    }
  })

  router.get(
    '/messages',
    jwtVerifyMiddleware,
    asyncMiddleware(async (req, res) => {
      res.set('Content-Type', 'application/json')
      const conversation_id = parseInt(req.query.conversation_id)
      const base_time = /^\d+$/.test(req.query.base_time) ? parseInt(req.query.base_time) : new Date().getTime()
      if (conversation_id > 0) {
        const hasConversation = await convRepo.checkHaveConversation(req.user_id, conversation_id)
        if (hasConversation) {
          if (base_time > 0) {
            messageRepo.getMessages(conversation_id, base_time, 15).then(data => {
              res.send(JSON.stringify({ conversation_id, messages: data }))
            })
          } else {
            res.status(400).send({ message: 'Invalid base time.' })
          }
        } else {
          res.status(403).send({ message: 'Forbidden.' })
        }
      } else {
        res.status(400).send({ message: 'Invalid conversation id.' })
      }
    })
  )

  router.get(
    '/conversations',
    jwtVerifyMiddleware,
    asyncMiddleware(async (req, res) => {
      const data = []
      const all_conv = await convRepo.getConversationsByUserId(req.user_id)
      for (const conv of all_conv) {
        const friend_id = conv.creator_id == req.user_id ? conv.member_id : conv.creator_id
        const friend = await userRepo.getUserByUserId(friend_id)
        data.push({
          conversation_id: conv.id,
          friend: omitProperties(friend)
        })
      }
      res.send(data)
    })
  )

  router.post('/settings', jwtVerifyMiddleware, (req, res) => {
    const re_phone = /(\+(?:\d{2})|0)\d{9,10}/
    const re_gender = /^male$|^female$/
    const name = req.body.name
    const gender = re_gender.test(req.body.gender) ? req.body.gender : null
    const swipe_gender = re_gender.test(req.body.swipe_gender) ? req.body.swipe_gender : null
    const age = parseInt(req.body.age)
    const phone = re_phone.test(req.body.phone) ? req.body.phone : null
    const description = req.body.description
    const max_distance = parseInt(req.body.max_distance)
    const min_age = parseInt(req.body.min_age)
    const max_age = parseInt(req.body.max_age)
    const user_id = req.user_id

    res.set('Content-Type', 'application/json')
    if (req.body.phone && !phone) {
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
    if (req.body.swipe_gender && !swipe_gender) {
      return res.status(400).send({ message: 'Invalid swipe_gender.' })
    }
    return userRepo
      .updateUserSetting(user_id, name, gender, age, phone, description, swipe_gender, max_distance, min_age, max_age)
      .then(() => {
        res.send({ message: 'Settings saved.' })
      })
  })

  router.post('/location', jwtVerifyMiddleware, (req, res) => {
    res.set('Content-Type', 'application/json')
    const latitude = parseFloat(req.body.latitude)
    const longitude = parseFloat(req.body.longitude)
    logger.info(`update location: (${latitude}, ${longitude})`)
    if (!isNaN(latitude) && !isNaN(longitude)) {
      if (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180) {
        return userRepo.updateLocation(req.user_id, latitude, longitude).then(() => {
          res.send({ message: 'ok' })
        })
      }
    }
    return res.status(400).send({ message: 'Invalid location.' })
  })

  router.get(
    '/get_swipes',
    jwtVerifyMiddleware,
    asyncMiddleware(async (req, res) => {
      res.set('Content-Type', 'application/json')
      const exist_user = await userRepo.getUserByUserId(req.user_id)
      if (exist_user) {
        const listSwipe = await userRepo.getSwipeUsers(exist_user, 50)
        const data = listSwipe.map(user => omitProperties(user))
        res.send(data)
      } else {
        res.status(400).send({ message: 'Bad request.' })
      }
    })
  )

  router.get('/statistic', (req, res) => {
    res.send({ online: clientManager.getCount() })
  })

  return router
}
