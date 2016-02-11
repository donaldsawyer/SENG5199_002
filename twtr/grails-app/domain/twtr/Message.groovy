package twtr

class Message {
    static belongsTo = [sentFromAccount: Account]
    String messageText

    static constraints = {
        messageText size: 1..40
    }
}
