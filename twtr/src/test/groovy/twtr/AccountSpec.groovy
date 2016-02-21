package twtr

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
@TestFor(Account)
@TestMixin(DomainClassUnitTestMixin)
class AccountSpec extends Specification {

    final static String goodHandle = 'scsu_huskies'
    final static String goodEmail = 'testemail@test.com'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    // HAPPY PATH TESTS //
    void 'account field values follow constraints for new account'() {

        when: 'Account is added using valid properties.'
        def sus = new Account(handle: goodHandle, emailAddress: goodEmail,
                password: goodPassword, displayName: goodDisplayName)

        then: 'The Account should be valid against Account constraints.'
        sus.validate()
    }
    // END OF HAPPY PATH TESTS //

    // HANDLE TESTS //
    def 'Good account handle variation: #description'() {
        when: 'An Account is added with various handles.'
        def sus = new Account(handle: goodHandle, emailAddress: goodEmail, password: goodPassword, displayName: goodDisplayName)

        then: 'The Account validates successfully against the Account constraints.'
        sus.validate()
    }

    def 'Bad account handle variation: #description'() {

        when: 'An Account is added with various handles.'
        def sus = new Account(handle: handle, emailAddress: goodEmail, password: goodPassword, displayName: goodDisplayName)

        then: "The Account doesn't validate against the Account constraints."
        !sus.validate()
        sus.errors.errorCount == 1
        sus.errors.getFieldError('handle').rejectedValue == sus.handle

        where:
        description         | handle
        'empty handle'      | ''
        'null handle'       | null
        'whitespace handle' | '      '
        'handle w/ space'   | 'my handle'
        'handle w/ @'       | '@my_handle'
    }
    // END OF HANDLE TESTS //

    // EMAIL ADDRESS TESTS //
    def 'Valid Account emailAddress variation: #description'() {

        when: 'An account is added with various email address values.'
        def sus = new Account(handle: goodHandle, emailAddress: emailAddress, password: goodPassword, displayName: goodDisplayName)

        then: 'The account validates successfully against the Account constraints.'
        sus.validate()

        where:
        description        | emailAddress
        'good email value' | goodEmail
        'good email 2'     | 'a.b@c.com'
    }

    def 'Bad Account emailAddress variation: #description'() {

        when: 'An account is added with various email address values.'
        def sus = new Account(handle: goodHandle, emailAddress: emailAddress, password: goodPassword, displayName: goodDisplayName)

        then: 'The account validates appropriately against the Account constraints.'
        !sus.validate()
        sus.errors.errorCount == 1
        sus.errors.getFieldError('emailAddress').rejectedValue == sus.emailAddress


        where:
        description        | emailAddress
        'null email'       | null
        'empty email'      | ''
        'whitespace email' | '    '
        'no @ symbol'      | 'umn.edu'
        'two @@ symbols'   | 'a@@b.com'
        'two separate @@'  | 'a@b@c.com'
        ', instead of .'   | 'a@b,com'
    }

    // END OF EMAIL ADDRESS TESTS //

    // PASSWORD TESTS //
    def 'Valid Account password variation: #description'() {

        when:
        def sus = new Account(handle: goodHandle, password: thePassword, emailAddress: goodEmail, displayName: goodDisplayName)

        then:
        sus.validate()

        where:
        description        | thePassword
        'Valid w/ symbols' | 'abc!ABC\$123'
        'Valid no symbols' | 'abcABC123'
    }

    def 'Invalid Account password variation: #description'() {

        when:
        def sus = new Account(handle: goodHandle, password: thePassword, emailAddress: goodEmail, displayName: goodDisplayName)

        then:
        !sus.validate()
        sus.errors.errorCount == 1
        sus.errors.getFieldError('password').rejectedValue == thePassword

        where:
        description     | thePassword
        'No numbers'    | 'abcdABCDE'
        'No Uppers'     | 'abcd12345'
        'No lowers'     | 'ABCD12345'
        '7 chars'       | 'abAB123'
        '17 characters' | 'abcdeABCDE1234567'
        'Null password' | null
    }
    // END OF PASSWORD TESTS //

    // DISPLAY NAME TESTS //
    def 'Valid Account display name variation: #description'() {
        when:
        def sus = new Account(handle: goodHandle, password: goodPassword, emailAddress: goodEmail, displayName: theDisplayName)

        then: 'a valid email will validate.  an invalid email will not validate and will have a displayName validation error.'
        sus.validate()

        where:
        description        | theDisplayName
        'single character' | 'd'
        'Awesome name'     | 'SCSU Huskies'
    }

    def 'Invalid Account display name variation: #description'() {
        when:
        def sus = new Account(handle: goodHandle, password: goodPassword, emailAddress: goodEmail, displayName: theDisplayName)

        then: 'a valid email will validate.  an invalid email will not validate and will have a displayName validation error.'
        !sus.validate()
        sus.errors.errorCount == 1
        sus.errors.getFieldError('displayName').rejectedValue == sus.displayName

        where:
        description         | theDisplayName
        'null display name' | null
        'empty string'      | ''
        'blank string'      | '       '
    }
    // END OF DISPLAY NAME TESTS //
}
