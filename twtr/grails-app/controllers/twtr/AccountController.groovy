package twtr

import grails.rest.RestfulController
import grails.transaction.Transactional

class AccountController extends RestfulController<Account> {
    static responseFormats = ['json', 'xml']

    def AccountController (){
        super (Account)
    }

    def handle() {
        respond Account.findByHandle(params.id)
    }


    def startFollowing() {
        def accountId = params.accountId

        Account followAccount;
        if(params.followAccount == null)
            respond getParams()
        else
            followAccount = Account.get(params.followAccount)

        Account follower = Account.get(params.accountId)
        followAccount.addToFollowers(follower).save()
        follower.addToFollowing(followAccount).save()
        respond follower
    }

    // TODO: USE A GORM QUERY TO FETCH BY ID //
    def getFollowers() {
        int maximum = params.max == null ? 1 : Integer.parseInt(params.max)
        def accountId = params.accountId

        Account account = Account.get(accountId)

        respond account.getFollowers()
    }
}
