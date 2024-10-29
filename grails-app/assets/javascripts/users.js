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
    if (user) {
        console.log("User found for editing:", user)
        // Populate the edit form with user data
        $('#editFirstName').val(user.firstName);
        $('#editMiddleName').val(user.middleName || '');
        $('#editLastName').val(user.lastName);
        $('#editDob').val(user.dob);

        $('#editUserForm').show(); // Show edit form

        // Handle edit form submission
        $('#editUserForm').off('submit').on('submit', function(event) {
            event.preventDefault();

            const updatedUserData = {
                firstName: $('#editFirstName').val(),
                lastName: $('#editLastName').val(),
                middleName: $('#editMiddleName').val(),
                dob: $('#editDob').val(),
                user_id: user.user_id
            };
            console.log("Updating user data:", updatedUserData); // Log updated user data
 
            updateUser(updatedUserData); // Call update user function
        });
    }else {
        console.error("User not found with user_id:", user_id); // Log if user is not found
    }
}

function updateUser(updatedUserData) {
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
    console.log("Updating user with ID:", updatedUserData.user_id); // Ensure user_id is not undefined

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
