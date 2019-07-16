package metrotransit.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class TimePointDeparture {

	@JsonProperty("VehicleLongitude")
	private String VehicleLongitude;

	@JsonProperty("DepartureText")
	private String DepartureText;

	@JsonProperty("DepartureTime")
	private String DepartureTime;

	@JsonProperty("RouteDirection")
	private String RouteDirection;

	@JsonProperty("Description")
	private String Description;

	@JsonProperty("VehicleLatitude")
	private String VehicleLatitude;

	@JsonProperty("Actual")
	private String Actual;

	@JsonProperty("Gate")
	private String Gate;

	@JsonProperty("BlockNumber")
	private String BlockNumber;

	@JsonProperty("Terminal")
	private String Terminal;

	@JsonProperty("Route")
	private String Route;

	@JsonProperty("VehicleHeading")
	private String VehicleHeading;

	public String getVehicleLongitude() {
		return VehicleLongitude;
	}

	public void setVehicleLongitude(String VehicleLongitude) {
		this.VehicleLongitude = VehicleLongitude;
	}

	public String getDepartureText() {
		return DepartureText;
	}

	public void setDepartureText(String DepartureText) {
		this.DepartureText = DepartureText;
	}

	public String getDepartureTime() {
		return DepartureTime;
	}

	public void setDepartureTime(String DepartureTime) {
		this.DepartureTime = DepartureTime;
	}

	public String getRouteDirection() {
		return RouteDirection;
	}

	public void setRouteDirection(String RouteDirection) {
		this.RouteDirection = RouteDirection;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public String getVehicleLatitude() {
		return VehicleLatitude;
	}

	public void setVehicleLatitude(String VehicleLatitude) {
		this.VehicleLatitude = VehicleLatitude;
	}

	public String getActual() {
		return Actual;
	}

	public void setActual(String Actual) {
		this.Actual = Actual;
	}

	public String getGate() {
		return Gate;
	}

	public void setGate(String Gate) {
		this.Gate = Gate;
	}

	public String getBlockNumber() {
		return BlockNumber;
	}

	public void setBlockNumber(String BlockNumber) {
		this.BlockNumber = BlockNumber;
	}

	public String getTerminal() {
		return Terminal;
	}

	public void setTerminal(String Terminal) {
		this.Terminal = Terminal;
	}

	public String getRoute() {
		return Route;
	}

	public void setRoute(String Route) {
		this.Route = Route;
	}

	public String getVehicleHeading() {
		return VehicleHeading;
	}

	public void setVehicleHeading(String VehicleHeading) {
		this.VehicleHeading = VehicleHeading;
	}

	@Override
	public String toString() {
		return "ClassPojo [VehicleLongitude = " + VehicleLongitude + ", DepartureText = " + DepartureText
				+ ", DepartureTime = " + DepartureTime + ", RouteDirection = " + RouteDirection + ", Description = "
				+ Description + ", VehicleLatitude = " + VehicleLatitude + ", Actual = " + Actual + ", Gate = " + Gate
				+ ", BlockNumber = " + BlockNumber + ", Terminal = " + Terminal + ", Route = " + Route
				+ ", VehicleHeading = " + VehicleHeading + "]";
	}
}
