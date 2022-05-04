package Convertion.VersITC;

import java.util.ArrayList;
import java.util.List;

import ITC.Model.ClassITC;
import ITC.Model.ClassRoomITC;
import ITC.Model.ClassRoomsITC;
import ITC.Model.ClassesITC;
import ITC.Model.ConfigITC;
import ITC.Model.CourseITC;
import ITC.Model.DistributionITC;
import ITC.Model.OptimizationITC;
import ITC.Model.ProblemITC;
import ITC.Model.RoomITC;
import ITC.Model.StudentITC;
import ITC.Model.SubpartITC;
import ITC.Model.TimesPenaltysITC;
import ITC.Model.TravelsITC;
import USP.Model.AllowedRoomUSP;
import USP.Model.AllowedRoomsUSP;
import USP.Model.ClassUSP;
import USP.Model.CourseUSP;
import USP.Model.FilterUSP;
import USP.Model.PartUSP;
import USP.Model.RoomUSP;
import USP.Model.RuleUSP;
import USP.Model.RulesUSP;
import USP.Model.SessionRuleUSP;
import USP.Model.StudentUSP;
import USP.Model.Timetabling;

public class ConvertisseurITC {
	static RulesUSP rules;
	static int nbDaysPerWeek;
	static int nbWeeks;

	public static ProblemITC getProblem(Timetabling time) {
		ArrayList<RoomITC> rooms = new ArrayList<>();
		ArrayList<DistributionITC> distributions = new ArrayList<>();
		ArrayList<StudentITC> students = new ArrayList<>();
		ArrayList<CourseITC> courses = new ArrayList<>();
		OptimizationITC optimization = new OptimizationITC("", "", "", "");
		ProblemITC problem = new ProblemITC(time.getName(), time.getNrDaysPerWeek(), time.getNrSlotsPerDay(),
				time.getNrWeeks());
		rules = new RulesUSP();
		rules = time.getRules();
		nbDaysPerWeek = Integer.parseInt(time.getNrDaysPerWeek());
		nbWeeks = Integer.parseInt(time.getNrWeeks());

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
			if (rule.getConstraint().containsCons("disjunctive", "hard") > -1) {
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
				ClassesITC classes = new ClassesITC();
				for (ClassUSP classe : part.getClasses()) {
					ClassRoomsITC rooms = getClassRooms(classe.getId(), part.getRoom());
					TimesPenaltysITC times = getTimesPenaltyS(classe.getId());
					ClassITC classI = new ClassITC(classe.getId(), classe.getMaxHeadCount(), rooms, times);
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

	private static ClassRoomsITC getClassRooms(String id, AllowedRoomsUSP alloRooms) {
		ClassRoomsITC rooms = new ClassRoomsITC();
		List<String> forbiddenRooms = rules.getForbidenRoomsClass(id);
		for (AllowedRoomUSP room : alloRooms) {
			if (!forbiddenRooms.contains(room.getRefId())) {
				ClassRoomITC roomI = new ClassRoomITC(room.getRefId(), "0");
				rooms.add(roomI);
			}
		}
		return rooms;
	}

	private static TimesPenaltysITC getTimesPenaltyS(String id) {
		List<String> forbiddenPeriod = rules.getForbidenPeriodClass(id);
		System.out.println(id + " period " + forbiddenPeriod.toString());
		TimesPenaltysITC times = ConvertisseurTimes.convertForbiddenToTimes(forbiddenPeriod, nbDaysPerWeek, nbWeeks, 1);
		return times;
	}

	private static ArrayList<RoomITC> convertionRooms(ArrayList<RoomITC> roomsItc, ArrayList<RoomUSP> roomsUsp) {
		for (RoomUSP roomU : roomsUsp) {
			RoomITC room = new RoomITC(roomU.getId(), new TravelsITC(), roomU.getCapacity(), new ArrayList<>());
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
