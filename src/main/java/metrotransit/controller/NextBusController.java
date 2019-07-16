package metrotransit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import metrotransit.domain.NextBusResponse;
import metrotransit.service.NextBusService;

/**
 * @author pawan Modi
 *
 */
@RestController
public class NextBusController {

	private static final Logger log = LoggerFactory.getLogger(NextBusController.class.getSimpleName());

	@Autowired
	NextBusService nextBusService;

	/**
	 * @param route
	 * @param stop
	 * @param direction
	 * @return
	 * 
	 * 		This is api method to get next bus json resposne with minutes to next
	 *         bus with exact scheduled time of departure. Along with route, stop,
	 *         direction & departures information for given inputs.
	 */
	@GetMapping(value = "/nextbus/{route}/{stop}/{direction}")  
	public NextBusResponse getNextBusForGivenRouteStopDirection(@PathVariable String route, @PathVariable String stop,
			@PathVariable String direction) {
		log.info("######### Next Bust Request: " + route + "|" + stop + "|" + direction);
		return nextBusService.getNextBus(route, stop, direction);
	}

}
