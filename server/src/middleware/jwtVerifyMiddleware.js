import jwt from 'jsonwebtoken'
import config from '../config'
import createLogger from '../utils/createLogger'

const logger = createLogger('JWT')

export default function(req, res, next) {
  const authorization = req.headers.authorization
  if (!authorization) {
    return res.status(403).send({
      result: false,
      message: 'Forbidden'
    })
  }
  const token = authorization.split(' ')[1]
  logger.info('token: ' + token)
  jwt.verify(token, config.jwtSecret, (err, decoded) => {
    if (err) {
      logger.warn(`${req.connection.remoteAddress} invalid authToken.`)
      return res.status(403).send({
        result: false,
        message: 'Invalid auth token provided.'
      })
    }
    req.user_id = decoded.user_id
    next()
  })
}
