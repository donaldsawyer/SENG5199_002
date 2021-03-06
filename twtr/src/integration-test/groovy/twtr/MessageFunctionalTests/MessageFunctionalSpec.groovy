package twtr.MessageFunctionalTests

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import groovyx.net.http.RESTClient
import org.junit.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Ignore
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
    final static String searchItem3 = 'Message not found'

    RESTClient restClient

    @Shared
    def goodId1

    @Shared
    def goodId2

    @Shared
    def goodDateCreated = []

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
        goodDateCreated[0] = response.data.dateCreated

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
    }

    def 'create a tweet with #message2 to account with #goodId1'() {
        when:
        def response = restClient.post(path: '/message/tweet', query: [accountId: goodId1],
                contentType: 'application/json', body: [messageText: message2])
        goodDateCreated[1] = response.data.dateCreated

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
        goodDateCreated[2] = response.data.dateCreated

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


    def 'return the most recent messages from an account with #goodId1'() {
        given: 'create 12 more messages for #goodId1 to make 15 messages total'
        def response
        (4..15).each { it ->
            response = restClient.post(path: '/message/tweet', query: [accountId: goodId1],
                    contentType: 'application/json', body: [messageText: messageTemplate + it])
            goodDateCreated[it - 1] = response.data.dateCreated
        }

        when: 'get the most recent message for #goodId1 with default max'
        response = restClient.get(path: "/accounts/${goodId1}/messages")

        then:
        response.status == 200
        response.data
        response.data.size() == 10
        (0..8).each { it ->
            response.data[it].dateCreated >= response.data[it + 1].dateCreated
            response.data[it].messageText == messageTemplate + (15 - it)
        }

        when:
        response = restClient.get(path: "/accounts/${goodId1}/messages", query: [max: 20])

        then:
        response.status == 200
        response.data
        response.data.size() == 15
        (0..11).each { it ->
            response.data[it].dateCreated >= response.data[it + 1].dateCreated
            response.data[it].messageText == messageTemplate + (15 - it)
        }
        response.data[12].dateCreated >= response.data[13].dateCreated
        response.data[12].messageText == message3
        response.data[13].dateCreated >= response.data[14].dateCreated
        response.data[13].messageText == message2
        response.data[14].messageText == message1

        when:
        response = restClient.get(path: "/accounts/${goodId1}/messages", query: [max: 5])

        then:
        response.status == 200
        response.data
        response.data.size() == 5
        (0..3).each { it ->
            response.data[it].dateCreated >= response.data[it + 1].dateCreated
            response.data[it].messageText == messageTemplate + (15 - it)
        }

        when:
        response = restClient.get(path: "/accounts/${goodId1}/messages", query: [max: 5, offset: 2])

        then:
        response.status == 200
        response.data
        response.data.size() == 5
        (0..3).each { it ->
            response.data[it].dateCreated >= response.data[it + 1].dateCreated
            response.data[it].messageText == messageTemplate + (15 - 2 - it)
        }
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
        goodDateCreated[15] = response.data.dateCreated

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

    def 'search for messages containing specific search term'() {
        when:
        def response = restClient.get(path: '/message/search', query: [text: searchItem1])

        then:
        response.status == 200
        response.data
        response.data.size() == 4
        response.data[0].handle == goodHandle + '1'
        response.data[0].messageText == message1
        response.data[0].dateCreated == goodDateCreated[0]
        response.data[1].handle == goodHandle + '1'
        response.data[1].messageText == message2
        response.data[1].dateCreated == goodDateCreated[1]
        response.data[2].handle == goodHandle + '1'
        response.data[2].messageText == message3
        response.data[2].dateCreated == goodDateCreated[2]
        response.data[3].handle == goodHandle + '2'
        response.data[3].messageText == message4
        response.data[3].dateCreated == goodDateCreated[15]

        when:
        response = restClient.get(path: '/message/search', query: [text: searchItem2])

        then:
        response.status == 200
        response.data
        response.data.size() == 12
        (4..15).each { it ->
            response.data[it - 4].handle == goodHandle + '1'
            response.data[it - 4].messageText == messageTemplate + "$it"
            response.data[it - 4].dateCreated == goodDateCreated[it - 1]
        }

        when:
        response = restClient.get(path: '/message/search', query: [text: searchItem3])

        then:
        response.status == 200
        response.data.size() == 0
    }

    def 'clean up accounts'() {
        when:
        restClient.delete(path: "/accounts/${goodId1}")
        restClient.delete(path: "/accounts/${goodId2}")
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 0
    }

}