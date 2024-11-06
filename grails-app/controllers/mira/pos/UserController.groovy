package mira.pos
import grails.converters.JSON
import java.text.SimpleDateFormat 

class UserController {

    def userService

    def index() {

    }

    def home() {
        if (!session.username) {
            redirect(controller: 'login', action: 'index')
            return
        }
        render(view: "/users/users", model: [userFullName: session.fullName])
    }

    def getUserData() {
        try {
            List<Map> userList = userService.getAllUsers()
            render userList as JSON
        } catch (Exception e) {
            log.error("Error fetching user data: ${e.message}", e)
            render(status: 500, text: 'Internal Server Error')  // Return a 500 error response
        }
    }

    def createUser() {
        try {
            def requestData = request.JSON 

            // Extract user data from the request
            String username = requestData.username
            String password = requestData.password
            String firstName = requestData.firstName
            String middleName = requestData.middleName
            String lastName = requestData.lastName
            Long userType = requestData.userType
            String dobString = requestData.dob


            // Call the service to create the user
            if (userService.createUser(username, password, firstName, middleName, lastName, userType, dobString)) {
                render status: 201, text: 'User created successfully' // Return success status
            } else {
                render status: 500, text: 'Failed to create user' // Return failure status
            }
        } catch (Exception e) {
            log.error("Error creating user: ${e.message}", e) // Log the error
            render status: 500, text: 'Internal Server Error' // Return a 500 status
        }
    }

    def updateUser(Long id){
        try{
            def requestData = request.JSON

            String firstName = requestData.firstName
            String middleName = requestData.middleName
            String lastName = requestData.lastName
            String dobString = requestData.dob

            def updateUser = userService.updateUser(id, firstName, middleName, lastName, dobString);

            if (updateUser){
                render status: 200, text:'User  updated successfully'
            }else{
                render status: 400, text: 'User not found'
            }
        }catch (Exception e){
            log.error("Error updating user: ${e.message}", e)
            render status: 500, text: 'Internal Server Error'
        }
    }

    def deleteUser(Long id){
        try{
            if (userService.deleteUser(id)){
                render status: 204
            }else{
                render status: 404
            }
        }catch(Exception e){
            log.error("Error deleting user id ${id}: ${e.message}", e)
            render status: 500, text: "An error occured while deletin the user"
        }
    }
    
}