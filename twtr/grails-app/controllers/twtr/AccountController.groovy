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
    def followers() {
        int maximum = params.max == null ? 1 : Integer.parseInt(params.max)
        int offset = params.offset == null ? 0 : Integer.parseInt(params.offset)
        long accountId = Long.parseLong(params.accountId)


//        respond getParams()
       respond Account.createCriteria().list {
           following {
               idEq(accountId)
           }
       }
    }
}
