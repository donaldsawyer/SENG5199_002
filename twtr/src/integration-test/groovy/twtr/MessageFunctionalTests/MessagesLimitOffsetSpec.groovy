package twtr.MessageFunctionalTests

import grails.test.mixin.integration.Integration
import groovyx.net.http.RESTClient
import spock.lang.*
import geb.spock.*


@Integration
@Stepwise
@Unroll
class MessagesLimitOffsetSpec extends GebSpec {

    final static String goodHandle = 'scsu_huskies'
    final static String goodEmailAccount = 'testemail'
    final static String goodEmailDomain = '@scsu.edu'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'
    final static String goodMessage = 'Hello Huskies - Welcome to 2016 - '

    @Shared
    def dateCreated = []

    @Shared
    def goodId

    @Shared
    int numberOfMessagesCreated = 12

    RESTClient restClient

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
        when:
        goodId = addAccount('1')
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
        response.data.messageCount == 0
    }

    def 'create 12 messages for account with #goodId'() {
        given:
        def response

        when:
        (1..numberOfMessagesCreated).each { it ->
            response = restClient.post(path: '/message/tweet', query: [accountId: goodId],
                    contentType: 'application/json', body: [messageText: goodMessage + it])
            dateCreated[it] = response.data.dateCreated
            sleep(1000)
        }

        then:
        response.status == 201
        response.data
        response.data.handle == "${goodHandle}1"

        when:
        response = restClient.get(path: "/accounts/${goodId}")

        then:
        response.status == 200
        response.data
        response.data.handle == "${goodHandle}1"
        response.data.messageCount == 12
    }

    def 'return most recent messages with default limit'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}/messages")

        then:
        response.status == 200
        response.data
        response.data.size() == 10
        (0..8).each { it ->
            response.data[it].dateCreated >= response.data[it + 1].dateCreated
            response.data[it].messageText == goodMessage + (12 - it).toString()
            response.data[it].dateCreated == dateCreated[12 - it]
        }
        response.data[9].messageText == goodMessage + '3'
    }

    def 'return most recent messages with #customLimit'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}/messages", query: [max: customLimit])
        def messagesCount = customLimit > numberOfMessagesCreated ? numberOfMessagesCreated : customLimit

        then:
        response.status == 200
        response.data
        response.data.size() == messagesCount
        (0..(messagesCount - 2)).each { it ->
            response.data[it].dateCreated >= response.data[it + 1].dateCreated
            response.data[it].messageText == goodMessage + (12 - it).toString()
            response.data[it].dateCreated == dateCreated[12 - it]
        }
        response.data[messagesCount - 1].messageText == goodMessage + (12 - (messagesCount - 1))

        where:
        description   | customLimit
        'limit of 2'  | 2
        'limit of 8'  | 8
        'limit of 12' | 12
        'limit of 20' | 20
    }

    def 'return most recent messages with #customOffset'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}/messages", query: [max: numberOfMessagesCreated, offset: customOffset])
        def messagesCount = (numberOfMessagesCreated >= customOffset) ? (numberOfMessagesCreated - customOffset) : 0

        then:
        response.status == 200
        response.data.size() == messagesCount
        if (messagesCount > 1) {
            (0..(messagesCount - 2)).each { it ->
                response.data[it].dateCreated >= response.data[it + 1].dateCreated
                response.data[it].messageText == goodMessage + (messagesCount - it).toString()
                response.data[it].dateCreated == dateCreated[messagesCount - it]
            }
        }

        if (messagesCount > 0) {
            response.data
            response.data[messagesCount - 1].messageText == goodMessage + '1'
        }

        where:
        description    | customOffset
        'Offset of 0'  | 0
        'Offset of 2'  | 2
        'Offset of 8'  | 8
        'Offset of 11' | 10
        'Offset of 12' | 12
        'Offset of 20' | 20
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
