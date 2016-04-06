package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
public class UsersLoginOk extends TwtrFunctionalTestBase
{

    def 'Admin logs in with the Login button'() {
        when:
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("ABCDr00t!")
        $("#login-form button[id=do-login]").click()
        sleep(1000)

        then:
        $("#auth-token").text() != "{}"
        $("#error-login").text() == ""
        //$('h2').first().text() == 'You are currently logged in as @admin'
    }

    def 'User @luluwang logs in with the Login button'() {
        when:
        go '/'
        $("#login-form input[id=loginHandle]").value("luluwang")
        $("#login-form input[id=loginPassword]").value("abcABC123!@#")
        $("#login-form button[id=do-login]").click()
        sleep(1000)

        then:
        $("#auth-token").text() != "{}"
        $("#error-login").text() == ""
        //$('h2').first().text() == "You are currently logged in as @luluwang"
    }
}