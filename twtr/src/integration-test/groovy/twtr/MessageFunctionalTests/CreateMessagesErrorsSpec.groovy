package twtr.MessageFunctionalTests

import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.apache.http.HttpResponse
import spock.lang.*
import geb.spock.*

@Integration
@Stepwise
@Unroll
class CreateMessagesErrorsSpec extends GebSpec {

    final static String goodHandle = 'scsu_huskies'
    final static String goodEmailAccount = 'testemail'
    final static String goodEmailDomain = '@scsu.edu'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    RESTClient restClient

    @Shared
    def goodId

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

    def 'create a message with invalid account Id and valid messageText'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}")

        then:
        response.status == 200
        response.data
        response.data.messageCount == 0

        when:
        restClient.post(path: '/message/tweet', query: [accountId: badId],
                contentType: 'application/json', body: [messageText: 'Hello World'])

        then:
        HttpResponseException error = thrown(HttpResponseException)
        error.statusCode == 404
        error.message.contains('Not Found')

        where:
        description                    | badId
        'invalid account id - number'  | '0'
        'invalid account id - letters' | 'abc'
        'invalid account id - empty'   | ''
        'invalid account id - null'    | null
    }

    def 'create a message with invalid account handle and valid messageText'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}")

        then:
        response.status == 200
        response.data
        response.data.messageCount == 0

        when:
        restClient.post(path: '/message/tweet', query: [handle: badhandle],
                contentType: 'application/json', body: [messageText: 'Hello World'])

        then:
        HttpResponseException error = thrown(HttpResponseException)
        error.statusCode == 404
        error.message.contains('Not Found')


        where:
        description                    | badhandle
        'invalid account id - number'  | '10000'
        'invalid account id - letters' | 'scsu_'
        'invalid account id - letters' | 'scsu_huskies???'
        'invalid account id - empty'   | ''
        'invalid account id - null'    | null
    }

    def 'create a message with parameters other than accountId and handle and valid messageText'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}")

        then:
        response.status == 200
        response.data
        response.data.messageCount == 0

        when:
        restClient.post(path: '/message/tweet', query: [badIdField: goodId],
                contentType: 'application/json', body: [messageText: 'Hello World'])

        then:
        HttpResponseException error = thrown(HttpResponseException)
        error.statusCode == 404
        error.message.contains('Not Found')
    }

    def 'create a message with valid account Id and message: #badMessage'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}")

        then:
        response.status == 200
        response.data
        response.data.messageCount == 0

        when:
        restClient.post(path: '/message/tweet', query: [accountId: goodId],
                contentType: 'application/json', body: [messageText: badMessage])

        then:
        HttpResponseException error = thrown(HttpResponseException)
        error.statusCode == 406
        error.message.contains('Not Acceptable')

        where:
        description                       | badMessage
        'invalid message text - empty'    | ''
        'invalid message text - too long' | 'a' * 50
        'invalid message text - null'     | null
    }

    def 'validate that no messages were created for account with #goodId'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId}")

        then:
        response.status == 200
        response.data
        response.data.messageCount == 0
    }

    def 'return most recent messages with badOffset: #badOffset'() {
        when:
        restClient.get(path: "/accounts/${goodId}/messages", query: [offset: badOffset])

        then:
        HttpResponseException error = thrown(HttpResponseException)
        error.statusCode == 400
        error.message.contains('Bad Request')

        where:
        description                           | badOffset
        'invalid Offset - letters'            | 'abc'
        'invalid Offset - special characters' | '**'
    }

    def 'return most recent messages with badMax: #badMax'() {
        when:
        restClient.get(path: "/accounts/${goodId}/messages", query: [offset: badMax])

        then:
        HttpResponseException error = thrown(HttpResponseException)
        error.statusCode == 400
        error.message.contains('Bad Request')

        where:
        description                        | badMax
        'invalid Max - letters'            | 'abc'
        'invalid Max - special characters' | '**'
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
