package ITC.Model;

import java.util.ArrayList;

public class SubpartITC {
	private String id;
	ArrayList<ClassITC> clas;

	public SubpartITC(String id, ArrayList<ClassITC> clas) {
		this.id = id;
		this.clas = clas;
	}

	public TimesPenaltysITC getAllTimes() {
		TimesPenaltysITC res = new TimesPenaltysITC();
		for (ClassITC classe : clas) {
			for (TimesPenaltyITC time : classe.getTimes()) {
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

	public ArrayList<ClassITC> getClas() {
		return clas;
	}

	public void setClas(ArrayList<ClassITC> clas) {
		this.clas = clas;
	}

}
