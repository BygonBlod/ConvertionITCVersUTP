package ITC.Model;

import java.util.ArrayList;

public class TimesPenaltysITC extends ArrayList<TimesPenaltyITC> {

	public TimesPenaltysITC() {
		super();
	}

	public boolean containsTime4(String days, String weeks, String start, String lenght) {
		for (TimesPenaltyITC time : this) {
			if (time.getDays().equals(days) && time.getLength().equals(lenght) && time.getStart().equals(start)
					&& time.getWeeks().equals(weeks)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkAllWeeks() {
		for (TimesPenaltyITC time : this) {
			if (!time.checkWeeks()) {
				return false;
			}
		}
		return true;
	}

}
