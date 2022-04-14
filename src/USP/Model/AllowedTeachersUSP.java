package USP.Model;

import java.util.ArrayList;

public class AllowedTeachersUSP {
	private String sessionTeachers;
	private ArrayList<AllowedTeacher> teacher;

	public AllowedTeachersUSP(String sessionTeachers, ArrayList<AllowedTeacher> teacher) {
		this.sessionTeachers = sessionTeachers;
		this.teacher = teacher;
	}

	public String getSessionTeachers() {
		return sessionTeachers;
	}

	public void setSessionTeachers(String sessionTeachers) {
		this.sessionTeachers = sessionTeachers;
	}

	public ArrayList<AllowedTeacher> getTeacher() {
		return teacher;
	}

	public void setTeacher(ArrayList<AllowedTeacher> teacher) {
		this.teacher = teacher;
	}

}
