package ITC.Model;

import java.util.ArrayList;

public class DistributionITC {
	private String type;
	private String required;
	private String penalty;
	private ArrayList<String> classId;

	public DistributionITC(String type, ArrayList<String> classId) {
		this.type = type;
		this.classId = classId;
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
