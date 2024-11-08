package mira.pos
import groovy.sql.Sql
import grails.gorm.transactions.Transactional
import java.text.SimpleDateFormat


@Transactional
class UserService {

    def dataSource

    def getAllUsers() {
        Sql sql = new Sql(dataSource)

        try {
            def parseFormat = new SimpleDateFormat("yyyy-MM-dd")
            def formatter = new SimpleDateFormat("dd-MM-yyyy")
            String query = """
                SELECT c.username,
                    CONCAT(u.lastName, ', ', u.firstName, COALESCE(CONCAT(' ', u.middleName), '')) AS fullName,
                    u.user_id,
                    u.firstName,
                    u.middleName,
                    u.lastName,
                    u.dob,
                    ut.usertype_name AS position
                FROM users u
                JOIN credentials c ON u.user_id = c.user_id        
                JOIN usertypes ut ON c.usertype_id = ut.usertype_id 
                WHERE u.active = 1
            """

            def result = []
            sql.eachRow(query) { rs ->
                def res = [:]
                res.username = rs.getString('username')
                res.fullName = rs.getString('fullName')
                res.user_id = rs.getInt('user_id')
                res.firstName = rs.getString('firstName')
                res.middleName = rs.getString('middleName')
                res.lastName = rs.getString('lastName')
                res.position = rs.getString('position')

                Date dob = parseFormat.parse(rs.getString('dob'))
                res.dob = formatter.format(dob)

                result.add(res)
            }
            return result
        } catch (Exception e) {
            log.error("Error fetching user data: ${e.message}", e)
            return []
        } finally {
            sql.close()
        }
    }


   def createUser(String username, String password, String firstName, String middleName, String lastName, Long userType, String dob) {
        Sql sql = new Sql(dataSource)

        try {
            sql.withTransaction {
                // Parse the date string to a Date object
                Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dob) // Parse the date string

                // Insert into users table
                String userQuery = """
                    INSERT INTO users (firstname, middlename, lastname, dob)
                    VALUES (?, ?, ?, ?)
                """
                sql.execute(userQuery, [firstName, middleName, lastName, dateOfBirth])

                // Retrieve the last inserted user ID
                long lastUserId = sql.firstRow("SELECT LAST_INSERT_ID() as id").id

                // Insert into credentials table
                String credentialQuery = """
                    INSERT INTO credentials (username, password, user_id, usertype_id)
                    VALUES (?, ?, ?, ?)
                """
                sql.execute(credentialQuery, [username, password, lastUserId, userType])
            }
        } catch (Exception e) {
            log.error("Error creating user data: ${e.message}", e)
            return false // Return false on error
        } finally {
            sql.close() // Ensure the SQL connection is closed
        }
        return true // Indicate success
    }


    def updateUser(Long id, String firstName, String middleName, String lastName, String dob){
        Sql sql = new Sql(dataSource)

        try{
            Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dob)

            String query = """
                UPDATE users
                SET firstname = ?, middlename = ?, lastname = ?, dob =?
                WHERE user_id = ?
            """

            int rowsAffected = sql.executeUpdate(query, [firstName, middleName, lastName, dateOfBirth, id])

            return true
        }catch(Exception e){
            log.error("Error updating user with id ${id}: ${e.message}", e)
            return false
        }finally{
            sql.close()
        }
    }

    def deleteUser(Long id){
        Sql sql = new Sql(dataSource)
        
        try{
            String query = "UPDATE users SET active = 0 WHERE user_id = ?"
            int rowsAffected = sql.executeUpdate(query, [id])

            return rowsAffected > 0
        }catch (Exception e){
            log.error("Error deleting the user with id ${id}: ${e.message}", e)
            return false
        }finally{
            sql.close()
        }
    }
    
}
