import ClientManager from '../ClientManager'
import SocketSender from '../SocketSender'

export default function(likeRepo, passRepo, conversationRepo) {
  const clientManager = ClientManager.getInstance()

  const handleLikeEvent = async (likerClient, msg) => {
    const liker_id = likerClient.user.id
    const liked_id = msg.liked_id
    const addLikeRet = await likeRepo.addLike(liker_id, liked_id)
    if (addLikeRet && !(addLikeRet instanceof Error)) {
      SocketSender.sendUserLike(likerClient.socket, liker_id, liked_id)
      const is_match = await likeRepo.checkLike(liked_id, liker_id)
      if (is_match && !(is_match instanceof Error)) {
        const addConvRet = await conversationRepo.addConversation(liker_id, liked_id)
        if (addConvRet && !(addConvRet instanceof Error)) {
          likerClient.all_conv[addConvRet.insertId] = liked_id
          SocketSender.sendMatched(likerClient.socket, addConvRet.insertId, liker_id, liked_id)
          const likedClient = clientManager.getClient(liked_id)
          if (likedClient) {
            likedClient.all_conv[addConvRet.insertId] = liker_id
            likerClient.all_conv[addConvRet.insertId] = liked_id
            SocketSender.sendMatched(likedClient.socket, addConvRet.insertId, liker_id, liked_id)
          }
        }
      }
    }
  }

  const handlePassEvent = async (passerClient, msg) => {
    const passer_id = passerClient.user.id
    const passed_id = msg.passed_id
    const addPassRet = await passRepo.addPassedUser(passer_id, passed_id)
    if (addPassRet && !(addPassRet instanceof Error)) {
      SocketSender.sendUserPass(passerClient.socket, passer_id, passed_id)
    }
  }

  return { handleLikeEvent, handlePassEvent }
}
