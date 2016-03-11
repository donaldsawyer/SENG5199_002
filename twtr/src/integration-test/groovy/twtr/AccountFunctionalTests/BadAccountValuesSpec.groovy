package twtr.AccountFunctionalTests

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.apache.http.HttpResponse
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

@Integration
@Unroll
class BadAccountValuesSpec extends GebSpec {
    final static String goodHandle = 'scsu_huskies'
    final static String goodEmail = 'testemail@test.com'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    final static String goodHandle2 = 'scsu_huskies2'
    final static String goodEmail2 = 'testemail2@test.com'

    RESTClient restClient

    @Shared
    def goodId

    @Shared
    def startingAccountCount

    def setup() {
        restClient = new RESTClient('http://localhost:8080')

        goodId = restClient.post(path: '/accounts', contentType: 'application/json',
                body: [handle  : goodHandle, emailAddress: goodEmail,
                       password: goodPassword, displayName: goodDisplayName]).data?.id

        startingAccountCount = restClient.get(path: '/accounts').data?.size()
    }

    def cleanup() {
        restClient.delete(path: "/accounts/$goodId")
    }

    def 'bad handle format: #description'() {
        when:
        restClient.post(path: '/accounts', contentType: 'application/json',
                body: [handle  : handle, emailAddress: goodEmail2,
                       password: goodPassword, displayName: goodDisplayName])

        then:
        HttpResponseException problem = thrown(HttpResponseException)
        problem.statusCode == 422
        problem.message.contains('Unprocessable Entity')

        when:
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data?.size() == startingAccountCount
        response.data[0].id == goodId
        response.data[0].handle == goodHandle
        response.data[0].emailAddress == goodEmail
        response.data[0].displayName == goodDisplayName
        response.data[0].password == goodPassword
        response.data[0].followerCount == 0
        response.data[0].followingCount == 0
        response.data[0].messageCount == 0

        where:
        description         | handle
        'non-unique handle' | goodHandle
        'empty handle'      | ''
        'null handle'       | null
        'whitespace handle' | '      '
        'handle w/ space'   | 'my handle'
        'handle w/ @'       | '@my_handle'
    }

    def 'bad email address: #description'() {
        when:
        restClient.post(path: '/accounts', contentType: 'application/json',
                body: [handle  : goodHandle2, emailAddress: emailAddress,
                       password: goodPassword, displayName: goodDisplayName])

        then:
        HttpResponseException problem = thrown(HttpResponseException)
        problem.statusCode == 422
        problem.message.contains('Unprocessable Entity')

        when:
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data?.size() == startingAccountCount
        response.data[0].id == goodId
        response.data[0].handle == goodHandle
        response.data[0].emailAddress == goodEmail
        response.data[0].displayName == goodDisplayName
        response.data[0].password == goodPassword
        response.data[0].followerCount == 0
        response.data[0].followingCount == 0
        response.data[0].messageCount == 0

        where:
        description                | emailAddress
        'non-unique email address' | goodEmail
        'null email'               | null
        'empty email'              | ''
        'whitespace email'         | '    '
        'no @ symbol'              | 'umn.edu'
        'two @@ symbols'           | 'a@@b.com'
        'two separate @@'          | 'a@b@c.com'
        ', instead of .'           | 'a@b,com'
    }

    def 'bad password: #description'() {
        when:
        restClient.post(path: '/accounts', contentType: 'application/json',
                body: [handle  : goodHandle2, emailAddress: goodEmail2,
                       password: password, displayName: goodDisplayName])

        then:
        HttpResponseException problem = thrown(HttpResponseException)
        problem.statusCode == 422
        problem.message.contains('Unprocessable Entity')

        when:
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data?.size() == startingAccountCount
        response.data[0].id == goodId
        response.data[0].handle == goodHandle
        response.data[0].emailAddress == goodEmail
        response.data[0].displayName == goodDisplayName
        response.data[0].password == goodPassword
        response.data[0].followerCount == 0
        response.data[0].followingCount == 0
        response.data[0].messageCount == 0

        where:
        description     | password
        'No numbers'    | 'abcdABCDE'
        'No Uppers'     | 'abcd12345'
        'No lowers'     | 'ABCD12345'
        '7 chars'       | 'abAB123'
        '30 characters' | 'aA1' * 10
        'Null password' | null
    }


}
