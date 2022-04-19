package ITC.Model;

public class ClassRoomITC {
	private String id;
	private String penalty;

	public ClassRoomITC(String id, String penalty) {
		this.id = id;
		this.penalty = penalty;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPenalty() {
		return penalty;
	}

	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}

}
