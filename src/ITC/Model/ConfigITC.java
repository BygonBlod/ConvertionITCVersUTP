package ITC.Model;

import java.util.ArrayList;

public class ConfigITC {
	private String id;
	private ArrayList<SubpartITC> subpart;

	public ConfigITC(String id, ArrayList<SubpartITC> subpart) {
		this.id = id;
		this.subpart = subpart;
	}

	public TimesPenaltysITC getAllTimes() {
		TimesPenaltysITC res = new TimesPenaltysITC();
		for (SubpartITC sub : subpart) {
			for (TimesPenaltyITC time : sub.getAllTimes()) {
				if (!res.containsTime4(time.getDays(), time.getWeeks(), time.getStart(), time.getLength())) {
					res.add(time);
				}
			}
		}
		return res;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<SubpartITC> getSubpart() {
		return subpart;
	}

	public void setSubpart(ArrayList<SubpartITC> subpart) {
		this.subpart = subpart;
	}

}
