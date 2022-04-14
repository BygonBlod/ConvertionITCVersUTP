package Convertion.VersITC;

import java.util.ArrayList;

import ITC.Model.CourseITC;
import ITC.Model.DistributionITC;
import ITC.Model.OptimizationITC;
import ITC.Model.ProblemITC;
import ITC.Model.RoomITC;
import ITC.Model.StudentITC;
import USP.Model.StudentUSP;
import USP.Model.Timetabling;

public class ConvertisseurITC {

	public static ProblemITC getProblem(Timetabling time) {
		ArrayList<RoomITC> rooms = new ArrayList<>();
		ArrayList<DistributionITC> distributions = new ArrayList<>();
		ArrayList<StudentITC> students = new ArrayList<>();
		ArrayList<CourseITC> courses = new ArrayList<>();
		OptimizationITC optimization = new OptimizationITC("", "", "", "");
		ProblemITC problem = new ProblemITC(time.getName(), time.getNrDaysPerWeek(), time.getNrSlotsPerDay(),
				time.getNrWeeks());

		students = convertionStudents(students, time.getStudents());
		problem.setCourses(courses);
		problem.setDistributions(distributions);
		problem.setOptimization(optimization);
		problem.setRooms(rooms);
		problem.setStudents(students);
		return problem;
	}

	private static ArrayList<StudentITC> convertionStudents(ArrayList<StudentITC> studentsItc,
			ArrayList<StudentUSP> studentsUsp) {
		for (StudentUSP stud : studentsUsp) {
			StudentITC student = new StudentITC(stud.getId(), stud.getCourses());
			studentsItc.add(student);
		}
		return studentsItc;
	}

}
