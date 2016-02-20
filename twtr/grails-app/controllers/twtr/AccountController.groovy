package twtr

class AccountController {

    def get() {
        def theAccount = Account.get(params.id)
        if(!theAccount) {
            response.sendError(404)
        }
        else {
//            render(contentType: "application/json") {
//                account (id: theAccount.id,
//                        handle: theAccount.handle,
//                        password: theAccount.password,
//                        displayName: theAccount.displayName)
//            }
            [account: theAccount]
        }
    }

    def index() {
        saveIfNotExists(goodAccount1)

        def allAccounts = Account.list()
        render (contentType: "application/json") {
            accounts = array {
                for (a in allAccounts) {
                    account (id: a.id,
                            handle: a.handle)
                }
            }
        }
    }

    def createAccount() {
        Account goodAccount = new Account(handle: request.JSON.handle, emailAddress: request.JSON.email,
                password: request.JSON.password, displayName: request.JSON.displayName)
    }
}
