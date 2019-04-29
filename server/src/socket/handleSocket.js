import socketio from 'socket.io'
import jwt from 'jsonwebtoken'
import Client from './model/Client'
import ClientManager from './ClientManager'
import createLogger from '../utils/createLogger'
import createUserRepo from '../repository/UserRepository'
import createConvRepo from '../repository/ConversationRepository'
import config from '../config'
import socketSender from './SocketSender'
import SocketEventHandler from './SocketEventHandler'

const logger = createLogger('SocketHandler')

export default (socketListener, mysqlClient, mailTransporter) => {
  const socketServer = socketio(socketListener)
  const userRepo = createUserRepo(mysqlClient)
  const convRepo = createConvRepo(mysqlClient)
  const clientManager = ClientManager.getInstance()
  const socketEventHandler = SocketEventHandler(mysqlClient, mailTransporter)

  const registerEventHandler = client => {
    const socket = client.socket
    const user = client.user
    socket.on('disconnect', () => logger.info(`user ${user.id} disconnected.`))
    socket.on('error', error => logger.error(`error from ${user.id}: ${error.message} + at ${error.stack}`))
    socketEventHandler.registerEventHandler(client)
  }

  socketServer.use((socket, next) => {
    const token = socket.handshake.query.token
    if (token) {
      jwt.verify(token, config.jwtSecret, async (err, decoded) => {
        if (!err) {
          const user_id = decoded.user_id
          if (user_id) {
            const exist_user = await userRepo.getUserByUserId(user_id)
            if (exist_user) {
              logger.info(`user ${user_id} connected.`)
              const all_conv = await convRepo.getConversations(user_id)
              logger.info(`user ${user_id}: ${all_conv}`)
              const client = new Client(socket, exist_user)
              all_conv.forEach(row => {
                if (row.creator_id == user_id) {
                  client.all_conv[row.id] = row.member_id
                } else {
                  client.all_conv[row.id] = row.creator_id
                }
              })
              registerEventHandler(client)
              clientManager.addClient(client)
              next()
            }
          }
        } else {
          logger.warn(`${socket.handshake.address} invalid authToken.`)
          socketSender.sendUnauthorized(socket)
          next(new Error('Authentication error'))
        }
      })
    } else {
      logger.warn(`${socket.handshake.address} empty authToken.`)
      socketSender.sendUnauthorized(socket)
      next(new Error('Authentication error'))
    }
  })
}
