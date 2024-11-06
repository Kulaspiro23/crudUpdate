package mira.pos
import groovy.sql.Sql
import grails.gorm.transactions.Transactional
import java.text.SimpleDateFormat

@Transactional
class OrderService {

    def dataSource 

    def saveSale(Long userId, List<Map> products) {
        Sql sql = new Sql(dataSource)
        def orderId

        try {
            sql.withTransaction {
                sql.executeInsert("INSERT INTO orders (user_id, dos) VALUES (?, ?)", [userId, new Date()])

                orderId = sql.firstRow("SELECT LAST_INSERT_ID() AS id").id

                products.each { product ->
                println("Inserting sale for product: ${product.productId}, quantity: ${product.quantity}, price: ${product.price}")
 
                    sql.executeInsert("INSERT INTO sales (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)", 
                        [orderId, product.productId, product.quantity, product.price])

                    sql.executeUpdate("UPDATE products SET quantity = quantity - ? WHERE product_id = ?", 
                       [product.quantity, product.productId])
                }
            }
        } catch (Exception e) {
            log.error("Error while saving sale: ${e.message}", e)
            throw new RuntimeException("Could not save the sale. Please try again.")
        }

        return orderId 
    }
}