import config from '../config'
import createUserRepo from './UserRepository'
import mysql from 'mysql'

const client = mysql.createConnection(config.mysqlOptions)
const userRepo = createUserRepo(client)

test('get user by id', async () => {
  const user = await userRepo.getUserByUserId(2)
  expect(user.id).toBe(2)
})

test('get user by email', async () => {
  const user = await userRepo.getUserByEmail('hoanghongkhang@gmail.com')
  expect(user.id).toBe(11)
})

test('activate user', async () => {
  const data = await userRepo.activateUser('hoanghongkhang@gmail.com')
  expect(data.changedRows).toBe(1)
})

test('update location', async () => {
  const data = await userRepo.updateLocation(11, 2143.32432, 23423.23434)
  expect(data.changedRows).toBe(1)
  client.end()
})

// test('get swipe', async () => {
//   const data = await userRepo.getSwipeUsers(
//     {
//       id: 2,
//       gender: 1,
//       longitude: 10,
//       latitude: 10
//     },
//     {
//       gender: 1,
//       min_age: 1,
//       max_age: 60,
//       max_distance: 10
//     },
//     10
//   )
//   // expect(data.changedRows).toBe(1)
//   client.end()
// })
