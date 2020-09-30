package com.akkax.app

import java.time.Instant

import akka.Done
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.akka.actors.ProcessorActor
import com.akka.actors.ProcessorActor.Create

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object Application extends App {
  val actorSystem = ActorSystem("messaging")
  implicit val timeout: Timeout = Timeout(60 seconds)
  val processor: ActorRef = actorSystem.actorOf(Props(new ProcessorActor))
  val request = List(
    Create("log1", "File Content - 1"),
    Create("log2", "File Content - 2"),
    Create("log3", "File Content - 3"),
    Create("log4", "File Content - 4"),
    Create("log5", "File Content - 5"),
    Create("log6", "File Content - 6"),
    Create("log7", "File Content - 7"),
    Create("log8", "File Content - 8"),
    Create("log9", "File Content - 9"),
    Create("log10", "File Content - 10")
  )
  val start = Instant.now()
  val results = request.map {
    request =>
      (processor ? request)
        .mapTo[Done]
        .recover {
          case exception: Exception => println(s"Error while writing to the file:[${request.fileName}]" + s"content:[${request.content}]")
            throw exception

        }
  }
  val finalRes: Future[List[Done]] = Future.sequence(results)
  finalRes.foreach { status =>
    val processTime = Instant.now().getEpochSecond - start.getEpochSecond
    println("Processing Time:=" + processTime)
    println("File Status:" + status.toString())
  }
  finalRes

}





