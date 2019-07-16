package metrotransit.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;

public class JsonObjectMapper {

	public static <T> T convertJsonToObject(String jsonString, Class<T> source) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);			
		objectMapper.enable(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		return objectMapper.readValue(jsonString, source);
	}
}
