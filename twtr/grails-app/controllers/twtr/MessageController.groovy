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
        def messageText = request.JSON.messageText


        if(params.accountId != null)
            tweeter = Account.findById(params.accountId)
        else if(params.handle != null)
            tweeter = Account.findByHandle(params.handle)
        else
        {
            response.sendError(422)
            return
        }

        //respond tweeter

        if(!tweeter) {
            response.sendError(404)
            return
        }

        //This validation does not work - gives 500 server error
//        if(messageText.size() > 40) {
//            response.sendError(422)
//            return
//        }

        Message newMessage = new Message(sentFromAccount: tweeter, messageText: messageText).save()
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
