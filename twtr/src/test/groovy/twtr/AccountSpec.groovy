package twtr

import grails.test.*
import grails.test.mixin.*
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Account)
@TestMixin(GrailsUnitTestMixin)
class AccountSpec extends Specification {

    def setup() {

    }

    def cleanup() {

    }

    void "account field values follow constraints for new account :-)"() {
        setup:

        when:
        def sus = new Account(handle: "@scsu-huskies", emailAddress: "testemail@test.com",
                              password: "abc123ABC", displayName: "SCSU Huskies")
        then:
        sus.validate()
    }

    void "account with invalid password format :-("() {
        when:
        def sus = new Account(handle: "@scsu-huskies", emailAddress: "testemail@test.com",
                              password: "abc123abc", displayName: "SCSU Huskies")

        then:
        !sus.validate()
    }

    void "accounts with same handle cannot be created"() {
        setup:
        def account1 = new Account(handle: "@scsu-huskies", emailAddress: "testemail@test.com",
                                   password: "abc123ABC", displayName: "SCSU Huskies")
        account1.save(flush: true)

        when:
        def sus = new Account(handle: "@scsu-huskies", emailAddress: "testemail2@test.com",
                              password: "abc123ABC", displayName: "SCSU Huskies")

        then:
        !sus.validate()
    }

    /* UNIT TESTS WE STILL NEED
    handle
     - empty
     - missing as required field (null)
     emailAddress
     - email format constraint
     - empty
     - duplicate
     - missing as required field (null)
     password
     - format
        + no digits
        + no uppers
        + no lowers
        + less than 8
        + more than 16
        + valid, but with symbols
     - missing as required field
     displayName
     - empty
     - missing as required field
     */

    /* INTEGRATION TESTS WE STILL NEED
    *** MAKE SURE WE DID THESE PROPERLY ***
    unique email
    unique handle

    Account can have multiple followers

    Accounts may follow each other
     */
}
