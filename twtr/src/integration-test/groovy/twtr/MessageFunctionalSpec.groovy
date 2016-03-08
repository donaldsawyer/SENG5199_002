package twtr

import geb.spock.GebSpec
import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Stepwise


@Integration
@Stepwise
class MessageFunctionalSpec extends GebSpec {

    RESTClient restClient

    @Shared
    def goodId

    def setup() {
        restClient = new RESTClient("http://localhost:8080")
    }

    def 'get all messages for account 1'() {
        when:
        def response = restClient.get(path: '/accounts/1/messages')

        then:
        response.status == 200
        response.data.size() == 0
    }


}