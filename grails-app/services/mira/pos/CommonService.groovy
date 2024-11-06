package mira.pos
import groovy.sql.Sql
import grails.gorm.transactions.Transactional
import java.text.SimpleDateFormat

@Transactional
class CommonService {

    def dataSource

    def getDbDateTime() {
        Sql sql = new Sql(dataSource)
        String query = "select now() as currentDateTime"
        def result = ""
        sql.eachRow(query) { rs ->
        result = rs.getString('currentDateTime')}
        return result
    }
}
 