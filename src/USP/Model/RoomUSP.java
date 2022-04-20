package USP.Model;

public class RoomUSP {
	private String id;
	private String capacity;
	private String label;

	public RoomUSP(String id, String capacity, String label) {
		this.id = id;
		this.capacity = capacity;
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
