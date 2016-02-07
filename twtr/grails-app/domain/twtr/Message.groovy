package twtr

class Message {
    static belongsTo = Account
    String messageText

    static constraints = {
        messageText size: 1..40
    }
}
