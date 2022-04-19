package ITC.Model;

public class TravelITC {
	private String room;
	private String value;

	public TravelITC(String room, String value) {
		this.room = room;
		this.value = value;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
