package twtr

import grails.rest.Resource

//@Resource(uri='/accounts', formats=['json', 'xml'])
class Account {
    String handle
    String emailAddress
    String password // 8-16 chars, at least 1 number, lower, upper
    String displayName

    static hasMany = [followers : Account, following : Account, messages: Message]

    static constraints = {
        handle blank: false, unique: true, matches:"^[a-zA-Z0-9_]{1,15}\$"
        emailAddress nullable: false, email: true, unique: true
        password nullable: false, matches: "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}\$"
        displayName nullable: false
    }

    long getFollowerCount() { !followers ? 0 : followers.size() }
    long getFollowingCount() { !following ? 0 : following.size() }
    long getMessageCount() {!messages ? 0 : messages.size() }
    static transients = ['followerCount', 'followingCount', 'messageCount']
}
