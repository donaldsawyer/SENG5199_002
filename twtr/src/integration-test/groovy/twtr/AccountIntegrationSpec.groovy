package twtr

/*
  To run the integration test, use the command in Run Target: grails test-app -integration
 */

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
        account1.addToFollowing(sus)
        account2.addToFollowing(sus)

        then:
        sus.validate()
        //TBD - add other validation conditions

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

        then:
        sus1.validate() && sus2.validate()
        //TBD - add other validation conditions
        //sus1.findAll{it.getFollowers()} == sus2.findAll{it.getfollowing()}
        //sus2.findAll{it.getFollowers()} == sus1.findAll{it.getfollowing()}
    }
}
