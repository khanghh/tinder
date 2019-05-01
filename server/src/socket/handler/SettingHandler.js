export default function(userRepo) {
  const handleLocationEvent = async (client, msg) => {
    const user_id = client.user.id
    const longtitude = msg.longtitude
    const latitude = msg.latitude
    await userRepo.updateLocation(user_id, longtitude, latitude)
  }

  return { handleLocationEvent }
}
