package twtr.AccountFunctionalTests

import geb.spock.GebSpec
import grails.converters.JSON
import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.springframework.boot.test.IntegrationTest
import spock.lang.Shared
import spock.lang.Stepwise


@Integration
@Stepwise
class AccountFunctionalSpec extends GebSpec {

    final static String goodHandle = 'scsu_huskies'
    final static String goodEmail = 'testemail@test.com'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    @Shared
    def goodid

    RESTClient restClient

    def setup
    {
        restClient = new RESTClient("http://localhost:8080")
    }

    def 'get all accounts'() {
        when:
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 0

    }

    def 'create an account with valid JSON data'() {
        when:
        def response = restClient.post(path: '/accounts', contentType: 'application/json',
                body: [handle  : goodHandle, emailAddress: goodEmail,
                       password: goodPassword, displayName: goodDisplayName])

        then:
        response.status == 201
        response.data

        when:
        goodid = response.data.id

        then:
        goodid
        response.data.handle == goodHandle
        response.data.emailAddress == goodEmail
        response.data.displayName == goodDisplayName
        response.data.password == goodPassword
        response.data.messageCount == 0
        response.data.followerCount == 0
        response.data.followingCount == 0
    }

    def 'get the account just created.'() {
        when:
        def response = restClient.get(path:"/accounts/${goodid}")

        then:
        response.status == 200
        response.data.id == goodid
        response.data.handle == goodHandle
        response.data.emailAddress == goodEmail
        response.data.displayName == goodDisplayName
        response.data.password == goodPassword
        response.data.messageCount == 0
        response.data.followerCount == 0
        response.data.followingCount == 0
    }

    def 'update account #goodid with new display name'() {
//        given:
//        def sut = new Account(id: goodid, handle: goodHandle, emailAddress: goodEmail, displayName: 'Huskies Number 1!')

        when:
        def response = restClient.put(path:"/accounts/${goodid}",
                                      body: [id: goodid,
                                             handle: goodHandle,
                                             emailAddress: goodEmail,
                                             password: goodPassword,
                                             displayName: 'Huskies Number 1!'],
                                      contentType:'application/json')

        then:
        response.status == 200
        response.data.displayName == 'Huskies Number 1!'
    }

    def 'get the account just updated.'() {
        when:
        def response = restClient.get(path:"/accounts/${goodid}")

        then:
        response.status == 200
        response.data.id == goodid
        response.data.handle == goodHandle
        response.data.emailAddress == goodEmail
        response.data.displayName == 'Huskies Number 1!'
        response.data.password == goodPassword
        response.data.messageCount == 0
        response.data.followerCount == 0
        response.data.followingCount == 0
    }


    def 'delete the new account'() {
        when:
        def response = restClient.delete(path:"/accounts/${goodid}")

        then:
        response.status == 204

        when:
        restClient.get(path:"/accounts/${goodid}")

        then:
        thrown(HttpResponseException)

        when:
        response = restClient.get(path:'/accounts')

        then:
        response.status == 200
        response.data.size() == 0
    }
}
