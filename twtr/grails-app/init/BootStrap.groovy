import grails.converters.JSON
import twtr.Role
import twtr.Message
import twtr.Account
import twtr.AccountRole

class BootStrap {

    def init = { servletContext ->

        def admin = new Account(
                handle: 'admin',
                password: 'ABCDr00t!',
                displayName: 'display name',
                emailAddress: 'abac@abc.com').save(flush: true, failOnError: true)
        def role = new Role(authority: 'ROLE_READ').save(flush: true, failOnError: true)
        new AccountRole(account: admin, role: role).save(flush: true, failOnError: true)

        String pwd = "abcABC123!@#"
        String edu = "@scsu.edu"

        Account acct1 = new Account(handle: "luluwang", password: pwd, emailAddress: "lulu.wang" + edu, displayName: "Lulu Wang").save()
        Account acct2 = new Account(handle: "donaldsawyer", password: pwd, emailAddress: "donald.sawyer" + edu, displayName: "Donald Sawyer").save()
        Account acct3 = new Account(handle: "mikecalvo", password: pwd, emailAddress: "mike.calvo" + edu, displayName: "Mike Calvo").save()
        Account acct4 = new Account(handle: "blizzard", password: pwd, emailAddress: "blizzard" + edu, displayName: "Blizzard").save()

        new AccountRole(account: acct1, role: role).save(flush: true, failOnError: true)
        new AccountRole(account: acct2, role: role).save(flush: true, failOnError: true)
        new AccountRole(account: acct3, role: role).save(flush: true, failOnError: true)
        new AccountRole(account: acct4, role: role).save(flush: true, failOnError: true)

        // add some messages for lulu //
        (1..15).each { id -> acct1.addToMessages(messageText: "Lulu Written Message #$id").save(flush: true) }

        // add a message for blizzard //
        acct4.addToMessages(messageText: "Message 4_1").save(flush: true)

        // add some messages for the admin //
        (1..20).each { id ->
            admin.addToMessages(messageText: "Admin Written Message #$id").save(flush: true)
        }

        acct1.addToFollowers(admin).save(flush: true);
        admin.addToFollowing(acct1).save(flush: true);

        environments {
            development {

            }
        }

        JSON.registerObjectMarshaller(Account) { Account a ->
            return [id            : a.id,
                    handle        : a.handle,
                    emailAddress  : a.emailAddress,
                    displayName   : a.displayName,
                    password      : a.password,
                    followerCount : a.followerCount,
                    followingCount: a.followingCount,
                    messageCount  : a.messageCount
            ]
        }

        JSON.registerObjectMarshaller(Message) { Message a ->
            return [handle     : a.sentFromAccount.handle,
                    messageText: a.messageText,
                    dateCreated: a.dateCreated,
                    lastUpdated: a.lastUpdated
            ]
        }
    }
    def destroy = {
    }
}
