package twtr.FeedFunctionalTests

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Stepwise

@Integration
@Stepwise
class FeedHappySpec extends GebSpec {
    final static String goodHandle = 'scsu_huskies'
    final static String goodEmailAccount = 'testemail'
    final static String goodEmailDomain = '@scsu.edu'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'
    final static String messageText = 'Hello World! Message #'

    @Shared
    def goodId1

    @Shared
    def goodId2

    @Shared
    def goodId3

    @Shared
    def messagesSent = [:]

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

    def tweet(def id, String postfix) {
        def response = restClient.post(path: "/message/tweet",
                query: [accountId: id],
                contentType: 'application/json',
                body: [messageText: messageText + postfix])
        messagesSent << ["$postfix": response.data.dateCreated]
        sleep(1000)
    }

    def 'add accounts for feed'() {
        when:
        goodId1 = addAccount('1')
        goodId2 = addAccount('2')
        goodId3 = addAccount('3')
        def response = restClient.get(path: '/accounts')

        then:
        response.status == 200
        response.data.size() == 3
    }

    def 'account #goodId1 feed is empty'() {
        when:
        def response = restClient.get(path: "/accounts/$goodId1/feed")

        then:
        response.status == 200
        response.data?.size() == 0
    }

    def '#goodId1 follows #goodId2 and #goodId3'() {
        when:
        restClient.post(path: "/accounts/$goodId1/startFollowing",
                query: [followAccount: goodId2],
                contentType: 'application/json')
        def response = restClient.post(path: "/accounts/$goodId1/startFollowing",
                query: [followAccount: goodId3],
                contentType: 'application/json')

        then:
        response.status == 201
        response.data?.followingCount == 2
    }

    def '#goodId1 feed is still empty'() {
        when:
        def response = restClient.get(path: "/accounts/$goodId1/feed")

        then:
        response.status == 200
        response.data?.size() == 0
    }

    def '#goodId2 sends 6 tweets'() {
        when:
        (1..6).each { it -> tweet(goodId2, "m$it") }
        def response = restClient.get(path: "/accounts/$goodId1/feed")

        then:
        response.status == 200
        response.data?.size() == 6
        (1..6).each { it ->
            assert response.data[it-1].dateCreated == messagesSent.find { m -> m.key == "m${6-it+1}"}.value
        }
    }

    def '#goodId3 sends 6 tweets'() {
        when:
        (7..12).each { it -> tweet(goodId3, "m$it") }
        def response = restClient.get(path: "/accounts/$goodId1/feed")

        then:
        response.status == 200
        response.data?.size() == 10
        (3..12).each { it ->
            assert response.data[it-3].dateCreated == messagesSent.find { m -> m.key == "m${12-it+3}"}.value
        }
    }

    def 'feed uses limit of 3'() {
        when:
        def response = restClient.get(path: "/accounts/$goodId1/feed", query:[max: 3])

        then:
        response.status == 200
        response.data?.size() == 3
        (1..3).each { it ->
            assert response.data[it-1].dateCreated == messagesSent.find { m -> m.key == "m${12-it+1}"}.value
        }
    }

    def 'feed uses limit of 3 and offset of 3'() {
        when:
        def response = restClient.get(path: "/accounts/$goodId1/feed", query:[max: 3, offset: 3])

        then:
        response.status == 200
        response.data?.size() == 3
        (1..3).each { it ->
            assert response.data[it-1].dateCreated == messagesSent.find { m -> m.key == "m${9-it+1}"}.value
        }
    }

    def 'feed only takes messages at/after a certain date/time'() {
        when:
        def midDate = messagesSent.find { m -> m.key == 'm4'}.value
        def response = restClient.get(path: "/accounts/$goodId1/feed", query:[fromDate: midDate])

        then:
        response.status == 200
        response.data?.size() == 9
        (1..9).each { it ->
            assert response.data[it-1].dateCreated == messagesSent.find { m -> m.key == "m${12-it+1}"}.value
        }
    }

    def 'remove test data'() {
        when:
        restClient.delete(path: "/accounts/${goodId1}")
        restClient.delete(path: "/accounts/${goodId2}")
        restClient.delete(path: "/accounts/${goodId3}")
        def response = restClient.get(path: '/accounts')

        then:
        response.data.size() == 0
    }
}
