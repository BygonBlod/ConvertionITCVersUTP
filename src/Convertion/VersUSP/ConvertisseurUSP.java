package Convertion.VersUSP;

import java.util.ArrayList;

import ITC.Model.ClassITC;
import ITC.Model.ClassRoomITC;
import ITC.Model.ConfigITC;
import ITC.Model.CourseITC;
import ITC.Model.DistributionITC;
import ITC.Model.ProblemITC;
import ITC.Model.RoomITC;
import ITC.Model.StudentITC;
import ITC.Model.SubpartITC;
import USP.Model.AllowedRoomUSP;
import USP.Model.AllowedRoomsUSP;
import USP.Model.AllowedSlotsUSP;
import USP.Model.AllowedTeachersUSP;
import USP.Model.ClassUSP;
import USP.Model.ConstraintUSP;
import USP.Model.ConstraintsUSP;
import USP.Model.CourseUSP;
import USP.Model.FilterUSP;
import USP.Model.PartUSP;
import USP.Model.RoomUSP;
import USP.Model.RuleUSP;
import USP.Model.SessionRuleUSP;
import USP.Model.SolutionUSP;
import USP.Model.StudentUSP;
import USP.Model.TeacherUSP;
import USP.Model.Timetabling;

public class ConvertisseurUSP {
	private static boolean unionroom = false;
	private static boolean multipleConfig = false;

	public static Timetabling getTimeTabling(ProblemITC problem, boolean unionR, boolean multiple) {
		unionroom = unionR;
		multipleConfig = multiple;
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
		rules = convertionDistibution(rules, problem.getDistributions());

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

	private static ArrayList<RuleUSP> convertionDistibution(ArrayList<RuleUSP> rules,
			ArrayList<DistributionITC> distributions) {
		for (DistributionITC distrib : distributions) {
			if (distrib.getType().equals("SameAttendees") && distrib.getRequired().equals("true")) {
				String s = "";
				for (String classe : distrib.getClassId()) {
					s += classe + ",";
				}
				s = s.substring(0, s.length() - 1);
				ArrayList<SessionRuleUSP> sessions = new ArrayList<>();
				ConstraintsUSP constraints = new ConstraintsUSP();
				ArrayList<FilterUSP> filters = new ArrayList<>();
				FilterUSP filter = new FilterUSP("class", "id");
				filter.setIn(s);
				filters.add(filter);
				SessionRuleUSP session = new SessionRuleUSP("courses", filters);
				sessions.add(session);
				ConstraintUSP cons = new ConstraintUSP("disjunctive", "hard", new ArrayList<>());
				constraints.add(cons);
				RuleUSP rule = new RuleUSP(sessions, constraints);
				rules.add(rule);
			}
		}

		return rules;
	}

	private static ArrayList<CourseUSP> convertionCourses(ArrayList<CourseUSP> coursesUsp,
			ArrayList<CourseITC> coursesItc) {
		for (CourseITC course : coursesItc) {
			ArrayList<PartUSP> parts = new ArrayList<>();
			if (course.getConfig().size() == 1 || multipleConfig) {
				for (ConfigITC conf : course.getConfig()) {
					for (SubpartITC sub : conf.getSubpart()) {
						ArrayList<ClassUSP> classes = new ArrayList<>();
						for (ClassITC clas : sub.getClas()) {
							ClassUSP clasU = new ClassUSP(clas.getId(), "");
							clasU.setParent(clas.getParent());
							classes.add(clasU);
						}
						AllowedRoomsUSP rooms = getAllowedRooms(sub.getClas());
						PartUSP part = new PartUSP(sub.getId(), "", "", classes, new AllowedSlotsUSP("0", "", "", ""),
								rooms, new AllowedTeachersUSP("0", new ArrayList<>()));
						parts.add(part);
					}
				}
			}
			CourseUSP courseU = new CourseUSP(course.getId(), parts);
			coursesUsp.add(courseU);
		}
		return coursesUsp;
	}

	private static AllowedRoomsUSP getAllowedRooms(ArrayList<ClassITC> clas) {
		AllowedRoomsUSP rooms = new AllowedRoomsUSP("none");
		for (ClassITC classe : clas) {
			if (unionroom) {
				for (ClassRoomITC room : classe.getRooms()) {
					if (!rooms.containsRoom(room.getId())) {
						AllowedRoomUSP roomU = new AllowedRoomUSP(room.getId(), "");
						rooms.add(roomU);
					}
				}
			} else {
				rooms = intersection(rooms, classe.getRooms());
			}
		}
		return rooms;
	}

	private static AllowedRoomsUSP intersection(AllowedRoomsUSP rooms, ArrayList<ClassRoomITC> roomsITC) {
		AllowedRoomsUSP res = new AllowedRoomsUSP(rooms.getSessionRooms());
		if (rooms.size() == 0) {
			for (ClassRoomITC roomI : roomsITC) {
				res.add(new AllowedRoomUSP(roomI.getId(), ""));
			}
		} else {
			for (ClassRoomITC roomI : roomsITC) {
				if (rooms.containsRoom(roomI.getId())) {
					res.add(new AllowedRoomUSP(roomI.getId(), ""));
				}
			}
		}
		return res;
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
