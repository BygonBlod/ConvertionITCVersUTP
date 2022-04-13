package ITC.Model;

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

}
