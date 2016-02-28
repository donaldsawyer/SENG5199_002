package twtr

import grails.rest.RestfulController

class AccountController extends RestfulController<Account> {
    static responseFormats = ['json', 'xml']

    def AccountController (){
        super (Account)
    }

    def handle() {
        respond Account.findByHandle(params.id)
    }

}
