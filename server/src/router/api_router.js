import express from 'express'
import createUserRepo from '../repository/UserRepository'
import createMessageRepo from '../repository/MessageRepositoy'

export default function(client) {
  const userRepo = createUserRepo(client)
  const messageRepo = createMessageRepo(client)

  const router = express.Router()

  router.get('/user_info', (req, res) => {
    res.set('Content-Type', 'application/json')
    const user_id = req.query.user_id
    userRepo.getUserByUserId(user_id).then(user => {
      res.send(JSON.stringify(user))
    })
  })

  router.get('/message', async (req, res) => {
    res.set('Content-Type', 'application/json')
    const conversation_id = req.query.conversation_id
    const messages = await messageRepo.getMessages(conversation_id)
    res.send(JSON.stringify(messages))
  })

  return router
}
