package org.diplom.dormitory.util;

import java.util.function.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.diplom.dormitory.model.ResidentDTO;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerThread extends Thread {

    private final Consumer<ResidentDTO> callback;
    private boolean running = true;

    public KafkaConsumerThread(Consumer<ResidentDTO> callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "javafx-consumer-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "latest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("is-present"));

            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String json = record.value();
                    try {
                        // ✅ Используем JsonBuilder для десериализации
                        ResidentDTO dto = JsonBuilder.getObjectMapper().readValue(json, ResidentDTO.class);
                        callback.accept(dto); // обновление интерфейса JavaFX
                    } catch (Exception e) {
                        e.printStackTrace(); // логируем ошибку при парсинге JSON
                    }
                }
            }
        }
    }


    public void stopConsuming() {
        running = false;
    }
}
