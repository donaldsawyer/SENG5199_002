import twtr.Account

class BootStrap {

    def init = { servletContext ->

        String pwd = "abcABC123!@#"
        String edu = "@scsu.edu"

        Account a1 = new Account(handle: "luluwang", password: pwd, emailAddress: "lulu.wang"+edu, displayName: "Lulu Wang").save()
        Account a2 = new Account(handle: "donaldsawyer", password: pwd, emailAddress: "donald.sawyer"+edu, displayName: "Donald Sawyer").save()
        Account s3 = new Account(handle: "mikecalvo", password: pwd, emailAddress: "mike.calvo"+edu, displayName: "Mike Calvo").save()
        Account s4 = new Account(handle: "blizzard", password: pwd, emailAddress: "blizzard"+edu, displayName: "Blizzard").save()
    }
    def destroy = {
    }
}
