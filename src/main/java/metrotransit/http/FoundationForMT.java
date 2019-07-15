package metrotransit.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import metrotransit.domain.Route;
import metrotransit.util.ExtendedJacksonObjectMapper;


@Service
public class FoundationForMT {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FoundationForMT.class.getSimpleName());

	@Value("${mt.getroutes}")
	private String mtRoutesUrl;
	 
	@Autowired
	APIConnectionProvider connection;
	
	@Autowired
	ExtendedJacksonObjectMapper objectMapper;
	
	public List<Route> getAllRoutesFromMT() throws IOException {
				 
		LOGGER.info("Fetching all routes");
		 		 
		LOGGER.info("FinalUrl to fetch all routes : " + mtRoutesUrl);

		boolean isProxyEnable = false;
		String proxyHost = "1.1.1.1";
		String proxyPort = "80";

		HttpResponse response = connection.get(mtRoutesUrl, isProxyEnable, proxyHost, proxyPort);

		if (response == null) {
			return null;
		}

		String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		LOGGER.info("Route Response: " + responseString);
		
		List<Route> routeList = objectMapper.convertJSonListStringToPojoList(responseString, Route.class);
		LOGGER.info("Total routes found " + routeList.size());
		
		return routeList;
	}

	public String getMtRoutesUrl() {
		return mtRoutesUrl;
	}

	public void setMtRoutesUrl(String mtRoutesUrl) {
		this.mtRoutesUrl = mtRoutesUrl;
	}
}
