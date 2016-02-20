package twtr

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AccountController)
@Mock(Account)
class AccountControllerSpec extends Specification {

    final static String goodHandle = 'scsu_huskies'
    final static String goodEmail = 'testemail@test.com'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    def setup() {
    }

    def cleanup() {
    }

    def 'gets the requested account'() {
        setup:
        def account = new Account(handle: goodHandle, emailAddress: goodEmail,
                                  password: goodPassword, displayName: goodDisplayName).save(flush: true)
        params.id = account.id

        when:
        def model = controller.get()

        then:
        model.account.id == account.id
        model.account.handle == goodHandle
    }


}
