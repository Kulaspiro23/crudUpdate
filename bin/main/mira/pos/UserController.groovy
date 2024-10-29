package mira.pos


class UserController {

    def index() {

    }

    def home() {
        render(view: "/users/users") 
    }
}