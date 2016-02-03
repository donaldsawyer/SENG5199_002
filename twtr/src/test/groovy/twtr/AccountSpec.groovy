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

    final String goodHandle = "@scsu-huskies"
    final String goodEmail  = "testemail@test.com"
    final String goodPassword = "abc123ABC"
    final String goodDisplayName = "SCSU Huskies"

    def setup() {

    }

    def cleanup() {

    }

    // HAPPY PATH TESTS //
    void "account field values follow constraints for new account :-)"() {
        setup:

        when:
        def sus = new Account(handle: goodHandle, emailAddress: goodEmail,
                              password: goodPassword, displayName: goodDisplayName)
        then:
        sus.validate()
    }
    // END OF HAPPY PATH TESTS //

    // HANDLE TESTS //
    void "accounts with same handle cannot be created"() {
        setup:
        def account1 = new Account(handle: goodHandle, emailAddress: goodEmail,
                password: goodPassword, displayName: goodDisplayName)
        account1.save(flush: true)

        when:
        def sus = new Account(handle: goodHandle, emailAddress: "testemail2@test.com",
                password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }

    void "accounts with an empty handle are invalid"() {
        when:
        def sus = new Account(handle: "", emailAddress: goodEmail, password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }

    void "accounts missing a handle are invalid"() {
        when:
        def sus = new Account(emailAddress: goodEmail, password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }
    // END OF HANDLE TESTS //

    // EMAIL ADDRESS TESTS //
    void "accounts missing an emailAddress are invalid"() {
        when:
        def sus = new Account(handle: goodHandle, password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }

    void "accounts with an empty emailAddress are invalid"() {
        when:
        def sus = new Account(handle: goodHandle, emailAddress: "", password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }

    void "accounts with whitespace for an emailAddress are invalid"() {
        when:
        def sus = new Account(handle: goodHandle, emailAddress: "    ", password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }

    void "accounts with an invalid email format are invalid - no @"() {
        when:
        def sus = new Account(handle: goodHandle, emailAddress: "umn.edu",
                              password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }

    void "accounts with an invalid email format are invalid - @@"() {
        when:
        def sus = new Account(handle: goodHandle, emailAddress: "sawy0059@@umn.edu",
                              password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }

    void "accounts with an invalid email format are invalid - comma instead of period"() {
        when:
        def sus = new Account(handle: goodHandle, emailAddress: "sawy0059@umn,edu",
                              password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }

    void "accounts with the same email address cannot be used"() {
        setup:
        Account account1 = new Account(handle: goodHandle, emailAddress: goodEmail,
                                       password: goodPassword, displayName: goodDisplayName)
        account1.save(flush: true)

        when:
        Account sus = new Account(handle: goodHandle+"2", emailAddress: goodEmail,
                                  password: goodPassword, displayName: goodDisplayName)

        then:
        !sus.validate()
    }
    // END OF EMAIL ADDRESS TESTS //

    // PASSWORD TESTS //
    void "account without password is invalid"() {
        when:
        def sus = new Account(handle: goodHandle, emailAddress: goodEmail, displayName: goodDisplayName)

        then:
        !sus.validate()
    }

    void "Account password variations"(String thePassword, boolean isValid) {
        expect:
        def sus = new Account(handle: goodHandle, password: thePassword, emailAddress: goodEmail, displayName: goodDisplayName)
        sus.validate() == isValid

        where:
        thePassword || isValid
        "abcdABCDE" || false
        "abcd12345" || false
        "ABCD12345" || false
        "abAB123"   || false
        "abcdeABCDE1234567" || false
        "abc!ABC\$123" || true
        "abcABC123" || true
    }
    // END OF PASSWORD TESTS //

    // DISPLAY NAME TESTS //
    void "Account with a missing display name is invalid"() {
        when:
        def sus = new Account(handle: goodHandle, emailAddress: goodEmail, password: goodPassword)

        then:
        !sus.validate()
    }
    void "Account display name variations"(String theDisplayName, boolean isValid) {
        expect:
        def sus = new Account(handle: goodHandle, password: goodPassword, emailAddress: goodEmail, displayName: theDisplayName)
        sus.validate() == isValid

        where:
        theDisplayName || isValid
        null           || false
        ""             || false
        "d"            || true
        "SCSU Huskies" || true

    }

    // END OF DISPLAY NAME TESTS //

    /* UNIT TESTS WE STILL NEED

    /* INTEGRATION TESTS WE STILL NEED
    *** MAKE SURE WE DID THESE PROPERLY ***
    unique email
    unique handle

    Account can have multiple followers

    Accounts may follow each other
     */
}
