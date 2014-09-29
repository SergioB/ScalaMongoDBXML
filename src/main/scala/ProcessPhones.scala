
import scala.io.Source
import scala.xml.pull._
import com.mongodb.casbah.Imports._

/**
 * Common superclass which connects to MongoDb
 */
class MongoConnector {
  val PHONES = "phones"
  val NAME = "name"
  val LAST_NAME = "lastName"

  val mongoClient = MongoClient("localhost", 27017)
  val db = mongoClient(PHONES)
  val coll = db(PHONES)

}

/**
 * Find a record by Name
 * @param name = name of the record
 */
class FindByName(name: String) extends MongoConnector {
  coll.find(MongoDBObject(NAME -> name)).foreach(row => printRow(row: DBObject))

  def printRow(row: DBObject) {
    println("Name: " + row.get(NAME))
    println("Last Name: " + row.get(LAST_NAME))
    println("Phones: " + row.get(PHONES))
  }
}

/**
 * Process xml with phone numbers
 */
class ProcessPhones(fileName: String) extends MongoConnector {
  val xml = new XMLEventReader(Source.fromFile(fileName))
  var name = ""
  var lastName = ""
  var phone = ""


  def printText(text: String, currElement: String) {
    currElement match {
      case NAME => name = text
      case LAST_NAME => lastName = text
      case "phone" => phone = text
      case _ => ()
    }
  }

  def processOneRecord(name: String, lastName: String, phone: String) {
    val foundObject = coll.findOne(MongoDBObject(NAME -> name, LAST_NAME -> lastName))
    if (foundObject == None) {
      //if no object found in database then add a new object
      coll.insert(MongoDBObject(NAME -> name, LAST_NAME -> lastName, PHONES -> MongoDBList(phone)))
    } else {
      //else update an existing one
      val query = MongoDBObject("_id" -> foundObject.get("_id"))
      coll.update(query, $addToSet(PHONES -> phone)) //addToSet adds the phone just once
    }
  }

  def parse(xml: XMLEventReader) {
    def loop(currElement: String) {
      if (xml.hasNext) {
        xml.next match {
          case EvElemStart(_, label, _, _) =>
            loop(label)
          case EvElemEnd(_, label) =>
            if (label == "contact") processOneRecord(name, lastName, phone)
            loop(label)
          case EvText(text) =>
            printText(text, currElement)
            loop(currElement)
          case _ => loop(currElement)
        }
      }
    }
    loop("")
  }
  parse(xml)
}

object ProcessPhones extends App {
  if (args.length > 1) {
    args(0) match {
      case "load" => new ProcessPhones(args(1))
      case "find" => new FindByName(args(1))
      case _ => println("Incorrect first argument")
    }
  } else {
     println("This program needs 2 arguments operation and parameter, possible operations\n load filename.xml and find Name")
  }
}

