export default {
  sendServerMessage(socket, message, is_disconnect) {
    socket.emit('message', { message })
    if (is_disconnect) {
      socket.disconnect()
    }
  },

  sendUnauthorized(socket) {
    socket.emit('unauthorized', { message: 'Invalid toekn provided.' })
    socket.disconnect()
  },

  sendUserLike(socket, liker_id, liked_id) {
    socket.emit('like', { liker_id, liked_id })
  },

  sendUserPass(socket, passer_id, passed_id) {
    socket.emit('pass', { passer_id, passed_id })
  },

  sendMatched(socket, conversation_id, user_id, friend_id) {
    socket.emit('match', { conversation_id, user_id, friend_id })
  },

  sendChatMessageResult(socket, conversation_id, message_id, is_received) {
    socket.emit('chat_message_result', { conversation_id, message_id, is_received })
  },

  sendChatSeen(socket, conversation_id, message_id, seen) {
    socket.emit('chat_seen', conversation_id, message_id, seen)
  },

  sendChatMessage(socket, conversation_id, message_id, sender_id, message) {
    socket.emit('chat_message', { conversation_id, message_id, sender_id, message })
  },

  sendChatTyping(socket, conversation_id, is_typing) {
    socket.emit('chat_typing', { conversation_id, is_typing })
  }
}
