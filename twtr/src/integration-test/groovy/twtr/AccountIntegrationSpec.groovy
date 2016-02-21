package twtr

import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class AccountIntegrationSpec extends Specification {

    final static String handle = 'scsu_huskies'
    final static String email  = 'testemail@test.com'
    final static String password = 'abc123ABC'
    final static String displayName = 'SCSU Huskies'

    final static String handle1 = 'scsu_huskies1'
    final static String email1  = 'testemail1@test.com'
    final static String password1 = 'abc123ABC'
    final static String displayName1 = 'SCSU Huskies1'

    final static String handle2 = 'scsu_huskies2'
    final static String email2  = 'testemail2@test.com'
    final static String password2 = 'abc123ABC'
    final static String displayName2 = 'SCSU Huskies2'

    def startingAccounts

    def setup() {
        startingAccounts = Account.count()
    }

    void 'saving an account with a non-unique email will fail.'() {
        setup: 'Add the first account with email ${email}'
        def account1 = new Account(handle: handle, emailAddress: email, password: password, displayName: displayName)
        account1.save(flush: true)

        when: 'Another account with ${email} is attempted to be saved.'
        def sus = new Account(handle: handle1, emailAddress: email, password: password1, displayName: displayName1)
        sus.save(flush: true)

        then: 'The 2nd account should be invalid and only the 1st account should exist.'
        !sus.validate()
        Account.get(account1.id).handle == handle
        !Account.findByHandle(sus.handle)
        Account.findAllByEmailAddress(email).size == 1
        Account.findByEmailAddress(email).handle == handle
        Account.count() == startingAccounts + 1

        cleanup: 'Deletes the account used for this test'
        Account.list().each {it.delete(flush: true, failOnError: true) }
    }

    void 'saving an account with a non-unique handle will fail'() {
        setup: 'Add the first account with handle ${handle}'
        def account1 = new Account(handle: handle, emailAddress: email, password: password, displayName: displayName)
        account1.save(flush: true)
        def startingNumberOfAccount = Account.count()

        when: 'Another account with ${handle} is attempted to be saved'
        def sus = new Account(handle: handle, emailAddress: email1, password: password1, displayName: displayName1)
        sus.save(flush: true)

        then: 'The 2nd account should be invalid and only the 1st account should exist'
        !sus.validate()
        sus.hasErrors()
        !sus.id
        sus.errors.getFieldError('handle').rejectedValue == sus.handle
        Account.findAllByHandle(handle).size == 1
        Account.get(account1.id).handle == handle
        Account.count() == startingNumberOfAccount

        cleanup: 'Deletes the account used for this test'
        Account.get(account1.id).delete(flush: true, failOnError: true)
    }

    void 'an account allows multiple followers'() {
        setup: 'Three accounts are created with valid properties'
        def sus = new Account(handle: handle, emailAddress: email, password: password, displayName: displayName)
        sus.save(flush: true)

        def account1 = new Account(handle: handle1, emailAddress: email1, password: password1, displayName: displayName1)
        account1.save(flush: true)

        def account2 = new Account(handle: handle2, emailAddress: email2, password: password2, displayName: displayName2)
        account2.save(flush: true)

        when: 'Both account1 and account2 are added as followers of sus account'
        sus.addToFollowers(account1)
        sus.addToFollowers(account2)
        sus.save(flush: true)

        then: 'sus account should have two valid followers'
        sus.validate()
        !sus.hasErrors()
        sus.followers.count {it} == 2
        sus.followers.findAll {it -> it.id == account1.id}.size() == 1
        sus.followers.findAll {it -> it.id == account2.id}.size() == 1
        sus.followers.findAll {it -> it.handle == account1.handle}.size() == 1
        sus.followers.findAll {it -> it.handle == account2.handle}.size() == 1
        Account.get(sus.id).getFollowers().size() == 2
        Account.get(sus.id).getFollowers().findAll {it -> it.id == account1.id}.size() == 1
        Account.get(sus.id).getFollowers().findAll {it -> it.id == account2.id}.size() == 1

        cleanup: 'Delete the accounts used for this test'
        sus.delete(flush: true, failOnError: true)
        account1.delete(flush: true, failOnError: true)
        account2.delete(flush: true, failOnError: true)
    }

    void 'two accounts may follow each other'() {
        setup: 'Two accounts are created with valid properties'
        def sus1 = new Account(handle: handle1, emailAddress: email1, password: password1, displayName: displayName1)
        sus1.save(flush: true)

        def sus2 = new Account(handle: handle2, emailAddress: email2, password: password2, displayName: displayName2)
        sus2.save(flush: true)

        when: 'sus2 starts following sus1'
        sus1.addToFollowing(sus2)
        sus2.addToFollowers(sus1)
        sus1.save()
        sus2.save(flush: true)

        then: 'sus2 should be a follower of sus1'
        Account.get(sus1.id)
        Account.get(sus2.id)
        Account.get(sus1.id).getFollowing().size() == 1
        Account.get(sus2.id).getFollowers().size() == 1
        (Account.get(sus2.id).getFollowers().findAll {it.id == sus1.id}).size() == 1
        (Account.get(sus1.id).getFollowing().findAll {it.id == sus2.id}).size() == 1

        // negative assertions //
        !(Account.get(sus1.id).getFollowers())
        !(Account.get(sus2.id).getFollowing())
        !(Account.get(sus2.id).getFollowing().find {it.id == sus1.id})
        !(Account.get(sus1.id).getFollowers().find {it.id == sus2.id})

        when: 'sus1 starts following sus2'
        sus2.addToFollowing(sus1)
        sus1.addToFollowers(sus2)
        sus1.save()
        sus2.save(flush: true)

        then: 'sus1 should be a follower of sus2'
        // re-assert original state //
        Account.get(sus1.id)
        Account.get(sus2.id)
        Account.get(sus1.id).getFollowing().size() == 1
        Account.get(sus2.id).getFollowers().size() == 1
        (Account.get(sus2.id).getFollowers().findAll {it.id == sus1.id}).size() == 1
        (Account.get(sus1.id).getFollowing().findAll {it.id == sus2.id}).size() == 1

        // new assertions //
        Account.get(sus1.id).getFollowers().size() == 1
        Account.get(sus2.id).getFollowing().size() == 1
        (Account.get(sus2.id).getFollowing().findAll {it.id == sus1.id}).size() == 1
        (Account.get(sus1.id).getFollowers().findAll {it.id == sus2.id}).size() == 1

        cleanup: 'Deletes the accounts used for this test'
        Account.get(sus1.id).delete(failOnError: true)
        Account.get(sus2.id).delete(flush: true, failOnError: true)
    }
}
