package nl.davefemi.webchess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "nl.davefemi.webchess")
public class GameServer {
    public static void main(String[] args) {
        SpringApplication.run(GameServer.class, args);
    }
}
