package store.onmom.onmomserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"ai.*", "controller", "service", "config.*","websocket"})
@EntityScan("entity.*")
@EnableJpaRepositories("repository.*")
public class OnmomServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnmomServerApplication.class, args);
	}

}
