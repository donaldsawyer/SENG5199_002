package twtr.FollowFunctionalTests

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Stepwise


@Integration
@Stepwise
class FollowersLimitOffsetSpec extends GebSpec {
    final static String goodHandle = 'scsu_huskies'
    final static String goodEmailAccount = 'testemail'
    final static String goodEmailDomain = '@scsu.edu'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    @Shared
    def goodIds = []

    @Shared
    int numberOfAccountsCreated = 12

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

    def 'max parameter for account index defaults to 10 results'() {
        when:
        (1..numberOfAccountsCreated).each { it ->
            goodIds.add(addAccount("$it"))
        }
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 10
        goodIds[0..9].each { it ->
            assert response.data.find { d -> d.id == it }
        }
    }

    def 'max parameter for followers endpoint defaults to 10 results'() {
        when: 'account 1 is followed by all other accounts'
        goodIds[1..(numberOfAccountsCreated - 1)].each { it ->
            restClient.post(path: "/accounts/$it/startFollowing",
                    query: [followAccount: goodIds[0]],
                    contentType: 'application/json')
        }
        def response = restClient.get(path: "/accounts/${goodIds[0]}")

        then:
        response.status == 200
        response.data.id == goodIds[0]
        response.data.followingCount == 0
        response.data.followerCount == 11

        when: 'no limit is used for follow'
        response = restClient.get(path: "/accounts/${goodIds[0]}/followers")

        then:
        response.status == 200
        response.data
        response.data.size == 10
        goodIds[1..10].each { it ->
            assert response.data.find { a -> a.id == it }.followingCount == 1
        }
    }

    def 'max 11 for followers endpoint with 11 followers'() {
        when:
        def response = restClient.get(path:"/accounts/${goodIds[0]}/followers", query:[max:11])

        then:
        response.status == 200
        response.data
        response.data.size == 11
        goodIds[1..(numberOfAccountsCreated-1)].each { it ->
            assert response.data.find { a -> a.id == it }.followingCount == 1
        }

        when: 'max of 3 is used for follow'
        response = restClient.get(path:"/accounts/${goodIds[0]}/followers", query:[max:3])

        then:
        response.status == 200
        response.data
        response.data.size == 3
        goodIds[1..3].each { it ->
            assert response.data.find { a -> a.id == it }.followingCount == 1
        }
    }

    def 'use only offset parameter for followers'() {
        when:
        def response = restClient.get(path: "/accounts/${goodIds[0]}/followers", query: [offset: 1])

        then:
        response.status == 200
        response.data
        response.data.size() == 10
        goodIds[2..(numberOfAccountsCreated - 1)].each { it ->
            assert response.data.find { a -> a.id == it }.followingCount == 1
        }
    }

    def 'use offset parameter and max parameter for followers'() {

        when:
        def response = restClient.get(path:"/accounts/${goodIds[0]}/followers", query: [offset:3, max:2])

        then:
        response.status == 200
        response.data
        response.data.size() == 2
        goodIds[4..5].each { it ->
            assert response.data.find { a -> a.id == it }.followingCount == 1
        }
    }

    def 'cleanup test data'() {
        when:
        goodIds.each { it -> restClient.delete(path:"/accounts/$it")}

        then:
        restClient.get(path:'/accounts')?.data?.size() == 0
    }
}
