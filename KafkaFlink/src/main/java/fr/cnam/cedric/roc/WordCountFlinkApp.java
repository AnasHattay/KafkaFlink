package fr.cnam.cedric.roc;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;

public class WordCountFlinkApp{

    public static void main(String[] args) throws  Exception{

<<<<<<< HEAD
        String inputTopic="DataCollect1";
        String outputTopic="testtopic";
=======
        String inputTopic="topic1";
        String outputTopic="topic2";
>>>>>>> c5c0fe5daab35cc190635f514bd113ac55f03887
        String server="localhost:9092";
        String groupId="fr.cnam.credic.roc";
        
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        
        KafkaSource<String> source = createKafkaSource(inputTopic,server,groupId);
        
        DataStream<String> text = environment.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
        
        DataStream<String> countOperator = countOperation(text);
        
        KafkaSink<String> sink =  createKafkaSink(outputTopic, server);
        
        countOperator.sinkTo(sink);
        
        environment.execute("WordCountFlinkApp");
    }

    public static KafkaSource<String> createKafkaSource(String inputTopic, String server, String groupId) throws Exception{

        
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
       .setBootstrapServers(server)
       .setTopics(inputTopic)
       .setGroupId(groupId)
       .setStartingOffsets(OffsetsInitializer.earliest())
       .setValueOnlyDeserializer(new SimpleStringSchema())
       .build();
        return kafkaSource;
    }
    
    
    public static DataStream<String> countOperation(DataStream<String> text){
    	
    	
    	 DataStream<String> counts = text.flatMap(new Tokenizer())
    				// Group by the tuple field "0" and sum up tuple field "1"
    				.keyBy(value -> value.f0)
    				.sum(1)
    				.flatMap(new Reducer());
    	return counts;
    }
    
    public static KafkaSink<String> createKafkaSink(String outputTopic, String server) {
    	
    	KafkaRecordSerializationSchema<String> serializer = KafkaRecordSerializationSchema.builder()
    			.setValueSerializationSchema(new SimpleStringSchema())
    			.setTopic(outputTopic)
    			.build();
    	
    	
    	KafkaSink<String> sink = KafkaSink.<String>builder()
    			.setBootstrapServers(server)
    			.setRecordSerializer(serializer)
    			.build();
    	
    	return sink;
    }

}
