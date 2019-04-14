import winston from 'winston'

const createLoger = function(objectName) {
  const myFormat = winston.format.printf(
    info =>
      winston.format
        .colorize()
        .colorize(info.level, `[${info.timestamp}][${objectName}][${info.level.toUpperCase()}]: `) + info.message
  )

  return winston.createLogger({
    format: winston.format.timestamp(),
    transports: [
      new winston.transports.Console({
        format: myFormat
      })
    ]
  })
}

export default createLoger
