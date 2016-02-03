package twtr


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class AccountIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "saving an account with a non-unique email will fail"() {
        setup:
        def account1 = new Account(handle: "@scsu-huskies1", emailAddress: "testemail@test.com",
                                   password: "abc123ABC", displayName: "SCSU Huskies")
        account1.save(flush: true)

        when:
        def sus = new Account(handle: "@scsu-huskies2", emailAddress: "testemail@test.com",
                              password: "abc123ABC", displayName: "SCSU Huskies")

        then:
        !sus.validate()
    }

    void "saving an account with a non-unique handle will fail"() {
        setup:
        def account1 = new Account(handle: "@scsu-huskies", emailAddress: "testemail1@test.com",
                                   password: "abc123ABC", displayName: "SCSU Huskies")
        account1.save(flush: true)

        when:
        def sus = new Account(handle: "@scsu-huskies", emailAddress: "testemail2@test.com",
                              password: "abc123ABC", displayName: "SCSU Huskies")

        then:
        !sus.validate()
    }

    void "an account allows multiple followers"() {
        setup:
        def sus = new Account(handle: "@scsu-huskies", emailAddress: "testemail1@test.com",
                              password: "abc123ABC", displayName: "SCSU Huskies")
        sus.save(flush: true)

        def account1 = new Account(handle: "@scsu-student1", emailAddress: "testemail2@test.com",
                                   password: "abc123ABC", displayName: "SCSU Student1")
        account1.save(flush: true)

        def account2 = new Account(handle: "@scsu-student2", emailAddress: "testemail2@test.com",
                                   password: "abc123ABC", displayName: "SCSU Student2")
        account2.save(flush: true)

        when:
        account1.addToFollowers(sus)
        account2.addToFollowers(sus)

        then:
        sus.validate()
    }

    void "two accounts may follow each other"() {
        setup:
        def sus1 = new Account(handle: "@scsu-student1", emailAddress: "testemail1@test.com",
                               password: "abc123ABC", displayName: "SCSU Student1")
        sus1.save(flush: true)

        def sus2 = new Account(handle: "@scsu-student2", emailAddress: "testemail2@test.com",
                               password: "abc123ABC", displayName: "SCSU Student2")
        sus2.save(flush: true)

        when:
        sus1.addToFollowers(sus2)
        sus2.addToFollowers(sus1)

        then:
        sus1.validate() && sus2.validate()
    }
}
