class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/accounts"(resources:'account') {
            "/messages"(resources:'message')
            "/startFollowing"(controller: 'account') {
                action = [POST: 'startFollowing']
            }
            "/followers"(controller:'account') {
                action = [GET: 'followers']
            }
            "/following"(controller:'account') {
                action = [GET: 'following']
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
