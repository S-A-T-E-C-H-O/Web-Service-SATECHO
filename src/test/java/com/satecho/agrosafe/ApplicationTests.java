package com.satecho.agrosafe;

import com.satecho.agrosafe.platform.apiservice.BackApiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = BackApiApplication.class)
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
