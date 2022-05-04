package USP.Model;

import java.util.ArrayList;

public class StudentUSP {
	private String id;
	private String label;
	private ArrayList<String> courses;

	public StudentUSP(String id, String label, ArrayList<String> courses) {
		this.id = id;
		this.label = label;
		this.courses = courses;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ArrayList<String> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<String> courses) {
		this.courses = courses;
	}

	public void changeCourse(String id2, int i) {
		courses.set(courses.indexOf(id2), id2 + "-" + i);

	}

}
