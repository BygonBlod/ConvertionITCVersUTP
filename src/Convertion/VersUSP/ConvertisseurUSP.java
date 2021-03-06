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
import ITC.Model.TravelsITC;
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
import USP.Model.ParametersUSP;
import USP.Model.PartUSP;
import USP.Model.RoomUSP;
import USP.Model.RuleUSP;
import USP.Model.RulesUSP;
import USP.Model.SessionRuleUSP;
import USP.Model.SessionsRuleUSP;
import USP.Model.SolutionUSP;
import USP.Model.StudentUSP;
import USP.Model.StudentsUSP;
import USP.Model.TeacherUSP;
import USP.Model.Timetabling;

public class ConvertisseurUSP {
	private static boolean multipleConfig = false;
	private static RulesUSP rules;
	private static StudentsUSP students;
	private static ArrayList<CourseUSP> courses;
	private static String vectorTravel;
	private static int slot;
	private static int nbDays;
	private static int nbSlots;
	private static int nbSlotsF;

	public static Timetabling getTimeTabling(ProblemITC problem, boolean multiple) {
		multipleConfig = multiple;
		nbSlots = 0;
		nbSlotsF = 0;
		ArrayList<RoomUSP> rooms = new ArrayList<>();
		ArrayList<TeacherUSP> teachers = new ArrayList<>();
		courses = new ArrayList<>();
		students = new StudentsUSP();
		rules = new RulesUSP();
		SolutionUSP solution;
		slot = Integer.parseInt(problem.getNrDays()) * Integer.parseInt(problem.getNrWeeks());
		nbDays = Integer.parseInt(problem.getNrDays());
		vectorTravel = getTravelVector(problem.getRooms());

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

		/*
		 * ArrayList<String> lignes = new ArrayList<>(); Path fichier =
		 * Paths.get("nbInstancesSlots.txt"); try { lignes.add(time.getName());
		 * lignes.add(nbSlotsF + " / " + nbSlots); lignes.add("\n");
		 * Files.write(fichier, lignes, Charset.forName("UTF-8"),
		 * StandardOpenOption.APPEND); } catch (IOException e) { e.printStackTrace(); }
		 */
		return time;
	}

	private static void setForbiddenPeriod(SubpartITC sub) {
		int start = (sub.getFirstWeek(sub.getAllTimes()) - 1) * 1440 * nbDays;
		int end = sub.getLastWeek(sub.getAllTimes()) * 1440 * nbDays;
		for (ClassITC classe : sub.getClas()) {
			ArrayList<String> forbidden = classe.getForbiddenPeriod(start, end);
			for (String forbid : forbidden) {
				String[] forbidSplit = forbid.split("-");
				if (forbidSplit.length == 2) {
					SessionsRuleUSP sessions = new SessionsRuleUSP();
					ConstraintsUSP constraints = new ConstraintsUSP();
					ArrayList<FilterUSP> filters = new ArrayList<>();
					SessionRuleUSP session = new SessionRuleUSP("class", filters);
					session.setAttributeName("id");
					session.setIn(classe.getId());
					sessions.add(session);
					ParametersUSP parameters = new ParametersUSP();
					ParameterUSP param1 = new ParameterUSP("first");
					param1.setType("slot");
					param1.setValue(forbidSplit[0]);
					ParameterUSP param2 = new ParameterUSP("last");
					param2.setType("slot");
					param2.setValue(forbidSplit[1]);
					parameters.add(param1);
					parameters.add(param2);
					ConstraintUSP cons = new ConstraintUSP("forbiddenPeriod", "hard", parameters);
					constraints.add(cons);
					RuleUSP rule = new RuleUSP(sessions, constraints);
					rules.add(rule);
				}
			}
		}
	}

