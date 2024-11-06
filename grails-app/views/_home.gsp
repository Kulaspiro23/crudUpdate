<div>
   <h3>Welcome : {{userDetails.fullName}}</h3>

    <a href="${createLink(controller: 'user', action: 'home')}">Manage User Accounts</a>
     <br/>
    <a href="${createLink(controller: 'product', action: 'home')}">Manage Products</a>
    <br/>
    <a href="${createLink(controller: 'order', action: 'order')}">Orders</a>
    <br/>
    <a href="${createLink(controller: 'login', action: 'logout')}">Logout</a> 

</div>