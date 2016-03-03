class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/accounts/$action?/$id?"(resources:'account') {

        }

        "/messages/$messageAction?/$messageId?"(resources:'message') {

        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
