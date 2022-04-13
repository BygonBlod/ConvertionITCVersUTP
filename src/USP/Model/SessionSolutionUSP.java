package USP.Model;

public class SessionSolutionUSP {
	private String classe;
	private String rank;
	private String slot;
	private String rooms;
	private String teachers;

	public SessionSolutionUSP(String classe, String rank, String slot, String rooms, String teachers) {
		this.classe = classe;
		this.rank = rank;
		this.slot = slot;
		this.rooms = rooms;
		this.teachers = teachers;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getRooms() {
		return rooms;
	}

	public void setRooms(String rooms) {
		this.rooms = rooms;
	}

	public String getTeachers() {
		return teachers;
	}

	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}

}
