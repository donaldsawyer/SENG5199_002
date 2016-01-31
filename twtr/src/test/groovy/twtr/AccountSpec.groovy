package twtr

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Account)
class AccountSpec extends Specification {

    def setup() {

    }

    def cleanup() {

    }

    void "save account where required fields are valid :-)"() {
        setup:
        Account sut = new Account(handle: "@scsu-huskies", emailAddress: "testemail@test.com",
                password: "abc123ABC", displayName: "SCSU Huskies")

        when:

        expect:"fix me"
            true == false
    }
}
