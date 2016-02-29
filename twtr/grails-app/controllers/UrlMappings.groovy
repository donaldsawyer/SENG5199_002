class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
//            "/message/$messageAction?/$messageId?"(resources: 'message')

            constraints {
                // apply constraints here
            }
        }

        "/account/"(resources:'account') {
            "/follow/$followAction?"(resources:'message')


        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
