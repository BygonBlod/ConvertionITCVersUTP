package Convertion.VersITC;

import java.util.ArrayList;

import ITC.Model.ClassITC;
import ITC.Model.ClassRoomITC;
import ITC.Model.ClassRoomsITC;
import ITC.Model.ConfigITC;
import ITC.Model.CourseITC;
import ITC.Model.DistributionITC;
import ITC.Model.OptimizationITC;
import ITC.Model.ProblemITC;
import ITC.Model.RoomITC;
import ITC.Model.StudentITC;
import ITC.Model.SubpartITC;
import ITC.Model.TimesPenaltysITC;
import USP.Model.AllowedRoomUSP;
import USP.Model.AllowedRoomsUSP;
import USP.Model.ClassUSP;
import USP.Model.CourseUSP;
import USP.Model.FilterUSP;
import USP.Model.PartUSP;
import USP.Model.RoomUSP;
import USP.Model.RuleUSP;
import USP.Model.SessionRuleUSP;
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
		rooms = convertionRooms(rooms, time.getRooms());
		courses = convertionCourses(courses, time.getCourses());
		distributions = convertionDistribution(distributions, time.getRules());

		problem.setCourses(courses);
		problem.setDistributions(distributions);
		problem.setOptimization(optimization);
		problem.setRooms(rooms);
		problem.setStudents(students);
		return problem;
	}

	private static ArrayList<DistributionITC> convertionDistribution(ArrayList<DistributionITC> distributions,
			ArrayList<RuleUSP> rules) {
		for (RuleUSP rule : rules) {
			ArrayList<String> classes = new ArrayList<>();
			if (rule.getConstraint().containsCons("disjunctive", "hard")) {
				for (SessionRuleUSP session : rule.getSessions()) {
					if (session.getGroupBy().equals("courses")) {
						for (FilterUSP filter : session.getFilter()) {
							if (filter.getAttributeName().equals("id") && filter.getType().equals("class")) {
								String s = filter.getIn();
								String[] decoupe = s.split(",");
								for (int i = 0; i < decoupe.length; i++) {
									classes.add(decoupe[i]);
								}
							}
						}
					}
				}
				DistributionITC distribution = new DistributionITC("SameAttendees", classes);
				distribution.setRequired("true");
				distributions.add(distribution);
			}
		}

		return distributions;
	}

	private static ArrayList<CourseITC> convertionCourses(ArrayList<CourseITC> coursesItc,
			ArrayList<CourseUSP> coursesUsp) {
		for (CourseUSP course : coursesUsp) {
			ArrayList<ConfigITC> configs = new ArrayList<>();
			ArrayList<SubpartITC> parts = new ArrayList<>();
			for (PartUSP part : course.getParts()) {
				ArrayList<ClassITC> classes = new ArrayList<>();
				for (ClassUSP classe : part.getClasses()) {
					ClassRoomsITC rooms = new ClassRoomsITC();
					AllowedRoomsUSP alloRooms = part.getRoom();
					for (AllowedRoomUSP room : alloRooms) {
						ClassRoomITC roomI = new ClassRoomITC(room.getRefId(), "");
						rooms.add(roomI);
					}
					ClassITC classI = new ClassITC(classe.getId(), classe.getMaxHeadCount(), rooms,
							new TimesPenaltysITC());
					classI.setParent(classe.getParent());
					classes.add(classI);
				}

				parts.add(new SubpartITC(part.getId(), classes));
			}
			configs.add(new ConfigITC(course.getId(), parts));
			CourseITC courseI = new CourseITC(course.getId(), configs);
			coursesItc.add(courseI);
		}
		return coursesItc;
	}

	private static ArrayList<RoomITC> convertionRooms(ArrayList<RoomITC> roomsItc, ArrayList<RoomUSP> roomsUsp) {
		for (RoomUSP roomU : roomsUsp) {
			RoomITC room = new RoomITC(roomU.getId(), new ArrayList<>(), roomU.getCapacity(), new ArrayList<>());
			roomsItc.add(room);
		}
		return roomsItc;
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
