package mira.pos


class HomeController {

    def index(){
        render(view:'/home')
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