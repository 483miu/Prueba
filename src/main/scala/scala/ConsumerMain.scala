
import java.util

import Modelo.DatosP
import com.google.gson.JsonParser
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConverters._

object ConsumerMain{

  var datosP = scala.collection.mutable.ArrayBuffer.empty[DatosP]
  println(datosP)


  def main(args: Array[String]): Unit = {

    import java.util.Properties

    val TOPIC = "test"

    val props = new Properties()
    props.put("bootstrap.servers", "10.15.191.238:9092")

    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("group.id", "something1")

    val consumer = new KafkaConsumer[String, String](props)

    consumer.subscribe(util.Collections.singletonList(TOPIC))

    while (true) {
      val records = consumer.poll(100)
      for (record <- records.asScala) {
        println(record.value())
        val parser = new JsonParser()
        val parse2 = parser.parse(record.value()).getAsJsonObject
        println(parse2)

        println(parse2.get("nombre"))
        println(parse2.get("edad"))
        println(parse2.get("fechaNa"))
        println(parse2.get("ciudad"))
        println(parse2.get("caracteristica"))

        val mongo = ConnectMongo
        mongo.conectar
     /*   var doc = MongoDBObject(
          "nombre" -> parse2.get("nombre").getAsString,
          "edad" -> parse2.get("edad").getAsInt,
          "fechaNa" -> parse2.get("fechaNa").getAsString,
          "ciudad" -> parse2.get("ciudad").getAsString,
          "caracteristica" -> parse2.get("caracteristica").getAsString
        ) */

          var objeto = DatosP(
            parse2.get("nombre").getAsString,
            parse2.get("edad").getAsInt,
            parse2.get("fechaNa").getAsString,
            parse2.get("ciudad").getAsString,
            parse2.get("caracteristica").getAsString
          )

          datosP.clear()
          datosP += objeto

      //  mongo.insertarDocumento("PERSONAS",doc)
        val dataSet = DataSetSpark
        dataSet.recibeArray(datosP)
        mongo.desconectar()
      }
    }
  }
}