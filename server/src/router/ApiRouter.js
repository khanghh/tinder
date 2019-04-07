import express from 'express'
import createUserRepo from '../repository/UserRepository'
import createMessageRepo from '../repository/MessageRepositoy'

export default function(client) {
  const userRepo = createUserRepo(client)
  const messageRepo = createMessageRepo(client)

  const router = express.Router()

  router.get('/userinfo', (req, res) => {
    const userid = req.query.userid
    res.setHeader('Content-Type', 'application/json')
    userRepo.getUserByUserId(userid).then(user => {
      res.send(JSON.stringify(user))
    })
  })

  router.get('/message', async (req, res) => {
    const sender_id = req.query.sender_id
    const conversation_id = req.query.conversation_id
    const messages = await messageRepo.getMessages(sender_id, conversation_id)
    res.send(JSON.stringify(messages))
  })

  return router
}
