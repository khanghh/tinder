import config from '../config'
import createMessageRepo from './MessageRepositoy'
import mysql from 'mysql'

const client = mysql.createConnection(config.mysqlOptions)
const messageRepo = createMessageRepo(client)

test('add message', async () => {
  const data = await messageRepo.addMessage(31, 2, 'test message')
  expect(data.insertId).toBeGreaterThan(0)
})

test('get message', async () => {
  const data = await messageRepo.getMessages(31)
  expect(data.length).toBeGreaterThan(0)
})

test('seen message', async () => {
  const data = await messageRepo.updateSeenMessage(68)
  expect(data.changedRows).toBeGreaterThan(0)
  client.end()
})
