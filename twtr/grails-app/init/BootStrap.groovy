import grails.converters.JSON
import security.Role
import security.User
import security.UserRole
import twtr.Account
import twtr.Message

class BootStrap {

    def init = { servletContext ->

        def admin = new User(username: 'admin', password: 'r00t!').save(flush: true, failOnError: true)
        def role = new Role(authority: 'ROLE_READ').save(flush: true, failOnError: true)
        new UserRole(user: admin, role: role).save(flush: true, failOnError: true)

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
