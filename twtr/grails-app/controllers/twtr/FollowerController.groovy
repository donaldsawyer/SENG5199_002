package twtr

import grails.rest.RestfulController

class FollowerController extends RestfulController<Account> {

    def FollowerController() {
        super(Account)
    }
}
