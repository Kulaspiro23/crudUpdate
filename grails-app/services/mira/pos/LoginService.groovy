package mira.pos

import grails.gorm.transactions.Transactional
import groovy.sql.Sql

@Transactional
class LoginService {

    def dataSource

    def authenticate(String username, String password) {
        def userDetails = [:]

        Sql sql = new Sql(dataSource)

        def query = """
            SELECT u.user_id, u.firstname, u.middlename, u.lastname, u.dob, ut.usertype_name
            FROM users u
            JOIN credentials c ON u.user_id = c.user_id
            JOIN usertypes ut ON c.usertype_id = ut.usertype_id
            WHERE c.username = ? AND c.password = ? AND u.active = 1
        """


        sql.eachRow(query, [username, password]) { row ->
            userDetails.user_id = row.user_id
            userDetails.fullName = "${row.lastname}, ${row.firstname} ${row.middlename ?: ''}".trim()
            userDetails.dob = row.dob
            userDetails.usertype_name = row.usertype_name
        }

        return userDetails ?: null
    }
    
}
