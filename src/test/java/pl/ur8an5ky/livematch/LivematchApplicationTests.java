package pl.ur8an5ky.livematch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.jpa.hibernate.ddl-auto=validate"
})
class LivematchApplicationTests {

	@Test
	void contextLoads() {
	}
}