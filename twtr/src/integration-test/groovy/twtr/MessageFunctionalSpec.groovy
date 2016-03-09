package twtr

import geb.spock.GebSpec
import grails.converters.JSON
import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Stepwise


@Integration
@Stepwise
class MessageFunctionalSpec extends GebSpec {

    final static String messageTemplate = 'Huskies Message '
    final static String message1 = 'Hello Message1_Account1'
    final static String message2 = 'Hello Message2_Account1'
    final static String message3 = 'Lulu Wang Message3_Account1'
    final static String message4 = 'Don Sawyer Message4_Account2'

    final static String goodHandle = 'scsu_huskies'
    final static String goodEmailAccount = 'testemail'
    final static String goodEmailDomain = '@scsu.edu'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    final static String searchItem1 = 'Account'
    final static String searchItem2 = 'Huskies'

    RESTClient restClient

    @Shared
    def goodId1

    @Shared
    def goodId2

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

    def 'create two accounts with no messages'() {
        when:
        goodId1 = addAccount('1')
        goodId2 = addAccount('2')
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 2

        when:
        response = restClient.get(path: "/accounts/${goodId1}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.messageCount == 0

        when:
        response = restClient.get(path: "/accounts/${goodId2}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId2
        response.data.messageCount == 0
    }

    def 'get all messages for account with #goodId1'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId1}/messages")

        then:
        response.status == 200
        response.data.size() == 0
    }

    def 'create a tweet with #message1 to account with #goodId1'() {
        when:
        def response = restClient.post(path: '/message/tweet', query: [accountId: goodId1],
                contentType: 'application/json', body: [messageText: message1])

        then:
        response.status == 201
        response.data

        when:
        response = restClient.get(path: "/accounts/${goodId1}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.messageCount == 1

        when:
        response = restClient.get(path: "/accounts/${goodId1}/messages")

        then:
        response.status == 200
        response.data

        when:
        def response2 = restClient.get(path: "/accounts/${goodId1}/messages/1")

        then:
        response2.status == 200
        response2.data
        response2.data.handle == response.data.handle[0]
        response2.data.messageText == response.data.messageText[0]
        response2.data.dateCreated == response.data.dateCreated[0]
    }

    def 'create a tweet with #message2 to account with #goodId1'() {
        when:
        def response = restClient.post(path: '/message/tweet', query: [accountId: goodId1],
                contentType: 'application/json', body: [messageText: message2])

        then:
        response.status == 201
        response.data

        when:
        response = restClient.get(path: "/accounts/${goodId1}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.messageCount == 2

        when:
        response = restClient.get(path: "/accounts/${goodId1}/messages")

        then:
        response.status == 200
        response.data
    }

    def 'create a tweet with #message3 to account with #goodId1'() {
        when:
        def response = restClient.post(path: '/message/tweet', query: [accountId: goodId1],
                contentType: 'application/json', body: [messageText: message3])

        then:
        response.status == 201
        response.data

        when:
        response = restClient.get(path: "/accounts/${goodId1}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.messageCount == 3

        when:
        response = restClient.get(path: "/accounts/${goodId1}/messages")

        then:
        response.status == 200
        response.data
    }

    def 'get all messages for account with #goodId2'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId2}/messages")

        then:
        response.status == 200
        response.data.size() == 0
    }

    def 'create a tweet with #message4 to account with #goodId2'() {
        when:
        def response = restClient.post(path: '/message/tweet', query: [accountId: goodId2],
                contentType: 'application/json', body: [messageText: message4])

        then:
        response.status == 201
        response.data

        when:
        response = restClient.get(path: "/accounts/${goodId2}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId2
        response.data.messageCount == 1

        when:
        response = restClient.get(path: "/accounts/${goodId2}/messages")

        then:
        response.status == 200
        response.data
    }

    def 'return the most recent messages from an account with #goodId1'() {
        given: 'create 10 more messages for #goodId1'
        for (def i = 4; i <= 15; i++) {
            restClient.post(path: '/message/tweet', query: [accountId: goodId1],
                    contentType: 'application/json', body: [messageText: messageTemplate + i])
        }

        when:
        def response = restClient.get(path: "/accounts/${goodId1}/messages")

        then:
        response.status == 200
        response.data
        response.data.size() == 10
        for (def i = 0; i < 9; i++) {
            response.data[i].dateCreated > response.data[i + 1].dateCreated
            response.data[i].messageText == messageTemplate + (14 - i)
        }

        when:
        response = restClient.get(path: "/accounts/${goodId1}/messages", query: [max: 20])

        then:
        response.status == 200
        response.data
        response.data.size() == 15
        for (int i = 0; i < 12; i++) {
            response.data[i].dateCreated >= response.data[i + 1].dateCreated
            response.data[i].messageText == messageTemplate + (15 - i)
        }
        response.data[12].dateCreated >= response.data[13].dateCreated
        response.data[12].messageText == message3
        response.data[13].dateCreated >= response.data[14].dateCreated
        response.data[13].messageText == message2
        response.data[14].messageText == message1

        when:
        response = restClient.get(path: "/accounts/${goodId1}/messages", query: [max: 5, offset: 2])

        then:
        response.status == 200
        response.data
        response.data.size() == 5
        for (int i = 0; i < 4; i ++) {
            response.data[i].dateCreated >= response.data[i + 1].dateCreated
            response.data[i].messageText == messageTemplate + (15 - 2 - i)
        }
    }

    def 'search for messages containing specific search term'(){
        when:
        def response = restClient.get(path: '/message/search', query: [text: searchItem1])

        then:
        response.status == 200
        response.data
        response.data.size() == 4

        // Add more test cases here //

        when:
        response = restClient.get(path: '/message/search', query: [text: searchItem2])

        then:
        response.status == 200
        response.data
        response.data.size() == 12

        // Add more test cases here //

    }

//    def cleanup() {
//        restClient.delete(path:"/accounts/${goodId1}")
//        restClient.delete(path:"/accounts/${goodId2}")
//    }

}