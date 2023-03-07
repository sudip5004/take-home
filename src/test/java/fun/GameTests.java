package fun;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class GameTests {
	@Test
	public void testNoGameNameInput() {
		String response = invokeAPI("http://localhost:8080/game?name=");
		assertTrue(response.toString().contains("Playing Sudoku is fun!"));
	}

	@Test
	public void testGameNameInput() {
		String response = invokeAPI("http://localhost:8080/game?name=Chess");
		assertTrue(response.toString().contains("Playing Chess is fun!"));

	}

	@Test
	public void validateWithJsonPath() {
		String json = invokeAPI("http://localhost:8080/game?name=testing");

		List<String> value = JsonPath.read(json, "$..text");
		assertTrue(value.get(0).contains("Playing"));
	}

	@Test
	public void validateJsonParsing() throws JsonParseException, JsonMappingException, IOException {
		String json = invokeAPI("http://localhost:8080/game?name=Chess");
		ObjectMapper objectMapper = new ObjectMapper();

		GameResponse gameResponse = objectMapper.readValue(json, GameResponse.class);

		assertTrue(gameResponse.getText().contains("Playing"));
	}

	private static String invokeAPI(String urlString) {
		RestTemplate rt= new RestTemplate();
		String response = rt.getForObject(urlString, String.class);
		return response;
	}

}
