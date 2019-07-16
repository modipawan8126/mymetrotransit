package metrotransit.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import metrotransit.domain.Direction;
import metrotransit.domain.Route;
import metrotransit.domain.Stop;
import metrotransit.domain.TimePointDeparture;
import metrotransit.util.JsonObjectMapper;

@Service
public class FoundationForMT {

	private static final Logger LOGGER = LoggerFactory.getLogger(FoundationForMT.class.getSimpleName());

	@Value("${proxy.host}")
	private String proxyHost;

	@Value("${proxy.port}")
	private String proxyPort;

	@Value("${proxy.enable}")
	private boolean proxyEnable;

	@Value("${mt.getroutes}")
	private String mtRoutesUrl;

	@Value("${mt.getdirections}")
	private String mtDirectionUrl;

	@Value("${mt.getstops}")
	private String mtStopUrl;

	@Value("${mt.gettimepointdepartures}")
	private String mtTimePointDepartureUrl;

	@Autowired
	APIConnectionProvider connection;

	/**
	 * @param routeid
	 * @param directionid
	 * @param stopcode
	 * @return
	 * @throws IOException
	 * 
	 *             This method to call HTTP adapter component to fetch departures
	 *             for given inputs.
	 */
	public TimePointDeparture[] getAllTimePointDepartureForGivenRouteDirectionStop(String routeid, String directionid,
			String stopcode) throws IOException {
		LOGGER.info("Fetching all departures for routeid " + routeid + " & direction " + directionid + " & stop "
				+ stopcode);

		StringBuilder sb = new StringBuilder();
		sb.append(mtTimePointDepartureUrl).append("/").append(routeid).append("/").append(directionid).append("/")
				.append(stopcode).append("?format=json");

		LOGGER.info("FinalUrl to fetch all departures for given route, direction & stop is: " + sb.toString());

		HttpResponse response = connection.get(sb.toString(), proxyEnable, proxyHost, proxyPort);

		if (response == null) {
			return null;
		}

		String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		LOGGER.info("Departure Response: " + responseString);

		TimePointDeparture[] departures = JsonObjectMapper.convertJsonToObject(responseString,
				TimePointDeparture[].class);

		LOGGER.info("Total departures found " + departures.length);

		return departures;
	}

	/**
	 * @param routeid
	 * @param directionid
	 * @return
	 * @throws IOException
	 * 
	 *             This method to call HTTP adapter component to fetch stop for
	 *             given inputs.
	 */
	public Stop[] getAllStopsForGivenRouteAndDirection(String routeid, String directionid) throws IOException {
		LOGGER.info("Fetching all Stops for routeid " + routeid + " & direction " + directionid);

		StringBuilder sb = new StringBuilder();
		sb.append(mtStopUrl).append("/").append(routeid).append("/").append(directionid).append("?format=json");

		LOGGER.info("FinalUrl to fetch all stops for given route & direction : " + sb.toString());

		HttpResponse response = connection.get(sb.toString(), proxyEnable, proxyHost, proxyPort);

		if (response == null) {
			return null;
		}

		String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		LOGGER.info("Stop Response: " + responseString);

		Stop[] stops = JsonObjectMapper.convertJsonToObject(responseString, Stop[].class);

		LOGGER.info("Total Stops found " + stops.length);

		return stops;
	}

	/**
	 * @param routeid
	 * @return
	 * @throws IOException
	 * 
	 *             This method to call HTTP adapter component to fetch direction for
	 *             given inputs.
	 */
	public Direction[] getAllDirectionsForGivenRoute(String routeid) throws IOException {
		LOGGER.info("Fetching all Direction for routeid " + routeid);

		StringBuilder sb = new StringBuilder();
		sb.append(mtDirectionUrl).append("/").append(routeid).append("?format=json");

		LOGGER.info("FinalUrl to fetch all direction for given route : " + sb.toString());

		HttpResponse response = connection.get(sb.toString(), proxyEnable, proxyHost, proxyPort);

		if (response == null) {
			return null;
		}

		String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		LOGGER.info("Direction Response: " + responseString);

		Direction[] directions = JsonObjectMapper.convertJsonToObject(responseString, Direction[].class);

		LOGGER.info("Total directions found " + directions.length);

		return directions;
	}

	/**
	 * @return
	 * @throws IOException
	 * 
	 *             This method to call HTTP adapter component to fetch routes for
	 *             given inputs.
	 */
	public Route[] getAllRoutesFromMT() throws IOException {

		LOGGER.info("Fetching all routes");
		LOGGER.info("FinalUrl to fetch all routes : " + mtRoutesUrl);

		HttpResponse response = connection.get(mtRoutesUrl, proxyEnable, proxyHost, proxyPort);

		if (response == null) {
			return null;
		}

		String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		LOGGER.info("Route Response: " + responseString);

		Route[] routes = JsonObjectMapper.convertJsonToObject(responseString, Route[].class);

		LOGGER.info("Total routes found " + routes.length);

		return routes;
	}

	public String getMtRoutesUrl() {
		return mtRoutesUrl;
	}

	public void setMtRoutesUrl(String mtRoutesUrl) {
		this.mtRoutesUrl = mtRoutesUrl;
	}

	public String getMtDirectionUrl() {
		return mtDirectionUrl;
	}

	public void setMtDirectionUrl(String mtDirectionUrl) {
		this.mtDirectionUrl = mtDirectionUrl;
	}
}
