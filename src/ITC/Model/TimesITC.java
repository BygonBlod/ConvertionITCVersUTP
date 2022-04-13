package ITC.Model;

public class TimesITC {
	private String days;
	private String start;
	private String length;
	private String weeks;

	public TimesITC(String days, String start, String length, String weeks) {
		this.days = days;
		this.start = start;
		this.length = length;
		this.weeks = weeks;
	}

	public String toString() {
		String s = "";
		s += "days: " + days;
		s += " weeks: " + weeks;
		s += " start:" + start;
		s += " length:" + length;
		s += "\n";
		return s;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}
}
