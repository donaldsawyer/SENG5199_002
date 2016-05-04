package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
public class UserDetailsFollowFunctionalSpec extends TwtrFunctionalTestBase {
    def setup() {
        go '/'
        $('#loginHandle').value("admin")
        $('#loginPassword').value("ABCDr00t!")
        $('#do-login').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }
    }

    def 'user admin starts to follow mikecalvo'() {
        when: "user mikecalvo's user detail page is displayed"
        go '/#/userDetail?handle=mikecalvo'
        waitFor(5, 0.1) { $('#feed-page-status').text() == 'Page load complete' }

        then: 'admin can view mikecalvo user detail page'
        $('#userDisplayName').value() == "Mike Calvo"
        $('#userEmail').value() == "mike.calvo@scsu.edu"
        $('#userDisplayName').readOnly
        $('#userEmail').readOnly
        !$('#save-account').enabled
        $('#follow').enabled
        $('#td-feed-content').allElements().size() == 0

        when: 'click on follow button for mike calvo'
        $('#follow').click()
        waitFor(5, 0.1) { $('#feed-page-status').text() == 'Page load complete' }

        // REQ R4 //
        then: "admin can follow mikecalvo"
        $('#follow-message').text() == 'You are following Mike Calvo'
        $('#followingStatus')

        when: 'navigate to blizzard page'
        go '/#/userDetail?handle=blizzard'
        waitFor(5, 0.1) { $('#feed-page-status').text() == 'Page load complete' }

        then: 'admin can view blizzard page'
        $('#userDisplayName').value() == "Blizzard"
        $('#userEmail').value() == "blizzard@scsu.edu"
        $('#userDisplayName').readOnly
        $('#userEmail').readOnly
        !$('#save-account').enabled
        // REQ R4 //
        $('#follow').enabled
        $('#followingStatus')

        $('#td-feed-content').allElements().size() == 1
        $('#td-feed-content').allElements()[0].getText() == "Message 4_1"
        $('#td-feed-date').allElements().size() == 1

        when: 'navigate back to mikecalvo page'
        go '/#/userDetail?handle=mikecalvo'
        waitFor(5, 0.1) { $('#feed-page-status').text() == 'Page load complete' }

        then: 'admin can view mikecalvo user detail page'
        $('#userDisplayName').value() == "Mike Calvo"
        $('#userEmail').value() == "mike.calvo@scsu.edu"
        $('#userDisplayName').readOnly
        $('#userEmail').readOnly
        !$('#save-account').enabled
        $('#td-feed-content').allElements().size() == 0
        // REQ R4 //
        $('#follow-message').text() == 'You are following Mike Calvo'
        $('#followingStatus')
    }
}