package cu.dssassignment2.mongospringutil;

import cu.dssassignment2.mongospringutil.grpc.server.EduCostStatServiceImpl;
import cu.dssassignment2.mongospringutil.repository.EduCostStatRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("cu.dssassignment2.mongospringutil.repository")
@ComponentScan("cu.dssassignment2.mongospringutil.*")
public class DssAssignment3Application {
    @Autowired
    EduCostStatRepository eduCostStatRepository;

    private int grpcServerPort = 6569;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DssAssignment3Application.class, args);
    }

    @Bean
    public Server grpcServer(EduCostStatServiceImpl eduCostStatServiceImpl) {
        return ServerBuilder.forPort(grpcServerPort)
                .addService(eduCostStatServiceImpl)
                .build();
    }

    @Bean
    public CommandLineRunner commandLineRunner(Server grpcServer) {
        return args -> {
            grpcServer.start();
            System.out.println("gRPC server started, listening on port: " + grpcServerPort);
            grpcServer.awaitTermination();
        };
    }

}