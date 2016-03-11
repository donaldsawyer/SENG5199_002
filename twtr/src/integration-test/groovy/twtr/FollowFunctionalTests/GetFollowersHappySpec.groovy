package twtr.FollowFunctionalTests

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Stepwise

@Integration
@Stepwise
class GetFollowersHappySpec extends GebSpec {
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

    def 'new accounts have no followers'() {
        when:
        goodId1 = addAccount('1')
        goodId2 = addAccount('2')
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 2

        when:
        def data1 = response.data.find { it -> it.id == goodId1 }
        def data2 = response.data.find { it -> it.id == goodId2 }

        then:
        data1.id == goodId1
        data1.followerCount == 0
        data1.followingCount == 0
        data2.id == goodId2
        data2.followerCount == 0
        data2.followingCount == 0

        when:
        response = restClient.get(path: "/accounts/$goodId1")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.followerCount == 0
        response.data.followingCount == 0

        when:
        response = restClient.get(path: "/accounts/$goodId2")

        then:
        response.status == 200
        response.data
        response.data.id == goodId2
        response.data.followerCount == 0
        response.data.followingCount == 0

        when:
        response = restClient.get(path: "/accounts/$goodId1/followers")

        then:
        response.status == 200
        response.data.size == 0

        when:
        response = restClient.get(path: "/accounts/$goodId2/followers")

        then:
        response.status == 200
        response.data.size == 0

        when:
        response = restClient.get(path: "/accounts/$goodId1/following")

        then:
        response.status == 200
        response.data.size == 0

        when:
        response = restClient.get(path: "/accounts/$goodId2/following")

        then:
        response.status == 200
        response.data.size == 0
    }

    def '#goodId1 follows #goodId2'() {
        when:
        def response = restClient.post(path: "/accounts/$goodId1/startFollowing", query: [followAccount: goodId2],
                contentType: 'application/json', body: [])

        then:
        response.status == 201
        response.data
        response.data.followerCount == 0
        response.data.followingCount == 1

        when:
        response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 2

        when:
        def data1 = response.data.find { it -> it.id == goodId1 }
        def data2 = response.data.find { it -> it.id == goodId2 }

        then:
        data1.id == goodId1
        data1.followerCount == 0
        data1.followingCount == 1
        data2.id == goodId2
        data2.followerCount == 1
        data2.followingCount == 0

        when:
        response = restClient.get(path: "/accounts/$goodId1")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.followerCount == 0
        response.data.followingCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId2")

        then:
        response.status == 200
        response.data
        response.data.id == goodId2
        response.data.followerCount == 1
        response.data.followingCount == 0

        when:
        response = restClient.get(path: "/accounts/$goodId1/followers")

        then:
        response.status == 200
        response.data.size == 0

        when:
        response = restClient.get(path: "/accounts/$goodId2/followers")

        then:
        response.status == 200
        response.data.size == 1

        when:
        def postfix = '1'
        def data = response.data.find { it -> it.id == goodId1 }

        then:
        data.id == goodId1
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 1
        data.followerCount == 0

        when:
        response = restClient.get(path: "/accounts/$goodId1/following")

        then:
        response.status == 200
        response.data.size == 1

        when:
        postfix = '2'
        data = response.data.find { it -> it.id == goodId2 }

        then:
        data.id == goodId2
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 0
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId2/following")

        then:
        response.status == 200
        response.data.size == 0
    }

