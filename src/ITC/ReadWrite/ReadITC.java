package ITC.ReadWrite;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import ITC.Model.ClassITC;
import ITC.Model.ConfigITC;
import ITC.Model.CourseITC;
import ITC.Model.DistributionITC;
import ITC.Model.OptimizationITC;
import ITC.Model.ProblemITC;
import ITC.Model.RoomITC;
import ITC.Model.StudentITC;
import ITC.Model.SubpartITC;
import ITC.Model.TimesITC;
import ITC.Model.TimesPenaltyITC;
import Utils.Pair;

public class ReadITC {
	static ArrayList<RoomITC> rooms = new ArrayList<RoomITC>();
	static ArrayList<DistributionITC> distributions = new ArrayList<>();
	static ArrayList<StudentITC> students = new ArrayList<>();
	static ArrayList<CourseITC> courses = new ArrayList<>();
	static ProblemITC problem;
	static OptimizationITC optimization;

	public static ProblemITC getProblem(String args) {
		rooms = new ArrayList<>();
		distributions = new ArrayList<>();
		students = new ArrayList<>();
		courses = new ArrayList<>();
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
			NodeIterator iterator = traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT,
					null, true);
			for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
				try {
					Element element = (Element) n;
					switch (element.getTagName()) {
					case "rooms":
						parseRoom(element);
						break;
					case "distributions":
						parseDistribution(element);
						break;
					case "students":
						parseStudent(element);
						break;
					case "problem":
						String name = element.getAttribute("name");
						String nrDays = element.getAttribute("nrDays");
						String slotsPerDay = element.getAttribute("slotsPerDay");
						String nrWeeks = element.getAttribute("nrWeeks");
						problem = new ProblemITC(name, nrDays, slotsPerDay, nrWeeks);
						break;
					case "optimization":
						String time = element.getAttribute("time");
						String room = element.getAttribute("room");
						String distribution = element.getAttribute("distribution");
						String student = element.getAttribute("student");
						optimization = new OptimizationITC(time, room, distribution, student);
						break;
					case "courses":
						parseCourses(element);
						break;
					}
				} catch (Exception e) {
					System.out.println("erreur lors de la convertion en objet:");
					e.printStackTrace();
				}
			}

		}
		problem.setCourses(courses);
		problem.setDistributions(distributions);
		problem.setOptimization(optimization);
		problem.setRooms(rooms);
		problem.setStudents(students);
		return problem;
	}

	private static void parseCourses(Element element) {
		NodeList cours = element.getChildNodes();
		if (cours != null) {
			for (int i = 0; i < cours.getLength(); i++) {// pour chaque cour
				Node nCour = cours.item(i);
				if (nCour.getNodeType() == Node.ELEMENT_NODE) {
					Element cour = (Element) nCour;
					CourseITC course = getCourseITC(cour);
					courses.add(course);
				}
			}
		}
	}

	private static CourseITC getCourseITC(Element cour) {
		String idCour = cour.getAttribute("id");
		ArrayList<ConfigITC> configs = new ArrayList<>();

		NodeList configS = cour.getChildNodes();
		if (configS != null) {
			for (int j = 0; j < configS.getLength(); j++) {// pour chaque config
				Node nConfig = configS.item(j);
				if (nConfig.getNodeType() == Node.ELEMENT_NODE) {
					Element config = (Element) nConfig;
					ConfigITC configI = getConfigITC(config);
					configs.add(configI);
				}
			}
		}
		return new CourseITC(idCour, configs);
	}

	private static ConfigITC getConfigITC(Element config) {
		String idConf = config.getAttribute("id");
		ArrayList<SubpartITC> subparts = new ArrayList<>();
		NodeList subpartS = config.getChildNodes();
		if (subpartS != null) {
			for (int k = 0; k < subpartS.getLength(); k++) {// pour chaque subpart
				Node nSubpart = subpartS.item(k);
				if (nSubpart.getNodeType() == Node.ELEMENT_NODE) {
					Element subpart = (Element) nSubpart;
					SubpartITC subpartI = getSubpartITC(subpart);
					subparts.add(subpartI);
				}
			}
		}
		return new ConfigITC(idConf, subparts);
	}

	private static SubpartITC getSubpartITC(Element subpart) {
		String idSub = subpart.getAttribute("id");
		ArrayList<ClassITC> classes = new ArrayList<>();
		NodeList classeS = subpart.getChildNodes();
		if (classeS != null) {
			for (int l = 0; l < classeS.getLength(); l++) {// pour chaque class
				Node classe = classeS.item(l);
				if (classe.getNodeType() == Node.ELEMENT_NODE) {
					Element clas = (Element) classe;
					ClassITC classI = getClassITC(clas);
					classes.add(classI);
				}
			}
		}
		return new SubpartITC(idSub, classes);
	}

	private static ClassITC getClassITC(Element clas) {
		String idCla = clas.getAttribute("id");
		String limitCla = clas.getAttribute("limit");
		String parentCla = clas.getAttribute("parent");
		ArrayList<Pair<String, String>> rooms = new ArrayList<>();
		ArrayList<TimesPenaltyITC> times = new ArrayList<>();
		NodeList roomTi = clas.getChildNodes();
		if (roomTi != null) {
			for (int m = 0; m < roomTi.getLength(); m++) {// room and time of this class
				Node node = roomTi.item(m);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element nRoomTi = (Element) node;
					switch (nRoomTi.getTagName()) {
					case "room":
						String idRoom = nRoomTi.getAttribute("id");
						String penalty = nRoomTi.getAttribute("penalty");
						rooms.add(new Pair(idRoom, penalty));
						break;
					case "time":
						String days = nRoomTi.getAttribute("days");
						String start = nRoomTi.getAttribute("start");
						String length = nRoomTi.getAttribute("length");
						String weeks = nRoomTi.getAttribute("weeks");
						String penaltys = nRoomTi.getAttribute("penalty");
						times.add(new TimesPenaltyITC(days, start, length, weeks, penaltys));
						break;
					}
				}
			}
		}
		ClassITC clasRes = new ClassITC(idCla, limitCla, rooms, times);
		if (!parentCla.equals("")) {
			clasRes.setParent(parentCla);
		}
		return clasRes;

	}

	public static void parseRoom(Element ele) {
		NodeList room = ele.getChildNodes();
		if (room != null) {
			for (int j = 0; j < room.getLength(); j++) {
				Node n = room.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					String id = e.getAttribute("id");
					String capacity = e.getAttribute("capacity");
					ArrayList<Pair<String, String>> travel = new ArrayList<>();
					ArrayList<TimesITC> unavailable = new ArrayList<>();
					NodeList child = e.getChildNodes();
					if (child != null) {
						for (int i = 0; i < child.getLength(); i++) {
							Node node = child.item(i);

							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element element = (Element) node;
								try {
									switch (element.getTagName()) {
									case "travel":
										String idRoom = element.getAttribute("room");
										String value = element.getAttribute("value");
										travel.add(new Pair(idRoom, value));
										break;
									case "unavailable":
										String days = element.getAttribute("days");
										String start = element.getAttribute("start");
										String length = element.getAttribute("length");
										String weeks = element.getAttribute("weeks");
										TimesITC time = new TimesITC(days, start, length, weeks);
										unavailable.add(time);
										break;
									}
								} catch (Exception exc) {
									System.out.println("erreur lors du parcour de " + id);

								}
							}
						}
					}
					rooms.add(new RoomITC(id, travel, capacity, unavailable));
				}
			}
		}
	}

	public static void parseDistribution(Element ele) {
		NodeList distrib = ele.getChildNodes();
		if (distrib != null) {
			for (int j = 0; j < distrib.getLength(); j++) {
				Node n = distrib.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element di = (Element) n;
					String type = di.getAttribute("type");
					String required = di.getAttribute("required");
					String penalty = di.getAttribute("penalty");
					ArrayList<String> classD = new ArrayList<>();
					NodeList classDi = di.getChildNodes();
					if (classDi != null) {
						for (int i = 0; i < classDi.getLength(); i++) {
							Node node = classDi.item(i);
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element clas = (Element) node;
								String id = clas.getAttribute("id");
								classD.add(id);
							}
						}
					}
					DistributionITC distribution = new DistributionITC(type, classD);
					if (!required.equals("")) {
						distribution.setRequired(required);
					}
					if (!penalty.equals("")) {
						distribution.setPenalty(penalty);
					}
					distributions.add(distribution);

				}
			}
		}

	}

	public static void parseStudent(Element ele) {
		NodeList studentS = ele.getChildNodes();
		if (studentS != null) {
			for (int j = 0; j < studentS.getLength(); j++) {
				Node n = studentS.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element student = (Element) n;
					String id = student.getAttribute("id");
					ArrayList<String> courses = new ArrayList<>();
					NodeList cours = student.getChildNodes();
					if (cours != null) {
						for (int i = 0; i < cours.getLength(); i++) {
							Node node = cours.item(i);
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								Element cour = (Element) node;
								String idCour = cour.getAttribute("id");
								courses.add(idCour);
							}
						}
					}
					students.add(new StudentITC(id, courses));
				}
			}
		}
	}

}
