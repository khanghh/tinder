import bodyParser from 'body-parser'
import createLogger from './utils/createLogger'
import express from 'express'
import router from './router'

const logger = createLogger('app')

const app = express()
// extended set to false to prevent posting nested object
app.use(bodyParser.urlencoded({ extended: false }))
// parse application/json
app.use(bodyParser.json())

app.use(router)

const listener = app.listen(3000, () => {
  logger.info('Listening on port ' + listener.address().port)
})
