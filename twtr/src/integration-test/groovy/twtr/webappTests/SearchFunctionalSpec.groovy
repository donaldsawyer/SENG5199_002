package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
class SearchFunctionalSpec extends TwtrFunctionalTestBase {

    def setup() {
        go '/'
        $('#loginHandle').value("admin")
        $('#loginPassword').value("ABCDr00t!")
        $('#do-login').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }
    }

    def 'found 1 tweet with valid handle and content'() {
        when: 'search with handle=luluwang and content=10'
        go '/#/search'
        $('#tweeter-handle').value('luluwang')
        $('#tweet-content').value('10')
        $('#do-search').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'found 1 tweet'
        $('#h2-message').text() == "Search tweets"
        !$('#tweet-not-found').text()
        $('#td-tweet-handle').text() == "luluwang"
        $('#td-tweet-content').text() == "Lulu Written Message #10"
        $('#td-tweet-date').text() != ""
        // Req S2: the results should be BELOW the search info and scrollable not scrollable here since it's one record//
        $('#tweet-results').y > $('#do-search').y
        $('#tweet-results-table').height <= $('#tweet-results').height
    }

    def 'found 1+ tweets with valid handle content'() {
        when: 'search with handle=luluwang and content=Message'
        go '/#/search'
        $('#tweeter-handle').value('luluwang')
        $('#tweet-content').value('Message')
        $('#do-search').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'Found 15 tweets'
        $('#h2-message').text() == "Search tweets"
        !$('#tweet-not-found').text()
        $('#td-tweet-handle').allElements().size() == 15
        $('#td-tweet-handle').allElements()[0].getText() == "luluwang"
        $('#td-tweet-handle').allElements()[14].getText() == "luluwang"
        $('#td-tweet-content').allElements().size() == 15
        $('#td-tweet-content').allElements()[0].getText() == "Lulu Written Message #15"
        $('#td-tweet-content').allElements()[14].getText() == "Lulu Written Message #1"
        $('#td-tweet-date').allElements().size() == 15
        $('#td-tweet-date').allElements()[0].getText() >= $('#td-tweet-date').allElements()[14].getText()
        // Req S2: the results should be BELOW the search info and scrollable //
        $('#tweet-results-table').height > $('#tweet-results').height
        $('#tweet-results').y > $('#do-search').y
    }

    def 'tweet not found with invalid tweet content'() {
        when: 'search with handle=luluwang and content=Message'
        go '/#/search'
        $('#tweeter-handle').value('luluwang')
        $('#tweet-content').value('This tweet does not exist')
        $('#do-search').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'no tweets found'
        $('#h2-message').text() == "Search tweets"
        $('#tweet-not-found').text() == "No tweets found"
        !$('#td-tweet-handle').text()
        !$('#td-tweet-content').text()
        !$('#td-tweet-date').text()
    }

    def 'tweet not found with invalid tweeter handle'() {
        when: 'search with handle=luluwang and content=Message'
        go '/#/search'
        $('#tweeter-handle').value('invalidHandle')
        $('#tweet-content').value('Message')
        $('#do-search').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'no tweets found'
        $('#h2-message').text() == "Search tweets"
        $('#tweet-not-found').text() == "No tweets found"
        !$('#td-tweet-handle').text()
        !$('#td-tweet-content').text()
        !$('#td-tweet-date').text()
    }

    def 'clicking on the user handle will navigate to the user details page'() {
        when: 'search with handle=luluwang and content=10'
        go '/#/search'
        $('#tweeter-handle').value('luluwang')
        $('#tweet-content').value('10')
        $('#do-search').click()
        waitFor(5, 0.1) { $('#page-status').text() == "Page load complete" }

        then: 'found 1 tweet'
        $('#h2-message').text() == "Search tweets"
        !$('#tweet-not-found').text()
        $('#td-tweet-handle').text() == "luluwang"
        $('#td-tweet-content').text() == "Lulu Written Message #10"
        $('#td-tweet-date').text() != ""

        when: 'clicking on the luluwang handle'
        def tweeterHandle = $("#td-tweet-handle").text()
        $('#tweet-handle-link').click()
        waitFor(5, 0.1) { $('#feed-page-status').text() == "Page load complete" }

        then: 'user is navigated to the user details page'
        $('#twtr-page-title').text() == "Twtr Page for @" + tweeterHandle
    }
}
