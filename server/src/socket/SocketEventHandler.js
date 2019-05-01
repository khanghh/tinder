import createLikedRepo from '../repository/LikedRepository'
import createPassedRepo from '../repository/PassedRepository'
import createConvRepo from '../repository/ConversationRepository'
import createMsgRepo from '../repository/MessageRepositoy'
import UserLikePassHandler from './handler/UserLikePassHandler'
import ChatHandler from './handler/ChatHandler'

// eslint-disable-next-line no-unused-vars
export default function(mysqlClient, mailTransporter) {
  const likedRepo = createLikedRepo(mysqlClient)
  const passedRepo = createPassedRepo(mysqlClient)
  const convRepo = createConvRepo(mysqlClient)
  const msgRepo = createMsgRepo(mysqlClient)

  const handleLikeEvent = UserLikePassHandler(likedRepo, passedRepo, convRepo).handleLikeEvent
  const handlePassEvent = UserLikePassHandler(likedRepo, passedRepo, convRepo).handlePassEvent
  const handleChatMessageEvent = ChatHandler(convRepo, msgRepo).handleChatMessageEvent
  const handleChatSeenEvent = ChatHandler(convRepo, msgRepo).handleSeenMesageEvent
  const handleTypingEvent = ChatHandler(convRepo, msgRepo).handleTypingEvent

  const registerNormalEvent = client => {
    const socket = client.socket
    socket.on('like', msg => handleLikeEvent(client, msg))
    socket.on('pass', msg => handlePassEvent(client, msg))
    socket.on('chat_message', msg => handleChatMessageEvent(client, msg))
    socket.on('chat_seen', msg => handleChatSeenEvent(client, msg))
    socket.on('chat_typing', msg => handleTypingEvent(client, msg))
  }

  const registerEventHandler = client => {
    const user = client.user
    if (user.is_activate && !user.is_banned) {
      registerNormalEvent(client)
    }
  }
  return { registerEventHandler }
}
