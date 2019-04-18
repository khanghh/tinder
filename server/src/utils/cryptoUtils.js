import crypto from 'crypto'

export default {
  encrypt(plaintext, key) {
    const iv = Buffer.alloc(16)
    const cipher = crypto.createCipheriv('aes-256-cbc', Buffer.from(key, 'hex'), iv)
    const encrypted = Buffer.concat([cipher.update(plaintext), cipher.final()])
    return encrypted.toString('hex')
  },

  decrypt(ciphertext, key) {
    const iv = Buffer.alloc(16)
    const encryptedText = Buffer.from(ciphertext, 'hex')
    const decipher = crypto.createDecipheriv('aes-256-cbc', Buffer.from(key, 'hex'), iv)
    const decrypted = Buffer.concat([decipher.update(encryptedText), decipher.final()])
    return decrypted.toString()
  }
}
