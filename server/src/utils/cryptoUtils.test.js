import crytoUtils from './cryptoUtils'
import crypto from 'crypto'

test('encrypt/decrypt text', () => {
  const key = crypto.randomBytes(32).toString('hex')

  const plainText = 'TestStringTestStringTestStringTestString'
  const encrypted = crytoUtils.encrypt(plainText, key)
  const decrypted = crytoUtils.decrypt(encrypted, key)
  // eslint-disable-next-line no-console
  console.log(key)
  expect(decrypted).toBe(plainText)
})
