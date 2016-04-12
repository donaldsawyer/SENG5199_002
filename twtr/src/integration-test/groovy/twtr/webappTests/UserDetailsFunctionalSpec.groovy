package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
public class UserDetailsFunctionalSpec extends TwtrFunctionalTestBase {
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
        waitFor(5, 0.1) { $('form').find("div", id: "feed-page-status").text() == "Page load complete" }
        sleep(500)

        then: "user's name, email, and tweets are displayed"
        $('form').find('input', id: 'userDisplayName').value() == "display name"
        $('form').find('input', id: 'userEmail').value() == "abac@abc.com"
        $('form').find("button", id: "save-account").enabled
        $('form').find("td", id: "td-feed-content").allElements().size() == 10
        $('form').find("td", id: "td-feed-content").allElements()[0].getText() == "Admin Written Message #20"
        $('form').find("td", id: "td-feed-content").allElements()[9].getText() == "Admin Written Message #11"
        $('form').find("td", id: "td-feed-date").allElements().size() == 10
        $('form').find("td", id: "td-feed-date").allElements()[0].getText() >= $('form').find("td", id: "td-feed-date").allElements()[9].getText()

        when: "update the user's name and email"
        $("#login-form input[id=userEmail]").value("updatedemail@email.com")
        $("#login-form input[id=userDisplayName]").value("updated Name")
        $("#login-form button[id=save-account]").click()
        waitFor(5, 0.1) { $('form').find("div", id: "feed-page-status").text() == "Page load complete" }

        then: "user's name and email are updated"
        $('form').find('input', id: 'userDisplayName').value() == "updated Name"
        $('form').find('input', id: 'userEmail').value() == "updatedemail@email.com"
    }

}