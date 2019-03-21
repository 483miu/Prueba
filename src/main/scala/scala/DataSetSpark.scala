import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object DataSetSpark{
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val session = SparkSession.builder().appName("DataSetConsumidor").master("local[1]").getOrCreate()
    import session.implicits._
    val datosPDataSet = ConsumerMain.datosP.toDS()
    datosPDataSet.printSchema()


  }

}