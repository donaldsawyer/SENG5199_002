package project1

import grails.test.mixin.TestFor
import org.mockito.BDDMockito
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(HelloController)
class HelloControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        when:
        controller.index()

        then:
        response.text == "hello world"
    }
}
