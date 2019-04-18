$('#btn-visible').click(function() {
    var $pwd = $('#inputPassword');
    if ($pwd.attr('type') === 'password') {
        $pwd.attr('type', 'text');
        $('#btn-visible').text('visibility_off')
    } else {
        $pwd.attr('type', 'password');
        $('#btn-visible').text('visibility')
    }
})
