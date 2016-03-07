class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/accounts"(resources:'account') {
            "/messages"(resources:'message') {}
            "/startFollowing"(controller: 'account') {
                action = [POST: 'startFollowing']
            }
            "/following"(controller:'account') {
                action = [GET: 'following']
            }
            "/followers"(controller:'account') {
                action = [GET: 'followers']
            }
            "/feed"(controller:'account') {
                action = [GET: 'feed']
            }
        }

        "/"(view:"/index")
//        "500"(view:'/error')
//        "404"(view:'/notFound')
    }
}
