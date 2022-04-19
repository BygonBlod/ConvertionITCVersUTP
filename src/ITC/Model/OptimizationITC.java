package ITC.Model;

public class OptimizationITC {
	private String time;
	private String room;
	private String distribution;
	private String student;

	public OptimizationITC(String time, String room, String distribution, String student) {
		this.time = time;
		this.room = room;
		this.distribution = distribution;
		this.student = student;
	}

	public OptimizationITC() {
		this.time = "";
		this.room = "";
		this.distribution = "";
		this.student = "";
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

}
