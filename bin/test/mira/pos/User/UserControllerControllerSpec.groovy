package mira.pos

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class User/UserControllerControllerSpec extends Specification implements ControllerUnitTest<User/UserControllerController> {

     void "test index action"() {
        when:
        controller.index()

        then:
        status == 200

     }
}
