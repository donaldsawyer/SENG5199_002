package dons.eatery3

class HelloController {

    //auto scaffolding doesn't work when the controller isn't named same as scaffold
    //def scaffold = Restaurant
    // adding a comment

    def index() {
        render "Hello WORld!!!!!"
    }

    def goodbye()
    {
        render "Goodbye"
    }

    def goodbye2()
    {
        render "GOODBYE 2--"
    }
}
