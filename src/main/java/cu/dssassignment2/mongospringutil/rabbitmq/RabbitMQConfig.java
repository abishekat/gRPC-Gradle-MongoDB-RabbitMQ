package cu.dssassignment2.mongospringutil.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private final static String QUEUE_1 = "Q1";
    private final static String QUEUE_2 = "Q2";
    private final static String QUEUE_3 = "Q3";
    private final static String QUEUE_4 = "Q4";
    private final static String QUEUE_5 = "Q5";

    private static final String MY_QUEUE = "MyQueue";

    public static ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri("amqps://aarumugam:guest1234567@b-cdef04c3-e248-40e2-9f6f-0427eb231362.mq.us-east-1.amazonaws.com:5671");
        return factory;
    }

    public Queue q1() {
        return new Queue(QUEUE_1, true);
    }

    public Queue q2() {
        return new Queue(QUEUE_2, true);
    }

    public Queue q3() {
        return new Queue(QUEUE_3, true);
    }

    public Queue q4() {
        return new Queue(QUEUE_4, true);
    }

    public Queue q5() {
        return new Queue(QUEUE_5, true);
    }

    Exchange myExchange() {
        return ExchangeBuilder.topicExchange("TopicExchange")
                .durable(true)
                .build();
    }

    Binding q1Binding() {
        return BindingBuilder.bind(q1())
                .to(myExchange())
                .with("topic1")
                .noargs();
    }

    Binding q2Binding() {
        return BindingBuilder.bind(q2())
                .to(myExchange())
                .with("topic2")
                .noargs();
    }

    Binding q3Binding() {
        return BindingBuilder.bind(q3())
                .to(myExchange())
                .with("topic3")
                .noargs();
    }

    Binding q4Binding() {
        return BindingBuilder.bind(q4())
                .to(myExchange())
                .with("topic4")
                .noargs();
    }

    Binding q5Binding() {
        return BindingBuilder.bind(q5())
                .to(myExchange())
                .with("topic5")
                .noargs();
    }

    public MessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueues(q1(), q2(), q3(), q4(), q5());
        container.setMessageListener(new RabbitMQMessageListener());
        return container;
    }

    public static class RabbitMQMessageListener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            System.out.println("message ::: " + new String(message.getBody()));
        }
    }
}
