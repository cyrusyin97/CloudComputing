import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.functions;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.RelationalGroupedDataset;
import scala.Tuple2;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import org.apache.spark.mllib.regression.GeneralizedLinearAlgorithm;

public class analyze_log {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Access Log");
        String logFile = "access_log"; // Should be some file on your system
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

	Dataset<Row> dataFile = spark.read().format("csv").option("sep", " ").option("inferSchema", "true").load(logFile);

        Dataset<Row> timestamp = dataFile.groupBy(dataFile.col("_c3").substr(5,8)).count();
	Dataset<Row> r = timestamp.withColumnRenamed("substring(_c3, 5, 8)", "Date");

	//SimpleDateFormat formatter=new SimpleDateFormat("MMM/yyyy");
	Dataset<Row> n = r.filter(r.col("Date").endsWith("9").or(r.col("Date").endsWith("0").or(r.col("Date").endsWith("1"))));

	Dataset<Row> n2 = n.selectExpr("split(Date, '/')[0] as Month", "split(Date, '/')[1] as Year", "count");

	Dataset<Row> n3 = n2.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Jan"), 1));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Feb"), 2).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Mar"), 3).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Apr"), 4).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("May"), 5).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Jun"), 6).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Jul"), 7).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Aug"), 8).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Sep"), 9).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Oct"), 10).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Nov"), 11).otherwise(n3.col("MonthInt")));
	n3 = n3.withColumn("MonthInt", functions.when(n2.col("Month").equalTo("Dec"), 12).otherwise(n3.col("MonthInt")));

	Dataset<Row> n4 = n3.withColumn("Days", n3.col("Year").multiply(365).plus(n3.col("MonthInt").multiply(30)));
	//n4.select(n4.col("count").cast("double"));

	Dataset<Row> datatable = n4.drop(n4.col("Month")).drop(n4.col("MonthInt")).drop(n4.col("Year"));
	datatable = datatable.withColumn("count", datatable.col("count").cast("double"));

	JavaRDD<LabeledPoint> parsedData = datatable.toJavaRDD().map(line -> {
		double[] v = new double[2];
		v[0] = line.getAs("count");
		v[1] = 1.0;
		return new LabeledPoint(line.getAs("Days"), Vectors.dense(v));
	});
	parsedData.saveAsTextFile("datatable");
	int numIterations = 5000;
	double stepSize = 0.0000000000001;
	LinearRegressionWithSGD lrm = new LinearRegressionWithSGD();
	//lrm.setIntercept(true);
	LinearRegressionModel model = lrm.train(JavaRDD.toRDD(parsedData), numIterations, stepSize);
	model.save(spark.sparkContext(), "LinearRegressionModel");
	System.out.println("Weights: " + model.weights());
	//System.out.println("Intercept " + model.intercept());

        spark.stop();
    }
}


