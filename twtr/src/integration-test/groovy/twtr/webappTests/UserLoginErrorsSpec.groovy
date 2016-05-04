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
        $('#loginHandle').value("admin")
        $('#loginPassword').value("BadPassword")
        $('#do-login').click()
        waitFor(10, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'system displays error message to user'
        $('#error-login').text() == "Username and Password do not match a valid user."
        !$('#logged-in-message').text()
        !$('#logout').text()
    }

    def 'mikecalvo logs in with the Login button with incorrect password'() {
        when: 'mikecalvo logs in with invalid password'
        go '/'
        $('#loginHandle').value("admin")
        $('#loginPassword').value("BadPassword")
        $('#do-login').click()
        waitFor(10, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'system displays error message to user'
        $('#error-login').text() == "Username and Password do not match a valid user."
        !$('#logged-in-message').text()
        !$('#logout').text()
    }

    def 'luluwang logs in with the Login button with incorrect username'() {
        when: 'luluwang logs in with invalid username'
        go '/'
        $('#loginHandle').value("luluwang1")
        $('#loginPassword').value("abcABC123!@#")
        $('#do-login').click()
        waitFor(10, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'system displays error message to user'
        $('#error-login').text() == "Username and Password do not match a valid user."
        !$('#logged-in-message').text()
        !$('#logout').text()
    }
}