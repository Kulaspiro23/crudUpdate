package mira.pos
import groovy.sql.Sql
import grails.gorm.transactions.Transactional
import java.text.SimpleDateFormat


@Transactional
class ProductService {

    def dataSource

    def getAllProducts() {
        Sql sql = new Sql(dataSource)

        try {
            String query = """
                SELECT p.product_id,
                    p.product_name,
                    p.sku,
                    p.barcode,
                    p.quantity,
                    p.price,
                    p.discountedprice,
                    p.is_discounted,
                    CONCAT(u.lastName, ', ', u.firstName, COALESCE(CONCAT(' ', u.middleName), '')) AS fullName 
                FROM products p
                JOIN users u ON p.created_by = u.user_id
                WHERE p.active = 1; 
            """

            def result = []
            sql.eachRow(query) { rs ->
                def res = [:]
                res.product_id = rs.getLong('product_id')
                res.product_name = rs.getString('product_name')
                res.sku = rs.getString('sku')
                res.barcode = rs.getString('barcode')
                res.quantity = rs.getInt('quantity')
                res.price = rs.getInt('price')
                res.discountedprice = rs.getInt('discountedprice')
                res.is_discounted = rs.getInt('is_discounted')
                res.fullName = rs.getString('fullName')
            
                result.add(res)
            }
            return result
        } catch (Exception e) {
            log.error("Error fetching product data: ${e.message}", e)
            return []
        } finally {
            sql.close()
        }
    }

    def createProduct(String product_name, String sku, String barcode, int quantity, int price, int discountedprice, int isDiscounted, Long createdBy) {
        Sql sql = new Sql(dataSource)

        try {
            if (checkDuplicateFields(null, product_name, sku, barcode)) {
            return [status: 'duplicate', message: 'Product name, SKU, or barcode already exists.']
            }

                String query = """
                    INSERT INTO products (product_name, sku, barcode, quantity, price, discountedprice, is_discounted, created_by)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """
                sql.execute(query, [product_name, sku, barcode, quantity, price, discountedprice, isDiscounted, createdBy])

                long lastProductId = sql.firstRow("SELECT LAST_INSERT_ID() as id").id
            return [status: 'success']
        } catch (Exception e) {
            log.error("Error creating product data: ${e.message}", e)
            return false 
        } finally {
            sql.close() 
        }
        return true 
    }

    def updateProduct(Long id, String product_name, String sku, String barcode, int quantity, int price, int discountedprice, int is_discounted){
        Sql sql = new Sql(dataSource)

        try{
            if (checkDuplicateFields(id, product_name, sku, barcode)) {
            return [status: 'duplicate', message: 'Product name, SKU, or barcode already exists.']
            }

            String query = """
                UPDATE products
                SET product_name = ?, sku = ?, barcode = ?, quantity = ?, price = ?, discountedprice = ?, is_discounted = ?
                WHERE product_id = ?
            """

            int rowsAffected = sql.executeUpdate(query, [product_name, sku, barcode, quantity, price, discountedprice, is_discounted, id])

            if (rowsAffected > 0) {
            return [status: 'success', message: 'Product updated successfully']
            } else {
                return [status: 'not_found', message: 'Product not found']
            }
        } catch (Exception e) {
            log.error("Error updating product with id ${id}: ${e.message}", e)
            return [status: 'error', message: 'Error updating product']
        } finally {
            sql.close()
        }
    }

    def deleteProduct(Long id){
        Sql sql = new Sql(dataSource)
        
        try{
            String query = "UPDATE products SET active = 0 WHERE product_id = ?"
            int rowsAffected = sql.executeUpdate(query, [id])

            return rowsAffected > 0
        }catch (Exception e){
            log.error("Error deleting the product with id ${id}: ${e.message}", e)
            return false
        }finally{
            sql.close()
        }
    }

