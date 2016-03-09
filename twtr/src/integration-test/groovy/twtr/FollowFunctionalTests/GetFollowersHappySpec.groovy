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

    def cleanup() {
//        restClient.delete(path:"/accounts/${goodId1}")
//        restClient.delete(path:"/accounts/${goodId2}")
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
//        def response = restClient.post(path: '/accounts', contentType: 'application/json',
//                body: [handle      : 'handle1',
//                       emailAddress: 'handle1@scsu.edu',
//                       password    : 'abcABC123',
//                       displayName : 'handle 1'])
//        goodId1 = response.data.id
        goodId1 = addAccount('1')
        def response = restClient.get(path:'/accounts')

        then:
        response.status == 200
        response.data.size() == 1

        when:
        def data1 = response.data.find { it -> it.id == goodId1 }

        then:
        data1.id == goodId1
        data1.followerCount == 0
        data1.followingCount == 0

        when:
        response = restClient.get(path:"/accounts/$goodId1")

        then:
        response.status == 200
        response.data
        response.data.id == goodId1
        response.data.followerCount == 0
        response.data.followingCount == 0

        when:
        response = restClient.get(path:"/accounts/$goodId1/followers")

        then:
        response.status == 200
        response.data.size == 0
    }

    def 'cleanup data'() {
        when:
        def response = restClient.delete(path:"/accounts/${goodId1}")
        response = restClient.get(path:'/accounts')

        then:
        response.data.size() == 0
    }
}