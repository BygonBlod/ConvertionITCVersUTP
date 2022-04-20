package USP.Model;

public class FilterUSP {
	private String type;
	private String attributeName;
	private String in;
	private String notIn;

	public FilterUSP(String type, String attributeName) {
		this.type = type;
		this.attributeName = attributeName;
		this.in = "";
		this.notIn = "";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getIn() {
		return in;
	}

	public void setIn(String in) {
		this.in = in;
	}

	public String getNotIn() {
		return notIn;
	}

	public void setNotIn(String notin) {
		this.notIn = notin;
	}

}
