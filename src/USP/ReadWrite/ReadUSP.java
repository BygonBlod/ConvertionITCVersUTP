package USP.ReadWrite;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;

import USP.Model.AllowedRoomUSP;
import USP.Model.AllowedRoomsUSP;
import USP.Model.AllowedSlotsUSP;
import USP.Model.AllowedTeacher;
import USP.Model.AllowedTeachersUSP;
import USP.Model.ClassUSP;
import USP.Model.ConstraintUSP;
import USP.Model.ConstraintsUSP;
import USP.Model.CourseUSP;
import USP.Model.FilterUSP;
import USP.Model.GroupSolutionUSP;
import USP.Model.ParameterUSP;
import USP.Model.PartUSP;
import USP.Model.RoomUSP;
import USP.Model.RuleUSP;
import USP.Model.SessionRuleUSP;
import USP.Model.SessionSolutionUSP;
import USP.Model.SolutionUSP;
import USP.Model.StudentUSP;
import USP.Model.TeacherUSP;
import USP.Model.Timetabling;
import Utils.UtilParse;
import Utils.Value_USP;

public class ReadUSP {
	static Timetabling timeTabling;
	static ArrayList<RoomUSP> rooms;
	static ArrayList<TeacherUSP> teachers;
	static ArrayList<CourseUSP> courses;
	static ArrayList<StudentUSP> students;
	static ArrayList<RuleUSP> rules;
	static SolutionUSP solution;

