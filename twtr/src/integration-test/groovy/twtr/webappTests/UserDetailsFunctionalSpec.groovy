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
        $("#loginHandle").value("admin")
        $("#loginPassword").value("ABCDr00t!")
        $("#do-login").click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }
    }

    def 'user details page displays the users name and tweets'() {
        when: 'user is navigated to user details page'
        go '/#/userDetail'
        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: "user's name, email, and tweets are displayed"
        !$('#userEmail').readOnly
        !$('#userDisplayName').readOnly
        $('#userDisplayName').value() == "display name"
        $('#userEmail').value() == "abac@abc.com"
        $('#save-account').enabled
        $('#td-feed-content').allElements().size() == 10
        $('#td-feed-content').allElements()[0].getText() == "Admin Written Message #20"
        $('#td-feed-content').allElements()[9].getText() == "Admin Written Message #11"
        $('#td-feed-date').allElements().size() == 10
        $('#td-feed-date').allElements()[0].getText() >= $('#td-feed-date').allElements()[9].getText()

        // REQ R5 //
        $('#td-feed-date').allElements()[0].getText().size() == 6
        months.contains($('#td-feed-date').allElements()[0].getText().substring(0,3))
        $('#td-feed-date').allElements()[0].getText()[3] == ' '
        $('#td-feed-date').allElements()[0].getText().substring(4,6).isNumber()

        // verify that the tweets are scrollable by the table being larger than the div //
        $('#user-tweets-table').height > $('#user-tweets').height

        when: "update the user's name and email"
        $('#userEmail').value("updatedemail@email.com")
        $('#userDisplayName').value("updated Name")
        $('#save-account').click()
        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: "user's name and email are updated"
        $('#userDisplayName').value() == "updated Name"
        $('#userEmail').value() == "updatedemail@email.com"
    }

    def 'user details page allows user to post a new message'() {
        when: 'user is navigated to user details page'
        go '/#/userDetail'
        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: 'user can tweet a new message'
        $('#message-to-post')
        $('#reset-button').enabled
        !$('#tweet-button').enabled

        when: 'user tweet a new message'
        $('#message-to-post').value("New Tweet from Admin")

        then: 'tweet button is enabled'
        // REQ R0 //
        $('#tweet-button').enabled

        when: 'click on tweet button'
        $('#tweet-button').click()
        waitFor(3, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: 'status indicate that new message is posted'
        // REQ R0 //
        $('#td-feed-content').allElements()[0].getText() == "New Tweet from Admin"
        // REQ R1 //
        $('#tweet-post-alert').displayed
        $('#tweet-post-alert').text().contains("Message Posted!")

        when: 'empty tweet message field'
        $('#message-to-post').value("testValue")
        $('#message-to-post').value("")
        $('#userEmail').value("a@b.com")
        sleep(2000)

        then: 'error message displays'
        // REQ R2 //
        !$("#tweet-button").enabled
        $("#invalid-message-span").text() == "Invalid message"
    }

    def "Other user's page is not allowed to post a new message"() {
        when: 'user is navigated to user details page'
        go '/#/userDetail?handle=luluwang'
        waitFor(5, 0.1) { $("#feed-page-status").text() == "Page load complete"}

        then: "tweet a new message option is not available"
        !$('message-to-post').text()
        !$('reset-button').text()
        !$('tweet-button').text()
    }

}