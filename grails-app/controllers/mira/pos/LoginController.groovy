package mira.pos

class LoginController {

    def loginService 

    def index() {
        render(view: '/login')
    }

    def authenticateUser() {
        def res = [:]


        String username = params.username
        String password = params.password

        def userDetails = loginService.authenticate(username, password)

        if (userDetails) {
            session.username = username
            session.fullName = userDetails.fullName
            session.dob = userDetails.dob
            session.user_type = userDetails.usertype_name 
            session.user_id = userDetails.user_id 

            res.success = true
            res.redirect = "${grailsApplication.config.grails.serverURL}/home/index"
        } else {
            res.success = false
            res.message = "Invalid credentials. Please try again."
        }

        withFormat {
            json {
                render(contentType: "text/json") { response result: res }
            }
        }
    }

     def logout() {
        session.invalidate() 
        redirect(action: 'index')
    }
}
