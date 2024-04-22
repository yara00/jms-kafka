import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
//import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ProducerKafka {
    //private static final Logger log = LoggerFactory.getLogger(ProducerKafka.class);

    public static void main(String[] args) {
     //   log.info("Hello from ProducerKafka");
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "off");
        String bootstrapServers = "localhost:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(properties);

        byte[] msg = new byte[1024];
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>("demoTopic", msg);
        long start = System.currentTimeMillis();
        producer.send(producerRecord);
        long responseTimeInMillis = System.currentTimeMillis() - start;
        producer.flush();
        producer.close();

        System.out.println("Response Time in Kafka Producer: " + responseTimeInMillis + " ms");
    }
}
