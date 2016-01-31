package twtr

class Message {
    Account sentFromAccount
    String messageText

    static constraints = {
        messageText size: 1..40
    }
}
