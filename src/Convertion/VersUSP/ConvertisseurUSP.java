package Convertion.VersUSP;

import java.util.ArrayList;

import ITC.Model.ClassITC;
import ITC.Model.ConfigITC;
import ITC.Model.CourseITC;
import ITC.Model.ProblemITC;
import ITC.Model.RoomITC;
import ITC.Model.StudentITC;
import ITC.Model.SubpartITC;
import USP.Model.AllowedRoomsUSP;
import USP.Model.AllowedSlotsUSP;
import USP.Model.AllowedTeachersUSP;
import USP.Model.ClassUSP;
import USP.Model.CourseUSP;
import USP.Model.PartUSP;
import USP.Model.RoomUSP;
import USP.Model.RuleUSP;
import USP.Model.SolutionUSP;
import USP.Model.StudentUSP;
import USP.Model.TeacherUSP;
import USP.Model.Timetabling;

public class ConvertisseurUSP {

	public static Timetabling getTimeTabling(ProblemITC problem) {
		ArrayList<RoomUSP> rooms = new ArrayList<>();
		ArrayList<TeacherUSP> teachers = new ArrayList<>();
		ArrayList<CourseUSP> courses = new ArrayList<>();
		ArrayList<StudentUSP> students = new ArrayList<>();
		ArrayList<RuleUSP> rules = new ArrayList<>();
		SolutionUSP solution;

		students = convertionStudents(students, problem.getStudents());
		rooms = convertionRooms(rooms, problem.getRooms());
		courses = convertionCourses(courses, problem.getCourses());
		solution = new SolutionUSP();

		Timetabling time = new Timetabling(problem.getName(), problem.getNrWeeks(), problem.getNrDays(),
				problem.getSlotsPerDay());
		time.setCourses(courses);
		time.setRooms(rooms);
		time.setRules(rules);
		time.setSolution(solution);
		time.setStudents(students);
		time.setTeachers(teachers);
		return time;
	}

	private static ArrayList<CourseUSP> convertionCourses(ArrayList<CourseUSP> coursesUsp,
			ArrayList<CourseITC> coursesItc) {
		for (CourseITC course : coursesItc) {
			ArrayList<PartUSP> parts = new ArrayList<>();
			System.out.println(course.getConfig().size());
			for (ConfigITC conf : course.getConfig()) {
				for (SubpartITC sub : conf.getSubpart()) {
					ArrayList<ClassUSP> classes = new ArrayList<>();
					for (ClassITC clas : sub.getClas()) {
						ClassUSP clasU = new ClassUSP(clas.getId(), "");
						clasU.setParent(clas.getParent());
						classes.add(clasU);
					}
					System.out.println("part " + classes.size());
					PartUSP part = new PartUSP(sub.getId(), "", "", classes, new AllowedSlotsUSP("", "", "", ""),
							new AllowedRoomsUSP("", new ArrayList<>()), new AllowedTeachersUSP("", new ArrayList<>()));
					parts.add(part);
				}
			}
			CourseUSP courseU = new CourseUSP(course.getId(), parts);
			coursesUsp.add(courseU);
		}
		return coursesUsp;
	}

	private static ArrayList<RoomUSP> convertionRooms(ArrayList<RoomUSP> roomsUsp, ArrayList<RoomITC> roomsItc) {
		for (RoomITC roomI : roomsItc) {
			RoomUSP room = new RoomUSP(roomI.getId(), roomI.getCapacity(), "");
			roomsUsp.add(room);
		}
		return roomsUsp;
	}

	private static ArrayList<StudentUSP> convertionStudents(ArrayList<StudentUSP> studentsUsp,
			ArrayList<StudentITC> studentsItc) {
		for (StudentITC stud : studentsItc) {
			StudentUSP student = new StudentUSP(stud.getId(), "", stud.getCourses());
			studentsUsp.add(student);
		}
		return studentsUsp;
	}

}
