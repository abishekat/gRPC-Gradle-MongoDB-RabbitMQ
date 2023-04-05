package cu.dssassignment2.mongospringutil.grpc.client;

import cu.assignment2.proto.*;
import cu.dssassignment2.mongospringutil.rabbitmq.RabbitMQConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQPublisher extends RabbitMQConfig {

    private final static String QUEUE_1 = "Q1";
    private final static String QUEUE_2 = "Q2";
    private final static String QUEUE_3 = "Q3";
    private final static String QUEUE_4 = "Q4";
    private final static String QUEUE_5 = "Q5";
    static ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6569)
            .usePlaintext()
            .build();
    static EduCostStatServiceGrpc.EduCostStatServiceBlockingStub stub = EduCostStatServiceGrpc.newBlockingStub(channel);

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Need one argument to work");
            return;
        }

        switch (args[0]) {
            case "test":
                doTest(channel);
                break;
            case "q1":
                getCost(channel);
                break;
            case "q2":
                get5ExpensiveState(channel);
                break;
            case "q3":
                get5EconomicState(channel);
                break;
            case "q4":
                get5TopGrowthRate(channel);
                break;
            case "q5":
                expenseByRegion(channel);
                break;

            default:
                System.out.println("Keyword Invalid: " + args[0]);
        }

        System.out.println("Shutting down");
        channel.shutdown();
    }

    private static void get5TopGrowthRate(ManagedChannel channel) {
        Query4Request request = Query4Request.newBuilder()
                .setYear("2013")
                .setType("Private")
                .setLength("4-year")
                .build();
        QueryResponse response = stub.q4(request);
        rabbitTemplate().convertAndSend("TopicExchange", "topic4", response.getEduCostStatsList().toString());


    }

    private static void get5EconomicState(ManagedChannel channel) {
        Query3Request request = Query3Request.newBuilder()
                .setYear("2013")
                .setType("Private")
                .setLength("4-year")
                .setExpense("Fees/Tuition")
                .build();
        QueryResponse response = stub.q3(request);
        rabbitTemplate().convertAndSend("TopicExchange", "topic3", response.getEduCostStatsList().toString());


    }

    private static void get5ExpensiveState(ManagedChannel channel) {
        Query2Request request = Query2Request.newBuilder()
                .setYear("2013")
                .setType("Private")
                .setLength("4-year")
                .build();

        QueryResponse response = stub.q2(request);
        rabbitTemplate().convertAndSend("TopicExchange", "topic2", response.getEduCostStatsList().toString());


    }

    private static void getCost(ManagedChannel channel) {

        QueryRequest request = QueryRequest.newBuilder()
                .setYear("2013")
                .setState("Alabama")
                .setType("Private")
                .setLength("4-year")
                .setExpense("Fees/Tuition")
                .build();

        QueryResponse response = stub.q1(request);
        rabbitTemplate().convertAndSend("TopicExchange", "topic1", response.getEduCostStatsList().toString());

    }

    private static void expenseByRegion(ManagedChannel channel) {

        Query4Request request = Query4Request.newBuilder()
                .setYear("2013")
                .setType("Private")
                .setLength("4-year")
                .build();
        Query5Response response = stub.q5(request);
        rabbitTemplate().convertAndSend("TopicExchange", "topic5", response.getRegionExpensesList().toString());

    }

    private static void doTest(ManagedChannel channel) {
        Response response = stub.test(Request.newBuilder().setName("Abishek").build());
        System.out.println(response.getName());
    }

    @Bean
    public static RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return rabbitTemplate;
    }

    @Bean
    public static ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri("amqps://aarumugam:guest1234567@b-cdef04c3-e248-40e2-9f6f-0427eb231362.mq.us-east-1.amazonaws.com:5671");
        return factory;
    }

    @Bean
    public Queue q1() {
        return new Queue(QUEUE_1, true);
    }


    @Bean
    public Queue q2() {
        return new Queue(QUEUE_2, true);
    }


    @Bean
    public Queue q3() {
        return new Queue(QUEUE_3, true);
    }


    @Bean
    public Queue q4() {
        return new Queue(QUEUE_4, true);
    }


    @Bean
    public Queue q5() {
        return new Queue(QUEUE_5, true);
    }


    @Bean
    Exchange myExchange() {
        return ExchangeBuilder.topicExchange("TopicExchange")
                .durable(true)
                .build();
    }

    @Bean
    Binding q1Binding() {
        return BindingBuilder.bind(q1())
                .to(myExchange())
                .with("topic1")
                .noargs();
    }

    @Bean
    Binding q2Binding() {
        return BindingBuilder.bind(q2())
                .to(myExchange())
                .with("topic2")
                .noargs();
    }

    @Bean
    Binding q3Binding() {
        return BindingBuilder.bind(q3())
                .to(myExchange())
                .with("topic3")
                .noargs();
    }

    @Bean
    Binding q4Binding() {
        return BindingBuilder.bind(q4())
                .to(myExchange())
                .with("topic4")
                .noargs();
    }

    @Bean
    Binding q5Binding() {
        return BindingBuilder.bind(q5())
                .to(myExchange())
                .with("topic5")
                .noargs();
    }

}
