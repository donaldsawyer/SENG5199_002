package twtr

import geb.spock.GebSpec
import groovyx.net.http.RESTClient


class TwtrFunctionalTestBase extends GebSpec {

    final static String goodHandle = 'scsu_huskies'
    final static String goodEmailAccount = 'testemail'
    final static String goodEmailDomain = '@scsu.edu'
    final static String goodEmail = goodEmailAccount + goodEmailDomain
    final static String goodPassword = 'abc123ABC'
    final static String goodDisplayName = 'SCSU Huskies'

    RESTClient restClient

    def addAccount(String postfix) {
        def response = restClient.post(path: '/accounts', contentType: 'application/json',
                body: [handle      : goodHandle + postfix,
                       emailAddress: goodEmailAccount + postfix + goodEmailDomain,
                       password    : goodPassword,
                       displayName : goodDisplayName + postfix])
        return response.data.id
    }

    def addAccountToMap(String postfix) {
        def newId = addAccount(postfix)
        return ["$postfix": newId]
    }

    def deleteAccounts(def accountsToDelete) {
        accountsToDelete.each { it -> deleteAccount(it.value)}
    }

    def deleteAccount(int accountToDelete) {
        restClient.delete(path:"/accounts/$accountToDelete")
    }
}
