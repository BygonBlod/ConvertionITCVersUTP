package ITC.Model;

import java.util.ArrayList;

public class CourseITC {
	private String id;
	private ArrayList<ConfigITC> config;

	public CourseITC(String id, ArrayList<ConfigITC> config) {
		this.id = id;
		this.config = config;
	}

	public boolean differentTimes4() {
		TimesPenaltysITC times = new TimesPenaltysITC();
		for (ConfigITC conf : config) {
			if (times.size() == 0) {
				times = conf.getAllTimes();
			} else {
				for (TimesPenaltyITC time : conf.getAllTimes()) {
					if (!times.containsTime4(time.getDays(), time.getWeeks(), time.getStart(), time.getLength())) {
						return true;
					}
				}
				for (TimesPenaltyITC time : times) {
					if (!conf.getAllTimes().containsTime4(time.getDays(), time.getWeeks(), time.getStart(),
							time.getLength())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<ConfigITC> getConfig() {
		return config;
	}

	public void setConfig(ArrayList<ConfigITC> config) {
		this.config = config;
	}
}
