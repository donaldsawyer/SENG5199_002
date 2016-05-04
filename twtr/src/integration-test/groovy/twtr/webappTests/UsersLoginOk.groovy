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
        $('#loginHandle').displayed
        $('#loginPassword').displayed
        $('#please-login').text() == "Please Login..."
    }

    def 'User cannot go to search page'() {
        when: 'user clicks on nav bar for search'
        $('#nav-search').click()
        waitFor(5, 0.1) { $('#page-status').text() == 'Page load complete' }

        then:
        getCurrentUrl().endsWith('#/home')
    }

    def 'User cannot go to user detail page'() {
        when: 'user clicks on nav bar for user details'
        $('#nav-userDetail').click()
        waitFor(5, 0.1) { $('#page-status').text() == 'Page load complete' }

        then:
        getCurrentUrl().endsWith('#/home')
    }

    def 'Admin logs in with the Login button'() {
        when: 'Admin logs in successfully'
        go '/'
        $('#loginHandle').value("admin")
        $('#loginPassword').value("ABCDr00t!")
        $('#do-login').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'token is generated and status indicates that user is logged in'
        $('#logout').text() == "Logout"
        $('#logged-in-message').text() == 'You are currently logged in as @admin'
        !($('#error-login').text())
    }

    def 'User luluwang logs in with the Login button'() {
        when: 'luluwang logs in successfully'
        go '/'
        $('#loginHandle').value("luluwang")
        $('#loginPassword').value("abcABC123!@#")
        $('#do-login').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'token is generated and status indicates that user is logged in'
        $('#logout').text() == "Logout"
        $('#logged-in-message').text() == 'You are currently logged in as @luluwang'
        !($('#error-login').text())
    }

    def 'User can go to search page'() {
        when: 'user clicks on nav bar for search'
        $('#nav-search').click()
        waitFor(5, 0.1) { $('#page-status').text() == 'Page load complete' }

        then:
        getCurrentUrl().endsWith('#/search')
    }

    def 'User can go to user detail page'() {
        when: 'user clicks on nav bar for user details'
        $('#nav-userDetail').click()
        waitFor(5, 0.1) { $('#feed-page-status').text() == 'Page load complete' }

        then:
        getCurrentUrl().endsWith('#/userDetail')
    }

    def 'User logs out successfully'() {
        when: 'Admin logs in successfully'
        go '/'
        $('#loginHandle').value("admin")
        $('#loginPassword').value("ABCDr00t!")
        $('#do-login').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'token is generated and status indicates that user is logged in'
        $('#logout').text() == "Logout"
        $('#logged-in-message').text() == 'You are currently logged in as @admin'
        !($('#error-login').text())

        when: 'logout button is clicked'
        $('#logout').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'status indicates that user is logged out'
        $('#logged-out-message').text() == "We'll miss you dearly!"
        !$('#logged-in-message').text()
        $('#please-login').text() == "Please Login..."
        $('#loginHandle').displayed
        $('#loginPassword').displayed
    }
}