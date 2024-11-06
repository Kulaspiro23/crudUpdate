package mira.pos


class HomeController {

    def index(){
        if (!session.username) {
            redirect(controller: 'login', action: 'index')
            return
        }
        render(view: '/home', model: [userFullName: session.fullName])
    }

    def getUserLoginDetails(){

        // access user details (fullName and dob) in session

        def userDetails = [:];
            userDetails.fullName = session.fullName;
            userDetails.user_type = session.user_type;


        withFormat {
            json {
               render(contentType: "text/json") {response userDetails:userDetails}
            }
        }

    }

}