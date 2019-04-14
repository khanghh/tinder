import path from 'path'

export default {
  mysqlOptions: {
    host: '167.99.69.92',
    user: 'root',
    password: '123456',
    database: 'test_db'
  },
  temp_upload_dir: '/tmp/upload',
  thumbnail_cache_dir: '/tmp/upload',
  upload_max_size: 50 * 1024 * 1024, // 50 MB
  uploadDir: path.join(process.cwd(), 'upload')
}
