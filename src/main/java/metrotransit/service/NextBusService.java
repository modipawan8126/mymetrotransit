package metrotransit.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import metrotransit.domain.Direction;
import metrotransit.domain.NextBusResponse;
import metrotransit.domain.Route;
import metrotransit.domain.Stop;
import metrotransit.domain.TimePointDeparture;
import metrotransit.http.FoundationForMT;
import metrotransit.util.DateUtil;

@Service
public class NextBusService {

	private static final Logger log = LoggerFactory.getLogger(NextBusService.class.getSimpleName());

	@Autowired
	FoundationForMT adapter;
	
	@Value("${mt.dateformat}")
	String dateFormat;
	
	@Value("${mt.timezone}")
	String timezone;

	/**
	 * @param route
	 * @param stop
	 * @param direction
	 * @return
	 * 
	 * 		This is api method to get next bus json resposne with minutes to next
	 *         bus with exact scheduled time of departure. Along with route, stop,
	 *         direction & departures information for given inputs.
	 * 
	 * 
	 *         This business layer method communicate with http adapter to fetch
	 *         data from metrotrasit apis. This class filter & process data to get
	 *         desired output.
	 */
	public NextBusResponse getNextBus(String route, String stop, String direction) {
		NextBusResponse nextBusResponse = new NextBusResponse();

		// 1. Fetching all routes
		Route[] routeList = fetchRoutes();

		// 2. Retrieve matching route for given user input route name
		Route matchingRoute = getMatchingRouteCode(route, routeList);
		nextBusResponse.setRoute(matchingRoute);
		if (matchingRoute == null) {
			log.error("No Matching route found. Check input!!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No Matching route found. Check input!!!");
			return nextBusResponse;
		}

		// 3. Now calling direction api by passing route code to get direction id
		Direction[] directions = fetchDirections(matchingRoute);

		// 4. Retrieve matching direction value out of it.
		Direction matchingDirection = getMatchingDirection(direction, directions);
		nextBusResponse.setDirection(matchingDirection);
		if (matchingDirection == null) {
			log.error("No Matching direction found. Check input!!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No Matching direction found. Check input!!!");
			return nextBusResponse;
		}

		// 5. Now calling stop api by passing route-id and direction to get stop-code
		Stop[] stops = fetchStops(matchingRoute, matchingDirection);

		// 6. Retrieve matching stop as per user input.
		Stop matchingStop = getMatchingStop(stop, stops);
		nextBusResponse.setStop(matchingStop);
		if (matchingStop == null) {
			log.error("No Matching stop found. Check input!!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No Matching stop found. Check input!!!");
			return nextBusResponse;
		}

		// 7. Now calling Time-point departure operation by passing route, direction &
		// stop code to get departures info.
		TimePointDeparture[] departures = fetchTimePointDepartures(matchingRoute, matchingDirection, matchingStop);
		if (departures == null || departures.length == 0) {
			log.error("No departure found for given input. Check input!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No departure found for given input. Check input!!");
			return nextBusResponse;
		}

		// 8. Retrieve next bus time for given inputs.
		getMinutesToNextBusDeparture(departures, nextBusResponse);
		if (nextBusResponse.getMinutesToNextBus() == null || nextBusResponse.getMinutesToNextBus().length() == 0) {
			log.error("No next departure found for given input. Check input!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No next departure found for given input. Check input!!");
			return nextBusResponse;
		} else {
			nextBusResponse.setStatus("SUCCESS");
		}

		log.info(timezone + "|" + dateFormat);
		String currentTime = DateUtil.getCurrentDateTime(timezone, dateFormat);
		log.info("Current Time: " + currentTime);
		nextBusResponse.setCurrentTime(currentTime);
		
		return nextBusResponse;
	}

