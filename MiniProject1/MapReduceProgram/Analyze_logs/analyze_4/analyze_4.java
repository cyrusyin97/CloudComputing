import java.io.IOException;
import java.lang.String;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class analyze_4 {
    public static class analogsMapper4
            extends Mapper<Object, Text, Text, IntWritable>{

         private final static IntWritable one = new IntWritable(1);
         private Text word = new Text();
        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
	try{
	   String[] logs = value.toString().split(" ");
	   word.set(logs[0]);
	   context.write(word, one);
        } catch (Exception e) {
                e.printStackTrace();
            }
	}
    }

    public static class IntSumReducer
            extends Reducer<Text,IntWritable,Text,IntWritable> {
        private IntWritable result = new IntWritable();
	private int max = 0;
        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
	    if (sum > max) {
		result.set(sum);
		max = sum;
		context.write(key, result);
	    }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "analyze_4");
        job.setJarByClass(analyze_4.class);
        job.setMapperClass(analogsMapper4.class);
       // job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
