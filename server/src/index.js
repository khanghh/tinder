import createLogger from './utils/createLogger'
import express from 'express'
import createApiRouter from './router/ApiRouter'
import createUploadRouter from './router/UploadRouter'
import mysql from 'mysql'
/* eslint import/no-nodejs-modules: ["error", {"allow": ["path"]}] */
import path from 'path'
import config from './config'
import fileUpload from 'express-fileupload'
import bodyParser from 'body-parser'

const client = mysql.createConnection(config.mysqlOptions)
const APIRouter = createApiRouter(client)
const uploadRouter = createUploadRouter(client)
const logger = createLogger('app')

const app = express()
app.set('view engine', 'ejs')
app.set('views', path.join(__dirname, 'views'))
app.use(express.static(path.join(__dirname, 'static')))
app.use('/upload', express.static(config.uploadDir))
app.use(
  fileUpload({
    limits: { fileSize: config.upload_max_size },
    abortOnLimit: true,
    responseOnLimit: JSON.stringify({ msg: 'File size limit has been reached' }),
    useTempFiles: true,
    tempFileDir: '/tmp/'
  })
)

app.use('/api', bodyParser.json(), APIRouter)
app.use(uploadRouter)

const listener = app.listen(3000, () => {
  logger.info('Listening on port ' + listener.address().port)
})
