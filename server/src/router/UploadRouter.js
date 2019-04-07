import express from 'express'
import createUserRepo from '../repository/UserRepository'
import config from '../config'
import path from 'path'

export default function(client) {
  const userRepo = createUserRepo(client)

  const router = express.Router()

  const toPromise = function(cb) {
    return new Promise((ok, fail) => {
      cb(err => (err ? fail(err) : ok(true)))
    })
  }

  async function saveImage(image, file_name) {
    if (image) {
      if (image.mimetype === 'image/jpeg') {
        return await toPromise(cb => {
          const dest = path.join(config.uploadDir, 'images', `/${file_name}`)
          image.mv(dest, cb)
        })
      }
    }
    return false
  }

  router.post('/upload_images', async (req, res) => {
    const userid = 1
    const image1 = req.files.image1
    const image2 = req.files.image2
    const image3 = req.files.image3
    const image4 = req.files.image4
    const image5 = req.files.image5
    const result = await Promise.all([
      saveImage(image1, `${userid}_image1.jpg`),
      saveImage(image2, `${userid}_image2.jpg`),
      saveImage(image3, `${userid}_image3.jpg`),
      saveImage(image4, `${userid}_image4.jpg`),
      saveImage(image5, `${userid}_image5.jpg`)
    ]).catch(console.log)
    res.send(result)
  })

  return router
}
