package com.example.bookstore.mainService.listeners;

import com.example.bookstore.mainService.constants.KafkaConfig;
import com.example.bookstore.mainService.dto.MessageDto;
import com.example.bookstore.mainService.dto.OrderDto;
import com.example.bookstore.mainService.services.OrderService;
import com.example.bookstore.mainService.wsServers.OrderWebSocketServer;
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
            Long newOid  = orderService.createOrder(orderDto);
            String key = orderDto.getUid().toString() + "," + newOid;
            MessageDto messageDto = MessageDto.builder().valid(true).message("您的订单已完成 oid:" +newOid).build();
            //成功完成订单
            kafkaTemplate.send(KafkaConfig.KAFKA_TOPIC2, key, objectMapper.writeValueAsString(messageDto));
        }catch (RuntimeException e){
            // 捕获事务中的运行时异常
            System.err.println("事务失败: " + e.getMessage());
            MessageDto errMsg = MessageDto.builder()
                    .valid(false)
                    .message("您的订单处理失败: " + e.getMessage())
                    .build();
            // 发送错误信息到 Kafka
            try {
                OrderDto orderDto = objectMapper.readValue(value, OrderDto.class);
                kafkaTemplate.send(KafkaConfig.KAFKA_TOPIC2,
                        orderDto.getUid() + "," + "errorKey" + value,
                        objectMapper.writeValueAsString(errMsg));
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }
        }
        catch (Exception e){
            // 捕获其他异常
            System.err.println(e.getMessage());
            MessageDto errMsg = MessageDto.builder()
                    .valid(false)
                    .message("您的订单处理失败: " + e.getMessage())
                    .build();
            // 发送错误信息到 Kafka
            try {
                OrderDto orderDto = objectMapper.readValue(value, OrderDto.class);
                kafkaTemplate.send(KafkaConfig.KAFKA_TOPIC2,
                        orderDto.getUid() + "," + "errorKey" + value,
                        objectMapper.writeValueAsString(errMsg));
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }
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
        //告知用户订单处理结果 也可能出错
        orderWebSocketServer.sendMessageToUser(keys[0], record.value());
    }
}
