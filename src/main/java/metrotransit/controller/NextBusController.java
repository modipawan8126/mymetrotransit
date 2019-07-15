package metrotransit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import metrotransit.service.NextBusService;

@RestController
public class NextBusController {

	private static final Logger log = LoggerFactory.getLogger(NextBusController.class.getSimpleName());
	
	@Autowired
	NextBusService nextBusService;
	
	@GetMapping(value = "/nextbus/{route}/{stop}/{direction}") // Here route is route number, stop is 4 char stop code, direction is string direction
	public String getNextBus(@PathVariable String route, @PathVariable String stop, @PathVariable String direction) {
		log.info("######### Next Bust Request: " + route + "|" + stop + "|" + direction);
		return nextBusService.getNextBus(route, stop, direction);
	}
}
