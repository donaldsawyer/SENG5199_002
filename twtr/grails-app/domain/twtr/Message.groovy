package twtr

class Message {
    static belongsTo = [sentFromAccount: Account]
    String messageText
    Date lastUpdated
    Date dateCreated

    static constraints = {
        messageText size: 1..40
    }
}