	private static void convertionDistibution(ArrayList<DistributionITC> distributions) {
		for (DistributionITC distrib : distributions) {
			String type = distrib.getType();
			if (type.contains("(")) {
				type = type.substring(0, type.indexOf("("));
			}
			switch (type) {
			case "DifferentDays":
				convertionDifferentDaysToDisjointWeekDays(distrib);
				break;
			case "DifferentRoom":
				convertionDifferentRoomToDisjointRooms(distrib);
				break;
			case "DifferentTime":
				convertionDifferentTimeToDisjunctiveDaily(distrib);
				break;
			case "DifferentWeeks":
				convertionDifferentWeeksToDisjointWeeks(distrib);
				break;
			case "MaxBlock":
				convertionMaxBlockToAtMostConsecutive(distrib);
				break;
			case "MaxDayLoad":
				convertionMaxDayLoadToAtMostDaily(distrib);
				break;
			case "MaxBreaks":
				convertionMaxBreaksToMaxBreaks(distrib);
				break;
			case "MaxDays":
				convertionMaxDaysToMaxWeekDays(distrib);
				break;
			case "MinGap":
				convertionMinGapToMinGap(distrib);
				break;
			case "NotOverlap":
				convertionNotOverlapToDisjunctive(distrib);
				break;
			case "Overlap":
				convertionOverlapToOverlap(distrib);
				break;
			case "Precedence":
				convertionPrecedenceToSequenced(distrib);
				break;
			case "SameAttendees":
				convertionSameAttendeesToTravelTime(distrib);
				break;
			case "SameDays":
				convertionSameDaysToSameWeekDay(distrib);
				break;
			case "SameRoom":
				convertionSameRoomToSameRooms(distrib);
				break;
			case "SameStart":
				convertionSameStartToSameDailySlot(distrib);
				break;
			case "SameTime":
				convertionSameTimeToDuring(distrib);
				break;
			case "SameWeeks":
				convertionSameWeeksToSameWeeks(distrib);
				break;
			case "WorkDay":
				convertionWorkDayToWorkLoad(distrib);
				break;
			}
		}
	}

