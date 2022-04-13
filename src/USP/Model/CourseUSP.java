package USP.Model;

import java.util.ArrayList;

public class CourseUSP {
	private String id;
	private ArrayList<PartUSP> parts;

	public CourseUSP(String id, ArrayList<PartUSP> parts) {
		this.id = id;
		this.parts = parts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<PartUSP> getParts() {
		return parts;
	}

	public void setParts(ArrayList<PartUSP> parts) {
		this.parts = parts;
	}

}
