package USP.Model;

import java.util.ArrayList;

public class AllowedSlotsUSP {
	private String sessionLength;
	private String dailySlots;
	private String days;
	private String weeks;

	public AllowedSlotsUSP(String sessionLength, String dailySlots, String days, String weeks) {
		this.sessionLength = sessionLength;
		this.dailySlots = dailySlots;
		this.days = days;
		this.weeks = weeks;
	}

	public String getSessionLength() {
		return sessionLength;
	}

	public String getLengthITC() {
		String s = "";
		int i = Integer.parseInt(getSessionLength()) / 5;
		return s + i;
	}

	public ArrayList<String> getDailySlotsITC() {
		ArrayList<String> s = new ArrayList<>();
		String[] daily = getDailySlots().split(",");
		for (int i = 0; i < daily.length; i++) {
			int value = Integer.parseInt(daily[i]) / 5;
			s.add(value + "");
		}
		return s;
	}

	public void setSessionLength(String sessionLength) {
		this.sessionLength = sessionLength;
	}

	public String getDailySlots() {
		return dailySlots;
	}

	public void setDailySlots(String dailySlots) {
		this.dailySlots = dailySlots;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

}
