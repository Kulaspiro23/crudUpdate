package mira.pos


class LoginController {

    def index(){

        render(view:'/login')
    }

    def authenticateUser(){

        //sample

        def res = [:]

        // return true and redirect if user account details is correct, else throw invalid credentials error
        // implement sql to get the user details
        // save user details (fullName and dob) in session

        session.username = params.username; 
        session.fullName = "Yukit, Yarker" //replace this from user details table
        session.user_type = "Admin" // replace this from user details table
   
        res.success = true;
        res.redirect = "${grailsApplication.config.grails.serverURL}/home/index"

        withFormat {
            json {
               render(contentType: "text/json") { response result: res}
            }
        }
    } 
} 