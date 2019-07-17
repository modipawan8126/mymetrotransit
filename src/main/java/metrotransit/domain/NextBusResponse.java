package metrotransit.domain;

import java.util.List;

public class NextBusResponse {

	String minutesToNextBus;
	String nextBusScheduledTime;
	String currentTime;
	String status;
	String failureMessage;
	Route route;
	Direction direction;
	Stop stop;
	List<TimePointDeparture> departures;

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Stop getStop() {
		return stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public List<TimePointDeparture> getDepartures() {
		return departures;
	}

	public void setDepartures(List<TimePointDeparture> departures) {
		this.departures = departures;
	}

	 

	public String getNextBusScheduledTime() {
		return nextBusScheduledTime;
	}

	public void setNextBusScheduledTime(String nextBusScheduledTime) {
		this.nextBusScheduledTime = nextBusScheduledTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public String getMinutesToNextBus() {
		return minutesToNextBus;
	}

	public void setMinutesToNextBus(String minutesToNextBus) {
		this.minutesToNextBus = minutesToNextBus;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

}
