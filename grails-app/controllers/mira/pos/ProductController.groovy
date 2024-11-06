package mira.pos
import grails.converters.JSON
import java.text.SimpleDateFormat 

class ProductController {

    def productService

    def index() {

    }

    def home() {
        if (!session.username) {
            redirect(controller: 'login', action: 'index')
            return
        }
        render(view: "/products/products", model: [userFullName: session.fullName])
    }

    def getProductData() {
        try {
            List<Map> productList = productService.getAllProducts()
            render productList as JSON
        } catch (Exception e) {
            log.error("Error fetching product data: ${e.message}", e)
            render(status: 500, text: 'Internal Server Error')  // Return a 500 error response
        }
    }

    def createProduct() {
        try {
            def requestData = request.JSON 

            String product_name = requestData.product_name
            String sku = requestData.sku
            String barcode = requestData.barcode
            int quantity = requestData.quantity as Integer
            int price = requestData.price as Integer
            int discountedprice = requestData.discountedprice as Integer
            Long createdBy = session.user_id

            int isDiscounted = requestData.is_discounted ?: 0

            def result = productService.createProduct(product_name, sku, barcode, quantity, price, discountedprice, isDiscounted, createdBy)
        
            if (result.status == 'duplicate') {
                render status: 409, text: result.message
            } else if (result.status == 'success') {
                render status: 201, text: result.message
            } else {
                render status: 500, text: result.message
            }
        } catch (Exception e) {
            log.error("Error creating product: ${e.message}", e)
            render status: 500, text: 'Internal Server Error'
        }
    }

    def updateProduct(Long id){
        try{
            def requestData = request.JSON

            

            String product_name = requestData.product_name
            String sku = requestData.sku
            String barcode = requestData.barcode
            int quantity = requestData.quantity?.toString()?.toInteger()
            int price = requestData.price?.toString()?.toInteger()
            int discountedprice = requestData.discountedprice?.toString()?.toInteger()
            int is_discounted = requestData.is_discounted?.toString()?.toInteger()

            def result = productService.updateProduct(id, product_name, sku, barcode, quantity, price, discountedprice, is_discounted)

            switch (result.status) {
                case 'duplicate':
                    render status: 409, text: result.message
                    break
                case 'success':
                    render status: 200, text: result.message
                    break
                case 'not_found':
                    render status: 404, text: result.message
                    break
                default:
                    render status: 500, text: 'Error updating product'
            }
        } catch (Exception e) {
            log.error("Error updating product: ${e.message}", e)
            render status: 500, text: 'Internal Server Error'
        }
    }

    def deleteProduct(Long id){
        try{
            if (productService.deleteProduct(id)){
                render status: 204
            }else{
                render status: 404
            }
        }catch(Exception e){
            log.error("Error deleting product id ${id}: ${e.message}", e)
            render status: 500, text: "An error occured while deleting the product"
        }
    }
    
}