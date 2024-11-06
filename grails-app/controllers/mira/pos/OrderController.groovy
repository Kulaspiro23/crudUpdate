package mira.pos
import grails.converters.JSON
import java.text.SimpleDateFormat 
import org.springframework.http.HttpStatus


class OrderController {

    def productService
    def userService
    def loginService
    def commonService

    def index() {

    }

    def home() {
        def userDetails = [:]

        userDetails.fullName = session.fullName
        userDetails.userType = session.user_type 


        def res = [:]
        res.userDetails = userDetails
        res.currentDateTime = commonService.getDbDateTime()

        render(contentType: "application/json") {
            result res
    }
    }

    def order(){
        render(view: "/orders/orders", model: [fullName: session.fullName, userType: session.user_type])
    }

    def searchProduct() {
        String searchQuery = params.search ?: ''
        def products = productService.searchProducts(searchQuery)
        render(contentType: "application/json", text: [products : products] as JSON)
    }

    def submitSale() {
    try {
        def products = request.JSON.products // Get products from the request

        products.each { product ->
            Long productId = product.productId ? product.productId.toLong() : null
            Integer quantity = product.quantity ? product.quantity.toInteger() : null
            Integer price = product.price ? product.price.toInteger() : null

            // Handle null/empty values if needed
            if (productId && quantity > 0 && price > 0) {
                // Call the service method to insert the sale or perform other actions
                productService.insertSale(productId, quantity, price) 
            } else {
                throw new IllegalArgumentException("Invalid product data")
            }
        }

        render status: 200, text: "Sale successfully submitted"

    } catch (Exception e) {
        log.error("Error submitting sale: ${e.message}", e)
        render status: 500, text: "Error submitting sale"
    }
}

}