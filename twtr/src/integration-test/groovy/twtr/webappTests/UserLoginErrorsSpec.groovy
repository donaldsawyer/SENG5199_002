package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
public class UserLoginErrorsSpec extends TwtrFunctionalTestBase {

    def 'Admin logs in with the Login button with incorrect password'() {
        when: 'admin logs in with invalid password'
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("BadPassword")
        $("#login-form button[id=do-login]").click()
        sleep(500)

        then: 'system displays error message to user'
        $("#auth-token").text() == ""
        $("#error-login").text() == "Username and Password do not match a valid user."
        $('#logged-in-message').text() == ""
        $('#logout').text() == ""
    }

    def 'mikecalvo logs in with the Login button with incorrect password'(){
        when: 'mikecalvo logs in with invalid password'
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("BadPassword")
        $("#login-form button[id=do-login]").click()
        sleep(500)

        then: 'system display error message to user'
        $("#auth-token").text() == ""
        $("#error-login").text() == "Username and Password do not match a valid user."
        $('#logged-in-message').text() == ""
        $('#logout').text() == ""
    }

    def 'luluwang logs in with the Login button with incorrect username'(){
        when: 'luluwang logs in with invalid username'
        go '/'
        $("#login-form input[id=loginHandle]").value("luluwang1")
        $("#login-form input[id=loginPassword]").value("abcABC123!@#")
        $("#login-form button[id=do-login]").click()
        sleep(500)

        then: 'system display error message to user'
        $("#auth-token").text() == ""
        $("#error-login").text() == "Username and Password do not match a valid user."
        $('#logged-in-message').text() == ""
        $('#logout').text() == ""
    }
}