package ITC.ReadWrite;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;

import ITC.Model.ClassITC;
import ITC.Model.ClassRoomITC;
import ITC.Model.ClassRoomsITC;
import ITC.Model.ConfigITC;
import ITC.Model.CourseITC;
import ITC.Model.DistributionITC;
import ITC.Model.OptimizationITC;
import ITC.Model.ProblemITC;
import ITC.Model.RoomITC;
import ITC.Model.SolutionITC;
import ITC.Model.StudentITC;
import ITC.Model.SubpartITC;
import ITC.Model.TimesITC;
import ITC.Model.TimesPenaltyITC;
import ITC.Model.TravelITC;
import Utils.UtilParse;

public class ReadITC {
	static ArrayList<RoomITC> rooms = new ArrayList<RoomITC>();
	static ArrayList<DistributionITC> distributions = new ArrayList<>();
	static ArrayList<StudentITC> students = new ArrayList<>();
	static ArrayList<CourseITC> courses = new ArrayList<>();
	static ProblemITC problem;
	static OptimizationITC optimization;
	static boolean ajout = false;
	static String filename;
	public static int nbFile = 0;
	static ArrayList<String> lignes;

	public static ProblemITC getProblem(String args, boolean mul) {
		filename = args;
		ajout = false;
		lignes = new ArrayList<>();
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
			Node racine = document.getLastChild();
			if (racine.getNodeType() == Node.ELEMENT_NODE) {
				Element racineE = (Element) racine;
				String name = racineE.getAttribute("name");
				String nrDays = racineE.getAttribute("nrDays");
				String slotsPerDay = racineE.getAttribute("slotsPerDay");
				String nrWeeks = racineE.getAttribute("nrWeeks");
				problem = new ProblemITC(name, nrDays, slotsPerDay, nrWeeks);
				NodeList listRacine = racineE.getChildNodes();
				ArrayList<Element> racineL = UtilParse.getElements(listRacine);
				for (Element element : racineL) {
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
					case "solution":
						parseSolution(element);
						break;
					}
				}
			} else {
				System.out.println("la racine n'est pas un élément");
			}
		}
		problem.setCourses(courses);
		problem.setDistributions(distributions);
		problem.setOptimization(optimization);
		problem.setRooms(rooms);
		problem.setStudents(students);
		if (mul && ajout) {
			Path fichier = Paths.get("multipleConfig.txt");
			try {
				lignes.add(0, filename);
				lignes.add("\n");
				Files.write(fichier, lignes, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
				nbFile++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return problem;
	}

	private static void parseSolution(Element element) {
		String name = element.getAttribute("name");
		String runtime = element.getAttribute("runtime");
		String cores = element.getAttribute("cores");
		String technique = element.getAttribute("technique");
		String author = element.getAttribute("author");
		String institution = element.getAttribute("institution");
		String country = element.getAttribute("country");
		ArrayList<ClassITC> classes = new ArrayList<>();
		NodeList listClass = element.getElementsByTagName("class");
		ArrayList<Element> classL = UtilParse.getElements(listClass);
		for (Element classE : classL) {
			String days = classE.getAttribute("days");
			String start = classE.getAttribute("start");
			String weeks = classE.getAttribute("weeks");
			String room = classE.getAttribute("room");
			ArrayList<StudentITC> students = new ArrayList<>();
			NodeList listStudent = classE.getElementsByTagName("student");
			ArrayList<Element> studentL = UtilParse.getElements(listStudent);
			for (Element studentE : studentL) {
				String id = studentE.getAttribute("id");
				StudentITC studentI = new StudentITC(id);
				students.add(studentI);
			}
			classes.add(new ClassITC(country, days, start, weeks, room, students));
		}
		problem.setSolution(new SolutionITC(name, runtime, cores, technique, author, institution, country, classes));
	}

	private static void parseCourses(Element element) {
		NodeList cours = element.getElementsByTagName("course");
		ArrayList<Element> coursL = UtilParse.getElements(cours);
		for (Element cour : coursL) {
			CourseITC course = getCourseITC(cour);
			courses.add(course);
		}
	}

	private static CourseITC getCourseITC(Element cour) {
		String idCour = cour.getAttribute("id");
		ArrayList<ConfigITC> configs = new ArrayList<>();
		boolean multiple = false;
		NodeList configS = cour.getElementsByTagName("config");
		ArrayList<Element> configL = UtilParse.getElements(configS);
		if (configL.size() > 1) {
			multiple = true;
			ajout = true;
			lignes.add("cour :" + idCour);
		}
		for (Element config : configL) {
			if (multiple) {
				String id = config.getAttribute("id");
				lignes.add("	config :" + id);
			}
			ConfigITC configI = getConfigITC(config);
			configs.add(configI);
		}
		if (multiple) {
			lignes.add("");
		}
		return new CourseITC(idCour, configs);
	}

	private static ConfigITC getConfigITC(Element config) {
		String idConf = config.getAttribute("id");
		ArrayList<SubpartITC> subparts = new ArrayList<>();
		NodeList subpartS = config.getElementsByTagName("subpart");
		ArrayList<Element> subpartL = UtilParse.getElements(subpartS);
		for (Element subpart : subpartL) {
			SubpartITC subpartI = getSubpartITC(subpart);
			subparts.add(subpartI);
		}
		return new ConfigITC(idConf, subparts);
	}

	private static SubpartITC getSubpartITC(Element subpart) {
		String idSub = subpart.getAttribute("id");
		ArrayList<ClassITC> classes = new ArrayList<>();
		NodeList classeS = subpart.getElementsByTagName("class");
		ArrayList<Element> classeL = UtilParse.getElements(classeS);
		for (Element clas : classeL) {
			ClassITC classI = getClassITC(clas);
			classes.add(classI);
		}
		return new SubpartITC(idSub, classes);
	}

	private static ClassITC getClassITC(Element clas) {
		String idCla = clas.getAttribute("id");
		String limitCla = clas.getAttribute("limit");
		String parentCla = clas.getAttribute("parent");
		String room = clas.getAttribute("room");
		String daysCla = clas.getAttribute("days");
		String startCla = clas.getAttribute("start");
		String weeksCla = clas.getAttribute("weeks");
		ClassRoomsITC rooms = new ClassRoomsITC();
		ArrayList<TimesPenaltyITC> times = new ArrayList<>();
		NodeList roomN = clas.getElementsByTagName("room");
		ArrayList<Element> roomL = UtilParse.getElements(roomN);
		for (Element roomE : roomL) {
			String idRoom = roomE.getAttribute("id");
			String penalty = roomE.getAttribute("penalty");
			rooms.add(new ClassRoomITC(idRoom, penalty));
		}
		NodeList timeN = clas.getElementsByTagName("time");
		ArrayList<Element> timeL = UtilParse.getElements(timeN);
		for (Element time : timeL) {
			String days = time.getAttribute("days");
			String start = time.getAttribute("start");
			String length = time.getAttribute("length");
			String weeks = time.getAttribute("weeks");
			String penaltys = time.getAttribute("penalty");
			times.add(new TimesPenaltyITC(days, start, length, weeks, penaltys));
		}
		ClassITC clasRes = new ClassITC(idCla, limitCla, rooms, times);
		clasRes.setParent(parentCla);
		clasRes.setDays(daysCla);
		clasRes.setRoom(room);
		clasRes.setStart(startCla);
		clasRes.setWeeks(weeksCla);
		return clasRes;

	}

	public static void parseRoom(Element ele) {
		NodeList room = ele.getElementsByTagName("room");
		ArrayList<Element> roomL = UtilParse.getElements(room);
		for (Element e : roomL) {
			String id = e.getAttribute("id");
			String capacity = e.getAttribute("capacity");
			ArrayList<TravelITC> travel = new ArrayList<>();
			ArrayList<TimesITC> unavailable = new ArrayList<>();
			NodeList childTravel = e.getElementsByTagName("travel");
			ArrayList<Element> travelL = UtilParse.getElements(childTravel);
			for (Element trave : travelL) {
				String idRoom = trave.getAttribute("room");
				String value = trave.getAttribute("value");
				travel.add(new TravelITC(idRoom, value));
			}
			NodeList childUnavailable = e.getElementsByTagName("unavailable");
			ArrayList<Element> unaL = UtilParse.getElements(childUnavailable);
			for (Element una : unaL) {
				String days = una.getAttribute("days");
				String start = una.getAttribute("start");
				String length = una.getAttribute("length");
				String weeks = una.getAttribute("weeks");
				TimesITC time = new TimesITC(days, start, length, weeks);
				unavailable.add(time);
			}
			rooms.add(new RoomITC(id, travel, capacity, unavailable));

		}
	}

	public static void parseDistribution(Element ele) {
		NodeList distrib = ele.getElementsByTagName("distribution");
		ArrayList<Element> distribL = UtilParse.getElements(distrib);
		for (Element di : distribL) {
			String type = di.getAttribute("type");
			String required = di.getAttribute("required");
			String penalty = di.getAttribute("penalty");
			ArrayList<String> classD = new ArrayList<>();
			NodeList classDi = di.getElementsByTagName("class");
			ArrayList<Element> classDiL = UtilParse.getElements(classDi);
			for (Element clas : classDiL) {
				String id = clas.getAttribute("id");
				classD.add(id);
			}
			DistributionITC distribution = new DistributionITC(type, classD);
			distribution.setRequired(required);
			distribution.setPenalty(penalty);
			distributions.add(distribution);

		}

	}

	public static void parseStudent(Element ele) {
		NodeList studentS = ele.getElementsByTagName("student");
		ArrayList<Element> studentList = UtilParse.getElements(studentS);
		for (Element student : studentList) {
			String id = student.getAttribute("id");
			ArrayList<String> courses = new ArrayList<>();
			NodeList cours = student.getElementsByTagName("course");
			ArrayList<Element> courS = UtilParse.getElements(cours);
			for (Element cour : courS) {
				String idCour = cour.getAttribute("id");
				courses.add(idCour);
			}
			students.add(new StudentITC(id, courses));
		}
	}

}
