package com.akka.actors

import java.io.{BufferedWriter, File, FileWriter}

import akka.Done
import akka.actor.Actor
import com.akka.actors.ProcessorActor.Create

class ProcessorActor extends Actor {
  val path = "C:\\Users\\POORVA\\IdeaProjects\\Akka_Assignment_1\\src\\main"


  override def receive: Receive = {
    case create: Create =>
      println(s"Message found by actor [${self.path.name}]:" + create.fileName + "Content" + create.content)
      val file = new File(path + create.fileName)
      val fw = new FileWriter(file, true)
      val bw = new BufferedWriter(fw)
      bw.write(create.content + "\n")
      bw.close()
      Thread.sleep(5000)
      sender() ! Done

    case msg => println(s"wrong message:$msg")
  }
}

object ProcessorActor {

  case class Create(fileName: String, content: String)

}