    def '#goodId2 follows #goodId1'() {
        when:
        def response = restClient.post(path: "/accounts/$goodId2/startFollowing", query: [followAccount: goodId1],
                contentType: 'application/json', body: [])

        then:
        response.status == 201
        response.data
        response.data.followerCount == 1
        response.data.followingCount == 1

        when:
        response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 2

        when:
        def data1 = response.data.find { it -> it.id == goodId1 }
        def data2 = response.data.find { it -> it.id == goodId2 }

        then:
        data1.id == goodId1
        data1.followerCount == 1
        data1.followingCount == 1
        data2.id == goodId2
        data2.followerCount == 1
        data2.followingCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId1")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.followerCount == 1
        response.data.followingCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId2")

        then:
        response.status == 200
        response.data
        response.data.id == goodId2
        response.data.followerCount == 1
        response.data.followingCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId1/followers")

        then:
        response.status == 200
        response.data.size == 1

        when:
        def postfix = '2'
        def data = response.data.find { it -> it.id == goodId2 }

        then:
        data.id == goodId2
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 1
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId2/followers")

        then:
        response.status == 200
        response.data.size == 1

        when:
        postfix = '1'
        data = response.data.find { it -> it.id == goodId1 }

        then:
        data.id == goodId1
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 1
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId1/following")

        then:
        response.status == 200
        response.data.size == 1

        when:
        postfix = '2'
        data = response.data.find { it -> it.id == goodId2 }

        then:
        data.id == goodId2
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 1
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId2/following")

        then:
        response.status == 200
        response.data.size == 1

        when:
        postfix = '1'
        data = response.data.find { it -> it.id == goodId1 }

        then:
        data.id == goodId1
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 1
        data.followerCount == 1
    }

    def '#goodId2 follows a new account'() {
        when:
        goodId3 = addAccount('3')
        def response = restClient.post(path: "/accounts/$goodId2/startFollowing", query: [followAccount: goodId3],
                contentType: 'application/json', body: [])

        then:
        response.status == 201
        response.data
        response.data.followerCount == 1
        response.data.followingCount == 2

        when:
        response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 3

        when:
        def data1 = response.data.find { it -> it.id == goodId1 }
        def data2 = response.data.find { it -> it.id == goodId2 }
        def data3 = response.data.find { it -> it.id == goodId3 }

        then:
        data1.id == goodId1
        data1.followerCount == 1
        data1.followingCount == 1
        data2.id == goodId2
        data2.followerCount == 1
        data2.followingCount == 2
        data3.id == goodId3
        data3.followerCount == 1
        data3.followingCount == 0

        when:
        response = restClient.get(path: "/accounts/$goodId1")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.followerCount == 1
        response.data.followingCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId2")

        then:
        response.status == 200
        response.data
        response.data.id == goodId2
        response.data.followerCount == 1
        response.data.followingCount == 2

        when:
        response = restClient.get(path: "/accounts/$goodId3")

        then:
        response.status == 200
        response.data
        response.data.id == goodId3
        response.data.followerCount == 1
        response.data.followingCount == 0

        when:
        response = restClient.get(path: "/accounts/$goodId1/followers")

        then:
        response.status == 200
        response.data.size == 1

        when:
        def postfix = '2'
        def data = response.data.find { it -> it.id == goodId2 }

        then:
        data.id == goodId2
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 2
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId2/followers")

        then:
        response.status == 200
        response.data.size == 1

        when:
        postfix = '1'
        data = response.data.find { it -> it.id == goodId1 }

        then:
        data.id == goodId1
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 1
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId3/followers")

        then:
        response.status == 200
        response.data.size == 1

        when:
        postfix = '2'
        data = response.data.find { it -> it.id == goodId2 }

        then:
        data.id == goodId2
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 2
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId1/following")

        then:
        response.status == 200
        response.data.size == 1

        when:
        postfix = '2'
        data = response.data.find { it -> it.id == goodId2 }

        then:
        data.id == goodId2
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 2
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId2/following")

        then:
        response.status == 200
        response.data.size == 2

        when:
        postfix = '1'
        data = response.data.find { it -> it.id == goodId1 }

        then:
        data.id == goodId1
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 1
        data.followerCount == 1

        when:
        postfix = '3'
        data = response.data.find { it -> it.id == goodId3 }

        then:
        data.id == goodId3
        data.handle == goodHandle + postfix
        data.displayName == goodDisplayName + postfix
        data.emailAddress == goodEmailAccount + postfix + goodEmailDomain
        data.followingCount == 0
        data.followerCount == 1

        when:
        response = restClient.get(path: "/accounts/$goodId3/following")

        then:
        response.status == 200
        response.data.size == 0
    }

    def 'cleanup test data'() {
        when:
        restClient.delete(path: "/accounts/${goodId1}")
        restClient.delete(path: "/accounts/${goodId3}")
        restClient.delete(path: "/accounts/${goodId2}")
        def response = restClient.get(path: '/accounts')

        then:
        response.data.size() == 0
    }
}