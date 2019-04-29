import toPromise from './toPromise'

class UserRepository {
  constructor(client) {
    this.client = client
  }

  async getUserByUserId(user_id, fields = '*') {
    const query = `select ${fields} from user where id=${user_id}`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data[0]
  }

  async getUserByEmail(email, fields = '*') {
    const query = `select ${fields} from user where email='${email}'`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data[0]
  }

  async addUser(name, email, password, gender, age) {
    const bitGender = gender === 'male' ? 1 : 0
    const query = `INSERT INTO user(name, email, password, gender, age, is_activate, is_banned) VALUES ("${name}", "${email}", "${password}", ${bitGender}, ${age}, 0, 0)`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data
  }

  async activateUser(email) {
    const query = `UPDATE user SET is_activate=1 WHERE email="${email}"`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }
}

export default function(client) {
  return new UserRepository(client)
}
