package twtr

import grails.test.*
import grails.test.mixin.*
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Account)
@TestMixin(DomainClassUnitTestMixin)
class AccountSpec extends Specification {

    final static String goodHandle = "@scsu-huskies"
    final static String goodEmail  = "testemail@test.com"
    final static String goodPassword = "abc123ABC"
    final static String goodDisplayName = "SCSU Huskies"

    def setup() {

    }

    def cleanup() {

    }

    // HAPPY PATH TESTS //
    void "account field values follow constraints for new account"() {

        when: "Account is added using valid properties."
        def sus = new Account(handle: goodHandle, emailAddress: goodEmail,
                              password: goodPassword, displayName: goodDisplayName)
        then: "The Account should be valid against Account constraints."
        sus.validate()
    }
    // END OF HAPPY PATH TESTS //

    // HANDLE TESTS //
    @Unroll
    void "account handle variation: #description"() {

        when: "An Account is added with various handles."
        def sus = new Account(handle: handle, emailAddress: emailAddress, password: password, displayName: displayName)

        then: "The Account validates appropriately against the Account constraints."
        sus.validate() == valid && ((sus.errors["handle"] == null) == !handleError)

        where:
        description         | handle        | emailAddress  | password      | displayName       | valid | handleError
        'good handle value' | goodHandle    | goodEmail     | goodPassword  | goodDisplayName   | true  | false
        'empty handle'      | ""            | goodEmail     | goodPassword  | goodDisplayName   | false | true
        'null handle'       | null          | goodEmail     | goodPassword  | goodDisplayName   | false | true
    }

    // theoretically, this is covered in the handle variation "Null handle test" //
    void "accounts missing a handle are invalid"() {
        when: "An account is created without a handle."
        def sus = new Account(emailAddress: goodEmail, password: goodPassword, displayName: goodDisplayName)

        then: "the account should be invalid."
        !sus.validate()
    }
    // END OF HANDLE TESTS //

    // EMAIL ADDRESS TESTS //
    @Unroll
    void "account emailAddress variation: #description"() {

        when: "An account is added with various email address values."
        def sus = new Account(handle: handle, emailAddress: emailAddress, password: password, displayName: displayName)

        then: "The account validates appropriately against the Account constraints."
        sus.validate() == valid && ((sus.errors["emailAddress"] == null) == !emailError)

        where:
        description         | handle        | emailAddress  | password      | displayName       | valid | emailError
        'good email value'  | goodHandle    | goodEmail     | goodPassword  | goodDisplayName   | true  | false
        'good email 2'      | goodHandle    | "a.b@c.com"   | goodPassword  | goodDisplayName   | true  | false
        'null email'        | goodHandle    | null          | goodPassword  | goodDisplayName   | false | true
        'empty email'       | goodHandle    | ""            | goodPassword  | goodDisplayName   | false | true
        'whitespace email'  | goodHandle    | "    "        | goodPassword  | goodDisplayName   | false | true
        'no @ symbol'       | goodHandle    | "umn.edu"     | goodPassword  | goodDisplayName   | false | true
        'two @@ symbols'    | goodHandle    | "a@@b.com"    | goodPassword  | goodDisplayName   | false | true
        'two separate @@'   | goodHandle    | "a@b@c.com"   | goodPassword  | goodDisplayName   | false | true
        ', instead of .'    | goodHandle    | "a@b,com"     | goodPassword  | goodDisplayName   | false | true
    }

    // this is likely handled in the data-driven test above 'null email' //
    void "accounts missing an emailAddress are invalid"() {
        when: "An account is created without an email address."
        def sus = new Account(handle: goodHandle, password: goodPassword, displayName: goodDisplayName)

        then: "The account should be invalid due to an emailAddress error."
        !sus.validate()
        sus.errors["emailAddress"] != null
    }
    // END OF EMAIL ADDRESS TESTS //

    // PASSWORD TESTS //
    void "account without password is invalid"() {

        when: "An account is created without a password."
        def sus = new Account(handle: goodHandle, emailAddress: goodEmail, displayName: goodDisplayName)

        then: "The account should be invalid due to a password error."
        !sus.validate()
        sus.errors["password"] != null
    }

    @Unroll
    void "Account password variations: #description"() {

        when:
        def sus = new Account(handle: goodHandle, password: thePassword, emailAddress: goodEmail, displayName: goodDisplayName)

        then:
        sus.validate() == isValid
        (sus.errors["password"] == null) == !passwordError

        where:
        description         | thePassword         | isValid   | passwordError
        "No numbers"        | "abcdABCDE"         | false     | true
        "No Uppers"         | "abcd12345"         | false     | true
        "No lowers"         | "ABCD12345"         | false     | true
        "7 chars"           | "abAB123"           | false     | true
        "17 characters"     | "abcdeABCDE1234567" | false     | true
        "Valid w/ symbols"  | "abc!ABC\$123"      | true      | false
        "Valid no symbols"  | "abcABC123"         | true      | false
    }
    // END OF PASSWORD TESTS //

    // DISPLAY NAME TESTS //
    void "Account with a missing display name is invalid"() {

        when: "An account is created without a display name."
        def sus = new Account(handle: goodHandle, emailAddress: goodEmail, password: goodPassword)

        then: "The account is invalid due to a displayName error."
        !sus.validate()
        sus.errors["displayName"] != null
    }

    @Unroll
    void "Account display name variations: #description"() {
        when:
        def sus = new Account(handle: goodHandle, password: goodPassword, emailAddress: goodEmail, displayName: theDisplayName)

        then: "a valid email will validate.  an invalid email will not validate and will have a displayName validation error."
        sus.validate() == isValid
        (sus.errors["displayName"] == null) == !displayNameError

        where:
        description         | theDisplayName | isValid    | displayNameError
        "null display name" | null           | false      | true
        "empty string"      | ""             | false      | true
        "single character"  | "d"            | true       | false
        "Awesome name"      |"SCSU Huskies"  | true       | false

    }
    // END OF DISPLAY NAME TESTS //
}
