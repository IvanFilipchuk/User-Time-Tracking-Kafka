package org.example;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import java.time.Duration;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class StreamAverage {
    private static final Logger logger = LoggerFactory.getLogger(StreamAverage.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) {
        Properties props = createStreamProperties();
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream("user_activity");
        KTable<Windowed<String>, Double> averageTimeSpent = calculateAverageTimeSpent(source);

        averageTimeSpent.toStream().foreach(StreamAverage::logAverageTimeSpent);

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        startStream(streams);
    }
    private static Properties createStreamProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-average");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return props;
    }
    private static KTable<Windowed<String>, Double> calculateAverageTimeSpent(KStream<String, String> source) {
        return source
                .mapValues(StreamAverage::extractTimeSpent)
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofSeconds(30)))
                .aggregate(
                        () -> 0.0,
                        (aggKey, newValue, aggValue) -> aggValue + newValue,
                        Materialized.with(Serdes.String(), Serdes.Double())
                )
                .mapValues(value -> value / 30);
    }

    private static int extractTimeSpent(String value) {
        try {
            JsonNode node = objectMapper.readTree(value);
            return node.get("time_spent").asInt();
        } catch (Exception e) {
            logger.error("Błąd podczas parsowania JSON: ", e);
            return 0;
        }
    }
    private static void logAverageTimeSpent(Windowed<String> key, Double value) {
        logger.info("Okno czasowe [{} - {}] | Średni czas spędzony: {} sekund", key.window().startTime(), key.window().endTime(), value);
    }
    private static void startStream(KafkaStreams streams) {
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

