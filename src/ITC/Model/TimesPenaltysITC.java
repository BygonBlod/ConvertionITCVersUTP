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

	public boolean containsTimePen(String days, String penalty, String start, String lenght) {
		for (TimesPenaltyITC time : this) {
			if (time.getDays().equals(days) && time.getLength().equals(lenght) && time.getStart().equals(start)
					&& time.getPenalty().equals(penalty)) {
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

	public void join(int nbSesPerWeek) {
		ArrayList<TimesPenaltysITC> times = new ArrayList<>();
		while (this.size() > 0) {
			TimesPenaltyITC time = this.get(0);
			if (times.size() == 0) {
				TimesPenaltysITC temp = new TimesPenaltysITC();
				temp.add(time);
				times.add(temp);
			} else {
				boolean contain = false;
				for (TimesPenaltysITC timesO : times) {
					if (timesO.containsTimePen(time.getDays(), time.getPenalty(), time.getStart(), time.getLength())) {
						timesO.add(time);
						contain = true;
					}
				}
				if (!contain) {
					TimesPenaltysITC timesO2 = new TimesPenaltysITC();
					timesO2.add(time);
					times.add(timesO2);
				}
			}
			this.remove(time);
		}
		addDifferentTimes(times, nbSesPerWeek);

	}

	private void addDifferentTimes(ArrayList<TimesPenaltysITC> times, int nbSesPerWeek) {
		ArrayList<String> days = new ArrayList<>();
		ArrayList<String> weeks = new ArrayList<>();
		for (TimesPenaltysITC timesO : times) {
			days.add(timesO.get(0).getDays());
			weeks.add(timesO.get(0).getWeeks());
			for (int i = 1; i < timesO.size(); i++) {
				addWeeks(weeks, timesO.get(i).getWeeks(), 14);
				addDays(days, timesO.get(i).getDays(), nbSesPerWeek);

			}
		}
		// double boucle for pour créer les times
	}

	private void addDays(ArrayList<String> days, String days2, int nbSesPerWeek) {

	}

	public String addWeeks(ArrayList<String> weeks2, String weeks, int nbWeeks) {
		String weeksThis = weeks2.get(0);
		for (int i = 0; i < weeksThis.length(); i++) {
			if (weeks.charAt(i) == '1') {
				weeksThis = weeksThis.substring(0, i) + '1' + weeksThis.substring(i + 1);
			}
		}
		return weeksThis;

	}

}
