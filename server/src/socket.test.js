import socketClient from 'socket.io-client'
function sleep(time) {
  return new Promise(resolve => setTimeout(resolve, time))
}

test('test socket', async () => {
  const socket = socketClient.connect('http://0.0.0.0:8889', {
    query: 'foo=bar'
  })
  await sleep(1000)
  socket.emit('message', { message: 'sfb' })
  // socket.on('connect', () => {
  //   socket
  //     .emit('authenticate', {
  //       token:
  //         'eyJhbGciOiJIUzI1NiIs5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxNSwiaWF0IjoxNTU2MDg0OTEwLCJleHAiOjE1NTg2NzY5MTB9.nb-zzOGuW0bzwfJWJr5t7jHstGbaqh67fbRqF0tF8Hw'
  //     }) //send the jwt
  //     .on('authenticated', () => {
  //       console.log('aaaas')
  //     })
  //     .on('unauthorized', msg => {
  //       console.log('unauthorized: ' + JSON.stringify(msg.data))
  //       throw new Error(msg.data.type)
  //     })
  // })
  await sleep(3000)
  socket.disconnect()
})
