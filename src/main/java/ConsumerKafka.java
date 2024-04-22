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
import java.util.*;

public class ConsumerKafka {
  // private static final Logger log = LoggerFactory.getLogger(ConsumerKafka.class);
    private static final int msgsCount = 100000;
    private static final String topic = "demoTopic3";

    public static void main(String[] args) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "off");
    //    log.info("Hello World");
        String bootstrapServers = "localhost:9092";
        String groupId = "hello-yara";

        // create consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));

        double responseTimeInMillis = 0;

        int ctr = 0;
        List<Long> timeDiff = new ArrayList<>();

        while(ctr < msgsCount) {
            long start = System.currentTimeMillis();
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(10));
            long end = System.currentTimeMillis();
         //   responseTimeInMillis += (end - start);
            ctr += records.count();
            for (ConsumerRecord<String, byte[]> record : records){
               timeDiff.add(end - record.timestamp());
            }
        }

        Collections.sort(timeDiff);
        long medianLatency;
        System.out.println(timeDiff.size());
        if (timeDiff.size() % 2 == 0) {
            medianLatency = (timeDiff.get(timeDiff.size() / 2 - 1) + timeDiff.get(timeDiff.size() / 2)) / 2;
        }
        else {
            medianLatency = timeDiff.get(timeDiff.size() / 2);
        }
        System.out.println("Response Time in Kafka consumer: " + responseTimeInMillis / ctr + " ms");
        System.out.println("Median Latency in Kafka consumer: " + medianLatency + " ms");
    }
}
