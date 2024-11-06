<div class="mb-4">
    <a href="${createLink(controller: 'home', action: 'index')}">Back</a>
    <br/>
    <h4>Product List</h4>
    <button class="btn btn-success mb-3" id="createProductBtn" type="button" onClick="button()">Create Product</button>
    <div id="productList">
        {{#each products}}
        <div class="card mb-3">
            <div class="card-body">
                    <div class="col-md-4">
                        <strong>Product Name:</strong> <span>{{product_name}}</span>
                    </div>
                    <div class="col-md-4">
                        <strong>SKU:</strong> <span>{{sku}}</span>
                    </div>
                    <div class="col-md-4">
                        <strong>Barcode:</strong> <span>{{barcode}}</span>
                    </div>
                    <div class="col-md-4">
                        <strong>Quantity:</strong> <span>{{quantity}}</span>
                    </div>
                    <div class="col-md-4">
                        <strong>Price:</strong> <span>{{price}}</span>
                    </div>
                    <div class="col-md-4">
                        <strong>Discounted Price:</strong> <span>{{discountedprice}}</span>
                    </div>
                    <div class="col-md-4">
                        <strong>Discounted:</strong> 
                        <input type="checkbox" id="discounted-{{product_id}}" {{#if is_discounted}}checked{{/if}} disabled>
                    </div>
                    <div class="col-md-6">
                        <strong>Created by:</strong> <span>{{fullName}}</span>
                    </div>
                <div class="mt-3">
                    <button class="btn btn-primary btn-sm" onclick="editProduct({{product_id}})">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteProduct({{product_id}})">Delete</button>
                </div>
            </div>
        </div>
        {{/each}}
    </div>
</div>
