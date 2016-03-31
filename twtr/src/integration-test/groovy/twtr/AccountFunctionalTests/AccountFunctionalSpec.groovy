package twtr.AccountFunctionalTests

import geb.spock.GebSpec
import grails.converters.JSON
import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.junit.Ignore
import org.springframework.boot.test.IntegrationTest
import spock.lang.Shared
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Ignore
@Integration
@Stepwise
class AccountFunctionalSpec extends TwtrFunctionalTestBase {

    @Shared
    def goodid

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
        def response = restClient.get(path: "/accounts/${goodid}")

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
        when:
        def response = restClient.put(path: "/accounts/${goodid}",
                body: [id          : goodid,
                       handle      : goodHandle,
                       emailAddress: goodEmail,
                       password    : goodPassword,
                       displayName : 'Huskies Number 1!'],
                contentType: 'application/json')

        then:
        response.status == 200
        response.data.displayName == 'Huskies Number 1!'
    }

    def 'get the account just updated.'() {
        when:
        def response = restClient.get(path: "/accounts/${goodid}")

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
        def response = restClient.delete(path: "/accounts/${goodid}")

        then:
        response.status == 204

        when:
        restClient.get(path: "/accounts/${goodid}")

        then:
        thrown(HttpResponseException)

        when:
        response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 0
    }
}
