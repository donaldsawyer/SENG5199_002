package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
public class UserDetailsFunctionalSpec extends TwtrFunctionalTestBase {

    @Shared
    def months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

    def setup() {
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("ABCDr00t!")
        $("#login-form button[id=do-login]").click()
        waitFor(5, 0.1) { $('form').find("div", id: "page-status").text() == "Page load complete" }
    }

    def 'user details page displays the users name and tweets'() {
        when: 'user is navigated to user details page'
        go '/#/userDetail'
        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: "user's name, email, and tweets are displayed"
        !$('form').find('input', id: 'userEmail').readOnly
        !$('form').find('input', id: 'userDisplayName').readOnly
        $('form').find('input', id: 'userDisplayName').value() == "display name"
        $('form').find('input', id: 'userEmail').value() == "abac@abc.com"
        $('form').find("button", id: "save-account").enabled
        $('form').find("td", id: "td-feed-content").allElements().size() == 10
        $('form').find("td", id: "td-feed-content").allElements()[0].getText() == "Admin Written Message #20"
        $('form').find("td", id: "td-feed-content").allElements()[9].getText() == "Admin Written Message #11"
        $('form').find("td", id: "td-feed-date").allElements().size() == 10
        $('form').find("td", id: "td-feed-date").allElements()[0].getText() >= $('form').find("td", id: "td-feed-date").allElements()[9].getText()

        // REQ R5 //
        $('form').find("td", id: "td-feed-date").allElements()[0].getText().size() == 6
        months.contains($('form').find("td", id: "td-feed-date").allElements()[0].getText().substring(0,3))
        $('form').find("td", id: "td-feed-date").allElements()[0].getText()[3] == ' '
        $('form').find("td", id: "td-feed-date").allElements()[0].getText().substring(4,6).isNumber()

        // verify that the tweets are scrollable by the table being larger than the div //
        $('form').find('table', id: "user-tweets-table").height > $('form').find('div', id: "user-tweets").height

        when: "update the user's name and email"
        $("#login-form input[id=userEmail]").value("updatedemail@email.com")
        $("#login-form input[id=userDisplayName]").value("updated Name")
        $("#login-form button[id=save-account]").click()
        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: "user's name and email are updated"
        $('form').find('input', id: 'userDisplayName').value() == "updated Name"
        $('form').find('input', id: 'userEmail').value() == "updatedemail@email.com"
    }

    def 'user details page allows user to post a new message'() {
        when: 'user is navigated to user details page'
        go '/#/userDetail'
        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: 'user can tweet a new message'
        $('form').find('input', id: 'message-to-post')
        $('form').find('button', id: 'reset-button').enabled
        !$('form').find('button', id: 'tweet-button').enabled

        when: 'user tweet a new message'
        $("#tweet-form input[id=message-to-post]").value("New Tweet from Admin")

        then: 'tweet button is enabled'
        $("#tweet-form button[id=tweet-button]").enabled

        when: 'click on tweet button'
        $("#tweet-form button[id=tweet-button]").click()
        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: 'status indicate that new message is posted'
        $('form').find('div', id: "tweet-message-status").text() == "New Tweet-message posted"
        !$('form').find('div', id: "tweet-message-error").text()
        $('form').find("td", id: "td-feed-content").allElements()[0].getText() == "New Tweet from Admin"

//        when: 'tweet message field is empty'
//        $("#tweet-form input[id=message-to-post]").value("")
//        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}
//
//        then: 'error message displays'
//        !$('form').find('button', id: 'tweet-button').enabled
//        $('form').find('div', id: 'invalid-message-span').text() == "Invalid message"
    }

    def "Other user's page is not allowed to post a new message"() {
        when: 'user is navigated to user details page'
        go '/#/userDetail?handle=luluwang'
        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: "tweet a new message option is not available"
        !$('form').find('input', id: 'message-to-post').text()
        !$('form').find('button', id: 'reset-button').text()
        !$('form').find('button', id: 'tweet-button').text()
    }

}