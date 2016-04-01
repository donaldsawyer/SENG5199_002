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

        def uc = Account.count()
        def ur = Role.count()
        def urc = AccountRole.count()

        environments {
            development {
//                String pwd = "abcABC123!@#"
//                String edu = "@scsu.edu"
//
//                Account acct1 = new Account(handle: "luluwang", password: pwd, emailAddress: "lulu.wang"+edu, displayName: "Lulu Wang").save()
//                Account acct2 = new Account(handle: "donaldsawyer", password: pwd, emailAddress: "donald.sawyer"+edu, displayName: "Donald Sawyer").save()
//                Account acct3 = new Account(handle: "mikecalvo", password: pwd, emailAddress: "mike.calvo"+edu, displayName: "Mike Calvo").save()
//                Account acct4 = new Account(handle: "blizzard", password: pwd, emailAddress: "blizzard"+edu, displayName: "Blizzard").save()
//
//                acct1.addToMessages(messageText: "Message 1_1");
//                acct1.addToMessages(messageText: "Message 1_2").save()
//
//                acct4.addToMessages(messageText: "Message 4_1").save()
            }
        }

        JSON.registerObjectMarshaller(Account) { Account a ->
            return [id: a.id,
                    handle: a.handle,
                    emailAddress: a.emailAddress,
                    displayName: a.displayName,
                    password:a.password,
                    followerCount: a.followerCount,
                    followingCount: a.followingCount,
                    messageCount: a.messageCount
            ]
        }

        JSON.registerObjectMarshaller(Message) { Message a ->
            return [handle: a.sentFromAccount.handle,
                    messageText: a.messageText,
                    dateCreated: a.dateCreated,
                    lastUpdated: a.lastUpdated
            ]
        }
    }
    def destroy = {
    }
}
