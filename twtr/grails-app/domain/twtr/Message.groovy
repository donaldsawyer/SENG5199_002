package twtr

import grails.rest.Resource

//@Resource(uri='/messages', formats=['json', 'xml'])
class Message {
    static belongsTo = [sentFromAccount: Account]
    String messageText

    static constraints = {
        messageText size: 1..40
    }
}
