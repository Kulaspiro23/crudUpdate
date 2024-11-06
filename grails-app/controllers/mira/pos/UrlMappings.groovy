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

        "/product/getProductData"(controller: "product", action: "getProductData")
        "/product/createProduct"(controller: "product", action: "createProduct")
        "/product/updateProduct/$id"(controller: "product", action: "updateProduct", method: "PUT")
        "/product/deleteProduct/$id"(controller: "product", action: "deleteProduct", method: "POST")

        "/order/getOrderData"(controller: "order", action: "getOrderData")
        "/order/getSessionData"(controller: "order", action: "getSessionData")
        "/order/searchProduct"(controller: "order", action: "searchProduct")
        "/order/submitSale"(controller: "order", action: "submitSale", method: "POST")
    
        "500"(view:'/error')
        "404"(view:'/notFound')

    }
}
