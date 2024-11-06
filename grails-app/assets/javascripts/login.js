$(document).on('click','#btnLogin',function(){
    let username = $('#txtUsername').val().trim();
    let password = $('#txtPassword').val().trim();

    if(!username){
        alert('Username cannot be empty!');
        return;
    }

    
    if(!password){
        alert('Password cannot be empty!');
        return;
    }

    $.ajax({
        type : 'POST',
        data : {
            username: username,
            password: password
        },
        url: '/login/authenticateUser',
        success: function(res){
            let n = res.response.result;
            if(n.success){
                window.location.href = n.redirect;
            }
        },
        error: function(res){
            console.log(res);
        }
    });
});
