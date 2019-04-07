import path from 'path'

export default {
  mysqlOptions: {
    host: '167.99.69.92',
    user: 'root',
    password: '123456',
    database: 'test_db'
  },
  upload_max_size: 1024 * 1024, // bytes
  uploadDir: path.join(path.dirname(require.main.filename), 'upload')
}
