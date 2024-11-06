let orderRjs = new Ractive({
    el: '#container',
    template: '#ordersRactive',
    data: {
        products: [],
        fullName: '',
        userType: '',
        currentDateTime: '',
        totalAmount: 0,
        searchResults: []
    },
    updateTotalAmount: function () {
        let totalAmount = this.get('products').reduce(function (total, product) {
            return total + (product.quantity * product.price);
        }, 0);
        this.set('totalAmount', totalAmount); 
    },
    addProduct: function (event) {
        let productId = event.productId;
        let productName = event.productName;
        let productPrice = event.price;
        let productQuantity = event.quantity; 

    
        console.log("Product :", event); // Log to check
    
        // Show SweetAlert if the quantity is 0
        if (productQuantity === 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Oops...',
                text: 'This product has a quantity of 0 and cannot be added!',
            });
            return; // Prevent further execution if quantity is 0
        }
    
        // Check if the product already exists in the cart
        let existingProduct = this.get('products').find(product => product.productId === productId);
    
        if (existingProduct) {
            // Increment the quantity of the existing product by 1
            existingProduct.quantity += 1;
        } else {
            // Add new product to the cart with the quantity
            this.push('products', {
                productId: productId,
                productName: productName,
                quantity: productQuantity,  // Use the productQuantity (either from event or default)
                price: productPrice
            });
        }
    
        // Update total amount after adding the product
        this.updateTotalAmount();
    
        // Clear search bar and results after adding product
        $('#searchBar').val('');
        orderRjs.set('searchResults', []);
    },
    
    increaseQuantity: function (event) {
        let productId = event.productId;
        let product = this.get('products').find(p => p.productId === productId);
        if (product) {
            product.quantity += 1;
            this.updateTotalAmount();
            this.update();
        }
    },
    
    decreaseQuantity: function (event) {
        let productId = event.productId;
        let product = this.get('products').find(p => p.productId === productId);
        if (product && product.quantity > 0) {
            product.quantity -= 1;
            if (product.quantity === 0) {
                // If the quantity is 0, remove the product from the cart
                this.set('products', this.get('products').filter(p => p.productId !== productId));
            }
            this.updateTotalAmount();
            this.update();
        }
    },

    updateQuantityFromInput: function (event) {
        let productId = event.productId;
        let newQuantity = parseInt(event.newQuantity);
        let product = this.get('products').find(p => p.productId === productId);
        if (product && newQuantity >= 0) {
            product.quantity = newQuantity;
            if (product.quantity === 0) {
                // If the quantity is 0, remove the product from the cart
                this.set('products', this.get('products').filter(p => p.productId !== productId));
            }
            this.updateTotalAmount();
        }
    }
});


// Function to retrieve home session data
function home() {
    $.ajax({
        method: 'GET',
        url: '/order/home',
        success: function (data) {
            orderRjs.set('fullName', data.result.userDetails.fullName);
            orderRjs.set('userType', data.result.userDetails.userType);
            orderRjs.set('currentDateTime', data.result.currentDateTime);
        },
        error: function (error) {
            console.error('Error fetching session data:', error);
        }
    });
}

// Search functionality
$(document).on('input', '#searchBar', function () {
    let query = $(this).val().trim().toLowerCase();
    if (query.length >= 3) {
        searchProduct(query);
    } else {
        orderRjs.set('searchResults', []);
    }
});

// Function to search products based on input query
function searchProduct(query) {
    $.ajax({
        method: 'GET',
        url: '/order/searchProduct',
        data: { search: query },
        success: function (data) {
            console.log(data.products);  // Check the exact structure of the returned products
            let products = data.products.length ? data.products : [{ message: 'No products found' }];
            orderRjs.set('searchResults', products);
            if (products.length > 0) {
                $('#searchResults').show();
            } else {
                $('#searchResults').hide();
            }
        },
        error: function (error) {
            console.error('Error searching for products:', error);
        }
    });
}





// Submit button click event to show a confirmation modal
$(document).on('click', '#submitBtn', function () {
    let products = orderRjs.get('products');

    // Calculate the total amount
    let totalAmount = 0;
    let $productList = $('#productList').empty();

    products.forEach(product => {
        totalAmount += product.quantity * product.price;

        // Add product to modal
        let row = `<tr data-product-id="${product.productId}">
                       <td>${product.productName}</td>
                       <td>${product.quantity}</td>
                       <td>${product.price}</td>
                   </tr>`;
        $productList.append(row);
    });

    $('#modalTotal').text('PHP ' + totalAmount);

    // Show confirmation modal
    $('#confirmationModal').modal('show');
});

// Confirmation modal submit
$(document).on('click', '#confirmSubmit', function () {
    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, submit it!',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            let products = [];
            $('#productList tr').each(function () {
                let productId = $(this).data('product-id');
                let productName = $(this).find('td').eq(0).text();
                let quantity = parseInt($(this).find('td').eq(1).text());
                let price = parseInt($(this).find('td').eq(2).text());

                if (productId && quantity > 0 && price > 0) {
                    products.push({
                        productId: productId,
                        name: productName,
                        quantity: quantity,
                        price: price
                    });
                }
            });

            // Submit sale to the server
            $.ajax({
                method: 'POST',
                url: '/order/submitSale',
                contentType: 'application/json',
                data: JSON.stringify({ products: products }),
                success: function (response) {
                    Swal.fire('Success!', 'Your sale has been submitted.', 'success');
                    $('#confirmationModal').modal('hide');
                },
                error: function (error) {
                    Swal.fire('Error!', 'There was an issue submitting your sale.', 'error');
                }
            });
        }
    });
});

// Cancel sale functionality
$(document).on('click', '.btn-secondary', function () {
    Swal.fire({
        title: 'Cancel Sale?',
        text: "Are you sure you want to cancel the sale?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, cancel it',
        cancelButtonText: 'No, keep it'
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire('Cancelled', 'The sale has been cancelled.', 'info');
            $('#confirmationModal').modal('hide');
        }
    });
});

// Fetch session data
home();
