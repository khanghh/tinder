import createUserRepo from './UserRepository'
import mysql from 'mysql'
const client = mysql.createConnection({
  host: '167.99.69.92',
  user: 'root',
  password: '123456',
  database: 'test_db'
})
const userRepo = createUserRepo(client)
test('get user', async () => {
  const user = await userRepo.getUserByUserId(2)
  expect(user.id).toBe(2)
})

test('like', async () => {
  const data = await userRepo.like(2, 3).then(data => {
    client.end()
    return data
  })
  // eslint-disable-next-line no-console
  console.log(data)
})
