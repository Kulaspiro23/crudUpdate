let homeRjs = new Ractive({ 
    el: '#container',
    template: '#usersRactive'
});

function fetchUserData() {

    $.ajax({
        method: 'GET',
        url: '/user/getUserData',
        success: function(data) {
            homeRjs.set('users', data);
        },
        error: function(error) {
            console.error('Error fetching user data:', error); // Handle errors
        }
    });
}

function createUser(event) {
    event.preventDefault();

    const userData = {
        username: $('#username').val(),
        password: $('#password').val(),
        firstName: $('#firstName').val(),
        middleName: $('#middleName').val(),
        lastName: $('#lastName').val(),
        userType: parseInt($('#userType').val(), 10),
        dob: $('#dob').val()
    };

    $.ajax({
        type: 'POST',
        url: '/user/createUser',
        contentType: 'application/json',
        data: JSON.stringify(userData),
        success: function() {
            console.log('User created successfully');
            fetchUserData(); // Refresh user data
            $('#createUserForm').hide().get(0).reset(); // Hide and reset form
        },
        error: function(xhr) {
            console.error('Error creating user:', xhr.responseText); // Handle errors
        }
    });
}

function editUser(user_id) {
    const user = homeRjs.get('users').find(u => u.user_id === user_id); // Find user by ID

    homeRjs.set('activeId',user.user_id)

    if (user) {
        $('#editFirstName').val(user.firstName);
        $('#editMiddleName').val(user.middleName || '');
        $('#editLastName').val(user.lastName);

        if (user.dob) {
            const [day, month, year] = user.dob.split('-');
            const formattedDate = `${year}-${month}-${day}`;
            $('#editDob').val(formattedDate);
        }

        $('#editUserForm').show(); // Show edit form
    }
}

$('#updateUserButton').on('click', function(event) {
    event.preventDefault();

    const firstName = $('#editFirstName').val().trim();
    const lastName = $('#editLastName').val().trim();
    const dob = $('#editDob').val().trim();

    if (!firstName) {
        alert('Please enter the first name.');
        $('#editFirstName').focus();
        return; 
    }
    
    if (!lastName) {
        alert('Please enter the last name.');
        $('#editLastName').focus();
        return; 
    }

    if (!dob) {
        alert('Please enter the date of birth.');
        $('#editDob').focus();
        return; 
    }

    const updatedUserData = {
        firstName: $('#editFirstName').val(),
        lastName: $('#editLastName').val(),
        middleName: $('#editMiddleName').val(),
        dob: $('#editDob').val(),
        user_id: homeRjs.get('activeId')
    };

    updateUser(updatedUserData); 
});

function updateUser(updatedUserData) {
    console.log("Updating user with ID:", updatedUserData.user_id)
    $.ajax({
        type: 'PUT',
        url: `/user/updateUser/${updatedUserData.user_id}`,
        contentType: 'application/json',
        data: JSON.stringify(updatedUserData),
        success: function() {
            alert('User updated successfully');
            fetchUserData(); // Refresh user data
            $('#editUserForm').hide(); // Hide edit form
        },
        error: function(xhr) {
            alert('Error updating user: ' + xhr.responseText); // Handle errors
        }
    });
}

function deleteUser(user_id) {
    if (confirm('Are you sure you want to delete this user?')) {
        $.ajax({
            type: 'DELETE',
            url: `/user/deleteUser/${user_id}`,
            success: function() {
                alert('User deleted successfully');
                fetchUserData(); // Refresh user data
            },
            error: function(xhr) {
                alert('Error deleting user: ' + xhr.responseText); // Handle errors
            }
        });
    }
}

function test(){
    $('#createUserForm').toggle();
}
    
fetchUserData();
