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
        $('#loginHandle').value('admin')
        $('#loginPassword').value('BadPassword')
        $('#do-login').click()
        waitFor(10, 0.1) { $('#page-status').text() == 'Page load complete' }

        then: 'system displays error message to user'
        $('#error-login').text() == 'Username and Password do not match a valid user.'
        !$('#logged-in-message').text()
        !$('#logout').text()
    }

    def 'mikecalvo logs in with the Login button with incorrect password'() {
        when: 'mikecalvo logs in with invalid password'
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("BadPassword")
        $("#login-form button[id=do-login]").click()
        waitFor(10, 0.1) { $('form').find("div", id: "page-status").text() == "Page load complete" }

        then: 'system displays error message to user'
        $('form').find("div", id: "error-login").text() == "Username and Password do not match a valid user."
        !$('form').find("h2", id: "logged-in-message").text()
        !$('form').find("button", id: "logout").text()
    }

    def 'luluwang logs in with the Login button with incorrect username'() {
        when: 'luluwang logs in with invalid username'
        go '/'
        $("#login-form input[id=loginHandle]").value("luluwang1")
        $("#login-form input[id=loginPassword]").value("abcABC123!@#")
        $("#login-form button[id=do-login]").click()
        waitFor(10, 0.1) { $('form').find("div", id: "page-status").text() == "Page load complete" }

        then: 'system displays error message to user'
        $('form').find("div", id: "error-login").text() == "Username and Password do not match a valid user."
        !$('form').find("h2", id: "logged-in-message").text()
        !$('form').find("button", id: "logout").text()
    }
}