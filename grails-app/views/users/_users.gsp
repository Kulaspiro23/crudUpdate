<div>
   <h3>User List</h3>
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
               <button class="btn btn-primary" onclick="editUser({{id}})">Edit</button>
               <button class="btn btn-danger" onclick="deleteUser({{id}})">Delete</button>
            </td>
         </tr>
         {{/each}}
      </tbody>
   </table>
</div>
