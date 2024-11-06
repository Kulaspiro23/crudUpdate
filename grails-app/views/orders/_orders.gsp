<div class="row">
    <div class="col-md-6 border-right pr-4">
        <h4>Order Details</h4>
        <form id="orderForm">
            <div class="form-group d-flex justify-content-between">
                <div style="width: 48%;">
                    <strong>Name:</strong> <span>{{fullName}}</span>
                </div>
                <div style="width: 48%;">
                    <strong>Date and Time:</strong> <span>{{currentDateTime}}</span>
                </div>
            </div>
            <div class="form-group mt-3">
                <strong>Position:</strong> <span>{{userType}}</span>
            </div>

            <h5 class="mt-4">Products</h5>
            <div class="product-row d-flex justify-content-between">
                <span style="flex: 2;">Product Name</span>
                <span style="flex: 1; text-align: center;">Quantity</span>
                <span style="flex: 1; text-align: right;">Price</span>
            </div>

            <div id="orderList">
                {{#each products}}
                    <div class="product-row d-flex justify-content-between my-2" data-product-id="{{productId}}">
                        <span class="product-name" style="flex: 2;">{{productName}}</span>
                        <div class="quantity-controls d-flex align-items-center">
                            <!-- Bind decrease quantity to Ractive method -->
                            <button type="button" class="btn btn-outline-secondary" on-click="@this.decreaseQuantity({ productId: productId })">-</button>
                            <input type="number" class="product-quantity" value="{{quantity}}" min="0" style="width: 60px; text-align: center;" 
                                on-input="@this.updateQuantityFromInput({ productId: productId, newQuantity: event.node.value })">
                            <!-- Bind increase quantity to Ractive method -->
                            <button type="button" class="btn btn-outline-secondary" on-click="@this.increaseQuantity({ productId: productId })">+</button>
                        </div>
                        <span class="product-price" style="flex: 1; text-align: right;">PHP {{price}}</span>
                    </div>
                {{/each}}
            </div>

        </form>

        <div id="totalAmountContainer" class="d-flex justify-content-end mt-2">
            <strong>Total:</strong> <span id="totalAmount" class="ml-2">{{totalAmount}}</span>
        </div>
    </div>

    <div class="col-md-6 pl-4">
        <h4>Search Product</h4>
        <div class="form-group">
            <input type="text" class="form-control" id="searchBar" placeholder="Search product">
            <ul id="searchResults" class="list-group mt-2" style="display: none;">
                {{#each searchResults}}
                    <li class="list-group-item" 
                        data-product-id="{{product_id}}" 
                        data-product-name="{{product_name}}" 
                        data-product-quantity="{{quantity}}" 
                        data-product-price="{{price}}" 
                        on-click="@this.addProduct({
                            productId: product_id, 
                            productName: product_name, 
                            price: price,
                            quantity: quantity
                        })">
                        {{#if message}}
                            {{message}}
                        {{else}}
                            <strong>{{product_name}}</strong> 
                            <span class="quantity">({{quantity}} in stock)</span> 
                            - PHP {{price}}
                        {{/if}}
                    </li>


                {{/each}}
            </ul>

        </div>

        <div class="d-flex justify-content-end mt-3">
            <button type="button" class="btn btn-success mr-2" id="submitBtn">Submit</button>
            <button type="button" class="btn btn-secondary" id="cancelBtn">Cancel</button>
        </div>
    </div>
</div>



<div id="confirmationModal" class="modal" tabindex="-1" role="dialog" style="display: none;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Sale Confirmation</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" label="Close"></button>
      </div>
      <div class="modal-body">
        <p><strong>Sale ID:</strong> <span id="saleId"></span></p>
        <table class="table">
          <thead>
            <tr>
              <th>Product Name</th>
              <th>Quantity</th>
              <th>Price</th>
            </tr>
          </thead>
          <tbody id="productList">
          </tbody>
        </table>
        <div class="d-flex justify-content-end">
          <strong>Total:</strong> <span id="modalTotal" class="ml-2"></span>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="confirmSubmit">Submit</button>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
      </div>
    </div>
  </div>
</div>
