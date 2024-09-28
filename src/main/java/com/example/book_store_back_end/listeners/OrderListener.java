package com.example.book_store_back_end.listeners;

import com.example.book_store_back_end.constants.KafkaConfig;
import com.example.book_store_back_end.dto.OrderDto;
import com.example.book_store_back_end.dto.ResponseDto;
import com.example.book_store_back_end.services.OrderService;
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

    @KafkaListener(topics = KafkaConfig.KAFKA_TOPIC1, groupId = KafkaConfig.KAFKA_GROUP_ID)
    public void topic1Listener(ConsumerRecord<String, String> record){
        String value = record.value();
        try {
            OrderDto orderDto = objectMapper.readValue(value, OrderDto.class);
            ResponseDto<Long> responseDto  = orderService.createOrder(orderDto);
            long newOid = responseDto.resource();
            System.out.println(newOid);
            kafkaTemplate.send(KafkaConfig.KAFKA_TOPIC2, KafkaConfig.KAFKA_ORDER_KEY, "Done"); //成功完成订单，发送信息
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaConfig.KAFKA_TOPIC2, groupId = KafkaConfig.KAFKA_GROUP_ID)
    public void topic2Listener(ConsumerRecord<String, String> record){
        String value = record.value();
        System.out.println(value);//之后更改为WebSocket提醒前端用户订单已完成
    }
}
