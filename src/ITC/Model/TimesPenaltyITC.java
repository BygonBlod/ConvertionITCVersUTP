package ITC.Model;

import java.util.ArrayList;

public class TimesPenaltyITC extends TimesITC {
	private String penalty;

	public TimesPenaltyITC(String days, String start, String length, String weeks, String penalty) {
		super(days, start, length, weeks);
		this.penalty = penalty;
	}

	public String getPenalty() {
		return penalty;
	}

	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}

	public String getLengthUSP() {
		String s = "";
		int i = Integer.parseInt(getLength()) * 5;
		return s + i;
	}

	public String getStartUSP() {
		String s = "";
		int i = Integer.parseInt(getStart()) * 5;
		return s + i;
	}

	public String getDaysUSP() {
		String s = "";
		String days = getDays();
		for (int i = 0; i < days.length(); i++) {
			char c = days.charAt(i);
			if (c == '1') {
				s += (i + 1) + ",";
			}
		}
		if (s.length() > 0) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	public boolean checkWeeks() {
		String weeks = getWeeks();
		for (int i = 1; i < weeks.length() - 1; i++) {
			char c1 = weeks.charAt(i - 1);
			char c2 = weeks.charAt(i);
			char c3 = weeks.charAt(i + 1);
			if (c1 == '1' && c3 == '1' && c2 != '1') {
				return false;
			}

		}
		return true;
	}

	public int getFirstWeeks() {
		String weeks = getWeeks();
		for (int i = 0; i < weeks.length(); i++) {
			char c = weeks.charAt(i);
			if (c == '1') {
				return i + 1;
			}
		}
		return -1;
	}

	public int getLastWeeks() {
		int max = 0;
		String weeks = getWeeks();
		for (int i = 0; i < weeks.length(); i++) {
			char c = weeks.charAt(i);
			if (c == '1') {
				max = i + 1;
			}
		}
		return max;
	}

	public String toString() {
		String s = "";
		s += "days =" + getDays();
		s += " weeks =" + getWeeks();
		return s;
	}

	public ArrayList<Integer> getWeeksUSP() {
		ArrayList<Integer> res = new ArrayList<>();
		String weeks = getWeeks();
		for (int i = 0; i < weeks.length(); i++) {
			if (weeks.charAt(i) == '1') {
				res.add(i + 1);
			}
		}
		return res;
	}

}
