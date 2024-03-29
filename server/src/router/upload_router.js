import fs from 'fs'
import express from 'express'
import createUserRepo from '../repository/UserRepository'
import config from '../config'
import path from 'path'
import multer from 'multer'
import sharp from 'sharp'
import quickthumb from 'quickthumb'
import createLogger from '../utils/createLogger'
import jwtVerifyMiddleware from '../middleware/jwtVerifyMiddleware'

export default function(mysqlClient) {
  // eslint-disable-next-line no-unused-vars
  const userRepo = createUserRepo(mysqlClient)
  const logger = createLogger('UploadRouter')
  const router = express.Router()

  function saveImage(image, width, height, file_name) {
    if (image) {
      if (image.mimetype === 'image/jpeg') {
        const dest = path.join(config.uploadDir, 'images', `/${file_name}`)
        logger.info(`${image.path} --> ${dest}`)
        return sharp(image.path)
          .jpeg({ quality: 60 })
          .resize(width, height, { fit: 'cover' })
          .toFile(dest)
          .then(() => 'ok')
          .catch(err => {
            logger.error(err.stack)
            return 'fail'
          })
      }
    }
    return 'fail'
  }

  const upload_storage = multer.diskStorage({
    destination(req, file, cb) {
      cb(null, '/tmp/upload')
    },
    filename(req, file, cb) {
      cb(null, 'tmp-' + Date.now())
    }
  })

  const upload_limit = {
    fileSize: config.uploadMaxSize
  }

  const upload_filter = (req, file, cb) => {
    if (file.mimetype !== 'image/jpeg') {
      const error = new multer.MulterError()
      error.code = 'LIMIT_FILE_TYPE'
      error.message = 'Unsupported media type'
      cb(error, false)
    } else cb(null, true)
  }

  const upload_single = multer({
    storage: upload_storage,
    limits: upload_limit,
    fileFilter: upload_filter
  }).single('image')

  const upload_multiple = multer({ storage: upload_storage, limits: upload_limit }).fields([
    { name: 'image1', maxCount: 1 },
    { name: 'image1', maxCount: 1 },
    { name: 'image2', maxCount: 1 },
    { name: 'image3', maxCount: 1 },
    { name: 'image4', maxCount: 1 },
    { name: 'image5', maxCount: 1 },
    { name: 'image6', maxCount: 1 }
  ])

  router.use(quickthumb.static(path.join(config.uploadDir, 'images'), { cacheDir: config.tempUploadDir }))
  router.get('*', (req, res) => {
    res.setHeader('Content-Type', 'application/json')
    res.status(404).send({ message: 'File not found.' })
  })

  router.post('/upload_image', jwtVerifyMiddleware, (req, res) => {
    upload_single(req, res, async err => {
      res.setHeader('Content-Type', 'application/json')
      if (err instanceof multer.MulterError) {
        if (err.code === 'LIMIT_FILE_SIZE') {
          res.status(413)
        } else if (err.code === 'LIMIT_FILE_TYPE') {
          res.status(415)
        }
        return res.send({ message: err.message })
      } else if (err) {
        res.status(500).send({ message: err.message })
      } else {
        const num = parseInt(req.body.num)
        if (num > 0 && num <= 6) {
          logger.info(`user ${req.user_id} upload image num: ${num}`)
          const user_id = req.user_id
          const width = 700
          const height = 875
          const image = req.file
          const result = await saveImage(image, width, height, `${user_id}_image${num}.jpg`)
          res.send({ message: result })
        } else {
          res.status(400).send({ message: 'num must be between 1 and 6' })
        }
      }
    })
  })

  router.post('/upload_images', jwtVerifyMiddleware, (req, res) => {
    upload_multiple(req, res, async err => {
      res.setHeader('Content-Type', 'application/json')
      if (err instanceof multer.MulterError) {
        if (err.code === 'LIMIT_FILE_SIZE') {
          res.status(413)
        } else if (err.code === 'LIMIT_FILE_TYPE') {
          res.status(415)
        }
        return res.send({ message: err.message })
      } else if (err) {
        res.sendStatus(500).send(err)
      } else {
        const userid = req.user_id
        const width = 700
        const height = 875
        const image1 = req.files.image1 ? req.files.image1[0] : null
        const image2 = req.files.image2 ? req.files.image2[0] : null
        const image3 = req.files.image3 ? req.files.image3[0] : null
        const image4 = req.files.image4 ? req.files.image4[0] : null
        const image5 = req.files.image5 ? req.files.image5[0] : null
        const image6 = req.files.image5 ? req.files.image6[0] : null
        const result = await Promise.all([
          saveImage(image1, width, height, `${userid}_image1.jpg`),
          saveImage(image2, width, height, `${userid}_image2.jpg`),
          saveImage(image3, width, height, `${userid}_image3.jpg`),
          saveImage(image4, width, height, `${userid}_image4.jpg`),
          saveImage(image5, width, height, `${userid}_image5.jpg`),
          saveImage(image6, width, height, `${userid}_image6.jpg`)
        ])
        res.send({ message: result })
      }
    })
  })

  router.post('/delete_image', jwtVerifyMiddleware, (req, res) => {
    res.setHeader('Content-Type', 'application/json')
    logger.info(`delete_image: user=${req.user_id}, num=${req.query.num}`)
    const num = parseInt(req.query.num)
    if (num > 0 && num <= 6) {
      const file_name = `${req.user_id}_image${num}.jpg`
      const srcFile = path.join(config.uploadDir, 'images', `/${file_name}`)
      const destFile = srcFile + '.deleted'
      logger.info(srcFile)
      logger.info(destFile)
      return fs.rename(srcFile, destFile, err => {
        if (err) {
          res.status(500).send({ message: 'fail' })
        } else {
          res.send({ message: 'ok' })
        }
      })
    }
    return res.status(400).send({ message: 'fail' })
  })

  return router
}
