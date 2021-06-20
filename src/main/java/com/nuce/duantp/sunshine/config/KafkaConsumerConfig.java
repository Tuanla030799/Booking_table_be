//package com.nuce.duantp.sunshine.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//@EnableKafka
//@Configuration
//public class KafkaConsumerConfig {
//    /*
//     *
//     *   KAFKA CONSUMER CONFIG
//     *
//     * */
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactoryTransaction() {
//        JsonDeserializer<String> deserializer = new JsonDeserializer<>(String.class);
//        deserializer.setRemoveTypeHeaders(false);
//        deserializer.addTrustedPackages("*");
//        deserializer.setUseTypeMapperForKey(true);
//
//
//        return new DefaultKafkaConsumerFactory<>(getProperties(), new StringDeserializer(), deserializer);
//    }
//
//
//
//    @Bean(name = "kafkaListenerContainerTransactionFactory")
//    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerTransactionFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactoryTransaction());
//        return factory;
//    }
//
//
//    @Bean
//    public ConsumerFactory<String, String> consumerStringFactoryTransaction() {
//
//        return new DefaultKafkaConsumerFactory<>(getProperties(), new StringDeserializer(), new StringDeserializer());
//    }
//
//    @Bean(name = "kafkaListenerContainerStringTransactionFactory")
//    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerStringTransactionFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerStringFactoryTransaction());
//        return factory;
//    }
//
//
//    private static Map<String, Object> getProperties() {
//
//        Map<String, Object> properties = new HashMap<>();
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "104.248.147.217:9092");
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "risk");
//        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
//        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
//        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//        return properties;
//    }
//    /*
//     *
//     *       KAFKA PRODUCER CONFIG
//     *
//     * */
//
//
//
//
//    @Bean
//    public Map<String, Object> producerStringConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "104.248.147.217:9092");
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        return props;
//    }
//
//
//
//}
