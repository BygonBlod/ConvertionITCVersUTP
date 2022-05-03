package ITC.Model;

public class TimesITC {
	private String days;
	private String start;
	private String length;
	private String weeks;

	public TimesITC(String days, String start, String length, String weeks) {
		this.days = days;
		if (start.contentEquals("")) {
			this.start = "0";
		} else {
			this.start = start;
		}
		if (length.contentEquals("")) {
			this.length = "0";
		} else {
			this.length = length;
		}
		this.weeks = weeks;
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
