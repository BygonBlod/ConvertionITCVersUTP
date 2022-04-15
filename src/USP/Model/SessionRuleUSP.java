package USP.Model;

import java.util.ArrayList;

public class SessionRuleUSP {
	private String groupBy;
	private String sessionsMask;
	private String attributeName;
	private String in;
	private String notIn;
	private ArrayList<FilterUSP> filter;

	public SessionRuleUSP(String groupBy, ArrayList<FilterUSP> filter) {
		this.groupBy = groupBy;
		this.filter = filter;
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

	public void setNotIn(String notIn) {
		this.notIn = notIn;
	}

	public String getSessionsMask() {
		return sessionsMask;
	}

	public void setSessionsMask(String sessionsMask) {
		this.sessionsMask = sessionsMask;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public ArrayList<FilterUSP> getFilter() {
		return filter;
	}

	public void setFilter(ArrayList<FilterUSP> filter) {
		this.filter = filter;
	}

}
