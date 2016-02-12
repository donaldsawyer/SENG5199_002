package twtr

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Message)
@TestMixin(DomainClassUnitTestMixin)
class MessageSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Message text is valid"() {
        setup:
        def account = new Account(handle: "scsu_huskies", emailAddress: "testemail@test.com",
                                  password: "abc123ABC", displayName: "SCSU Huskies")

        when:
        Message sus = new Message(sentFromAccount: account, messageText: "Twitter Message - woohoo")

        then:
        sus.validate()
    }

    void "Message text is invalid because it's empty :-("() {
        setup:
        def account = new Account(handle: "scsu_huskies", emailAddress: "testemail@test.com",
                                  password: "abc123ABC", displayName: "SCSU Huskies")

        when:
        Message sus = new Message(sentFromAccount: account, messageText: "")

        then:
        !sus.validate()
    }

    void "Message text is invalid because it's too long :-("() {
        setup:
        def account = new Account(handle: "scsu_huskies", emailAddress: "testemail@test.com",
                                  password: "abc123ABC", displayName: "SCSU Huskies")
        String messageText = "11111222223333344444555556666677777888889"

        when:
        Message sus = new Message(sentFromAccount: account, messageText: messageText)

        then:
        !sus.validate()
    }

    void "Message text is valid because it's juuust right :-)"() {
        setup:
        def account = new Account(handle: "scsu_huskies", emailAddress: "testemail@test.com",
                                  password: "abc123ABC", displayName: "SCSU Huskies")
        String messageText = "1111122222333334444455555666667777788888"

        when:
        Message sus = new Message(sentFromAccount: account, messageText: messageText)

        then:
        sus.validate()
    }

    void "Message text is valid because it's the minimum flare :-)"() {
        setup:
        def account = new Account(handle: "scsu_huskies", emailAddress: "testemail@test.com",
                                  password: "abc123ABC", displayName: "SCSU Huskies")
        String messageText = "1"

        when:
        Message sus = new Message(sentFromAccount: account, messageText: messageText)

        then:
        sus.validate()
    }
}
