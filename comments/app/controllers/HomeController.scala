package controllers

import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.parser._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.libs.json.Json
import play.api.libs.ws._
import play.api.mvc._

import javax.inject._
import scala.collection.mutable.{ArrayBuffer, Map => MMap}
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
  case class Comment(content: String)
  case class Data(id: String, content: String, postId: String)
  case class Event(`type`: String, data: Data)

  object Data {
    implicit val DataFormat = Json.format[Data]
  }

  object Event {
    implicit val EventFormat = Json.format[Event]
  }

  val commentsByPostId = MMap.empty[String, ArrayBuffer[Map[String, String]]]

  def readComment(id: String) = Action {
    Ok(commentsByPostId.getOrElse(id, ArrayBuffer[Map[String, String]]()).asJson)
  }

  def createComment(id: String) = Action { request =>
    val commentId = Random.nextLong().toHexString
    val postVals  = request.body.asJson.get.toString()

    decode[Comment](postVals) match {
      case Right(value) =>
        val comments = commentsByPostId.getOrElse(id, ArrayBuffer[Map[String, String]]())
        comments += Map("id" -> commentId, "content" -> value.content)
        commentsByPostId(id) = comments

        val commentEvent =
          Json.toJson(Event("CommentCreated", Data(commentId, value.content, id)))

        ws.url("http://localhost:9005/events").post(commentEvent)

        Created(comments.asJson)

      case Left(_) => BadRequest

    }

  }

  def events() = Action { request =>
    println(request.body.asJson.get)
    Ok
  }
}
