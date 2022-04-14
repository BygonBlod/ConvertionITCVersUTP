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

public class ReadUSP {
	static Timetabling timeTabling;
	static ArrayList<RoomUSP> rooms;
	static ArrayList<TeacherUSP> teachers;
	static ArrayList<CourseUSP> courses;
	static ArrayList<StudentUSP> students;
	static ArrayList<RuleUSP> rules;
	static ArrayList<SolutionUSP> solution;

	public static Timetabling getTimeTabling(String args) {
		rooms = new ArrayList<>();
		teachers = new ArrayList<>();
		courses = new ArrayList<>();
		students = new ArrayList<>();
		rules = new ArrayList<>();
		solution = new ArrayList<>();
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
				String name = ele.getAttribute("name");
				String nrWeeks = ele.getAttribute("nrWeeks");
				String nrDaysPerWeek = ele.getAttribute("nrDaysPerWeek");
				String nrSlotsPerDay = ele.getAttribute("nrSlotsPerDay");
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
		NodeList solutionS = element.getChildNodes();
		if (solutionS != null) {
			for (int j = 0; j < solutionS.getLength(); j++) {
				Node n = solutionS.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					ArrayList<GroupSolutionUSP> groups = new ArrayList<>();
					ArrayList<SessionSolutionUSP> sessions = new ArrayList<>();
				}
			}
		}

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
					NodeList sessionsL = e.getElementsByTagName("sessions");
					if (sessionsL != null) {
						for (int i = 0; i < sessionsL.getLength(); i++) {
							Node sessionN = sessionsL.item(i);
							if (sessionN.getNodeType() == Node.ELEMENT_NODE) {
								Element sesEl = (Element) sessionN;
								String groupBy = sesEl.getAttribute("groupBy");
								String sessionMask = sesEl.getAttribute("sessionsMask");
								String attributeName = sesEl.getAttribute("attributeName");
								String inSes = sesEl.getAttribute("in");
								ArrayList<FilterUSP> filter = new ArrayList<>();
								NodeList filterL = sesEl.getElementsByTagName("filter");
								if (filterL != null) {
									for (int k = 0; k < filterL.getLength(); k++) {
										Node filterN = filterL.item(k);
										if (filterN.getNodeType() == Node.ELEMENT_NODE) {
											Element filterEl = (Element) filterN;
											String type = filterEl.getAttribute("type");
											String attributeNameFil = filterEl.getAttribute("attributeName");
											String inF = filterEl.getAttribute("in");
											String notIn = filterEl.getAttribute("notIn");
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
								session.add(ses);
							}
						}
					}
					NodeList constraintsL = e.getElementsByTagName("constraint");
					if (constraintsL != null) {
						for (int i = 0; i < constraintsL.getLength(); i++) {
							Node constraintN = constraintsL.item(i);
							if (constraintN.getNodeType() == Node.ELEMENT_NODE) {
								Element constraintEl = (Element) constraintN;
								String name = constraintEl.getAttribute("name");
								String type = constraintEl.getAttribute("type");
								ArrayList<ParameterUSP> parameters = new ArrayList<>();
								NodeList paramL = constraintEl.getElementsByTagName("parameter");
								if (paramL != null) {
									for (int k = 0; k < paramL.getLength(); k++) {
										Node paramN = paramL.item(k);
										if (paramN.getNodeType() == Node.ELEMENT_NODE) {
											Element paramE = (Element) paramN;
											String typeP = paramE.getAttribute("type");
											String nameP = paramE.getAttribute("name");
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
					String id = e.getAttribute("id");
					String label = e.getAttribute("label");
					ArrayList<String> courses = new ArrayList<>();
					NodeList list = e.getElementsByTagName("course");
					for (int i = 0; i < list.getLength(); i++) {
						Node course = list.item(i);
						if (course.getNodeType() == Node.ELEMENT_NODE) {
							Element courE = (Element) course;
							String s = courE.getAttribute("refId");
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
		String idCourse = e.getAttribute("id");
		ArrayList<PartUSP> parts = new ArrayList<>();

		NodeList partS = e.getElementsByTagName("part");
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
		return new CourseUSP(idCourse, parts);
	}

	private static PartUSP getPartUSP(Element part) {
		String idPart = part.getAttribute("id");
		String nrSessions = part.getAttribute("nrSessions");
		String label = part.getAttribute("label");
		ArrayList<ClassUSP> classes = new ArrayList<>();
		AllowedSlotsUSP slotU = new AllowedSlotsUSP("", "", "", "");
		AllowedRoomsUSP roomU = new AllowedRoomsUSP("", new ArrayList<>());
		AllowedTeachersUSP teacherU = new AllowedTeachersUSP("", new ArrayList<>());
		NodeList slotsL = part.getElementsByTagName("allowedSlots");
		Node n = slotsL.item(0);
		if (n.getNodeType() == Node.ELEMENT_NODE) {
			Element slot = (Element) n;
			slotU = getAllowedSlots(slot);
		}
		NodeList roomsL = part.getElementsByTagName("allowedRooms");
		Node n1 = roomsL.item(0);
		if (n1.getNodeType() == Node.ELEMENT_NODE) {
			Element room = (Element) n1;
			roomU = getAllowedRooms(room);
		}
		NodeList teacherL = part.getElementsByTagName("allowedTeachers");
		Node n2 = teacherL.item(0);
		if (n2.getNodeType() == Node.ELEMENT_NODE) {
			Element teacher = (Element) n2;
			teacherU = getAllowedTeachers(teacher);
		}

		NodeList classeL = part.getElementsByTagName("class");
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
		String sessionTeachers = teacher.getAttribute("sessionTeachers");
		ArrayList<AllowedTeacher> teacherS = new ArrayList<>();

		NodeList list = teacher.getElementsByTagName("teacher");
		for (int i = 0; i < list.getLength(); i++) {
			Node teacherN = list.item(i);
			if (teacherN.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) teacherN;
				String ref = ele.getAttribute("refId");
				String nrSessions = ele.getAttribute("nrSessions");
				teacherS.add(new AllowedTeacher(ref, nrSessions));
			}
		}
		return new AllowedTeachersUSP(sessionTeachers, teacherS);
	}

	private static AllowedRoomsUSP getAllowedRooms(Element room) {
		String sessionRooms = room.getAttribute("sessionRooms");
		ArrayList<AllowedRoomUSP> roomS = new ArrayList<>();

		NodeList list = room.getElementsByTagName("room");
		for (int i = 0; i < list.getLength(); i++) {
			Node roomN = list.item(i);
			if (roomN.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) roomN;
				String ref = ele.getAttribute("refId");
				String mandatory = ele.getAttribute("mandatory");
				roomS.add(new AllowedRoomUSP(ref, mandatory));
			}
		}
		return new AllowedRoomsUSP(sessionRooms, roomS);
	}

	private static AllowedSlotsUSP getAllowedSlots(Element slot) {
		String sessionLength = slot.getAttribute("sessionLength");
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
		String idClas = classe.getAttribute("id");
		String maxHeadCount = classe.getAttribute("maxHeadCount");
		String parent = classe.getAttribute("parent");
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
					String id = e.getAttribute("id");
					String label = e.getAttribute("label");

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
					String id = e.getAttribute("id");
					String capacity = e.getAttribute("capacity");
					String label = e.getAttribute("label");

					RoomUSP roomU = new RoomUSP(id, capacity, label);
					rooms.add(roomU);
				}
			}
		}
	}

}
