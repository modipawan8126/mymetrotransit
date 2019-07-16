package metrotransit.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	/*
	 * public NextBusResponse getNextBusBetweenTwoStops(String source, String
	 * target, String direction) { NextBusResponse nextBusResponse = new
	 * NextBusResponse();
	 * 
	 * //Fetching all routes Route[] routeList = fetchRoutes();
	 * 
	 * //Find matching route for given user route name Route matchingRoute =
	 * getMatchingRouteCode(route, routeList);
	 * nextBusResponse.setRoute(matchingRoute); if (matchingRoute == null) {
	 * log.error("No Matching route found. Check input!!!"); return nextBusResponse;
	 * }
	 * 
	 * //now call direction api by passing route code to get direction id
	 * Direction[] directions = fetchDirections(matchingRoute); // Retrieve
	 * direction numberi value out of it. Direction matchingDirection =
	 * getMatchingDirection(direction, directions);
	 * nextBusResponse.setDirection(matchingDirection); if (matchingDirection ==
	 * null) { log.error("No Matching direction found. Check input!!!"); return
	 * nextBusResponse; }
	 * 
	 * //now call stop operation by passing route id and direction to get stop-id
	 * Stop[] stops = fetchStops(matchingRoute, matchingDirection); // retrieve 4
	 * digit stop id. Stop matchingStop = getMatchingStop(stop, stops);
	 * nextBusResponse.setStop(matchingStop); if (matchingStop == null) {
	 * log.error("No Matching stop found. Check input!!!"); return nextBusResponse;
	 * }
	 * 
	 * //Now call time point departure operation 6 by passing route, direction id &
	 * stopcode to get departure info. TimePointDeparture[] departures =
	 * fetchTimePointDepartures(matchingRoute, matchingDirection, matchingStop);
	 * //retrieve next bus time and return. TimePointDeparture matchingDeparture =
	 * getNextBusDeparture(departures);
	 * nextBusResponse.setDeparture(matchingDeparture); if (matchingDeparture ==
	 * null) { log.error("No next departure found for given input. Check input!!");
	 * return nextBusResponse; }
	 * 
	 * return nextBusResponse; }
	 */

	public NextBusResponse getNextBus(String route, String stop, String direction) {
		NextBusResponse nextBusResponse = new NextBusResponse();

		// Fetching all routes
		Route[] routeList = fetchRoutes();

		// Find matching route for given user route name
		Route matchingRoute = getMatchingRouteCode(route, routeList);
		nextBusResponse.setRoute(matchingRoute);
		if (matchingRoute == null) {
			log.error("No Matching route found. Check input!!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No Matching route found. Check input!!!");
			return nextBusResponse;
		}

		// now call direction api by passing route code to get direction id
		Direction[] directions = fetchDirections(matchingRoute);
		// Retrieve direction numberi value out of it.
		Direction matchingDirection = getMatchingDirection(direction, directions);
		nextBusResponse.setDirection(matchingDirection);
		if (matchingDirection == null) {
			log.error("No Matching direction found. Check input!!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No Matching direction found. Check input!!!");
			return nextBusResponse;
		}

		// now call stop operation by passing route id and direction to get stop-id
		Stop[] stops = fetchStops(matchingRoute, matchingDirection);
		// retrieve 4 digit stop id.
		Stop matchingStop = getMatchingStop(stop, stops);
		nextBusResponse.setStop(matchingStop);
		if (matchingStop == null) {
			log.error("No Matching stop found. Check input!!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No Matching stop found. Check input!!!");
			return nextBusResponse;
		}

		// Now call time point departure operation 6 by passing route, direction id &
		// stopcode to get departure info.
		TimePointDeparture[] departures = fetchTimePointDepartures(matchingRoute, matchingDirection, matchingStop);
		if (departures == null || departures.length == 0) {
			log.error("No departure found for given input. Check input!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No departure found for given input. Check input!!");
			return nextBusResponse;
		}

		// retrieve next bus time and return.
		getMinutesToNextBusDeparture(departures, nextBusResponse);
		if (nextBusResponse.getMinutesToNextBus() == 0) {
			log.error("No next departure found for given input. Check input!!");
			nextBusResponse.setStatus("FAILED");
			nextBusResponse.setFailureMessage("No next departure found for given input. Check input!!");
			return nextBusResponse;
		} else {
			nextBusResponse.setStatus("SUCCESS");			 
		}

		return nextBusResponse;
	}

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
		nextBusResponse.setMinutesToNextBus(minList.get(0));
		nextBusResponse.setNextBusScheduledTime(departures[0].getDepartureText());

	}

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

	private Stop[] fetchStops(Route route, Direction direction) {
		try {
			return adapter.getAllStopsForGivenRouteAndDirection(route.getRoute(), direction.getValue());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error Occured: " + e);
			return null;
		}
	}

	private Direction[] fetchDirections(Route route) {
		try {
			return adapter.getAllDirectionsForGivenRoute(route.getRoute());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error Occured: " + e);
			return null;
		}
	}

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
