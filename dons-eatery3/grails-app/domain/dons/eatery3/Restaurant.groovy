package dons.eatery3

class Restaurant {

    String name
    Integer capacity

    static constraints = {
        name size: 1..20
        capacity: 0..200
    }
}
