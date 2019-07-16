package metrotransit.util;

import org.codehaus.jackson.map.ObjectMapper;

import metrotransit.domain.Route;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;

public class TestJson {

	public static void main(String s[]) throws JsonParseException, JsonMappingException, IOException {
		String json = "[{\"Description\":\"METRO Blue Line\",\"ProviderID\":\"8\",\"Route\":\"901\"},{\"Description\":\"METRO Green Line\",\"ProviderID\":\"8\",\"Route\":\"902\"},{\"Description\":\"METRO Red Line\",\"ProviderID\":\"9\",\"Route\":\"903\"},{\"Description\":\"METRO A Line\",\"ProviderID\":\"8\",\"Route\":\"921\"},{\"Description\":\"METRO C Line\",\"ProviderID\":\"8\",\"Route\":\"923\"}]";
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		/*objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);			
		objectMapper.enable(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);*/
		Route[] routes = objectMapper.readValue(json, Route[].class);
		
		System.out.println(routes.length);
	}
}
