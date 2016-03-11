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
    def tweet() {
        Account tweeter;
        def messageText = request.JSON.messageText


        if (params.accountId) {
            if (params.accountId.isLong())
                tweeter = Account.findById(params.accountId)
        } else if (params.handle)
            tweeter = Account.findByHandle(params.handle)
        else {
            response.sendError(404)
            return
        }

        if (!tweeter) {
            response.sendError(404)
            return
        }

        if (!messageText || messageText.size() > 40) {
            response.sendError(406)
            return
        }

        Message newMessage = new Message(sentFromAccount: tweeter, messageText: messageText).save()
        respond newMessage, [status: 201]
    }

    @Override
    def index(Integer max) {
        int maximum
        int offset

        if(params.max){
            if(params.max.isLong())
                maximum = Integer.parseInt(params.max)
            else {
                response.sendError(400)
                return
            }
        }
        else
            maximum = 10

        if(params.offset){
            if(params.offset.isLong())
                offset = Integer.parseInt(params.offset)
            else {
                response.sendError(400)
                return
            }
        }
        else
            offset = 0

        def accountId = params.accountId

        respond Message.createCriteria().list(max: maximum, offset: offset) {
            eq('sentFromAccount', Account.get(accountId))
            order('dateCreated', 'desc')
        }
    }

    @Override
    protected Message queryForResource(Serializable id) {

        def accountId = params.accountId

        return Message.where {
            id == id && sentFromAccount.id == accountId
        }.find()
    }

    def search() {
        def searchText = params.text

        def results = Message.where { messageText ==~ "%$searchText%" }.list() {
            order('dateCreated', 'asc')
        }

        respond results
    }
}
