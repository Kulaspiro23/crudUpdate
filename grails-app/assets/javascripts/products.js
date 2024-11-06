let productRjs = new Ractive({ 
    el: '#container',
    template: '#productsRactive'
});

function fetchProductData() {
    $.ajax({
        method: 'GET',
        url: '/product/getProductData',
        success: function(data) {
            productRjs.set('products', data);
        },
        error: function(error) {
            console.error('Error fetching product data:', error);
        }
    }); 
}

function button(){
    $('#createProductForm').toggle();
    
}

function createProduct(event) {
    event.preventDefault();

    const isDiscounted = $('#discounted').is(':checked') ? 1 : 0;

    const productData = {
        product_name: $('#product_name').val(),
        sku: $('#sku').val(),
        barcode: $('#barcode').val(),
        quantity: $('#quantity').val(),
        price: $('#price').val(),
        discountedprice: $('#discountedprice').val(),
        is_discounted: isDiscounted
    };

    $.ajax({
        type: 'POST',
        url: '/product/createProduct',
        contentType: 'application/json',
        data: JSON.stringify(productData),
        success: function() {
            fetchProductData(); 
            $('#createProductForm').hide().get(0).reset(); 
        },
        error: function(xhr) {
            if (xhr.status === 409) {
                alert(xhr.responseText);
            } else {
            console.error('Error creating product:', xhr.responseText);
            } 
        }
    });
}

function deleteProduct(product_id) {
    if (confirm('Are you sure you want to delete this product?')) {
        $.ajax({
            type: 'DELETE',
            url: `/product/deleteProduct/${product_id}`,
            success: function() {
                alert('Product deleted successfully');
                fetchProductData(); 
            },
            error: function(xhr) {
                alert('Error deleting product: ' + xhr.responseText); 
            }
        });
    }
}

function editProduct(product_id) {
    const product = productRjs.get('products').find(p => p.product_id === product_id); 

    productRjs.set('activeId',product.product_id)


    if (product) {
        $('#editProduct_name').val(product.product_name);
        $('#editSku').val(product.sku);
        $('#editBarcode').val(product.barcode);
        $('#editQuantity').val(product.quantity);
        $('#editPrice').val(product.price);
        $('#editDiscountedprice').val(product.discountedprice);
        $('#editDiscounted').prop('checked', product.is_discounted === 1);

        $('#editProductForm').show();
    }
}

$('#updateProductButton').on('click', function(event) {
    event.preventDefault();

    const product_name = $('#editProduct_name').val().trim();
    const sku = $('#editSku').val().trim();
    const barcode = $('#editBarcode').val().trim();
    const quantity = $('#editQuantity').val()
    const price = $('#editPrice').val()
    const discountedprice = $('#editDiscountedprice').val()
    


    if (!product_name) {
        alert('Please enter the product name.');
        $('#editProduct_name').focus();
        return; 
    }
    
    if (!sku) {
        alert('Please enter the sku.');
        $('#editSku').focus();
        return; 
    }

    if (!barcode) {
        alert('Please enter the barcode.');
        $('#editBarcode').focus();
        return; 
    }

    if (!quantity) {
        alert('Please enter the quantity.');
        $('#editQuantity').focus();
        return; 
    }

    if (!price) {
        alert('Please enter the price.');
        $('#editPrice').focus();
        return; 
    }

    if (!discountedprice) {
        alert('Please enter the discounted price.');
        $('#editDiscountedprice').focus();
        return; 
    }

    const updatedProductData = {
        product_name: $('#editProduct_name').val(),
        sku: $('#editSku').val(),
        barcode: $('#editBarcode').val(),
        quantity: $('#editQuantity').val(),
        price: $('#editPrice').val(),
        discountedprice: $('#editDiscountedprice').val(),
        is_discounted: $('#editDiscounted').is(':checked') ? 1 : 0, 
        product_id: productRjs.get('activeId')
    };

    updateProduct(updatedProductData); 
});

function updateProduct(updatedProductData) {
    $.ajax({
        type: 'PUT',
        url: `/product/updateProduct/${updatedProductData.product_id}`,
        contentType: 'application/json',
        data: JSON.stringify(updatedProductData),
        success: function() {
            alert('Product updated successfully');
            fetchProductData();
            $('#editProductForm').hide(); 
        },
        error: function(xhr) {
            if (xhr.status === 409) {
                alert(xhr.responseText); 
            } else {
            alert('Error updating product: ' + xhr.responseText);
            }
        }
    });
}

fetchProductData()
