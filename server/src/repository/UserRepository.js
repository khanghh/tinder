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

  async like(liker_id, liked_id) {
    const query = `insert into user_likes (liker_user_id, liked_user_id) values (${liker_id},${liked_id})`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data
  }

  async addUser(name, email, password, gender) {
    const bitGender = gender === 'male' ? 1 : 0
    const query = `INSERT INTO user(name, email, password, gender, is_active, is_banned) VALUES ("${name}", "${email}", "${password}", ${bitGender}, 0, 0)`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data
  }

  async activateUser(email) {
    const query = `UPDATE user SET is_active=1 WHERE email="${email}"`
    const client = this.client
    const data = await toPromise(cb => {
      client.query(query, cb)
    })
    return data
  }
}

export default function(client) {
  return new UserRepository(client)
}
