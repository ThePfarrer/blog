package controllers

import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.parser._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.libs.json.Json
import play.api.libs.ws._
import play.api.mvc._

import javax.inject._
import scala.collection.mutable.{Map => MMap}
import scala.util.Random

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (ws: WSClient, cc: ControllerComponents) extends AbstractController(cc) with Circe {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method will be
   * called when the application receives a `GET` request with a path of `/`.
   */

  case class Post(title: String)
  case class Data(id: String, title: String)
  case class Event(`type`: String, data: Data)

  object Data {
    implicit val DataFormat = Json.format[Data]
  }

  object Event {
    implicit val EventFormat = Json.format[Event]
  }

  val posts = MMap.empty[String, Data]

  def readPost() = Action {
    Ok(posts.asJson)
  }

  def createPost() = Action { implicit request =>
    val id       = Random.nextLong().toHexString
    val postVals = request.body.asJson.get.toString()

    decode[Post](postVals) match {
      case Right(value) =>
        posts += (id -> Data(id, value.title))

        val postEvent = Json.toJson(Event("PostCreated", Data(id, value.title)))

        ws.url("http://localhost:9005/events").post(postEvent)

        Created(posts(id).asJson)

      case Left(_) => BadRequest

    }

  }

  def events() = Action { request =>
    println(request.body.asJson.get)
    Ok
  }
}
