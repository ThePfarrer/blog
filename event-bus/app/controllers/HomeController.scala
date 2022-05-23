package controllers

import play.api.libs.ws._
import play.api.mvc._

import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (ws: WSClient, val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method will be
   * called when the application receives a `GET` request with a path of `/`.
   */

  def events() = Action { request =>
    val event = request.body.asJson.get

    ws.url("http://localhost:9000/events").post(event)
    ws.url("http://localhost:9001/events").post(event)
    ws.url("http://localhost:9002/events").post(event)

    Ok
  }
}
