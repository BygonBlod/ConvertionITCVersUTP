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
				for (int i = 0; i < listes.getLength(); i++) {
					Node n = listes.item(i);
					if (n.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) n;
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
		NodeList group = element.getElementsByTagName(Value_USP.Groups_Group);
		if (group != null) {
			for (int i = 0; i < group.getLength(); i++) {
				Node nGroup = group.item(i);
				if (nGroup.getNodeType() == Node.ELEMENT_NODE) {
					Element eGroup = (Element) nGroup;
					GroupSolutionUSP groupU = getGroupUSP(eGroup);
					groups.add(groupU);
				}
			}
		}
		NodeList session = element.getElementsByTagName(Value_USP.SolutionSessions_Session);
		if (session != null) {
			for (int i = 0; i < session.getLength(); i++) {
				Node nSession = session.item(i);
				if (nSession.getNodeType() == Node.ELEMENT_NODE) {
					Element eSession = (Element) nSession;
					SessionSolutionUSP sessionU = getSessionUSP(eSession);
					sessions.add(sessionU);
				}
			}
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

	private static GroupSolutionUSP getGroupUSP(Element eGroup) {
		String id = eGroup.getAttribute(Value_USP.Attibute_Id);
		String headCount = eGroup.getAttribute(Value_USP.Attibute_HeadCount);
		ArrayList<String> students = new ArrayList<>();
		ArrayList<String> classes = new ArrayList<>();
		NodeList student = eGroup.getElementsByTagName(Value_USP.SolutionStudents_Student);
		if (student != null) {
			for (int i = 0; i < student.getLength(); i++) {
				Node nStudent = student.item(i);
				if (nStudent.getNodeType() == Node.ELEMENT_NODE) {
					Element eStudent = (Element) nStudent;
					String ref = eStudent.getAttribute(Value_USP.Attibute_RefId);
					students.add(ref);
				}
			}
		}
		NodeList classL = eGroup.getElementsByTagName(Value_USP.SolutionClasses_Class);
		if (classL != null) {
			for (int i = 0; i < classL.getLength(); i++) {
				Node nClass = classL.item(i);
				if (nClass.getNodeType() == Node.ELEMENT_NODE) {
					Element eClass = (Element) nClass;
					String ref = eClass.getAttribute(Value_USP.Attibute_RefId);
					classes.add(ref);
				}
			}
		}
		return new GroupSolutionUSP(id, headCount, students, classes);
	}

	private static void parseRules(Element element) {
		NodeList ruleS = element.getChildNodes();
		if (ruleS != null) {
			for (int j = 0; j < ruleS.getLength(); j++) {
				Node n = ruleS.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					ArrayList<SessionRuleUSP> session = new ArrayList<>();
					ArrayList<ConstraintUSP> constraints = new ArrayList<>();
					NodeList sessionsL = e.getElementsByTagName(Value_USP.Rule_Sessions);
					if (sessionsL != null) {
						for (int i = 0; i < sessionsL.getLength(); i++) {
							Node sessionN = sessionsL.item(i);
							if (sessionN.getNodeType() == Node.ELEMENT_NODE) {
								Element sesEl = (Element) sessionN;
								String groupBy = sesEl.getAttribute(Value_USP.Attibute_GroupBy);
								String sessionMask = sesEl.getAttribute(Value_USP.Attibute_SessionsMask);
								String attributeName = sesEl.getAttribute(Value_USP.Attibute_AttributeName);
								String inSes = sesEl.getAttribute(Value_USP.Attibute_In);
								String notInSes = sesEl.getAttribute(Value_USP.Attibute_NotIn);
								ArrayList<FilterUSP> filter = new ArrayList<>();
								NodeList filterL = sesEl.getElementsByTagName(Value_USP.Sessions_Filter);
								if (filterL != null) {
									for (int k = 0; k < filterL.getLength(); k++) {
										Node filterN = filterL.item(k);
										if (filterN.getNodeType() == Node.ELEMENT_NODE) {
											Element filterEl = (Element) filterN;
											String type = filterEl.getAttribute(Value_USP.Attibute_Type);
											String attributeNameFil = filterEl
													.getAttribute(Value_USP.Attibute_AttributeName);
											String inF = filterEl.getAttribute(Value_USP.Attibute_In);
											String notIn = filterEl.getAttribute(Value_USP.Attibute_NotIn);
											FilterUSP fil = new FilterUSP(type, attributeNameFil);
											fil.setIn(inF);
											fil.setNotIn(notIn);
											filter.add(fil);
										}
									}
								}
								SessionRuleUSP ses = new SessionRuleUSP(groupBy, filter);
								ses.setSessionsMask(sessionMask);
								ses.setAttributeName(attributeName);
								ses.setIn(inSes);
								ses.setNotIn(notInSes);
								session.add(ses);
							}
						}
					}
					NodeList constraintsL = e.getElementsByTagName(Value_USP.Rule_Constraint);
					if (constraintsL != null) {
						for (int i = 0; i < constraintsL.getLength(); i++) {
							Node constraintN = constraintsL.item(i);
							if (constraintN.getNodeType() == Node.ELEMENT_NODE) {
								Element constraintEl = (Element) constraintN;
								String name = constraintEl.getAttribute(Value_USP.Attibute_Name);
								String type = constraintEl.getAttribute(Value_USP.Attibute_Type);
								ArrayList<ParameterUSP> parameters = new ArrayList<>();
								NodeList paramL = constraintEl.getElementsByTagName(Value_USP.Prameters_Parameter);
								if (paramL != null) {
									for (int k = 0; k < paramL.getLength(); k++) {
										Node paramN = paramL.item(k);
										if (paramN.getNodeType() == Node.ELEMENT_NODE) {
											Element paramE = (Element) paramN;
											String typeP = paramE.getAttribute(Value_USP.Attibute_Type);
											String nameP = paramE.getAttribute(Value_USP.Attibute_Name);
											String value = paramE.getTextContent();
											ParameterUSP paramU = new ParameterUSP(nameP);
											paramU.setType(typeP);
											paramU.setValue(value);
											parameters.add(paramU);
										}
									}
								}
								ConstraintUSP consU = new ConstraintUSP(name, type, parameters);
								constraints.add(consU);
							}
						}
					}
					RuleUSP ruleU = new RuleUSP(session, constraints);
					rules.add(ruleU);
				}
			}
		}

	}

	private static void parseStudents(Element element) {
		NodeList studentS = element.getChildNodes();
		if (studentS != null) {
			for (int j = 0; j < studentS.getLength(); j++) {
				Node n = studentS.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					String id = e.getAttribute(Value_USP.Attibute_Id);
					String label = e.getAttribute(Value_USP.Attibute_Label);
					ArrayList<String> courses = new ArrayList<>();
					NodeList list = e.getElementsByTagName(Value_USP.StudentCourses_Course);
					for (int i = 0; i < list.getLength(); i++) {
						Node course = list.item(i);
						if (course.getNodeType() == Node.ELEMENT_NODE) {
							Element courE = (Element) course;
							String s = courE.getAttribute(Value_USP.Attibute_RefId);
							courses.add(s);
						}
					}

					StudentUSP student = new StudentUSP(id, label, courses);
					students.add(student);
				}
			}
		}

	}

	private static void parseCourses(Element element) {
		NodeList courseS = element.getChildNodes();
		if (courseS != null) {
			for (int j = 0; j < courseS.getLength(); j++) {
				Node n = courseS.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					CourseUSP cour = getCourse(e);
					courses.add(cour);

				}
			}
		}
	}

	private static CourseUSP getCourse(Element e) {
		String idCourse = e.getAttribute(Value_USP.Attibute_Id);
		String label = e.getAttribute(Value_USP.Attibute_Label);
		ArrayList<PartUSP> parts = new ArrayList<>();

		NodeList partS = e.getElementsByTagName(Value_USP.Course_Part);
		if (partS != null) {
			for (int j = 0; j < partS.getLength(); j++) {// pour chaque part
				Node nPart = partS.item(j);
				if (nPart.getNodeType() == Node.ELEMENT_NODE) {
					Element part = (Element) nPart;
					PartUSP PartU = getPartUSP(part);
					parts.add(PartU);
				}
			}
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
		AllowedRoomsUSP roomU = new AllowedRoomsUSP("", new ArrayList<>());
		AllowedTeachersUSP teacherU = new AllowedTeachersUSP("", new ArrayList<>());
		NodeList slotsL = part.getElementsByTagName(Value_USP.Part_AllowedSlots);
		Node n = slotsL.item(0);
		if (n.getNodeType() == Node.ELEMENT_NODE) {
			Element slot = (Element) n;
			slotU = getAllowedSlots(slot);
		}
		NodeList roomsL = part.getElementsByTagName(Value_USP.Part_AllowedRooms);
		Node n1 = roomsL.item(0);
		if (n1.getNodeType() == Node.ELEMENT_NODE) {
			Element room = (Element) n1;
			roomU = getAllowedRooms(room);
		}
		NodeList teacherL = part.getElementsByTagName(Value_USP.Part_AllowedTeachers);
		Node n2 = teacherL.item(0);
		if (n2.getNodeType() == Node.ELEMENT_NODE) {
			Element teacher = (Element) n2;
			teacherU = getAllowedTeachers(teacher);
		}

		NodeList classeL = part.getElementsByTagName(Value_USP.Classes_Class);
		for (int i = 0; i < classeL.getLength(); i++) {
			Node clas = classeL.item(i);
			if (clas.getNodeType() == Node.ELEMENT_NODE) {
				Element classe = (Element) clas;
				ClassUSP classU = getClassUSp(classe);
				classes.add(classU);
			}
		}

		return new PartUSP(idPart, nrSessions, label, classes, slotU, roomU, teacherU);
	}

	private static AllowedTeachersUSP getAllowedTeachers(Element teacher) {
		String sessionTeachers = teacher.getAttribute(Value_USP.Attibute_SessionTeachers);
		ArrayList<AllowedTeacher> teacherS = new ArrayList<>();

		NodeList list = teacher.getElementsByTagName(Value_USP.AllowedTeachers_Teacher);
		for (int i = 0; i < list.getLength(); i++) {
			Node teacherN = list.item(i);
			if (teacherN.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) teacherN;
				String ref = ele.getAttribute(Value_USP.Attibute_RefId);
				String nrSessions = ele.getAttribute(Value_USP.Attibute_NrSessions);
				teacherS.add(new AllowedTeacher(ref, nrSessions));
			}
		}
		return new AllowedTeachersUSP(sessionTeachers, teacherS);
	}

	private static AllowedRoomsUSP getAllowedRooms(Element room) {
		String sessionRooms = room.getAttribute(Value_USP.Attibute_SessionRooms);
		ArrayList<AllowedRoomUSP> roomS = new ArrayList<>();

		NodeList list = room.getElementsByTagName(Value_USP.AllowedRooms_Room);
		for (int i = 0; i < list.getLength(); i++) {
			Node roomN = list.item(i);
			if (roomN.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) roomN;
				String ref = ele.getAttribute(Value_USP.Attibute_RefId);
				String mandatory = ele.getAttribute(Value_USP.Attibute_Mandatory);
				roomS.add(new AllowedRoomUSP(ref, mandatory));
			}
		}
		return new AllowedRoomsUSP(sessionRooms, roomS);
	}

	private static AllowedSlotsUSP getAllowedSlots(Element slot) {
		String sessionLength = slot.getAttribute(Value_USP.Attibute_SessionLength);
		String dailySlots = "", days = "", weeks = "";
		NodeList list = slot.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;
				switch (ele.getTagName()) {
				case "dailySlots":
					dailySlots = ele.getTextContent();
					break;
				case "days":
					days = ele.getTextContent();
					break;
				case "weeks":
					weeks = ele.getTextContent();
					break;
				}
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
		NodeList teacherS = element.getChildNodes();
		if (teacherS != null) {
			for (int j = 0; j < teacherS.getLength(); j++) {
				Node n = teacherS.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					String id = e.getAttribute(Value_USP.Attibute_Id);
					String label = e.getAttribute(Value_USP.Attibute_Label);

					TeacherUSP teach = new TeacherUSP(id, label);
					teachers.add(teach);
				}
			}
		}

	}

	private static void parseRooms(Element element) {
		NodeList room = element.getChildNodes();
		if (room != null) {
			for (int j = 0; j < room.getLength(); j++) {
				Node n = room.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					String id = e.getAttribute(Value_USP.Attibute_Id);
					String capacity = e.getAttribute(Value_USP.Attibute_Capacity);
					String label = e.getAttribute(Value_USP.Attibute_Label);

					RoomUSP roomU = new RoomUSP(id, capacity, label);
					rooms.add(roomU);
				}
			}
		}
	}

}
