package mira.pos

class UrlMappings {
    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        // "/"(view:"/index")
        "/"(controller: "login", action: "index")
        "/user/getUserData"(controller: "user", action: "getUserData")
        "/user/createUser"(controller: "user", action: "createUser")
        "/user/updateUser/$id"(controller: "user", action: "updateUser", method: "PUT")
        "/user/deleteUser/$id"(controller: "user", action: "deleteUser", method: "POST")
    
        "500"(view:'/error')
        "404"(view:'/notFound')

    }
}
