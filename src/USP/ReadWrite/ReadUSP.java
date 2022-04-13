package USP.ReadWrite;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;

import USP.Model.ConstraintUSP;
import USP.Model.CourseUSP;
import USP.Model.FilterUSP;
import USP.Model.ParameterUSP;
import USP.Model.RoomUSP;
import USP.Model.RuleUSP;
import USP.Model.SessionRuleUSP;
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
									Node filterN = filterL.item(0);
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
				}
			}
		}
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
