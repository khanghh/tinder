$(document ).ready(function() {
  $('#btn-visible').click(function() {
      var $pwd = $('#inputPassword');
      if ($pwd.attr('type') === 'password') {
          $pwd.attr('type', 'text');
          $('#btn-visible').text('visibility_off');
      } else {
          $pwd.attr('type', 'password');
          $('#btn-visible').text('visibility');
      }
  });
  $('#signup-form').submit(function() {
    var password = $('#inputPassword').val();
    $('#inputPassword').val(md5(password))
  })
})
