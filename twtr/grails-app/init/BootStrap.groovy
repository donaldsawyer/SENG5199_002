import twtr.Account

class BootStrap {

    def init = { servletContext ->

        String pwd = "abcABC123!@#"
        String edu = "@scsu.edu"

        new Account(handle: "luluwang", password: pwd, emailAddress: "lulu.wang"+edu, displayName: "Lulu Wang").save()
        new Account(handle: "donaldsawyer", password: pwd, emailAddress: "donald.sawyer"+edu, displayName: "Donald Sawyer").save()
        new Account(handle: "mikecalvo", password: pwd, emailAddress: "mike.calvo"+edu, displayName: "Mike Calvo").save()
        new Account(handle: "blizzard", password: pwd, emailAddress: "blizzard"+edu, displayName: "Blizzard").save()
    }
    def destroy = {
    }
}
