export default function(cb) {
  return new Promise((ok, fail) => {
    cb((err, results) => (err ? fail(err) : ok(results)))
  })
}
