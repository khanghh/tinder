import createLogger from './utils/createLogger'
import express from 'express'
import createApiRouter from './router/api_router'
import createUploadRouter from './router/upload_router'
import mysql from 'mysql'
import path from 'path'
import config from './config'
import bodyParser from 'body-parser'

const client = mysql.createConnection(config.mysqlOptions)
const api_router = createApiRouter(client)
const upload_router = createUploadRouter(client)
const logger = createLogger('app')

const app = express()
app.set('view engine', 'ejs')
app.set('views', path.join(__dirname, 'views'))
app.use(express.static(path.join(__dirname, 'static')))

app.use('/api', bodyParser.json(), api_router)
app.use('/upload', upload_router)

const listener = app.listen(3000, () => {
  logger.info('Listening on port ' + listener.address().port)
})
