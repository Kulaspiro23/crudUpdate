$(document).on('click','#btnLogin',function(){
    let username = $('#txtUsername').val().trim();
    let password = $('#txtPassword').val().trim();

    if(username.length == 0){
        alert('username cannot be empty!');
        console.log(username)
        return;
    }

    
    if(password.length == 0){
        alert('Password cannot be empty!');
        return;
    }

    let url = "login/authenticateUser";

    let data = {
        username:username,
        password:password
    }

    $.ajax({
        type : 'POST',
        data : data,
        url:url,
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
