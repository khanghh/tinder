import toPromise from './toPromise'

class PassedRepository {
  constructor(client) {
    this.client = client
  }

  addPassedUser(passer_id, passed_id) {
    const query = `INSERT INTO passed_users(passer_id, passed_id) VALUES (${passer_id}, ${passed_id})`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  checkPassed(passer_id, passed_id) {
    const query = `SELECT id FROM passed_users WHERE passer_id=${passer_id} and passed_id=${passed_id}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }
}

export default function(client) {
  return new PassedRepository(client)
}
