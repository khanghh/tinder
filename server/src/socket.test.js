import socketClient from 'socket.io-client'

test('test socket', async () => {
  const socket = socketClient('http://0.0.0.0:8889')
  socket.on('connect', () => {
    socket.emit('send_message', { message: 'teste' })
  })
  socket.on('event', data => {})
  socket.on('disconnect', () => {})

  // expect(data.).toBe(2)
})
