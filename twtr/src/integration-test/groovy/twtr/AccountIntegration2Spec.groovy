package twtr


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class AccountIntegration2Spec extends Specification {

    final static String goodHandle = "@scsu-huskies"
    final static String goodEmail  = "testemail@test.com"
    final static String goodPassword = "abc123ABC"
    final static String goodDisplayName = "SCSU Huskies"

    final static String handle1 = "@scsu-huskies1"
    final static String email1  = "testemail1@test.com"
    final static String password1 = "abc123ABC"
    final static String displayName1 = "SCSU Huskies1"

    final static String handle2 = "@scsu-huskies2"
    final static String email2  = "testemail2@test.com"
    final static String password2 = "abc123ABC"
    final static String displayName2 = "SCSU Huskies2"

    def setup() {
    }

    def cleanup() {
    }

    void "saving an account with a non-unique email will fail."() {
        setup: "Add the first account with email ${goodEmail}"
        def account1 = new Account(handle: goodHandle, emailAddress: goodEmail, password: goodPassword, displayName: goodDisplayName)
        account1.save(flush: true)

        when: "Another account with ${goodEmail} is attempted to be saved."
        def sus = new Account(handle: handle1, emailAddress: goodEmail, password: password1, displayName: displayName1)
        sus.save(flush: true)

        then: "The 2nd account should be invalid and only the 1st account should exist."
        !sus.validate()
        Account.get(account1.id).handle == goodHandle
        !Account.findByHandle(sus.handle)
        Account.findAllByEmailAddress(goodEmail).size == 1
        Account.findByEmailAddress(goodEmail).handle == goodHandle

        cleanup:
        Account.list().each {it.delete(flush: true, failOnError: true) }
    }

    void "two accounts may follow each other"() {
        setup:
        def sus1 = new Account(handle: handle1, emailAddress: email1, password: password1, displayName: displayName1)
        sus1.save(flush: true)

        def sus2 = new Account(handle: handle2, emailAddress: email2, password: password2, displayName: displayName2)
        sus2.save(flush: true)

        when: 'sus2 starts following sus1'
        sus1.addToFollowing(sus2)
        sus2.addToFollowers(sus1)
        sus1.save()
        sus2.save(flush: true)

        then:
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

        then:
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

        cleanup:
        Account.get(sus1.id).delete(failOnError: true)
        Account.get(sus2.id).delete(flush: true, failOnError: true)
    }
}
