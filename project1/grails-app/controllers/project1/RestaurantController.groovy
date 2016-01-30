package project1

class RestaurantController {

    static scaffold = Restaurant

    //If the method index is included, the Restaurant List doesn't show the restaurants created
    //def index() {}

    def update(Integer id) {
        def RestaurantInstance = Restaurant.get(id)
        if (!RestaurantInstance) {
            //nothing
        }

        //redirect(action: "index", id: RestaurantInstance.name)
    }

    def show(Integer id) {
        def RestaurantInstance = Restaurant.get(id)
        //redirect(action: "list", id: RestaurantInstance.name)
    }

    def list() {

        //[RestaurantInstanceList: Restaurant.list(prams), RestaurantInstanceTotal: Restaurant.count()]
    }
}