	/**
	 * @param departures
	 * @param nextBusResponse
	 *            This method to process & get minutes to next bus.
	 */
	private void getMinutesToNextBusDeparture(TimePointDeparture[] departures, NextBusResponse nextBusResponse) {

		List<Long> minList = new ArrayList<Long>();
		for (TimePointDeparture s : departures) {
			Long l = DateUtil.getTimeToNextBus1(s.getDepartureTime());
			minList.add(l);
			if (nextBusResponse.getDepartures() == null) {
				List<TimePointDeparture> list = new ArrayList<TimePointDeparture>();
				list.add(s);
				nextBusResponse.setDepartures(list);
			} else {
				nextBusResponse.getDepartures().add(s);
			}
			log.info("Departure: " + s.getDepartureText() + "|" + s.getDepartureTime() + "|" + l);
		}

		Collections.sort(minList);
		if (minList.size() != 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(minList.get(0)).append(" Minutes");
			nextBusResponse.setMinutesToNextBus(sb.toString());
		}
		nextBusResponse.setNextBusScheduledTime(departures[0].getDepartureText());

	}

	/**
	 * @param stop
	 * @param stops
	 * @return
	 * 
	 * 		This method is to get matching stop from given list of stop.
	 */
	private Stop getMatchingStop(String stop, Stop[] stops) {
		Stop matchingStop = null;
		for (Stop s : stops) {
			if (s.getText().contains(stop)) {
				matchingStop = s;
				log.info("Found Matching Stop " + s.getText() + "|" + s.getValue());
				break;
			}
		}

		return matchingStop;
	}

	/**
	 * @param direction
	 * @param directions
	 * @return
	 * 
	 * 		This method is to get direction from given list of directions.
	 */
	private Direction getMatchingDirection(String direction, Direction[] directions) {
		Direction matchingDirection = null;
		for (Direction d : directions) {
			if (d.getText().contains(direction.toUpperCase())) {
				matchingDirection = d;
				log.info("Found Matching Direction " + d.getText() + "|" + d.getValue());
				break;
			}
		}

		return matchingDirection;
	}

	/**
	 * @param routeName
	 * @param routeList
	 * @return
	 * 
	 * 		This method is to get matching route from given list of routes.
	 */
	private Route getMatchingRouteCode(String routeName, Route[] routeList) {
		Route matchingRoute = null;
		for (Route route : routeList) {
			if (route.getDescription().contains(routeName)) {
				matchingRoute = route;
				log.info("Found Matching Route " + route.getDescription() + "|" + route.getRoute());
				break;
			}
		}

		return matchingRoute;
	}

	/**
	 * @param route
	 * @param direction
	 * @param stop
	 * @return
	 * 
	 * 		This method to call HTTP adapter component to fetch departures for
	 *         given inputs.
	 */
	private TimePointDeparture[] fetchTimePointDepartures(Route route, Direction direction, Stop stop) {
		try {
			return adapter.getAllTimePointDepartureForGivenRouteDirectionStop(route.getRoute(), direction.getValue(),
					stop.getValue());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error Occured: " + e);
			return null;
		}
	}

	/**
	 * @param route
	 * @param direction
	 * @return
	 * 
	 * 		This method to call HTTP adapter component to fetch stops for given
	 *         inputs.
	 */
	private Stop[] fetchStops(Route route, Direction direction) {
		try {
			return adapter.getAllStopsForGivenRouteAndDirection(route.getRoute(), direction.getValue());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error Occured: " + e);
			return null;
		}
	}

	/**
	 * @param route
	 * @return
	 * 
	 * 		This method to call HTTP adapter component to fetch directions for
	 *         given inputs.
	 */
	private Direction[] fetchDirections(Route route) {
		try {
			return adapter.getAllDirectionsForGivenRoute(route.getRoute());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error Occured: " + e);
			return null;
		}
	}

	/**
	 * @return
	 * 
	 * 		This method to call HTTP adapter component to fetch all routes.
	 */
	private Route[] fetchRoutes() {
		try {
			return adapter.getAllRoutesFromMT();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error Occured: " + e);
			return null;
		}
	}
}
