package Convertion.VersUSP;

import java.util.ArrayList;

import ITC.Model.ClassITC;
import ITC.Model.ClassRoomITC;
import ITC.Model.ClassRoomsITC;
import ITC.Model.ConfigITC;
import ITC.Model.CourseITC;
import ITC.Model.DistributionITC;
import ITC.Model.ProblemITC;
import ITC.Model.RoomITC;
import ITC.Model.StudentITC;
import ITC.Model.SubpartITC;
import ITC.Model.TimesPenaltyITC;
import USP.Model.AllowedRoomUSP;
import USP.Model.AllowedRoomsUSP;
import USP.Model.AllowedSlotsUSP;
import USP.Model.AllowedTeachersUSP;
import USP.Model.ClassUSP;
import USP.Model.ConstraintUSP;
import USP.Model.ConstraintsUSP;
import USP.Model.CourseUSP;
import USP.Model.FilterUSP;
import USP.Model.ParameterUSP;
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
	private static ArrayList<RuleUSP> rules;
	private static int slot;
	private static int nbDays;

	public static Timetabling getTimeTabling(ProblemITC problem, boolean unionR, boolean multiple) {
		unionroom = unionR;
		multipleConfig = multiple;
		ArrayList<RoomUSP> rooms = new ArrayList<>();
		ArrayList<TeacherUSP> teachers = new ArrayList<>();
		ArrayList<CourseUSP> courses = new ArrayList<>();
		ArrayList<StudentUSP> students = new ArrayList<>();
		rules = new ArrayList<>();
		SolutionUSP solution;
		slot = Integer.parseInt(problem.getNrDays()) * Integer.parseInt(problem.getNrWeeks());
		nbDays = Integer.parseInt(problem.getNrDays());

		students = convertionStudents(students, problem.getStudents());
		rooms = convertionRooms(rooms, problem.getRooms());
		courses = convertionCourses(courses, problem.getCourses());
		solution = new SolutionUSP();
		convertionDistibution(problem.getDistributions());

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

	private static void setForbiddenPeriod(SubpartITC sub) {
		int start = (sub.getFirstWeek(sub.getAllTimes()) - 1) * 1440 * nbDays;
		int end = sub.getLastWeek(sub.getAllTimes()) * 1440 * nbDays;
		for (ClassITC classe : sub.getClas()) {
			ArrayList<String> forbidden = classe.getForbiddenPeriod(start, end);
			System.out.println(forbidden.toString());
		}
	}

	private static void convertionDistibution(ArrayList<DistributionITC> distributions) {
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
	}

	private static ArrayList<CourseUSP> convertionCourses(ArrayList<CourseUSP> coursesUsp,
			ArrayList<CourseITC> coursesItc) {
		for (CourseITC course : coursesItc) {
			if (course.getConfig().size() == 1 || course.differentTimes4()) {
				int i = 0;
				for (ConfigITC conf : course.getConfig()) {
					ArrayList<PartUSP> parts = new ArrayList<>();
					for (SubpartITC sub : conf.getSubpart()) {
						ArrayList<ClassUSP> classes = new ArrayList<>();
						for (ClassITC clas : sub.getClas()) {
							if (clas.checkWeeks()) {
								setPeriodic(clas);
							}
							ClassUSP clasU = new ClassUSP(clas.getId(), clas.getLimit());
							clasU.setParent(clas.getParent());
							classes.add(clasU);
						}
						AllowedRoomsUSP rooms = getAllowedRooms(sub.getClas());
						AllowedSlotsUSP slots = getAllowedSlots(sub);
						PartUSP part = new PartUSP(sub.getId(), "", "", classes, slots, rooms,
								new AllowedTeachersUSP("0", new ArrayList<>()));
						parts.add(part);
					}
					if (course.getConfig().size() > 1) {
						CourseUSP courseU = new CourseUSP(course.getId() + "-" + i, parts);
						coursesUsp.add(courseU);
					} else {
						CourseUSP courseU = new CourseUSP(course.getId(), parts);
						coursesUsp.add(courseU);
					}
					i++;
				}
			}
		}
		return coursesUsp;
	}

	private static void setPeriodic(ClassITC clas) {
		ArrayList<SessionRuleUSP> sessions = new ArrayList<>();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ArrayList<FilterUSP> filters = new ArrayList<>();
		FilterUSP filter = new FilterUSP("class", "id");
		filter.setIn(clas.getId());
		filters.add(filter);
		SessionRuleUSP session = new SessionRuleUSP("class", filters);
		sessions.add(session);
		ArrayList<ParameterUSP> parameters = new ArrayList<>();
		ParameterUSP param1 = new ParameterUSP("value");
		param1.setType("slots");
		param1.setValue(clas.getNbSessionWeek() + "");
		ParameterUSP param2 = new ParameterUSP("unit");
		param2.setType("time");
		param2.setValue("week");
		parameters.add(param1);
		parameters.add(param2);
		ConstraintUSP cons = new ConstraintUSP("Periodic", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);
	}

	private static AllowedSlotsUSP getAllowedSlots(SubpartITC sub) {
		AllowedSlotsUSP slots = new AllowedSlotsUSP("", "", "", "");
		String weeks = sub.getWeeksLSD();
		if (sub.sameTimesLSD(sub.getId()) && sub.getAllTimes().size() > 0 && !weeks.equals("")) {
			TimesPenaltyITC time = sub.getAllTimes().get(0);
			System.out.println(sub.getId());
			slots.setDailySlots(time.getStartUSP());
			slots.setSessionLength(time.getLengthUSP());
			slots.setDays(time.getDaysUSP());
			slots.setWeeks(weeks);
			setForbiddenPeriod(sub);
		}

		return slots;
	}

	private static AllowedRoomsUSP getAllowedRooms(ArrayList<ClassITC> clas) {
		AllowedRoomsUSP rooms = new AllowedRoomsUSP("none");
		for (ClassITC classe : clas) {
			for (ClassRoomITC room : classe.getRooms()) {
				if (!rooms.containsRoom(room.getId())) {
					AllowedRoomUSP roomU = new AllowedRoomUSP(room.getId(), "");
					rooms.add(roomU);
				}
			}
		}
		for (ClassITC classe : clas) {
			String idrooms = inintersection(rooms, classe.getRooms());
			if (!idrooms.equals("")) {
				ArrayList<SessionRuleUSP> sessions = new ArrayList<>();
				ConstraintsUSP constraints = new ConstraintsUSP();
				ArrayList<FilterUSP> filters = new ArrayList<>();
				SessionRuleUSP session = new SessionRuleUSP("class", filters);
				session.setAttributeName("id");
				session.setIn(classe.getId());
				sessions.add(session);
				ArrayList<ParameterUSP> parameters = new ArrayList<>();
				ParameterUSP param = new ParameterUSP("rooms");
				param.setType("ids");
				param.setValue(idrooms);
				parameters.add(param);

				ConstraintUSP cons = new ConstraintUSP("forbiddenRooms", "hard", parameters);
				constraints.add(cons);
				RuleUSP rule = new RuleUSP(sessions, constraints);
				rules.add(rule);
			}
		}
		return rooms;
	}

	private static String inintersection(AllowedRoomsUSP rooms, ClassRoomsITC roomsITC) {
		String res2 = "";
		if (rooms.size() != roomsITC.size()) {
			for (AllowedRoomUSP roomI : rooms) {
				if (!roomsITC.containsRoom(roomI.getRefId())) {
					res2 += roomI.getRefId() + ",";
				}
			}
			if (!res2.equals("")) {
				res2 = res2.substring(0, res2.length() - 1);
			}
		}
		return res2;
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
