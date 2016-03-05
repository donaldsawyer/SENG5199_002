package twtr

import grails.rest.RestfulController
import grails.transaction.Transactional

class MessageController extends RestfulController<Message> {

    static allowedMethods = [save: "POST", update: "PUT", patch: "PATCH", delete: "DELETE", tweet: "POST"]
    static responseFormats = ['json', 'xml']

    def MessageController() {
        super(Message)
    }

    @Transactional
    def tweet()
    {
        Account tweeter;
        if(params.accountId != null)
            tweeter = Account.get(params.accountId)
        else if(params.handle != null)
            tweeter = Account.findByHandle(params.handle)

        Message newMessage = new Message(sentFromAccount: tweeter, messageText: request.JSON.messageText).save()
        respond newMessage, [status: 201]
    }

    @Override
    def index(Integer max) {
        def accountId = params.accountId

//        if(!!accountId) super.queryForResource(max)
//        else {
//
//        }
        respond Account.get(accountId).getMessages()
    }

    @Override
    protected Message queryForResource(Serializable id) {

        def accountId = params.accountId

//        return new Message(messageText: "$id+$accountId")
        return Message.where {
            id == id && sentFromAccount.id == accountId
        }.find()
    }
}
