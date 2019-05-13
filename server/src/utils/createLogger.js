import winston from 'winston'
import config from '../config'

const createLoger = function(objectName) {
  const myFormat = winston.format.printf(
    info =>
      winston.format
        .colorize()
        .colorize(info.level, `[${info.timestamp}][${objectName}][${info.level.toUpperCase()}]: `) + info.message
  )

  const myFormatFile = winston.format.printf(
    info => (info.level, `[${info.timestamp}][${objectName}][${info.level.toUpperCase()}]: `) + info.message
  )

  return winston.createLogger({
    format: winston.format.timestamp(),
    transports: [
      new winston.transports.Console({
        format: myFormat
      }),
      new winston.transports.File({
        filename: config.logFile,
        format: myFormatFile
      })
    ]
  })
}

export default createLoger
