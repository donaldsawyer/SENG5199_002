package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
public class AdminLoginOk extends TwtrFunctionalTestBase
{

    def 'login button logs in'() {
        when:
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("ABCDr00t!")
        $("#login-form button[id=do-login]").click()
        sleep(1000)

        then:
        $("#auth-token").text() != "{}"

    }
//
//import geb.spock.GebSpec
//import grails.test.mixin.integration.Integration
//
//@Integration
//class WelcomePageFunctionalSpec extends GebSpec {
//
//    def 'welcome page displays welcome message'() {
//        when:
//        go '/'
//
//        then: 'Static welcome displayed properly'
//        $('h1').first().text() == 'Hello World'
//
//        and: 'Angular generated test displayed properly'
//        $('h2').first().text() == 'Hello Stranger'
//    }
//
//}
}