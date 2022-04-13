package USP.Model;

import java.util.ArrayList;

import Utils.Pair;

public class AllowedTeachersUSP {
	private String sessionTeachers;
	private ArrayList<Pair<String, String>> teacher;

	public AllowedTeachersUSP(String sessionTeachers, ArrayList<Pair<String, String>> teacher) {
		this.sessionTeachers = sessionTeachers;
		this.teacher = teacher;
	}

	public String getSessionTeachers() {
		return sessionTeachers;
	}

	public void setSessionTeachers(String sessionTeachers) {
		this.sessionTeachers = sessionTeachers;
	}

	public ArrayList<Pair<String, String>> getTeacher() {
		return teacher;
	}

	public void setTeacher(ArrayList<Pair<String, String>> teacher) {
		this.teacher = teacher;
	}

}
