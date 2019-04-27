import createLogger from './utils/createLogger'
import createUserLikesRepo from './repository/UserLikesRepository'

const logger = createLogger('SocketHandler')

export default function(socket, mysqlClient) {
  const UserLikesRepo = createUserLikesRepo(mysqlClient)

  socket.on('send_message', mesage => {
    logger.info(mesage)
    const msg = JSON.stringify({ sender_id: 2, conversation_id: 1, message: 'I have received message' })
    socket.emit('receive_message', msg)
    logger.info(msg)
  })

  socket.on('like', msg => {
    const liker_id = msg.liker_user_id
    const liked_id = msg.liked_user_id
    logger.info(liker_id)
    logger.info(liked_id)
    UserLikesRepo.addLike(liker_id, liked_id).then(result => {
      if (result && result.insertId) {
        socket.emit('like', { result: true, liker_id, liked_id })
      } else {
        socket.emit('like', { result: false })
      }
    })
  })

  socket.on('disconnect', () => {
    logger.info('client disconnected')
  })
}
