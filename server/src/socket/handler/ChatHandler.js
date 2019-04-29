import ClientManager from '../ClientManager'
import SocketSender from '../SocketSender'

export default function(convRepo, msgRepo) {
  const clientManager = ClientManager.getInstance()

  const handleChatMessageEvent = async (senderClient, msg) => {
    const sender_id = senderClient.user.id
    const conv_id = msg.conversation_id
    const message = msg.message
    const addMsgRet = await msgRepo.addMessage(conv_id, sender_id, message)
    if (addMsgRet) {
      const receiver_id = senderClient.all_conv[conv_id]
      const recvClient = clientManager.getClient(receiver_id)
      if (recvClient) {
        SocketSender.sendChatMessage(recvClient.socket, conv_id, addMsgRet.insertId, sender_id, message)
      }
    }
  }

  const handleSeenMesageEvent = async (client, msg) => {
    const conversation_id = msg.conversation_id
    const message_id = msg.message_id
    const friend_id = client.all_conv[conversation_id]
    const updateSeenRet = await msgRepo.updateSeenMessage(conversation_id, message_id)
    if (updateSeenRet) {
      const friendClient = clientManager.getClient(friend_id)
      if (friendClient) {
        SocketSender.sendChatSeen(friendClient.socket, conversation_id, message_id)
      }
    }
  }

  return { handleChatMessageEvent, handleSeenMesageEvent }
}
