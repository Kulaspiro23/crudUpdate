   <h3>User List</h3>
   <button id="createUserBtn" type="button" onClick="test()">Create User</button>
   <form id="createUserForm" style="display: none" onsubmit="createUser(event)">
      <label for="username">Username:</label>
      <input type="text" id="username" name="username" required>

      <label for="password">Password:</label>
      <input type="text" id="password" name="password" required>

      <label for="firstName">First Name:</label>
      <input type="text" id="firstName" name="firstName" required>

      <label for="middleName">Middle Name:</label>
      <input type="text" id="middleName" name="middleName">

      <label for="lastName">Last Name:</label>
      <input type="text" id="lastName" name="lastName" required>

      <label for="dob">Position:</label>
      <select id="userType" name="userType" required>
         <option value="1">Admin</option>
         <option value="2">Manager</option>
         <option value="3">Cashier</option>
      </select>

      <label for="dob">DOB:</label>
      <input type="date" id="dob" name="dob" required>
      <button type="submit">Create User</button>
   </form>

   <form id="editUserForm" style="display: none" onsubmit="updateUser(event)">
      <label for="firstName">First Name:</label>
      <input type="text" id="editFirstName" name="firstName" required>
      <label for="middleName">Middle Name:</label>
      <input type="text" id="editMiddleName" name="middleName">
      <label for="lastName">Last Name:</label>
      <input type="text" id="editLastName" name="lastName" required>
      <label for="dob">Dob:</label>
      <input type="date" id="editDob" name="dob" required>
      <button type="submit">Update User</button>
   </form>
   <table class="table">
      <thead>
         <tr>
            <th>Full Name</th>
            <th>Date of Birth</th>
            <th>Position</th>
            <th>Actions</th>
         </tr>
      </thead>
      <tbody>
         {{#each users}}
         <tr>
            <td>{{fullName}}</td>
            <td>{{dob}}</td>
            <td>{{position}}</td>
            <td>
               <button class="btn btn-primary" onclick="editUser({{user_id}})">Edit</button>
               <button class="btn btn-danger" onclick="deleteUser({{user_id}})">Delete</button>
            </td>
         </tr>
         {{/each}}
      </tbody>
   </table>