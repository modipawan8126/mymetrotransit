package metrotransit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import metrotransit.domain.Route;
import metrotransit.http.FoundationForMT;



@Service
public class NextBusService {

	@Autowired
	FoundationForMT adapter;
	
	public String getNextBus(String route, String stop, String direction) {
		String nextBusTime = null;
		
		
		return nextBusTime;
	}
	
	
	private List<Route> fetchRoutes() {
		return adapter.getAllRoutesFromMT();
	}
}
