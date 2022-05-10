package ITC.Model;

import java.util.ArrayList;

import USP.Model.FilterUSP;
import USP.Model.SessionRuleUSP;
import USP.Model.SessionsRuleUSP;

public class DistributionITC {
	private String type;
	private String required;
	private String penalty;
	private ArrayList<String> classId;

	public DistributionITC(String type, ArrayList<String> classId) {
		this.type = type;
		this.classId = classId;
		this.required = "";
		this.penalty = "";
	}

	public String getClassIDString() {
		String s = "";
		for (String classe : getClassId()) {
			s += classe + ",";
		}
		if (s.length() > 1) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	public SessionsRuleUSP getSessionIdClass() {
		SessionsRuleUSP res = new SessionsRuleUSP();
		ArrayList<FilterUSP> filters = new ArrayList<>();
		for (String classe : getClassId()) {
			SessionRuleUSP session = new SessionRuleUSP("class", filters);
			session.setAttributeName("id");
			session.setIn(classe);
			res.add(session);
		}
		return res;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPenalty() {
		return penalty;
	}

	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public ArrayList<String> getClassId() {
		return classId;
	}

	public void setClassId(ArrayList<String> classId) {
		this.classId = classId;
	}

}
