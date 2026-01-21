package tech.buildrun.orderworkerms.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest;
import tech.buildrun.orderworkerms.ContainersConfig;
import tech.buildrun.orderworkerms.ServiceConnectionConfig;
import tech.buildrun.orderworkerms.dto.OrderDto;
import tech.buildrun.orderworkerms.dto.OrderEventDto;
import tech.buildrun.orderworkerms.entity.Order;
import tech.buildrun.orderworkerms.repository.OrderRepository;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static tech.buildrun.orderworkerms.consumer.OrderConsumer.ORDER_CONFIRMED_QUEUE;
import static tech.buildrun.orderworkerms.producer.ShippingProducer.SHIPPING_QUEUE;

@Import(ServiceConnectionConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderConsumerIT extends ContainersConfig {

    @Autowired
    private SqsTemplate sqsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeAll
    public static void beforeAll() {
        setupSqs();
    }

    @BeforeEach
    public void beforeEach() {
        orderRepository.deleteAll();

        var sqsClient = getSqsClient();
        sqsClient.purgeQueue(PurgeQueueRequest.builder().queueUrl(ORDER_CONFIRMED_QUEUE).build());
        sqsClient.purgeQueue(PurgeQueueRequest.builder().queueUrl(SHIPPING_QUEUE).build());
    }

    @Test
    void whenExistingOrderShouldPublishToShippingQueue() throws com.fasterxml.jackson.core.JsonProcessingException {

        // ARRANGE
        var orderNumber = "1234";
        var order = new Order(orderNumber, "teste@test.com", false);
        orderRepository.save(order);

        var event = new OrderEventDto(orderNumber);
        var payload = objectMapper.writeValueAsString(event);

        // ACT
        sqsTemplate.send(ORDER_CONFIRMED_QUEUE, payload);

        // ASSERT
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(()-> {
            var message = sqsTemplate.receive(SHIPPING_QUEUE, String.class);
            assertTrue(message.isPresent());

            var dto = objectMapper.readValue(message.get().getPayload(), OrderDto.class);
            assertEquals(orderNumber, dto.orderNumber());
            assertEquals(order.getCustomerEmail(), dto.customerEmail());
        });

    }

    @Test
    void whenExistingOrderShouldUpdateDatabase() throws JsonProcessingException {
        // ARRANGE
        var orderNumber = "1234";
        var order = new Order(orderNumber, "teste@test.com", false);
        orderRepository.save(order);

        var event = new OrderEventDto(orderNumber);
        var payload = objectMapper.writeValueAsString(event);

        // ACT
        sqsTemplate.send(ORDER_CONFIRMED_QUEUE, payload);

        // ASSERT
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(()-> {
           var orderDb = orderRepository.findByOrderNumber(orderNumber);
           assertTrue(orderDb.isPresent());
           assertTrue(orderDb.get().isNotified());
        });
    }

    @Test
    void whenOrderNotFoundShouldNotPublishToShippingQueue() throws JsonProcessingException, InterruptedException {
        // ARRANGE
        var orderNumber = "1234";
        var event = new OrderEventDto(orderNumber);
        var payload = objectMapper.writeValueAsString(event);

        // ACT
        sqsTemplate.send(ORDER_CONFIRMED_QUEUE, payload);

        // ASSERT
        await().atMost(12, TimeUnit.SECONDS).untilAsserted(()-> {
            var message = sqsTemplate.receive(SHIPPING_QUEUE, String.class);
            assertTrue(message.isEmpty());
        });
    }
}
