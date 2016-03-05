package twtr

import grails.rest.RestfulController

class FollowerController extends RestfulController<Account> {

    static allowedMethods = [save: "POST", update: "PUT", patch: "PATCH", delete: "DELETE", startFollowing: "POST"]
    static responseFormats = ['json', 'xml']

    def FollowerController() {
        super(Account)
    }

    def startFollowing() {
        def accountId = params.accountId
        respond Account.get(accountId)
    }
}
