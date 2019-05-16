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
    const bitSwipeGender = gender === 'male' ? 0 : 1
    const query = `INSERT INTO user(name, email, password, gender, age, swipe_gender, is_activate, is_banned) VALUES ("${name}", "${email}", "${password}", ${bitGender}, ${age}, ${bitSwipeGender}, 0, 0)`
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

  updateLocation(user_id, latitude, longitude) {
    const query = `UPDATE user SET latitude=${latitude},longitude=${longitude},updated_at=now() WHERE id=${user_id}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  updateUserSetting(
    user_id,
    name,
    gender,
    age,
    phone,
    workplace,
    city,
    description,
    swipe_gender,
    max_distance,
    min_age,
    max_age
  ) {
    const qname = name ? `name="${name}",` : ''
    const qgender = gender ? `gender=${gender == 'male' ? 1 : 0},` : ''
    const qswipe_gender = swipe_gender ? `swipe_gender=${swipe_gender == 'male' ? 1 : 0},` : ''
    const qage = age ? `age=${age},` : ''
    const qphone = phone ? `phone="${phone}",` : ''
    const qworkplace = workplace ? `workplace="${workplace}",` : ''
    const qcity = city ? `city="${city}",` : ''
    const qdescription = description ? `description="${description}",` : ''
    const qmax_distance = max_distance ? `max_distance=${max_distance},` : ''
    const qmin_age = min_age ? `min_age=${min_age},` : ''
    const qmax_age = max_age ? `max_age=${max_age},` : ''
    const query = `UPDATE user SET ${qname}${qgender}${qage}${qphone}${qworkplace}${qcity}${qdescription}${qswipe_gender}${qmax_distance}${qmin_age}${qmax_age}updated_at=now() WHERE id=${user_id}`
    console.log(query)
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }

  getSwipeUsers(user, limit) {
    const gender = `gender=${user.swipe_gender} AND `
    const age = `age BETWEEN ${user.min_age} AND ${user.max_age} AND `
    const distance = `ST_Distance_Sphere(
      point(${user.longitude}, ${user.latitude}),
      point(longitude, latitude)
    ) < ${user.max_distance} AND `
    // eslint-disable-next-line prettier/prettier
    const passed = `NOT EXISTS (SELECT * FROM passed_users WHERE passed_users.passer_id=${user.id} AND passed_users.passed_id=user.id) AND `
    // eslint-disable-next-line prettier/prettier
    // const liked = `NOT EXISTS (SELECT * FROM user_likes WHERE user_likes.liker_id=${user.id} AND user_likes.liked_id=user.id) AND `
    const liked = ``
    const not_id = `id <> ${user.id}`
    const query = `SELECT * FROM user WHERE ${gender}${age}${distance}${passed}${liked}${not_id} LIMIT ${limit}`
    const client = this.client
    return toPromise(cb => {
      client.query(query, cb)
    })
  }
}

export default function(client) {
  return new UserRepository(client)
}
