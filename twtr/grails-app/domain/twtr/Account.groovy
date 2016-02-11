package twtr

class Account {
    String handle
    String emailAddress
    String password // 8-16 chars, at least 1 number, lower, upper
    String displayName

    static hasMany = [followers : Account, following : Account, messages: Message]

    static constraints = {
        handle blank: false, unique: true
        emailAddress nullable: false, email: true, unique: true
        password nullable: false, matches: "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}\$"
        displayName nullable: false
    }
}