	public static Timetabling getTimeTabling(String args) {
		rooms = new ArrayList<>();
		teachers = new ArrayList<>();
		courses = new ArrayList<>();
		students = new ArrayList<>();
		rules = new ArrayList<>();
		solution = new SolutionUSP();
		Document document = null;
		DocumentBuilderFactory factory = null;
		DocumentTraversal traversal = null;

		try {
			factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(args);
			traversal = (DocumentTraversal) document;
		} catch (Exception e) {
			System.out.println("échec du parsing du document :");
			e.printStackTrace();
		}
		if (traversal != null) {
			Node racine = document.getFirstChild();
			if (racine.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) racine;
				String name = ele.getAttribute(Value_USP.Attibute_Name);
				String nrWeeks = ele.getAttribute(Value_USP.Attibute_NrWeeks);
				String nrDaysPerWeek = ele.getAttribute(Value_USP.Attibute_NrDaysPerWeek);
				String nrSlotsPerDay = ele.getAttribute(Value_USP.Attibute_NrSlotsPerDay);
				timeTabling = new Timetabling(name, nrWeeks, nrDaysPerWeek, nrSlotsPerDay);
				NodeList listes = ele.getChildNodes();
				ArrayList<Element> list = UtilParse.getElements(listes);
				for (Element element : list) {
					switch (element.getTagName()) {
					case "rooms":
						parseRooms(element);
						break;
					case "teachers":
						parseTeachers(element);
						break;
					case "courses":
						parseCourses(element);
						break;
					case "students":
						parseStudents(element);
						break;
					case "rules":
						parseRules(element);
						break;
					case "solution":
						parseSolution(element);
						break;
					}
				}
			}
		}
		if (timeTabling != null) {
			timeTabling.setCourses(courses);
			timeTabling.setRooms(rooms);
			timeTabling.setRules(rules);
			timeTabling.setSolution(solution);
			timeTabling.setStudents(students);
			timeTabling.setTeachers(teachers);
		}
		return timeTabling;
	}

	private static void parseSolution(Element element) {

		ArrayList<GroupSolutionUSP> groups = new ArrayList<>();
		ArrayList<SessionSolutionUSP> sessions = new ArrayList<>();
		NodeList groupN = element.getElementsByTagName(Value_USP.Groups_Group);
		ArrayList<Element> groupL = UtilParse.getElements(groupN);
		for (Element groupE : groupL) {
			GroupSolutionUSP groupU = getGroupUSP(groupE);
			groups.add(groupU);
		}
		NodeList sessionN = element.getElementsByTagName(Value_USP.SolutionSessions_Session);
		ArrayList<Element> sessionL = UtilParse.getElements(sessionN);
		for (Element sessionE : sessionL) {
			SessionSolutionUSP sessionU = getSessionUSP(sessionE);
			sessions.add(sessionU);
		}
		solution.setGroups(groups);
		solution.setSessions(sessions);
	}

	private static SessionSolutionUSP getSessionUSP(Element eSession) {
		String clas = eSession.getAttribute(Value_USP.Attibute_Class);
		String rank = eSession.getAttribute(Value_USP.Attibute_Rank);
		String slot = eSession.getAttribute(Value_USP.Attibute_Slot);
		String rooms = eSession.getAttribute(Value_USP.Attibute_Rooms);
		String teachers = eSession.getAttribute(Value_USP.Attibute_Teachers);
		SessionSolutionUSP res = new SessionSolutionUSP(clas, rank, slot, rooms, teachers);
		return res;
	}

	private static GroupSolutionUSP getGroupUSP(Element groupE) {
		String id = groupE.getAttribute(Value_USP.Attibute_Id);
		String headCount = groupE.getAttribute(Value_USP.Attibute_HeadCount);
		ArrayList<String> students = new ArrayList<>();
		ArrayList<String> classes = new ArrayList<>();
		NodeList studentN = groupE.getElementsByTagName(Value_USP.SolutionStudents_Student);
		ArrayList<Element> studentL = UtilParse.getElements(studentN);
		for (Element studentE : studentL) {
			String ref = studentE.getAttribute(Value_USP.Attibute_RefId);
			students.add(ref);
		}
		NodeList classN = groupE.getElementsByTagName(Value_USP.SolutionClasses_Class);
		ArrayList<Element> classL = UtilParse.getElements(classN);
		for (Element classE : classL) {
			String ref = classE.getAttribute(Value_USP.Attibute_RefId);
			classes.add(ref);
		}
		return new GroupSolutionUSP(id, headCount, students, classes);
	}

	private static void parseRules(Element element) {
		NodeList ruleN = element.getElementsByTagName(Value_USP.Rules_Rule);
		ArrayList<Element> ruleL = UtilParse.getElements(ruleN);
		for (Element ruleE : ruleL) {
			ArrayList<SessionRuleUSP> sessions = new ArrayList<>();
			ConstraintsUSP constraints = new ConstraintsUSP();
			NodeList sessionsN = ruleE.getElementsByTagName(Value_USP.Rule_Sessions);
			ArrayList<Element> sessionL = UtilParse.getElements(sessionsN);
			for (Element sessionE : sessionL) {
				String groupBy = sessionE.getAttribute(Value_USP.Attibute_GroupBy);
				String sessionMask = sessionE.getAttribute(Value_USP.Attibute_SessionsMask);
				String attributeName = sessionE.getAttribute(Value_USP.Attibute_AttributeName);
				String inSes = sessionE.getAttribute(Value_USP.Attibute_In);
				String notInSes = sessionE.getAttribute(Value_USP.Attibute_NotIn);
				ArrayList<FilterUSP> filters = new ArrayList<>();
				NodeList filterN = sessionE.getElementsByTagName(Value_USP.Sessions_Filter);
				ArrayList<Element> filterL = UtilParse.getElements(filterN);
				for (Element filterE : filterL) {
					String type = filterE.getAttribute(Value_USP.Attibute_Type);
					String attributeNameFil = filterE.getAttribute(Value_USP.Attibute_AttributeName);
					String inF = filterE.getAttribute(Value_USP.Attibute_In);
					String notIn = filterE.getAttribute(Value_USP.Attibute_NotIn);
					FilterUSP filterU = new FilterUSP(type, attributeNameFil);
					filterU.setIn(inF);
					filterU.setNotIn(notIn);
					filters.add(filterU);
				}
				SessionRuleUSP sessionU = new SessionRuleUSP(groupBy, filters);
				sessionU.setSessionsMask(sessionMask);
				sessionU.setAttributeName(attributeName);
				sessionU.setIn(inSes);
				sessionU.setNotIn(notInSes);
				sessions.add(sessionU);
			}
			NodeList constraintN = ruleE.getElementsByTagName(Value_USP.Rule_Constraint);
			ArrayList<Element> constraintL = UtilParse.getElements(constraintN);
			for (Element constraintE : constraintL) {
				String name = constraintE.getAttribute(Value_USP.Attibute_Name);
				String type = constraintE.getAttribute(Value_USP.Attibute_Type);
				ArrayList<ParameterUSP> parameters = new ArrayList<>();
				NodeList paramN = constraintE.getElementsByTagName(Value_USP.Prameters_Parameter);
				ArrayList<Element> paramL = UtilParse.getElements(paramN);
				for (Element paramE : paramL) {
					String typeP = paramE.getAttribute(Value_USP.Attibute_Type);
					String nameP = paramE.getAttribute(Value_USP.Attibute_Name);
					String value = paramE.getTextContent();
					ParameterUSP paramU = new ParameterUSP(nameP);
					paramU.setType(typeP);
					paramU.setValue(value);
					parameters.add(paramU);
				}
				ConstraintUSP consU = new ConstraintUSP(name, type, parameters);
				constraints.add(consU);
			}
			RuleUSP ruleU = new RuleUSP(sessions, constraints);
			rules.add(ruleU);
		}
	}

	private static void parseStudents(Element element) {
		NodeList studentN = element.getElementsByTagName(Value_USP.Students_Student);
		ArrayList<Element> studentL = UtilParse.getElements(studentN);
		for (Element studentE : studentL) {
			String id = studentE.getAttribute(Value_USP.Attibute_Id);
			String label = studentE.getAttribute(Value_USP.Attibute_Label);
			ArrayList<String> courses = new ArrayList<>();
			NodeList coursesN = studentE.getElementsByTagName(Value_USP.StudentCourses_Course);
			ArrayList<Element> coursesL = UtilParse.getElements(coursesN);
			for (Element courseE : coursesL) {
				String s = courseE.getAttribute(Value_USP.Attibute_RefId);
				courses.add(s);
			}
			StudentUSP student = new StudentUSP(id, label, courses);
			students.add(student);
		}
	}

	private static void parseCourses(Element element) {
		NodeList courseN = element.getElementsByTagName(Value_USP.Courses_Course);
		ArrayList<Element> courseL = UtilParse.getElements(courseN);
		for (Element courseE : courseL) {
			CourseUSP cour = getCourse(courseE);
			courses.add(cour);
		}
	}

	private static CourseUSP getCourse(Element e) {
		String idCourse = e.getAttribute(Value_USP.Attibute_Id);
		String label = e.getAttribute(Value_USP.Attibute_Label);
		ArrayList<PartUSP> parts = new ArrayList<>();

		NodeList partN = e.getElementsByTagName(Value_USP.Course_Part);
		ArrayList<Element> partL = UtilParse.getElements(partN);
		for (Element partE : partL) {
			PartUSP PartU = getPartUSP(partE);
			parts.add(PartU);
		}
		CourseUSP cour = new CourseUSP(idCourse, parts);
		cour.setLabel(label);
		return cour;
	}

	private static PartUSP getPartUSP(Element part) {
		String idPart = part.getAttribute(Value_USP.Attibute_Id);
		String nrSessions = part.getAttribute(Value_USP.Attibute_NrSessions);
		String label = part.getAttribute(Value_USP.Attibute_Label);
		ArrayList<ClassUSP> classes = new ArrayList<>();
		AllowedSlotsUSP slotU = new AllowedSlotsUSP("", "", "", "");
		AllowedRoomsUSP roomU = new AllowedRoomsUSP("");
		AllowedTeachersUSP teacherU = new AllowedTeachersUSP("", new ArrayList<>());
		NodeList slotsL = part.getElementsByTagName(Value_USP.Part_AllowedSlots);
		Node n = slotsL.item(0);
		Element slot = (Element) n;
		slotU = getAllowedSlots(slot);

		NodeList roomsL = part.getElementsByTagName(Value_USP.Part_AllowedRooms);
		Node n1 = roomsL.item(0);
		Element room = (Element) n1;
		roomU = getAllowedRooms(room);

		NodeList teacherL = part.getElementsByTagName(Value_USP.Part_AllowedTeachers);
		Node n2 = teacherL.item(0);
		Element teacher = (Element) n2;
		teacherU = getAllowedTeachers(teacher);

		NodeList classeN = part.getElementsByTagName(Value_USP.Classes_Class);
		ArrayList<Element> classeL = UtilParse.getElements(classeN);
		for (Element classeE : classeL) {
			ClassUSP classU = getClassUSp(classeE);
			classes.add(classU);
		}

		return new PartUSP(idPart, nrSessions, label, classes, slotU, roomU, teacherU);
	}

	private static AllowedTeachersUSP getAllowedTeachers(Element teacher) {
		String sessionTeachers = teacher.getAttribute(Value_USP.Attibute_SessionTeachers);
		ArrayList<AllowedTeacher> teacherS = new ArrayList<>();

		NodeList teacherN = teacher.getElementsByTagName(Value_USP.AllowedTeachers_Teacher);
		ArrayList<Element> teacherL = UtilParse.getElements(teacherN);
		for (Element teacherE : teacherL) {
			String ref = teacherE.getAttribute(Value_USP.Attibute_RefId);
			String nrSessions = teacherE.getAttribute(Value_USP.Attibute_NrSessions);
			teacherS.add(new AllowedTeacher(ref, nrSessions));
		}
		return new AllowedTeachersUSP(sessionTeachers, teacherS);
	}

	private static AllowedRoomsUSP getAllowedRooms(Element room) {
		String sessionRooms = room.getAttribute(Value_USP.Attibute_SessionRooms);
		AllowedRoomsUSP roomS = new AllowedRoomsUSP(sessionRooms);

		NodeList roomN = room.getElementsByTagName(Value_USP.AllowedRooms_Room);
		ArrayList<Element> roomL = UtilParse.getElements(roomN);
		for (Element roomE : roomL) {
			String ref = roomE.getAttribute(Value_USP.Attibute_RefId);
			String mandatory = roomE.getAttribute(Value_USP.Attibute_Mandatory);
			roomS.add(new AllowedRoomUSP(ref, mandatory));
		}
		return roomS;
	}

	private static AllowedSlotsUSP getAllowedSlots(Element slot) {
		String sessionLength = slot.getAttribute(Value_USP.Attibute_SessionLength);
		String dailySlots = "", days = "", weeks = "";
		NodeList listN = slot.getChildNodes();
		ArrayList<Element> listL = UtilParse.getElements(listN);
		for (Element listE : listL) {
			switch (listE.getTagName()) {
			case "dailySlots":
				dailySlots = listE.getTextContent();
				break;
			case "days":
				days = listE.getTextContent();
				break;
			case "weeks":
				weeks = listE.getTextContent();
				break;
			}
		}
		return new AllowedSlotsUSP(sessionLength, dailySlots, days, weeks);
	}

	private static ClassUSP getClassUSp(Element classe) {
		String idClas = classe.getAttribute(Value_USP.Attibute_Id);
		String maxHeadCount = classe.getAttribute(Value_USP.Attibute_MaxHeadCount);
		String parent = classe.getAttribute(Value_USP.Attibute_Parent);
		ClassUSP res = new ClassUSP(idClas, maxHeadCount);
		res.setParent(parent);
		return res;
	}

	private static void parseTeachers(Element element) {
		NodeList teacherN = element.getElementsByTagName(Value_USP.AllowedTeachers_Teacher);
		ArrayList<Element> teacherL = UtilParse.getElements(teacherN);
		for (Element teacherE : teacherL) {
			String id = teacherE.getAttribute(Value_USP.Attibute_Id);
			String label = teacherE.getAttribute(Value_USP.Attibute_Label);

			TeacherUSP teachU = new TeacherUSP(id, label);
			teachers.add(teachU);
		}
	}

	private static void parseRooms(Element element) {
		NodeList roomN = element.getElementsByTagName(Value_USP.Rooms_Room);
		ArrayList<Element> roomL = UtilParse.getElements(roomN);
		for (Element roomE : roomL) {
			String id = roomE.getAttribute(Value_USP.Attibute_Id);
			String capacity = roomE.getAttribute(Value_USP.Attibute_Capacity);
			String label = roomE.getAttribute(Value_USP.Attibute_Label);

			RoomUSP roomU = new RoomUSP(id, capacity, label);
			rooms.add(roomU);
		}
	}

}
