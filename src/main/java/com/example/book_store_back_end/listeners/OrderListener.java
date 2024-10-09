package com.example.book_store_back_end.listeners;

import com.example.book_store_back_end.constants.KafkaConfig;
import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.services.OrderService;
import com.example.book_store_back_end.wsServers.OrderWebSocketServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderListener {
    private final OrderService orderService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final OrderWebSocketServer orderWebSocketServer;

    @KafkaListener(topics = KafkaConfig.KAFKA_TOPIC1, groupId = KafkaConfig.KAFKA_GROUP_ID)
    public void topic1Listener(ConsumerRecord<String, String> record){
        String value = record.value();
        try {
            OrderDto orderDto = objectMapper.readValue(value, OrderDto.class);
            ResponseDto<Long> responseDto  = orderService.createOrder(orderDto);
            long newOid = responseDto.resource();
            System.out.println(newOid);
            String key = orderDto.getUid().toString() + "," + orderDto.getOid();
            kafkaTemplate.send(KafkaConfig.KAFKA_TOPIC2, key, "Done"); //成功完成订单，发送信息
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaConfig.KAFKA_TOPIC2, groupId = KafkaConfig.KAFKA_GROUP_ID)
    public void topic2Listener(ConsumerRecord<String, String> record){
        String value = record.value();
        System.out.println(value);
        String[] keys = record.key().split(",");
        if(keys.length < 1){
            System.err.println("拿取key错误");
            return;
        }
        orderWebSocketServer.sendMessageToUser(keys[0], record.value());
    }
}
