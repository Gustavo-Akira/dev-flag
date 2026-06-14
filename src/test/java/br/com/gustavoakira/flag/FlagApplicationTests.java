package br.com.gustavoakira.flag;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class FlagApplicationTests {

	@Test
	void contextLoads() {
	}

}
