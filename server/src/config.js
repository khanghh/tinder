export default {
  mysqlOptions: {
    connectionLimit: 10,
    host: process.env.mysqlHost,
    user: process.env.mysqlUser,
    password: process.env.mysqlPassword,
    database: process.env.mysqlDatabase,
    charset: 'utf8mb4_general_ci'
  },
  mailerOptions: {
    pool: true,
    host: 'smtp.gmail.com',
    port: 587,
    secure: false,
    auth: {
      user: process.env.mailerUser,
      pass: process.env.mailerPassword
    }
  },
  cryptoKey: process.env.cryptoKey,
  cryptoSalt: process.env.cryptoSalt,
  homeUrl: process.env.homeUrl,
  cookieSecret: 'tinderServer',
  jwtSecret: process.env.jwtSecret,
  jwtMaxAge: '30 days',
  tempUploadDir: '/tmp/upload',
  uploadMaxSize: 50 * 1024 * 1024, // 50 MB
  uploadDir: process.env.uploadDir,
  socketPort: 8888
}
