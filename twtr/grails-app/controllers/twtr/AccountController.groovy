package twtr

import grails.rest.RestfulController
import grails.transaction.Transactional

class AccountController extends RestfulController<Account> {
//    static allowedMethods = [save: "POST", update: "PUT", patch: "PATCH", delete: "DELETE", followOne: "GET"]
    static responseFormats = ['json', 'xml']

    def AccountController (){
        super (Account)
    }

    def handle() {
        respond Account.findByHandle(params.id)
    }


    def startFollowing() {
        // account id is in params.id //
        Account followAccount;
        if(params.followAccount == null)
            respond getParams()
        else
            followAccount = Account.get(params.followAccount)

        Account follower = Account.get(params.id)
        followAccount.addToFollowers(follower).save()
        follower.addToFollowing(followAccount).save()
        respond follower
    }
}
