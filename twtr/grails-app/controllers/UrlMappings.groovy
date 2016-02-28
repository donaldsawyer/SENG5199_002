class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            "/message/$messageAction?/$messageId?"(resources: 'message')
            //"/follow/$followAction?"
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
