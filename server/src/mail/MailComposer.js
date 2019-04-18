import ejs from 'ejs'
import config from '../config'
import querystring from 'querystring'
import path from 'path'

const templateDir = path.join(__dirname, 'template')

export default {
  async compose_activate_user(sender, receiver, name, activateToken) {
    const query = { token: activateToken }
    const activate_url = config.homeUrl + '/activate_user?' + querystring.stringify(query)

    return {
      from: sender,
      to: receiver,
      subject: 'Activate your account',
      html: await ejs.renderFile(path.join(templateDir, 'activate_user.ejs'), { name, activate_url })
    }
  }
}
