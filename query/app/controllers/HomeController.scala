package controllers

import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.parser._
import io.circe.syntax._
import play.api.libs.circe.Circe
import play.api.mvc._

import javax.inject._
import scala.collection.mutable.{ArrayBuffer, Map => MMap}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents) extends BaseController with Circe {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method will be
   * called when the application receives a `GET` request with a path of `/`.
   */
  case class Event(`type`: String, data: Map[String, String])
  case class Comment(id: String, content: String)
  case class Post(id: String, title: String, comments: ArrayBuffer[Comment])

  val posts = MMap.empty[String, Post]

  def readPost() = Action {
    Ok(posts.asJson)
  }

  def createEvent() = Action { implicit request: Request[AnyContent] =>
    val postVals = request.body.asJson.get.toString()

    decode[Event](postVals) match {
      case Right(value) =>
        value.`type` match {
          case "PostCreated" =>
            val (id, title) = (value.data("id"), value.data("title"))
            posts += (id -> Post(id, title, ArrayBuffer[Comment]()))

            println(posts.asJson)
          case "CommentCreated" =>
            val (id, content, postId) = (value.data("id"), value.data("content"), value.data("postId"))

            val post = posts(postId)
            post.comments += Comment(id, content)
        }

        Ok

      case Left(_) => BadRequest
    }
  }
}
