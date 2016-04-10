package twtr.webappTests

import grails.test.mixin.integration.Integration
import spock.lang.Stepwise
import twtr.TwtrFunctionalTestBase

@Integration
@Stepwise
class SearchFunctionalSpec extends TwtrFunctionalTestBase {

    def setup() {
        go '/'
        $("#login-form input[id=loginHandle]").value("admin")
        $("#login-form input[id=loginPassword]").value("ABCDr00t!")
        $("#login-form button[id=do-login]").click()
        waitFor (5, 0.1) {$('form').find("div", id: "page-status").text() == "Page load complete"}
    }

    def cleanup() {
    }


    def 'found 1 tweet with valid handle and content'() {
        when:'search with handle=luluwang and content=10'
        go '/#/search'
        $('#search-form input[id=tweeter-handle').value('luluwang')
        $('#search-form input[id=tweet-content').value('10')
        $('#search-form button[id=do-search').click()
        waitFor (5, 0.1) {$('form').find("div", id: "page-status").text() == "Page load complete"}

        then:'found 1 tweet'
        $('form').find("h2", id:"h2-message").text() == "Search tweets"
        !$('form').find("div", id:"tweet-not-found").text()
        $('form').find("td", id:"td-tweet-handle").text() == "luluwang"
        $('form').find("td", id:"td-tweet-content").text() == "Lulu Written Message #10"
        $('form').find("td", id:"td-tweet-date").text() != ""
    }

    def 'found 1+ tweets with valid handle content'() {
        when:'search with handle=luluwang and content=Message'
        go '/#/search'
        $('#search-form input[id=tweeter-handle').value('luluwang')
        $('#search-form input[id=tweet-content').value('Message')
        $('#search-form button[id=do-search').click()
        waitFor (5, 0.1) {$('form').find("div", id: "page-status").text() == "Page load complete"}

        then:'Found X tweets'
        $('form').find("h2", id:"h2-message").text() == "Search tweets"
        !$('form').find("div", id:"tweet-not-found").text()
        $('form').find("td", id:"td-tweet-handle").allElements().size() == 15
        $('form').find("td", id:"td-tweet-handle").allElements()[0].getText() == "luluwang"
        $('form').find("td", id:"td-tweet-handle").allElements()[14].getText() == "luluwang"
        $('form').find("td", id:"td-tweet-content").allElements().size() == 15
        $('form').find("td", id:"td-tweet-content").allElements()[0].getText() == "Lulu Written Message #15"
        $('form').find("td", id:"td-tweet-content").allElements()[14].getText() == "Lulu Written Message #1"
        $('form').find("td", id:"td-tweet-date").allElements().size() == 15
        $('form').find("td", id:"td-tweet-date").allElements()[0].getText() >= $('form').find("td", id:"td-tweet-date").allElements()[14].getText()
    }

    def 'tweet not found with invalid tweet content'() {
        when:'search with handle=luluwang and content=Message'
        go '/#/search'
        $('#search-form input[id=tweeter-handle').value('luluwang')
        $('#search-form input[id=tweet-content').value('This tweet does not exist')
        $('#search-form button[id=do-search').click()
        waitFor (5, 0.1) {$('form').find("div", id: "page-status").text() == "Page load complete"}

        then:'no tweets found'
        $('form').find("h2", id:"h2-message").text() == "Search tweets"
        $('form').find("div", id:"tweet-not-found").text() == "No tweets found"
        !$('form').find("td", id:"td-tweet-handle").text()
        !$('form').find("td", id:"td-tweet-content").text()
        !$('form').find("td", id:"td-tweet-date").text()
    }

    def 'tweet not found with invalid tweeter handle'() {
        when:'search with handle=luluwang and content=Message'
        go '/#/search'
        $('#search-form input[id=tweeter-handle').value('invalidHandle')
        $('#search-form input[id=tweet-content').value('Message')
        $('#search-form button[id=do-search').click()
        waitFor (5, 0.1) {$('form').find("div", id: "page-status").text() == "Page load complete"}

        then:'no tweets found'
        $('form').find("h2", id:"h2-message").text() == "Search tweets"
        $('form').find("div", id:"tweet-not-found").text() == "No tweets found"
        !$('form').find("td", id:"td-tweet-handle").text()
        !$('form').find("td", id:"td-tweet-content").text()
        !$('form').find("td", id:"td-tweet-date").text()
    }
}
