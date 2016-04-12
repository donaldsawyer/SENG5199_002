package twtr.FollowFunctionalTests

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import groovyx.net.http.RESTClient
import org.junit.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Ignore
@Integration
@Stepwise
class FollowHappySpec extends GebSpec {
    final static String goodHandle = 'scsu_huskies'
    final static String goodEmailAccount = 'testemail'
    final static String goodEmailDomain = '@scsu.edu'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    @Shared
    long goodId1

    @Shared
    long goodId2

    @Shared
    long goodId3

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

    def 'only two accounts with no followers or following'() {
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
        response.data.followerCount == 0
        response.data.followingCount == 0
        response.data.id == goodId1

        when:
        response = restClient.get(path: "/accounts/${goodId2}")

        then:
        response.status == 200
        response.data
        response.data.followerCount == 0
        response.data.followingCount == 0
        response.data.id == goodId2
    }

    def 'Account 1 can follow Account 2'() {
        when:
        def response = restClient.get(path: "/accounts/${goodId1}")

        then:
        response.status == 200
        response.data
        response.data.followerCount == 0
        response.data.followingCount == 0

        when:
        response = restClient.post(path: "/accounts/${goodId1}/startFollowing", query: [followAccount: goodId2],
                contentType: 'application/json', body: [])

        then:
        response.status == 201
        response.data
        response.data.id == goodId1
        response.data.followerCount == 0
        response.data.followingCount == 1

        when:
        response = restClient.get(path: "/accounts/${goodId1}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.followerCount == 0
        response.data.followingCount == 1

        when:
        response = restClient.get(path: "/accounts/${goodId1}/following")

        then:
        response.status == 200
        response.data
        response.data.size() == 1

        when:
        def data = response.data.find { it -> it.id == goodId2 }

        then:
        data
        data.followingCount == 0
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/${goodId2}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId2
        response.data.followerCount == 1
        response.data.followingCount == 0

        when:
        response = restClient.get(path: "/accounts/${goodId2}/followers")

        then:
        response.status == 200
        response.data
        response.data.size() == 1

        when:
        data = response.data.find { it -> it.id == goodId1 }

        then:
        data
        data.followingCount == 1
        data.followerCount == 0
    }

    def 'Account 2 can follow Account 1'() {
        when:
        def response = restClient.post(path: "/accounts/${goodId2}/startFollowing", query: [followAccount: goodId1],
                contentType: 'application/json', body: [])

        then:
        response.status == 201
        response.data
        response.data.id == goodId2
        response.data.followerCount == 1
        response.data.followingCount == 1

        when:
        response = restClient.get(path: "/accounts/${goodId2}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId2
        response.data.followerCount == 1
        response.data.followingCount == 1

        when:
        response = restClient.get(path: "/accounts/${goodId2}/following")

        then:
        response.status == 200
        response.data
        response.data.size() == 1

        when:
        def data = response.data.find { it -> it.id == goodId1 }

        then:
        data
        data.followingCount == 1
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/${goodId1}/followers")

        then:
        response.status == 200
        response.data
        response.data.size() == 1

        when:
        data = response.data.find { it -> it.id == goodId2 }

        then:
        data
        data.followingCount == 1
        data.followerCount == 1
    }

    def 'Account 1 follows Account 3'() {
        setup:
        goodId3 = addAccount('3')

        when:
        def response = restClient.post(path: "/accounts/${goodId1}/startFollowing", query: [followAccount: goodId3],
                contentType: 'application/json', body: [])

        then:
        response.status == 201
        response.data
        response.data.id == goodId1
        response.data.followerCount == 1
        response.data.followingCount == 2

        when:
        response = restClient.get(path: "/accounts/${goodId1}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.followerCount == 1
        response.data.followingCount == 2

        when:
        response = restClient.get(path: "/accounts/${goodId1}/following")

        then:
        response.status == 200
        response.data
        response.data.size() == 2

        when:
        def data = response.data.find { it -> it.id == goodId3 }

        then:
        data
        data.followingCount == 0
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/${goodId3}")

        then:
        response.status == 200
        response.data
        response.data.id == goodId3
        response.data.followerCount == 1
        response.data.followingCount == 0

        when:
        response = restClient.get(path: "/accounts/${goodId3}/followers")

        then:
        response.status == 200
        response.data
        response.data.size() == 1

        when:
        data = response.data.find { it -> it.id == goodId1 }

        then:
        data
        data.followingCount == 2
        data.followerCount == 1
    }

    def 'cleanup tests'() {
        when:
        restClient.delete(path: "/accounts/${goodId1}")
        restClient.delete(path: "/accounts/${goodId2}")
        restClient.delete(path: "/accounts/${goodId3}")
        def response = restClient.get(path: '/accounts')

        then:
        response.data.size() == 0
    }
}
