package twtr

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Message)
@TestMixin(DomainClassUnitTestMixin)
@Unroll
class MessageSpec extends Specification {

    def 'Message text is valid: #description'() {
        setup:
        def account = new Account(handle: 'scsu_huskies', emailAddress: 'testemail@test.com',
                                  password: 'abc123ABC', displayName: 'SCSU Huskies')

        when:
        Message sus = new Message(sentFromAccount: account, messageText: messageText)

        then:
        sus.validate()

        where:
        description       | messageText
        'very short'      | '1'
        'almost too long' | '1'*40
    }

    def "Message text is invalid because it's: #description"() {
        setup:
        def account = new Account(handle: 'scsu_huskies', emailAddress: 'testemail@test.com',
                                  password: 'abc123ABC', displayName: 'SCSU Huskies')

        when:
        Message sus = new Message(sentFromAccount: account, messageText: '')

        then:
        !sus.validate()

        where:
        description | text
        'null'      | null
        'empty'     | ''
        'too long'  | '1'*100
    }

}
