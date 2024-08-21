package store.onmom.onmomserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"hello.*"})
public class OnmomServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnmomServerApplication.class, args);
	}

}
