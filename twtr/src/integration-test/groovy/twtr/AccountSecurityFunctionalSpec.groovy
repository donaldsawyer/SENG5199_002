package twtr

import geb.spock.GebSpec
import grails.converters.JSON
import grails.test.mixin.integration.Integration
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import spock.lang.*

@Integration
@Stepwise
class AccountSecurityFunctionalSpec extends GebSpec {

    RESTClient restClient

    @Shared
    def token

    def setup() {
        restClient = new RESTClient(baseUrl)
    }

    def 'calling accounts endpoint without token is forbidden'() {
        when:
        restClient.get(path: '/accounts')

        then:
        HttpResponseException problem = thrown(HttpResponseException)
        problem.statusCode == 403
        problem.message.contains('Forbidden')
    }

    def 'passing a valid username and password generates a token'() {
        setup:
        def authentication = ([
                username: 'admin',
                password: 'ABCDr00t!'] as JSON) as String

        when:
        def response = restClient.post(path: '/api/login', body: authentication, requestContentType: 'application/json')

        then:
        response.status == 200
        response.data.username == 'admin'
        response.data.roles == ['ROLE_READ']
        //noinspection GroovyDoubleNegation
        !!(token = response.data.access_token)
    }

    def 'using token access to accounts endpoint is allowed'() {
        when:
        def response = restClient.get(path: '/accounts', headers: ['X-Auth-Token': token])

        then:
        response.status == 200
        response.data.size() == 1
    }
}