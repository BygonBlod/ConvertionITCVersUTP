package USP.Model;

import java.util.ArrayList;

public class StudentsUSP extends ArrayList<StudentUSP> {

	public StudentsUSP() {
		super();
	}

	public StudentsUSP getStudentsCourse(String idCours) {
		StudentsUSP res = new StudentsUSP();
		for (StudentUSP student : this) {
			for (String s : student.getCourses()) {
				if (s.equals(idCours)) {
					res.add(student);
				}
			}
		}
		return res;
	}

}
