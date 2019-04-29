import config from '../config'
import createLikedRepo from './LikedRepository'
import mysql from 'mysql'

const client = mysql.createConnection(config.mysqlOptions)
const likedRepo = createLikedRepo(client)

test('add like', async () => {
  const data = await likedRepo.addLike(2, 3)
  expect(data.insertId).toBeGreaterThan(0)
})

test('check like', async () => {
  const data = await likedRepo.checkLike(2, 3)
  expect(data).toBeTruthy()
  client.end()
})
