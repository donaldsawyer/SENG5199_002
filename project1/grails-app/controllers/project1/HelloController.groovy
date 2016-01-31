package project1

class HelloController {

    def index() {
        render 'hello world'
    }

    def goodbye() {
        render 'Goodbye'
    }

    def greeting() {
        [words: 'Hi you']
    }
}
