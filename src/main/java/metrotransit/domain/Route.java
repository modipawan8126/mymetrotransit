package metrotransit.domain;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Route {

	@JsonProperty("Route")
	private String Route;
	
	@JsonProperty("Description")
	private String Description;
	
	@JsonProperty("ProviderID")
	private String ProviderID;

	public String getRoute() {
		return Route;
	}

	public void setRoute(String route) {
		Route = route;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getProviderID() {
		return ProviderID;
	}

	public void setProviderID(String providerID) {
		ProviderID = providerID;
	}

	 
	
	/*private String ProviderID;

    private String Description;

    private String Route;

    public String getProviderID ()
    {
        return ProviderID;
    }

    public void setProviderID (String ProviderID)
    {
        this.ProviderID = ProviderID;
    }

    public String getDescription ()
    {
        return Description;
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
    }

    public String getRoute ()
    {
        return Route;
    }

    public void setRoute (String Route)
    {
        this.Route = Route;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ProviderID = "+ProviderID+", Description = "+Description+", Route = "+Route+"]";
    }*/
	
}
