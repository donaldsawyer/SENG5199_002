package twtr

import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class AccountIntegrationSpec extends Specification {

    final static String handle = "@scsu-huskies"
    final static String email  = "testemail@test.com"
    final static String password = "abc123ABC"
    final static String displayName = "SCSU Huskies"

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

    void "saving an account with a non-unique email will fail"() {
        setup:
        def account1 = new Account(handle: handle, emailAddress: email, password: password, displayName: displayName)
        account1.save(flush: true)

        when:
        def sus = new Account(handle: handle1, emailAddress: email, password: password1, displayName: displayName1)

        then:
        !sus.validate()
    }

    void "saving an account with a non-unique handle will fail"() {
        setup:
        def account1 = new Account(handle: handle, emailAddress: email, password: password, displayName: displayName)
        account1.save(flush: true)

        when:
        def sus = new Account(handle: handle, emailAddress: email1, password: password1, displayName: displayName1)

        then:
        !sus.validate()
    }

    void "an account allows multiple followers"() {
        setup:
        def sus = new Account(handle: handle, emailAddress: email, password: password, displayName: displayName)
        sus.save(flush: true)

        def account1 = new Account(handle: handle1, emailAddress: email1, password: password1, displayName: displayName1)
        account1.save(flush: true)

        def account2 = new Account(handle: handle2, emailAddress: email2, password: password2, displayName: displayName2)
        account2.save(flush: true)

        when:
        sus.addToFollowers(account1)
        sus.addToFollowers(account2)

        then:
        sus.validate()
        sus.followers.count {it} == 2
        sus.followers.findAll{it -> (it.handle == account1.handle || it.handle == account2.handle)}
    }

    void "two accounts may follow each other"() {
        setup:
        def sus1 = new Account(handle: handle1, emailAddress: email1, password: password1, displayName: displayName1)
        sus1.save(flush: true)

        def sus2 = new Account(handle: handle2, emailAddress: email2, password: password2, displayName: displayName2)
        sus2.save(flush: true)

        when:
        sus1.addToFollowers(sus2)
        sus2.addToFollowers(sus1)
        sus1.addToFollowing(sus2)
        sus2.addToFollowing(sus1)

        then:
        sus1.validate() && sus2.validate()
        sus1.followers.find{it.handle == sus2.handle} != null
        !sus1.followers.find{it.handle == sus1.handle}
        sus2.followers.find{it.handle == sus1.handle} != null
        !sus2.followers.find{it.handle == sus2.handle}
        sus1.following.find{it.handle == sus2.handle} != null
        !sus1.following.find{it.handle == sus1.handle}
        sus2.following.find{it.handle == sus1.handle} != null
        !sus2.following.find{it.handle == sus2.handle}
    }
}
