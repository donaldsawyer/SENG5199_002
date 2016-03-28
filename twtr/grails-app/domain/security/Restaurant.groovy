package security

import grails.rest.Resource

@Resource(uri='/api/restaurants', formats = ['json'])
class Restaurant {

    String name
    String city
    String state

    static constraints = {
        state inList: ['FL', 'IL', 'MN', 'WI']
    }
}