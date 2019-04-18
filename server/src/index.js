import express from 'express'
import mysql from 'mysql'
import path from 'path'
import bodyParser from 'body-parser'
import nodemailer from 'nodemailer'
import cookieSession from 'cookie-session'

import config from './config'
import createLogger from './utils/createLogger'
import createApiRouter from './router/api_router'
import createUploadRouter from './router/upload_router'
import createMainRouter from './router/main_router'

const pool = mysql.createPool(config.mysqlOptions)
const mailTransporter = nodemailer.createTransport(config.mailerOptions)
const logger = createLogger('app')

const main_router = createMainRouter(pool, mailTransporter)
const api_router = createApiRouter(pool, mailTransporter)
const upload_router = createUploadRouter(pool)

const app = express()
app.set('view engine', 'ejs')
app.set('views', path.join(__dirname, 'views'))
app.use(express.static(path.join(__dirname, 'static')))
app.use('/api', api_router)
app.use('/upload', upload_router)
app.use(
  '/',
  cookieSession({
    name: 'tinder',
    secret: config.cookieSecret,
    signed: true,
    maxAge: 2 * 60 * 60 * 1000 // 24 hours
  }),
  main_router
)

mailTransporter.verify(error => {
  if (error) {
    logger.error(error)
  } else {
    logger.info(`Mail user ${mailTransporter.options.auth.user} is ready`)
  }
})

const listener = app.listen(3000, () => {
  logger.info('Listening on port ' + listener.address().port)
})
