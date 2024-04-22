import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerKafka {
    private static final Logger log = LoggerFactory.getLogger(ConsumerKafka.class);

    public static void main(String[] args) {
        log.info("Hello World");

        String bootstrapServers = "localhost:9092";
        String groupId = "hello-y";
        String topic = "demoTopic";

        // create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));


        long responseTimeInMillis = 0;
        // poll for new data
        while(true){
            long start = System.currentTimeMillis();
            ConsumerRecords<String, byte[]> records =
                    consumer.poll(Duration.ofMillis(100));
            responseTimeInMillis += (System.currentTimeMillis() - start);
            for (ConsumerRecord<String, byte[]> record : records){
                log.info("Key: " + record.key() + ", Value: " + record.value());
                       log.info("Partition: " + record.partition() + ", Offset:" + record.offset());
            }
            System.out.println("Response Time in Kafka consumer: " + responseTimeInMillis + " ms");

        }

    }
}
