package metrotransit.domain;

public class Route {

	private String Description;	               
	private int ProviderID;
	private int Route;
	
	public Route() {
		
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getProviderID() {
		return ProviderID;
	}
	public void setProviderID(int providerID) {
		ProviderID = providerID;
	}
	public int getRoute() {
		return Route;
	}
	public void setRoute(int route) {
		Route = route;
	}
	
	
}
