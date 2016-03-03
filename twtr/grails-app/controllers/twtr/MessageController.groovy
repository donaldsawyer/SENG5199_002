package twtr

import grails.rest.RestfulController

class MessageController extends RestfulController<Message> {

    static responseFormats = ['json', 'xml']

    def MessageController () {
        super(Message)
    }

    @Override
    def save() {
        //TBD - get the account ID/Handle and the message text

    }

//    @Override
//    protected Message queryForResource(Serializable id) {
//
//        //return super.queryForResource(id)
//    }
}
