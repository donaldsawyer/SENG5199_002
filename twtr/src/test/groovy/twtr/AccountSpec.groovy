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

    final static String goodHandle = "scsu_huskies"
    final static String goodEmail = "testemail@test.com"
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
    void "Good account handle variation: #description"() {
        when: "An Account is added with various handles."
        def sus = new Account(handle: handle, emailAddress: emailAddress, password: password, displayName: displayName)

        then: "The Account validates successfully against the Account constraints."
        sus.validate()

        where:
        description         | handle     | emailAddress | password     | displayName
        'good handle value' | goodHandle | goodEmail    | goodPassword | goodDisplayName
    }

    @Unroll
    void "Bad account handle variation: #description"() {

        when: "An Account is added with various handles."
        def sus = new Account(handle: handle, emailAddress: emailAddress, password: password, displayName: displayName)

        then: "The Account doesn't validate against the Account constraints."
        //sus.validate() == valid && ((sus.errors["handle"] == null) == !handleError)
        !sus.validate()
        sus.errors.errorCount == 1
        sus.errors.getFieldError("handle").rejectedValue == sus.handle

        where:
        description         | handle       | emailAddress | password     | displayName
        'empty handle'      | ""           | goodEmail    | goodPassword | goodDisplayName
        'null handle'       | null         | goodEmail    | goodPassword | goodDisplayName
        'whitespace handle' | "      "     | goodEmail    | goodPassword | goodDisplayName
        'handle w/ space'   | "my handle"  | goodEmail    | goodPassword | goodDisplayName
        "handle w/ @"       | "@my_handle" | goodEmail    | goodPassword | goodDisplayName
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
    void "Valid Account emailAddress variation: #description"() {

        when: "An account is added with various email address values."
        def sus = new Account(handle: handle, emailAddress: emailAddress, password: password, displayName: displayName)

        then: "The account validates successfully against the Account constraints."
        sus.validate()

        where:
        description        | handle     | emailAddress | password     | displayName
        'good email value' | goodHandle | goodEmail    | goodPassword | goodDisplayName
        'good email 2'     | goodHandle | "a.b@c.com"  | goodPassword | goodDisplayName
    }

    @Unroll
    void "Bad Account emailAddress variation: #description"() {

        when: "An account is added with various email address values."
        def sus = new Account(handle: handle, emailAddress: emailAddress, password: password, displayName: displayName)

        then: "The account validates appropriately against the Account constraints."
        //sus.validate() == valid && ((sus.errors["emailAddress"] == null) == !emailError)
        !sus.validate()
        sus.errors.errorCount == 1
        sus.errors.getFieldError("emailAddress").rejectedValue == sus.emailAddress


        where:
        description        | handle     | emailAddress | password     | displayName
        'null email'       | goodHandle | null         | goodPassword | goodDisplayName
        'empty email'      | goodHandle | ""           | goodPassword | goodDisplayName
        'whitespace email' | goodHandle | "    "       | goodPassword | goodDisplayName
        'no @ symbol'      | goodHandle | "umn.edu"    | goodPassword | goodDisplayName
        'two @@ symbols'   | goodHandle | "a@@b.com"   | goodPassword | goodDisplayName
        'two separate @@'  | goodHandle | "a@b@c.com"  | goodPassword | goodDisplayName
        ', instead of .'   | goodHandle | "a@b,com"    | goodPassword | goodDisplayName
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
    void "Valid Account password variation: #description"() {

        when:
        def sus = new Account(handle: goodHandle, password: thePassword, emailAddress: goodEmail, displayName: goodDisplayName)

        then:
        sus.validate()

        where:
        description        | thePassword
        "Valid w/ symbols" | "abc!ABC\$123"
        "Valid no symbols" | "abcABC123"
    }

    @Unroll
    void "Invalid Account password variation: #description"() {

        when:
        def sus = new Account(handle: goodHandle, password: thePassword, emailAddress: goodEmail, displayName: goodDisplayName)

        then:
        !sus.validate()
        sus.errors.errorCount == 1
        sus.errors.getFieldError("password").rejectedValue == thePassword

        where:
        description     | thePassword
        "No numbers"    | "abcdABCDE"
        "No Uppers"     | "abcd12345"
        "No lowers"     | "ABCD12345"
        "7 chars"       | "abAB123"
        "17 characters" | "abcdeABCDE1234567"
        "Null password" | null
    }
    // END OF PASSWORD TESTS //

    // DISPLAY NAME TESTS //
    @Unroll
    void "Valid Account display name variation: #description"() {
        when:
        def sus = new Account(handle: goodHandle, password: goodPassword, emailAddress: goodEmail, displayName: theDisplayName)

        then: "a valid email will validate.  an invalid email will not validate and will have a displayName validation error."
        sus.validate()

        where:
        description        | theDisplayName
        "single character" | "d"
        "Awesome name"     | "SCSU Huskies"
    }

    void "Account with a missing display name is invalid"() {

        when: "An account is created without a display name."
        def sus = new Account(handle: goodHandle, emailAddress: goodEmail, password: goodPassword)

        then: "The account is invalid due to a displayName error."
        !sus.validate()
        sus.errors["displayName"] != null
    }

    @Unroll
    void "Invalid Account display name variation: #description"() {
        when:
        def sus = new Account(handle: goodHandle, password: goodPassword, emailAddress: goodEmail, displayName: theDisplayName)

        then: "a valid email will validate.  an invalid email will not validate and will have a displayName validation error."
        !sus.validate()
        sus.errors.errorCount == 1
        sus.errors.getFieldError("displayName").rejectedValue == sus.displayName

        where:
        description         | theDisplayName
        "null display name" | null
        "empty string"      | ""
    }
    // END OF DISPLAY NAME TESTS //
}
