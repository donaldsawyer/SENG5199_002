package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
public class UsersLoginOk extends TwtrFunctionalTestBase {
    def 'Navigates the user to home page when page first loads'() {
        when: 'User is navigated to login screen'
        go '/'

        then:
        $('form').find("input", id: "loginHandle").displayed
        $('form').find("input", id: "loginPassword").displayed
        $('form').find("h2", id: "please-login").text() == "Please Login..."
    }

    def 'Admin logs in with the Login button'() {
        when: 'Admin logs in successfully'
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("ABCDr00t!")
        $("#login-form button[id=do-login]").click()
        waitFor(5, 0.1) { $('form').find("div", id: "page-status").text() == "Page load complete" }

        then: 'token is generated and status indicates that user is logged in'
        $('form').find("button", id: "logout").text() == "Logout"
        $('form').find("h2", id: "logged-in-message").text() == 'You are currently logged in as @admin'
        !($('form').find("div", id: "error-login").text())
    }

    def 'User luluwang logs in with the Login button'() {
        when: 'luluwang logs in successfully'
        go '/'
        $("#login-form input[id=loginHandle]").value("luluwang")
        $("#login-form input[id=loginPassword]").value("abcABC123!@#")
        $("#login-form button[id=do-login]").click()
        waitFor(5, 0.1) { $('form').find("div", id: "page-status").text() == "Page load complete" }

        then: 'token is generated and status indicates that user is logged in'
        $('form').find("button", id: "logout").text() == "Logout"
        $('form').find("h2", id: "logged-in-message").text() == 'You are currently logged in as @luluwang'
        !($('form').find("div", id: "error-login").text())
    }

    def 'User logs out successfully'() {
        when: 'Admin logs in successfully'
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("ABCDr00t!")
        $("#login-form button[id=do-login]").click()
        waitFor(5, 0.1) { $('form').find("div", id: "page-status").text() == "Page load complete" }

        then: 'token is generated and status indicates that user is logged in'
        $('form').find("button", id: "logout").text() == "Logout"
        $('form').find("h2", id: "logged-in-message").text() == 'You are currently logged in as @admin'
        !($('form').find("div", id: "error-login").text())

        when: 'logout button is clicked'
        $("#login-form button[id=logout").click()
        waitFor(5, 0.1) { $('form').find("div", id: "page-status").text() == "Page load complete" }

        then: 'status indicates that user is logged out'
        $('form').find("h2", id: "logged-out-message").text() == "We'll miss you dearly!"
        !$('form').find("h2", id: "logged-in-message").text()
        $('form').find("h2", id: "please-login").text() == "Please Login..."
        $('form').find("input", id: "loginHandle").displayed
        $('form').find("input", id: "loginPassword").displayed
    }
}