package tech.buildrun.promowisems;

import org.springframework.boot.SpringApplication;

import static tech.buildrun.promowisems.ContainerConfig.getProperties;
import static tech.buildrun.promowisems.ContainerConfig.wireMockContainer;

public class TestPromowisemsApplication {

	public static void main(String[] args) {
		wireMockContainer.start();
		getProperties().forEach(System::setProperty);
		SpringApplication.from(PromowisemsApplication::main)
				.with(ServiceConnectionConfig.class)
				.run(args);
	}

}
