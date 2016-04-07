package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
public class UsersLoginOk extends TwtrFunctionalTestBase
{

    def 'Admin logs in with the Login button'() {
        when: 'Admin logs in successfully'
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("ABCDr00t!")
        $("#login-form button[id=do-login]").click()
        sleep(500)

        then: 'token is generated and status indicates that user is logged in'
        $("#auth-token").text() != ""
        $("#error-login").text() == ""
        $('#logged-in-message').text() == 'You are currently logged in as @admin'
        $('#logout').text() == "Logout"
    }

    def 'User luluwang logs in with the Login button'() {
        when: 'luluwang logs in successfully'
        go '/'
        $("#login-form input[id=loginHandle]").value("luluwang")
        $("#login-form input[id=loginPassword]").value("abcABC123!@#")
        $("#login-form button[id=do-login]").click()
        sleep(500)

        then: 'token is generated and status indicates that user is logged in'
        $("#auth-token").text() != ""
        $("#error-login").text() == ""
        $('#logged-in-message').text() == "You are currently logged in as @luluwang"
        $('#logout').text() == "Logout"
    }
}