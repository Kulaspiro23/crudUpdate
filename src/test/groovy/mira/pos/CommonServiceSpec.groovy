package mira.pos

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class CommonServiceSpec extends Specification implements ServiceUnitTest<CommonService> {

     void "test something"() {
        expect:
        service.doSomething()
     }
}
