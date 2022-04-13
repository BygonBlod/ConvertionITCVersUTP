package USP.Model;

import java.util.ArrayList;

public class GroupSolutionUSP {
	private String id;
	private String headCount;
	private ArrayList<String> students;
	private ArrayList<String> classes;

	public GroupSolutionUSP(String id, String headCount, ArrayList<String> students, ArrayList<String> classes) {
		this.id = id;
		this.headCount = headCount;
		this.students = students;
		this.classes = classes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeadCount() {
		return headCount;
	}

	public void setHeadCount(String headCount) {
		this.headCount = headCount;
	}

	public ArrayList<String> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<String> students) {
		this.students = students;
	}

	public ArrayList<String> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<String> classes) {
		this.classes = classes;
	}
}