	private static void convertionDifferentDaysToDisjointWeekDays(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("disjointWeekDays", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionDifferentRoomToDisjointRooms(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("disjointRooms", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionDifferentTimeToDisjunctiveDaily(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("disjunctiveDaily", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionDifferentWeeksToDisjointWeeks(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("disjointWeeks", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionMaxBlockToAtMostConsecutive(DistributionITC distrib) {
		String type = distrib.getType();
		String valueM = type.substring(type.indexOf("(") + 1, type.indexOf(","));
		String valueS = type.substring(type.indexOf(",") + 1, type.indexOf(")"));
		valueM = Integer.parseInt(valueM) * 5 + "";
		valueS = Integer.parseInt(valueS) * 5 + "";

		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ParameterUSP parameter = new ParameterUSP("M");
		parameter.setType("slot");
		parameter.setValue(valueM);
		parameters.add(parameter);
		ParameterUSP parameter2 = new ParameterUSP("S");
		parameter2.setType("slot");
		parameter2.setValue(valueS);
		parameters.add(parameter2);
		ConstraintUSP cons = new ConstraintUSP("atMostConsecutive", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);
	}

	private static void convertionMaxDayLoadToAtMostDaily(DistributionITC distrib) {
		String type = distrib.getType();
		String value = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
		value = Integer.parseInt(value) * 5 + "";
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ParameterUSP parameter = new ParameterUSP("first");
		parameter.setType("slot");
		parameter.setValue("1");
		parameters.add(parameter);
		ParameterUSP parameter2 = new ParameterUSP("last");
		parameter2.setType("slot");
		parameter2.setValue("1440");
		parameters.add(parameter2);
		ParameterUSP parameter3 = new ParameterUSP("count");
		parameter3.setType("slot");
		parameter3.setValue(value);
		parameters.add(parameter3);
		ConstraintUSP cons = new ConstraintUSP("atMostDaily", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionMaxBreaksToMaxBreaks(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ParameterUSP parameter = new ParameterUSP("maxBreaks");
		parameter.setType("int");
		String type = distrib.getType();
		String valueR = type.substring(type.indexOf("(") + 1, type.indexOf(","));
		parameter.setValue(valueR);
		parameters.add(parameter);
		ParameterUSP parameter2 = new ParameterUSP("time");
		parameter2.setType("slot");
		String valueS = type.substring(type.indexOf(",") + 1, type.indexOf(")"));
		valueS = Integer.parseInt(valueS) * 5 + "";
		parameter2.setValue(valueS);
		parameters.add(parameter2);
		ConstraintUSP cons = new ConstraintUSP("maxBreaks", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionMaxDaysToMaxWeekDays(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ParameterUSP parameter = new ParameterUSP("max");
		parameter.setType("int");
		String type = distrib.getType();
		String value = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
		parameter.setValue(value);
		parameters.add(parameter);
		ConstraintUSP cons = new ConstraintUSP("maxWeekDay", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionMinGapToMinGap(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ParameterUSP parameter = new ParameterUSP("min");
		parameter.setType("slot");
		String type = distrib.getType();
		String value = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(")"))) * 5 + "";
		parameter.setValue(value);
		parameters.add(parameter);
		ConstraintUSP cons = new ConstraintUSP("minGap", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionNotOverlapToDisjunctive(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("disjunctive", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionOverlapToOverlap(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("overlap", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionPrecedenceToSequenced(DistributionITC distrib) {
		SessionsRuleUSP sessions = new SessionsRuleUSP();
		ConstraintsUSP constraints = new ConstraintsUSP();
		for (String classe : distrib.getClassId()) {
			ArrayList<FilterUSP> filters = new ArrayList<>();
			SessionRuleUSP session = new SessionRuleUSP("class", filters);
			session.setSessionsMask("1");
			session.setAttributeName("id");
			session.setIn(classe);
			sessions.add(session);
		}

		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("sequenced", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);
	}

	public static void convertionSameAttendeesToTravelTime(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ParameterUSP parameter = new ParameterUSP("travels");
		parameter.setType("slots");
		parameter.setValue(vectorTravel);
		parameters.add(parameter);
		ConstraintUSP cons = new ConstraintUSP("travelTime", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);
	}

	private static void convertionSameDaysToSameWeekDay(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("sameWeekDays", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionSameRoomToSameRooms(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("sameRooms", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionSameStartToSameDailySlot(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("sameDailySlot", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionSameTimeToDuring(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("during", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionSameWeeksToSameWeeks(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("sameWeeks", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void convertionWorkDayToWorkLoad(DistributionITC distrib) {
		SessionsRuleUSP sessions = distrib.getSessionIdClass();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ParametersUSP parameters = new ParametersUSP();
		ParameterUSP parameter = new ParameterUSP("value");
		parameter.setType("slots");
		String type = distrib.getType();
		String value = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(")"))) * 5 + "";
		parameter.setValue(value);
		parameters.add(parameter);
		ParameterUSP parameter2 = new ParameterUSP("unit");
		parameter2.setType("time");
		parameter2.setValue("dailySlot");
		parameters.add(parameter2);
		ConstraintUSP cons = new ConstraintUSP("workLoad", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);
	}

	private static String getTravelVector(ArrayList<RoomITC> roomSITC) {
		int size = roomSITC.size();
		String s2 = "";
		for (int i = 0; i < size; i++) {
			RoomITC roomi = roomSITC.get(i);
			TravelsITC travelsRoomi = roomi.getTravel();
			for (int j = i + 1; j < size; j++) {
				RoomITC roomj = roomSITC.get(j);
				TravelsITC travelsRoomj = roomj.getTravel();
				int index = travelsRoomi.containsId(roomj.getId());
				int index2 = travelsRoomj.containsId(roomi.getId());
				if (index > -1 || index2 > -1) {
					if (index > -1) {
						String value1 = travelsRoomi.get(index).getValue() + "";
						s2 += value1;
					} else {
						String value2 = travelsRoomj.get(index2).getValue() + "";
						s2 += value2;
					}
				} else {
					s2 += "0";
				}
				s2 += ",";
			}
		}
		s2 = s2.substring(0, s2.length() - 1);
		return s2;
	}

	private static ArrayList<CourseUSP> convertionCourses(ArrayList<CourseUSP> coursesUsp,
			ArrayList<CourseITC> coursesItc) {
		for (CourseITC course : coursesItc) {
			int nbConfig = course.getConfig().size();
			if (nbConfig == 1 || (course.differentTimes4() && multipleConfig)) {
				int i = 0;
				if (nbConfig > 1) {
					changeStudentCourse(course.getId(), nbConfig);
				}
				for (ConfigITC conf : course.getConfig()) {
					ArrayList<PartUSP> parts = new ArrayList<>();
					for (SubpartITC sub : conf.getSubpart()) {
						ArrayList<ClassUSP> classes = new ArrayList<>();
						for (ClassITC clas : sub.getClas()) {
							if (clas.checkWeeks()) {
								setPeriodic(clas);
							}
							setSameRoomTeacher(clas);
							if (clas.getTimes().size() > 0) {
								String days = clas.getTimes().get(0).getDays();
								if (Utils.Util.nbOccurrences(days, "1") == 1) {
									setSameSlot(clas.getId(), "sameWeeklySlot");
								} else {
									setSameSlot(clas.getId(), "sameDailySlot");
								}
							}
							ClassUSP clasU = new ClassUSP(clas.getId(), clas.getLimit());
							clasU.setParent(clas.getParent());
							classes.add(clasU);
						}
						AllowedRoomsUSP rooms = getAllowedRooms(sub.getClas());
						AllowedSlotsUSP slots = getAllowedSlots(sub);
						PartUSP part = new PartUSP(sub.getId(), sub.getNrSession(), "", classes, slots, rooms,
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

	private static void setSameRoomTeacher(ClassITC clas) {
		// sameRoom
		SessionsRuleUSP sessions = new SessionsRuleUSP();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ArrayList<FilterUSP> filters = new ArrayList<>();
		FilterUSP filter = new FilterUSP("class", "id");
		filter.setIn(clas.getId());
		filters.add(filter);
		SessionRuleUSP session = new SessionRuleUSP("class", filters);
		session.setSessionsMask("1-" + clas.getNrSession());
		sessions.add(session);
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP("SameRoom", "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

		// sameTeacher
		ConstraintsUSP constraints2 = new ConstraintsUSP();
		ConstraintUSP cons2 = new ConstraintUSP("SameTeacher", "hard", parameters);
		constraints2.add(cons2);
		rule = new RuleUSP(sessions, constraints2);
		rules.add(rule);

	}

	private static void changeStudentCourse(String id, int size) {
		StudentsUSP studentCourse = students.getStudentsCourse(id);
		int i = 0;
		int nb = 0;
		for (StudentUSP student : studentCourse) {
			students.setStud(student.getId(), id, i);
			if (i < size - 1 && nb == (i + 1) * ((studentCourse.size() / size) - 1)) {
				i++;
			}
			nb++;
		}

	}

	private static void setSameSlot(String id, String type) {
		SessionsRuleUSP sessions = new SessionsRuleUSP();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ArrayList<FilterUSP> filters = new ArrayList<>();
		SessionRuleUSP session = new SessionRuleUSP("class", filters);
		session.setAttributeName("id");
		session.setIn(id);
		sessions.add(session);
		ParametersUSP parameters = new ParametersUSP();
		ConstraintUSP cons = new ConstraintUSP(type, "hard", parameters);
		constraints.add(cons);
		RuleUSP rule = new RuleUSP(sessions, constraints);
		rules.add(rule);

	}

	private static void setPeriodic(ClassITC clas) {
		SessionsRuleUSP sessions = new SessionsRuleUSP();
		ConstraintsUSP constraints = new ConstraintsUSP();
		ArrayList<FilterUSP> filters = new ArrayList<>();
		FilterUSP filter = new FilterUSP("class", "id");
		filter.setIn(clas.getId());
		filters.add(filter);
		SessionRuleUSP session = new SessionRuleUSP("class", filters);
		sessions.add(session);
		ParametersUSP parameters = new ParametersUSP();
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
		if ((sub.sameTimesNb() || sub.sameTimesLSD()) && sub.getAllTimes().size() > 0 && !weeks.equals("")) {
			TimesPenaltyITC time = sub.getAllTimes().get(0);
			nbSlotsF++;
			// System.out.println(sub.getId());
			slots.setDailySlots(sub.getStartUSP());
			slots.setSessionLength(time.getLengthUSP());
			slots.setDays(sub.getDaysUSP(sub.getId()));
			slots.setWeeks(weeks);
			setForbiddenPeriod(sub);
		}
		nbSlots++;
		return slots;
	}

	private static AllowedRoomsUSP getAllowedRooms(ArrayList<ClassITC> clas) {
		AllowedRoomsUSP rooms = new AllowedRoomsUSP("single");
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
				SessionsRuleUSP sessions = new SessionsRuleUSP();
				ConstraintsUSP constraints = new ConstraintsUSP();
				ArrayList<FilterUSP> filters = new ArrayList<>();
				SessionRuleUSP session = new SessionRuleUSP("class", filters);
				session.setAttributeName("id");
				session.setIn(classe.getId());
				sessions.add(session);
				ParametersUSP parameters = new ParametersUSP();
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

	private static StudentsUSP convertionStudents(StudentsUSP studentsUsp, ArrayList<StudentITC> studentsItc) {
		for (StudentITC stud : studentsItc) {
			StudentUSP student = new StudentUSP(stud.getId(), "", stud.getCourses());
			studentsUsp.add(student);
		}
		return studentsUsp;
	}

}
