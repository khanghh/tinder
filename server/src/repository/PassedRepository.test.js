import config from '../config'
import createPassedRepo from './PassedRepository'
import mysql from 'mysql'

const client = mysql.createConnection(config.mysqlOptions)
const passedRepo = createPassedRepo(client)

test('add passed user', async () => {
  const data = await passedRepo.addPassedUser(2, 3)
  expect(data.insertId).toBeGreaterThan(0)
})

test('check user passed by another user', async () => {
  const data = await passedRepo.checkPassed(2, 3)
  expect(data).toBeTruthy()
  client.end()
})
