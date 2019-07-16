package metrotransit.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class Direction {

	@JsonProperty("Text")
	private String Text;
	
	@JsonProperty("Value")
	private String Value;

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}
	
	 
	
}