    def checkDuplicateFields(Long id, String product_name, String sku, String barcode) {
        Sql sql = new Sql(dataSource)

        try {
            String query = """
                SELECT COUNT(*) as count
                FROM products
                WHERE (product_name = ? OR sku = ? OR barcode = ?)
            """

            if (id) {
                query += " AND product_id != ?"
                return sql.firstRow(query, [product_name, sku, barcode, id])?.count > 0
            } else {
                return sql.firstRow(query, [product_name, sku, barcode])?.count > 0
            }
        } catch (Exception e) {
            log.error("Error checking for duplicates: ${e.message}", e)
            return false
        } finally {
            sql.close()
        }
    }

    def searchProducts(String searchTerm) {
        Sql sql = new Sql(dataSource)
        def result = []
        searchTerm = "%${searchTerm}%"
        try {
            String query = """
                SELECT product_id, product_name, price, quantity
                FROM products
                WHERE LOWER(product_name) LIKE ? AND active = 1
            """
            sql.eachRow(query, [searchTerm]) { rs ->
                def res = [:]
                res.product_id = rs.getLong('product_id')
                res.product_name = rs.getString('product_name')
                res.price = rs.getInt('price')
                res.quantity = rs.getInt('quantity')
                result.add(res)
                println(res)
            }
        } catch (Exception e) {
            log.error("Error searching for products: ${e.message}", e)
        } finally {
            sql.close()
        }
        return result
    }

    def insertSale(Long orderId, Long productId, int quantity, int price) {
        Sql sql = new Sql(dataSource)
        def insertedSale = [:]
        try {
            String query = """
                INSERT INTO sales (sale_id, order_id, product_id, quantity, price)
                VALUES (?, ?, ?, ?, ?)
            """
            sql.executeInsert(query, [saleId, orderId, productId, quantity, price])

            String selectQuery = """
                SELECT product_id, product_name, price, quantity
                FROM products
                WHERE product_id = ?
            """
            sql.eachRow(selectQuery, [productId]) { rs ->
                insertedSale.product_id = rs.getLong('product_id')
                insertedSale.product_name = rs.getString('product_name')
                insertedSale.price = rs.getInt('price')
                insertedSale.quantity = rs.getInt('quantity')
            }
            return insertedSale
        } catch (Exception e) {
            log.error("Error inserting sale record: ${e.message}", e)
            return null
        } finally {
            sql.close()
        }
    }

    def updateProductStock(Long productId, int quantitySold) {
        Sql sql = new Sql(dataSource)
        def updatedProduct = [:]
        try {
            String query = """
                UPDATE products
                SET quantity = quantity - ?
                WHERE product_id = ? AND quantity >= ?
            """
            int rowsUpdated = sql.executeUpdate(query, [quantitySold, productId, quantitySold])

            if (rowsUpdated > 0) {
                String selectQuery = """
                    SELECT product_id, product_name, quantity
                    FROM products
                    WHERE product_id = ?
                """
                sql.eachRow(selectQuery, [productId]) { rs ->
                    updatedProduct.product_id = rs.getLong('product_id')
                    updatedProduct.product_name = rs.getString('product_name')
                    updatedProduct.quantity = rs.getInt('quantity')
                }
                return updatedProduct
            } else {
                return null
            }
        } catch (Exception e) {
            log.error("Error updating product stock: ${e.message}", e)
            return null
        } finally {
            sql.close()
        }
    }

    def createOrder(Long userId) {
        Sql sql = new Sql(dataSource)
        try {
            String query = """
                INSERT INTO orders (user_id, dos)
                VALUES (?, ?)
            """
            def dos = new Date() // current timestamp
            def result = sql.executeInsert(query, [userId, dos])
            return result[0][0] // Returning the order_id
        } catch (Exception e) {
            log.error("Error creating order: ${e.message}", e)
            throw new RuntimeException("Error creating order")
        } finally {
            sql.close()
        }
    }

}
