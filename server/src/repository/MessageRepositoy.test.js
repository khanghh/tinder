import createMessageRepo from './MessageRepositoy'
import mysql from 'mysql'
const client = mysql.createConnection({
  host: '167.99.69.92',
  user: 'root',
  password: '123456',
  database: 'test_db'
})
const messageRepo = createMessageRepo(client)
test('get user', async () => {
  const data = await messageRepo.getMessages(10).then(data => {
    client.end()
    return data
  })
  // eslint-disable-next-line no-console
  console.log(JSON.stringify(data))
  // expect(data.).toBe(2)
})
