package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
public class UserDetailsFollowFunctionalSpec extends TwtrFunctionalTestBase {
    def setup() {
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("ABCDr00t!")
        $("#login-form button[id=do-login]").click()
        waitFor(5, 0.1) { $('form').find("div", id: "page-status").text() == "Page load complete" }
    }

    def 'user admin starts to follow mikecalvo'() {
        when: "user mikecalvo's user detail page is displayed"
        go '/#/userDetail?handle=mikecalvo'
        waitFor(5, 0.1) { $('form').find("div", id: 'feed-page-status').text() == 'Page load complete' }
        sleep(500)

        then: 'admin can view mikecalvo user detail page'
        $('form').find('input', id: 'userDisplayName').value() == "Mike Calvo"
        $('form').find('input', id: 'userEmail').value() == "mike.calvo@scsu.edu"
        $('form').find('input', id: 'userDisplayName').readOnly
        $('form').find('input', id: 'userEmail').readOnly
        !$('form').find('button', id: 'save-account').enabled
        $('form').find('button', id: 'follow').enabled
        $('form').find("td", id: "td-feed-content").allElements().size() == 0

        when: 'click on follow button for mike calvo'
        $("#login-form button[id=follow").click()
        //TODO: Do the waitFor here
        sleep(500)

        then: "admin can follow mikecalvo"
        $('form').find('div', id: 'follow-message').text() == 'You are following Mike Calvo'

        when: 'navigate to blizzard page'
        go '/#/userDetail?handle=blizzard'
        waitFor(5, 0.1) { $('form').find('div', id: 'feed-page-status').text() == 'Page load complete' }
        sleep(500)

        then: 'admin can view blizzard page'
        $('form').find('input', id: 'userDisplayName').value() == "Blizzard"
        $('form').find('input', id: 'userEmail').value() == "blizzard@scsu.edu"
        $('form').find('input', id: 'userDisplayName').readOnly
        $('form').find('input', id: 'userEmail').readOnly
        !$('form').find('button', id: 'save-account').enabled
        $('form').find('button', id: 'follow').enabled
        $('form').find("td", id: "td-feed-content").allElements().size() == 1
        $('form').find("td", id: "td-feed-content").allElements()[0].getText() == "Message 4_1"
        $('form').find("td", id: "td-feed-date").allElements().size() == 1

        when: 'navigate back to mikecalvo page'
        go '/#/userDetail?handle=mikecalvo'
        waitFor(5, 0.1) { $('form').find("div", id: 'feed-page-status').text() == 'Page load complete' }
        sleep(500)

        then: 'admin can view mikecalvo user detail page'
        $('form').find('input', id: 'userDisplayName').value() == "Mike Calvo"
        $('form').find('input', id: 'userEmail').value() == "mike.calvo@scsu.edu"
        $('form').find('input', id: 'userDisplayName').readOnly
        $('form').find('input', id: 'userEmail').readOnly
        !$('form').find('button', id: 'save-account').enabled
        $('form').find("td", id: "td-feed-content").allElements().size() == 0
        $('form').find('div', id: 'follow-message').text() == 'You are following Mike Calvo'
    }
}