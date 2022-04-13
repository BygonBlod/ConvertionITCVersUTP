package ITC.ReadWrite;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

public class WriteITC {

	public static void write(ProblemITC problem, String filename) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.newDocument();
			final Element racine = document.createElement("problem");
			document.appendChild(racine);
			racine.setAttribute("name", problem.getName());
			racine.setAttribute("nrDays", problem.getNrDays());
			racine.setAttribute("slotsPerDay", problem.getSlotsPerDay());
			racine.setAttribute("nrWeeks", problem.getNrWeeks());

			Element opt = document.createElement("optimization");
			racine.appendChild(opt);
			OptimizationITC optimization = problem.getOptimization();
			opt.setAttribute("time", optimization.getTime());
			opt.setAttribute("room", optimization.getRoom());
			opt.setAttribute("distribution", optimization.getDistribution());
			opt.setAttribute("student", optimization.getStudent());

			setRooms(document, racine, problem.getRooms());
			setCourses(document, racine, problem.getCourses());
			setDistributions(document, racine, problem.getDistributions());
			setStudents(document, racine, problem.getStudents());

			// écriture dans un fichier
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(document);
			final StreamResult sortie = new StreamResult(new File(filename));
			// final StreamResult sortie = new StreamResult(System.out);

			// prologue
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

			// formatage
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			// sortie
			transformer.transform(source, sortie);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}

	private static void setStudents(Document document, Element racine, ArrayList<StudentITC> students) {
		Element st = document.createElement("students");
		racine.appendChild(st);
		for (StudentITC stud : students) {
			Element stu = document.createElement("student");
			st.appendChild(stu);
			stu.setAttribute("id", stud.getId());
			for (String cour : stud.getCourses()) {
				Element courEle = document.createElement("course");
				stu.appendChild(courEle);
				courEle.setAttribute("id", cour);
			}
		}

	}

	private static void setDistributions(Document document, Element racine, ArrayList<DistributionITC> distributions) {
		Element dist = document.createElement("distributions");
		racine.appendChild(dist);
		for (DistributionITC distrib : distributions) {
			Element distri = document.createElement("distribution");
			dist.appendChild(distri);
			distri.setAttribute("type", distrib.getType());
			if (distrib.getRequired() != null) {
				distri.setAttribute("required", distrib.getRequired());
			}
			if (distrib.getPenalty() != null) {
				distri.setAttribute("penalty", distrib.getPenalty());
			}
			for (String s : distrib.getClassId()) {
				Element clas = document.createElement("class");
				distri.appendChild(clas);
				clas.setAttribute("id", s);
			}
		}

	}

	private static void setCourses(Document document, Element racine, ArrayList<CourseITC> courses) {
		Element cs = document.createElement("courses");
		racine.appendChild(cs);
		for (CourseITC course : courses) {
			Element courseEle = document.createElement("course");
			cs.appendChild(courseEle);
			courseEle.setAttribute("id", course.getId());
			for (ConfigITC config : course.getConfig()) {
				Element configEle = document.createElement("config");
				courseEle.appendChild(configEle);
				configEle.setAttribute("id", config.getId());
				for (SubpartITC subp : config.getSubpart()) {
					Element subEle = document.createElement("subpart");
					configEle.appendChild(subEle);
					subEle.setAttribute("id", subp.getId());
					for (ClassITC classe : subp.getClas()) {
						Element classEle = document.createElement("class");
						subEle.appendChild(classEle);
						classEle.setAttribute("id", classe.getId());
						classEle.setAttribute("limit", classe.getLimit());
						if (classe.getParent() != null) {
							classEle.setAttribute("parent", classe.getParent());
						}
						if (classe.getRooms().size() == 0) {
							classEle.setAttribute("room", "false");
						} else {
							for (Pair<String, String> room : classe.getRooms()) {
								Element roomEle = document.createElement("room");
								classEle.appendChild(roomEle);
								roomEle.setAttribute("id", room.getKey());
								roomEle.setAttribute("penalty", room.getValue());
							}
						}
						for (TimesPenaltyITC time : classe.getTimes()) {
							Element timeEle = document.createElement("time");
							classEle.appendChild(timeEle);
							timeEle.setAttribute("days", time.getDays());
							timeEle.setAttribute("start", time.getStart());
							timeEle.setAttribute("length", time.getLength());
							timeEle.setAttribute("weeks", time.getWeeks());
							timeEle.setAttribute("penalty", time.getPenalty());
						}
					}
				}
			}
		}

	}

	private static void setRooms(Document document, Element racine, ArrayList<RoomITC> roomS) {
		Element rooms = document.createElement("rooms");
		racine.appendChild(rooms);
		for (RoomITC room : roomS) {
			Element roomEle = document.createElement("room");
			rooms.appendChild(roomEle);
			roomEle.setAttribute("id", room.getId());
			roomEle.setAttribute("capacity", room.getCapacity());
			for (Pair<String, String> travel : room.getTravel()) {
				Element travelEle = document.createElement("travel");
				roomEle.appendChild(travelEle);
				travelEle.setAttribute("room", travel.getKey());
				travelEle.setAttribute("value", travel.getValue());
			}
			for (TimesITC time : room.getUnvailable()) {
				Element unaEle = document.createElement("unavailable");
				roomEle.appendChild(unaEle);
				unaEle.setAttribute("days", time.getDays());
				unaEle.setAttribute("start", time.getStart());
				unaEle.setAttribute("length", time.getLength());
				unaEle.setAttribute("weeks", time.getWeeks());
			}

		}

	}

}
