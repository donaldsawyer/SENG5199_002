package twtr.AccountFunctionalTests

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.junit.Ignore
import spock.lang.Shared
import spock.lang.Unroll
import twtr.TwtrFunctionalTestBase

@Ignore
@Integration
@Unroll
class GetByIdOrHandleSpec extends TwtrFunctionalTestBase {

    @Shared
    def goodIds = [:]

    def setup() {
        restClient = new RESTClient("http://localhost:8080")

        goodIds << ['1': addAccount('1')]
        goodIds << ['2': addAccount('2')]
        goodIds << ['3': addAccount('3')]
    }

    def cleanup() {
        deleteAccounts(goodIds)
        goodIds.clear()
    }

    def 'get by valid account id: #description'() {
        when:
        def goodId = goodIds.find { it -> it.key == postfix }.value
        def response = restClient.get(path: "/accounts/$goodId")

        then:
        response.status == 200
        response.data
        response.data.id == goodId
        response.data.handle == goodHandle + postfix
        response.data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        response.data.displayName == goodDisplayName + postfix
        response.data.followerCount == 0
        response.data.followingCount == 0
        response.data.messageCount == 0

        where:
        description | postfix
        'account 1' | '1'
        'account 2' | '2'
        'account 3' | '3'
    }

    def 'get by valid handle: #description'() {
        when:
        def goodId = goodIds.find { it -> it.key == postfix }.value
        def response = restClient.get(path: "/account/handle/${goodHandle + postfix}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId
        response.data.handle == goodHandle + postfix
        response.data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        response.data.displayName == goodDisplayName + postfix
        response.data.followerCount == 0
        response.data.followingCount == 0
        response.data.messageCount == 0

        where:
        description | postfix
        'account 1' | '1'
        'account 2' | '2'
        'account 3' | '3'
    }

    def 'get by invalid handle: #description'() {
        when:
        restClient.get(path: "/account/handle/$handle")

        then:
        HttpResponseException problem = thrown(HttpResponseException)
        problem.statusCode == 404

        where:
        description                    | handle
        'same as a password'           | 'abc123ABC'
        'same as handle prefix'        | 'scsu_huskies'
        'one more than largest handle' | 'scsu_huskies4'
        'starts the same as a handle'  | 'scsu_huskies10'
        'same as a postfix'            | '1'
    }

    def 'get by invalid id: #description'() {
        when:
        restClient.get(path: "/accounts/$accountId")

        then:
        HttpResponseException problem = thrown(HttpResponseException)
        problem.statusCode == 500

        where:
        description                    | accountId
        'same as a password'           | 'abc123ABC'
        'same as handle prefix'        | 'scsu_huskies'
        'one more than largest handle' | 'scsu_huskies4'
        'starts the same as a handle'  | 'scsu_huskies10'
        'same as a handle'             | 'scsu_huskies1'
    }

    def 'get by invalid numeric id'() {
        when:
        restClient.get(path: "/accounts/${goodIds.max { it.value }.value + 1}")

        then:
        HttpResponseException problem = thrown(HttpResponseException)
        problem.statusCode == 404
    }
}
