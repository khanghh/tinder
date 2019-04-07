import createConvRepo from './ConversationRepository'
import mysql from 'mysql'
const client = mysql.createConnection({
  host: '167.99.69.92',
  user: 'root',
  password: '123456',
  database: 'test_db'
})
const convRepo = createConvRepo(client)
test('get conversations', async () => {
  const data = await convRepo.getConversations(10, 'id').then(data => {
    client.end()
    return data
  })
  // eslint-disable-next-line no-console
  console.log(data)
  // expect(data.).toBe(2)
})
