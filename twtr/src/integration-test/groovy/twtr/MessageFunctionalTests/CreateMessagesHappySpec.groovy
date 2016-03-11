package twtr.MessageFunctionalTests

import grails.test.mixin.integration.Integration
import groovyx.net.http.RESTClient
import spock.lang.*
import geb.spock.*

@Integration
@Stepwise
@Unroll
class CreateMessagesHappySpec extends GebSpec {

    final static String goodHandle = 'scsu_huskies'
    final static String goodEmailAccount = 'testemail'
    final static String goodEmailDomain = '@scsu.edu'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    final static String shortMessageText = 'a'
    final static String goodMessageText = 'Hello world, I am here!'
    final static String longMessageText = 'b' * 40

    RESTClient restClient

    @Shared
    def goodId

    @Shared
    def accountNumber

    def setup() {
        restClient = new RESTClient("http://localhost:8080")
    }

    def addAccount(String postfix) {
        def response = restClient.post(path: '/accounts', contentType: 'application/json',
                body: [handle      : goodHandle + postfix,
                       emailAddress: goodEmailAccount + postfix + goodEmailDomain,
                       password    : goodPassword,
                       displayName : goodDisplayName + postfix])
        return response.data.id
    }

    def 'create an account'() {
        given:
        accountNumber = '1'

        when:
        goodId = addAccount(accountNumber)
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data

        when:
        response = restClient.get(path: "/accounts/${goodId}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId
        response.data.handle == "$goodHandle$accountNumber"
        response.data.messageCount == 0
    }

    def 'create a message with valid account Id and #description'() {
        when:
        def response = restClient.post(path: '/message/tweet', query: [accountId: goodId],
                contentType: 'application/json', body: [messageText: message])

        then:
        response.status == 201
        response.data
        response.data.handle == "$goodHandle$accountNumber"
        response.data.messageText == message

        where:
        description                   | message
        'valid message text - short'  | shortMessageText
        'valid message text - normal' | goodMessageText
        'valid message text - long'   | longMessageText
    }

    def 'validate account Id with #goodId has messages created'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId
        response.data.handle == "$goodHandle$accountNumber"
        response.data.messageCount == 3
    }

    def 'create a message with valid account handle and #description'() {
        when:
        def response = restClient.post(path: '/message/tweet', query: [handle: "$goodHandle$accountNumber"],
                contentType: 'application/json', body: [messageText: message])

        then:
        response.status == 201
        response.data
        response.data.handle == "$goodHandle$accountNumber"
        response.data.messageText == message

        where:
        description                   | message
        'valid message text - short'  | shortMessageText
        'valid message text - normal' | goodMessageText
        'valid message text - long'   | longMessageText
    }

    def 'validate account Id with #goodHandle+#accountNumber has messages created'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId
        response.data.handle == "$goodHandle$accountNumber"
        response.data.messageCount == 6
    }

    def 'clean up accounts'() {
        when:
        restClient.delete(path: "/accounts/${goodId}")
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 0
    }
}
