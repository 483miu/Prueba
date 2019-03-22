import Modelo.DatosP
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

import scala.collection.mutable.ArrayBuffer

object DataSetSpark{

    Logger.getLogger("org").setLevel(Level.ERROR)
    val session = SparkSession.builder().appName("DataSetConsumidor").master("local[1]").getOrCreate()

  def recibeArray(array: ArrayBuffer[DatosP]) = {
      import session.implicits._
      val datosPDataSet = ConsumerMain.datosP.toDS()
      datosPDataSet.printSchema()
      datosPDataSet.show(4)
    datosPDataSet.write.mode("append").save("hdfs://quickstart.cloudera:8020/user/hive/warehouse/proyecto/test/personas.db/persona")

  }

}