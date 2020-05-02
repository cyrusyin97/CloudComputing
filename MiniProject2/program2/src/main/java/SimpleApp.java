/* SimpleApp.java */
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.functions;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
public class SimpleApp {
    public static void main(String[] args) {
	SparkConf conf = new SparkConf().setMaster("local").setAppName("Simple Application");
        String logFile = "user_artists.dat"; // Should be some file on your system
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> dataFile = spark.read().format("csv").option("delimiter", "\t").option("inferSchema", "true").option("header", "true").load(logFile);

	Dataset<Row> counts = dataFile.groupBy(dataFile.col("artistID")).sum("weight");
	Dataset<Row> result1 = counts.withColumnRenamed("sum(weight)", "sum_weight");
	Dataset<Row> result2 = result1.sort(result1.col("sum_weight").desc());

        result2.write().option("header", "true").csv("part2_result.csv");
        spark.stop();
    }
}
