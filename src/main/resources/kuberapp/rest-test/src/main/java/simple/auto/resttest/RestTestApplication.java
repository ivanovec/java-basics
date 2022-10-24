package simple.auto.resttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@SpringBootApplication
@RestController
public class RestTestApplication {

	@RequestMapping("/dontworry")
	public String happy() {
		return "behappy";
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(RestTestApplication.class);
		app.setDefaultProperties(
				Collections.singletonMap("server.port", "8083")
		);
		app.run(args);
	}

}
