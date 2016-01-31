package dons.eatery3

class Restaurant {

    String name
    Integer capacity
    def reservations
    Address mailingAddress

    static hasMany = [reservations : Reservation]

    static constraints = {
        name size: 1..20
        capacity: 0..200
        reservations()
        mailingAddress()
    }
}
