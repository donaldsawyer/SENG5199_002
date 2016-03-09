package twtr

import grails.converters.JSON
import grails.rest.RestfulController
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.NO_CONTENT

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
        if(params.followAccount == null) {
            respond getParams()
            return
        }
        else {
            followAccount = Account.get(params.followAccount)
        }

        Account follower = Account.get(params.accountId)
        followAccount.addToFollowers(follower).save()
        follower.addToFollowing(followAccount).save(flush: true)

        respond follower, [status: 201]
    }

    def followers() {
        int maximum = params.max == null ? 10 : Integer.parseInt(params.max)
        int offset = params.offset == null ? 0 : Integer.parseInt(params.offset)
        long accountId = Long.parseLong(params.accountId)

        respond Account.findAll("from Account as a where a.id in (:accounts) order by a.id",
                [accounts: Account.get(accountId).followers*.id], [max: maximum, offset: offset])
    }

    def following() {
        int maximum = params.max == null ? 10 : Integer.parseInt(params.max)
        int offset = params.offset == null ? 0 : Integer.parseInt(params.offset)
        long accountId = Long.parseLong(params.accountId)

        respond Account.findAll("from Account as a where a.id in (:accounts) order by a.id",
            [accounts: Account.get(accountId).following*.id], [max: maximum, offset: offset])
    }

    def feed() {
        int maximum = params.max == null ? 10 : Integer.parseInt(params.max)
        int offset = params.offset == null ? 0 : Integer.parseInt(params.offset)
        def fromDate = params.fromDate
        long accountId = Long.parseLong(params.accountId)

        def accountIds = Account.get(accountId).following*.id

        respond Message.createCriteria().list(max: maximum, offset: offset) {
            'in'('sentFromAccount', Account.get(accountId).following)
            if(fromDate != null) {
                gte('dateCreated', new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(fromDate))
            }
            order('dateCreated', 'desc')
        }
    }

    @Override
    @Transactional
    def delete() {
        if(handleReadOnly()) {
            return
        }

        Account instance = queryForResource(params.id)
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        // all of the references to the account being deleted must be deleted first //
        // without this, referential integrity violations will occur //
        instance.followers.each { it -> it.removeFromFollowing(instance)}
        instance.following.each { it -> it.removeFromFollowers(instance)}
        instance.followers.clear()
        instance.following.clear()
        instance.messages.clear()
        instance.save(flush: true)
        instance.delete flush:true

        render status: NO_CONTENT
    }
}
