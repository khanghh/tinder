import config from '../config'
import createConvRepo from './ConversationRepository'
import mysql from 'mysql'

const client = mysql.createConnection(config.mysqlOptions)
const convRepo = createConvRepo(client)

test('add conversations', async () => {
  const data = await convRepo.addConversation(4, 3)
  expect(data.insertId).toBeGreaterThan(0)
})

test('get conversations by user id', async () => {
  const data = await convRepo.getConversationsByUserId(4)
  expect(data.length).toBeGreaterThan(0)
  client.end()
})
