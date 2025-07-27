typealias RF = Route.() -> Unit
typealias RFT = Route.() -> String

class Route {
    fun path(p: String, f: RF) {}
    fun get(p: String? = "", f: RFT) {}
    fun post(p: String? = "", f: RFT) {}
}

interface Router {
    fun Route.setup()
}

class MyRouter(): Router {
    override fun Route.setup() {
        path("document/{id}"){ get { "OK" } }
        path("user/{id}"){ get { "OK" } }
    }
}

class AdminRouter(): Router {
    override fun Route.setup() {
        path("/admin") {
            path("document/detail/{id}"){ get { "OK" } }
            path("user"){ post { "OK" } }
        }
    }
}


fun main() {
    val rootRoute = Route()
    rootRoute.setupApp()
}

fun Route.setupApp() {
    val myRouter = MyRouter()
    val adminRouter = AdminRouter()

    myRouter.run { setup() }
    adminRouter.run { setup() }

}