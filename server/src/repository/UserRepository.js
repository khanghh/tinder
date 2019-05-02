import toPromise from './toPromise'

class UserRepository {
  constructor(client) {
    this.client = client
  }

  getUserByUserId(user_id, fields = '*') {
    const query = `select ${fields} from user where id=${user_id}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    }).then(data => data[0])
  }

  getUserByEmail(email, fields = '*') {
    const query = `select ${fields} from user where email='${email}'`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    }).then(data => data[0])
  }

  addUser(name, email, password, gender, age) {
    const bitGender = gender === 'male' ? 1 : 0
    const query = `INSERT INTO user(name, email, password, gender, age, is_activate, is_banned) VALUES ("${name}", "${email}", "${password}", ${bitGender}, ${age}, 0, 0)`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  activateUser(email) {
    const query = `UPDATE user SET is_activate=1 WHERE email="${email}"`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  updateLocation(user_id, longitude, latitude) {
    const query = `UPDATE user SET longitude=${longitude},latitude=${latitude},updated_at=now() WHERE id=${user_id}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  updateUserSetting(user_id, name, gender, age, phone, description, max_distance, min_age, max_age) {
    const qname = name ? `name="${name}",` : ''
    const qgender = gender ? `gender=${gender == 'male' ? 1 : 0},` : ''
    const qage = age ? `age=${age},` : ''
    const qphone = phone ? `phone="${phone}",` : ''
    const qdescription = description ? `description="${description}",` : ''
    const qmax_distance = max_distance ? `gender=${max_distance},` : ''
    const qmin_age = min_age ? `min_age=${min_age},` : ''
    const qmax_age = max_age ? `max_age=${max_age},` : ''
    const query = `UPDATE user SET ${qname}${qgender}${qage}${qphone}${qdescription}${qmax_distance}${qmin_age}${qmax_age}updated_at=now() WHERE id=${user_id}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }
}

export default function(client) {
  return new UserRepository(client)
}
