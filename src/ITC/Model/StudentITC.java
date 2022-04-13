package ITC.Model;

import java.util.ArrayList;

public class StudentITC {
	private String id;
	private ArrayList<String> courses;

	public StudentITC(String id, ArrayList<String> courses) {
		this.id = id;
		this.courses = courses;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<String> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<String> courses) {
		this.courses = courses;
	}

}
