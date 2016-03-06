package twtr

import geb.spock.GebSpec
import grails.converters.JSON
import groovyx.net.http.RESTClient
import org.springframework.boot.test.IntegrationTest
import spock.lang.Shared
import spock.lang.Stepwise

@IntegrationTest
@Stepwise
class AccountFunctionalSpec extends GebSpec{

    final static String goodHandle = 'scsu_huskies'
    final static String goodEmail  = 'testemail@test.com'
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    final static String handle1 = 'scsu_huskies1'
    final static String email1  = 'testemail1@test.com'
    final static String password1 = 'abc123ABC'
    final static String displayName1 = 'SCSU Huskies1'

    @Shared
    def accountID

    //@Shared
    //def startingAccountNum

    RESTClient restClient



    def setup
    {
        restClient = new RESTClient("http://localhost:8080/account/")
        //startingAccountNum = getStartingAccountNum()
    }


    def 'create an account with valid JSON data'()
    {
        given:
        Account account = new Account(handle: goodHandle, emailAddress: goodEmail,
                password: goodPassword, displayName: goodDisplayName)

        //def accountJSON = account as JSON

        when:
        def response = restClient.post(path: 'save', contentType: 'application/json',
                                       body: [handle: goodHandle, emailAddress: goodEmail,
                                              password: goodPassword, displayName: goodDisplayName])
        def code = response

        then:
        response.status == 201

        then:
        response.responseData['handle'] == goodHandle
        response.responseData['emailAddress'] == goodEmail
        response.responseData['displayName'] == goodDisplayName
        response.responseData['password'] == goodPassword
    }

    def 'get the account created with #goodHandle'() {

    }

    def 'create an account with invalid'()
    {

    }
}
