package metrotransit.util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import metrotransit.domain.Route;

@Service
public class ExtendedJacksonObjectMapper extends ObjectMapper {

	private static Logger logger = LoggerFactory.getLogger(ExtendedJacksonObjectMapper.class);

	

	public <T> ConcurrentLinkedQueue<T> convertJSonStringToConcurrentLinkedQueuePojo(List<String> jsonStringList,
			Class<T> clazz) throws Exception {
		logger.info("Total number of JsonStringList received in convertJSonStringToConcurrentLinkedQueuePojo: "
				+ jsonStringList.size());
		ConcurrentLinkedQueue<T> tList = new ConcurrentLinkedQueue<T>();

		if (isListNotEmpty(jsonStringList)) {
			for (String string : jsonStringList) {
				T t = readValue(string, clazz);
				tList.add(t);
			}
		}
		logger.info("Number of List created from JsonStringList: " + tList.size());
		return tList;
	}

	public <T> String convertPojoToJSonString(T details) throws JsonProcessingException {
		String jsonString = writeValueAsString(details);
		// logger.info("Converted json string in convertPojoToJSonString is: " +
		// jsonString);
		return jsonString;
	}

	public <T> String convertPojoListToJSonString(List<T> tList) throws JsonProcessingException {
		logger.info("Size of list received in convertPojoListToJSonString: " + tList.size());
		StringBuilder jSonStringBuilder = new StringBuilder();
		jSonStringBuilder.append("[");
		String resultString = "[";

		if (isListNotEmpty(tList)) {
			for (T t : tList) {
				jSonStringBuilder.append(writeValueAsString(t));
				jSonStringBuilder.append(",");
			}
			resultString = jSonStringBuilder.toString().substring(0, jSonStringBuilder.toString().length() - 1);
		}
		resultString = resultString + "]";

		// logger.info("Converted string from convertPojoListToJSonString: " +
		// resultString);
		return resultString;

	}

	public <T> List<T> convertJSonListStringToPojoList1(String jsonListString, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("JsonListString received in convertJSonListStringToPojoList: " + jsonListString);
		 
		List<T> tList = null;
		if (jsonListString != null && jsonListString.trim().length() > 0) {
			tList = readValue(jsonListString,
					TypeFactory.defaultInstance().constructCollectionLikeType(List.class, clazz));
			logger.info("Size of List created: " + tList.size());
		}

		return tList;

	}
	
	public <T> List<T> convertJSONStringToListOfObject2(String jsonString, Class<T> type) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		/*objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);		 
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);*/
		return objectMapper.readValue(jsonString,
				TypeFactory.defaultInstance().constructCollectionType(List.class, type));
	}
	
	public List<Route> convertJSonListStringToPojoList(String jsonListString)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("JsonListString received in convertJSonListStringToPojoList: " + jsonListString);
		 
		List<Route> myObjects = null;
		if (jsonListString != null && jsonListString.trim().length() > 0) {
			/*tList = readValue(jsonListString,
					TypeFactory.defaultInstance().constructCollectionLikeType(List.class, clazz));*/
			 //myObjects = readValue(jsonListString, new TypeReference<List<Route>>(){});
			disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			Route[] routes = readValue(jsonListString, Route[].class);

			logger.info("Size of List created: " + myObjects.size());
		}

		return myObjects;

	}
	
	public Route[] convertJSonListStringToPojoArray(String jsonListString)
			throws JsonParseException, JsonMappingException, IOException {
		logger.info("JsonListString received in convertJSonListStringToPojoList: " + jsonListString);
		 
		Route[] routes = null;
		if (jsonListString != null && jsonListString.trim().length() > 0) {
			 
			disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			routes = readValue(jsonListString, Route[].class);

			logger.info("Size of List created: " + routes.length);
		}

		return routes;

	}
	
	 
	
	

	public static boolean isListNotEmpty(List t) {
		if (t != null && t.size() > 0) {
			return true;
		}
		logger.info("Provided list is NULL or Empty.");
		return false;
	}

}
