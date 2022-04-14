package USP.ReadWrite;

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

import USP.Model.AllowedRoomUSP;
import USP.Model.AllowedRoomsUSP;
import USP.Model.AllowedSlotsUSP;
import USP.Model.AllowedTeacher;
import USP.Model.AllowedTeachersUSP;
import USP.Model.ClassUSP;
import USP.Model.ConstraintUSP;
import USP.Model.CourseUSP;
import USP.Model.FilterUSP;
import USP.Model.ParameterUSP;
import USP.Model.PartUSP;
import USP.Model.RoomUSP;
import USP.Model.RuleUSP;
import USP.Model.SessionRuleUSP;
import USP.Model.StudentUSP;
import USP.Model.TeacherUSP;
import USP.Model.Timetabling;

public class WriteUSP {

	public static void write(Timetabling time, String filename) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.newDocument();
			final Element racine = document.createElement("timetabling");
			document.appendChild(racine);
			racine.setAttribute("name", time.getName());
			racine.setAttribute("nrDaysPerWeek", time.getNrDaysPerWeek());
			racine.setAttribute("nrSlotsPerDay", time.getNrSlotsPerDay());
			racine.setAttribute("nrWeeks", time.getNrWeeks());
			racine.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			racine.setAttribute("xsi:schemaLocation", "test");// à changer par usp_timetabling_v0_2.xsd

			setRooms(document, racine, time.getRooms());
			setTeachers(document, racine, time.getTeachers());
			setCourses(document, racine, time.getCourses());
			setStudents(document, racine, time.getStudents());
			setRules(document, racine, time.getRules());

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

	private static void setRules(Document document, Element racine, ArrayList<RuleUSP> rules) {
		Element ruleS = document.createElement("rules");
		racine.appendChild(ruleS);
		for (RuleUSP rule : rules) {
			Element ruleE = document.createElement("rule");
			ruleS.appendChild(ruleE);
			for (SessionRuleUSP ses : rule.getSessions()) {
				Element sesE = document.createElement("sessions");
				ruleE.appendChild(sesE);
				if (ses.getGroupBy() != null && !ses.getGroupBy().equals("")) {
					sesE.setAttribute("groupBy", ses.getGroupBy());
				}
				if (ses.getAttributeName() != null && !ses.getAttributeName().equals("")) {
					sesE.setAttribute("attributeName", ses.getAttributeName());
				}
				if (ses.getSessionsMask() != null && !ses.getSessionsMask().equals("")) {
					sesE.setAttribute("sessionsMask", ses.getSessionsMask());
				}
				if (ses.getIn() != null && !ses.getIn().equals("")) {
					sesE.setAttribute("in", ses.getIn());
				}
				if (ses.getFilter() != null) {
					ArrayList<FilterUSP> filters = ses.getFilter();
					for (FilterUSP filter : filters) {
						Element filterE = document.createElement("filter");
						sesE.appendChild(filterE);
						if (filter.getType() != null && !filter.getType().equals("")) {
							filterE.setAttribute("type", filter.getType());
						}
						if (filter.getAttributeName() != null && !filter.getAttributeName().equals("")) {
							filterE.setAttribute("attributeName", filter.getAttributeName());
						}
						if (filter.getIn() != null && !filter.getIn().equals("")) {
							filterE.setAttribute("in", filter.getIn());
						}
						if (filter.getNotIn() != null && !filter.getNotIn().equals("")) {
							filterE.setAttribute("notIn", filter.getNotIn());
						}
					}
				}
			}
			for (ConstraintUSP cons : rule.getConstraint()) {
				Element consE = document.createElement("constraint");
				ruleE.appendChild(consE);
				consE.setAttribute("name", cons.getName());
				consE.setAttribute("type", cons.getType());
				consE.setTextContent(" \n");
				if (cons.getParameters().size() > 0) {
					Element parameters = document.createElement("parameters");
					consE.appendChild(parameters);
					for (ParameterUSP param : cons.getParameters()) {
						Element paramE = document.createElement("parameter");
						parameters.appendChild(paramE);
						paramE.setAttribute("name", param.getName());
						if (param.getType() != null && !param.getType().equals("")) {
							paramE.setAttribute("type", param.getType());
						}
						if (param.getValue() != null && !param.getValue().equals("")) {
							paramE.setTextContent(param.getValue());
						}

					}
				}
			}
		}

	}

	private static void setCourses(Document document, Element racine, ArrayList<CourseUSP> courses) {
		Element courseS = document.createElement("courses");
		racine.appendChild(courseS);
		for (CourseUSP course : courses) {
			Element coursE = document.createElement("course");
			courseS.appendChild(coursE);
			coursE.setAttribute("id", course.getId());
			if (course.getLabel() != null && !course.getLabel().equals("")) {
				coursE.setAttribute("label", course.getLabel());
			}
			for (PartUSP part : course.getParts()) {
				Element partE = document.createElement("part");
				coursE.appendChild(partE);
				partE.setAttribute("id", part.getId());
				partE.setAttribute("nrSessions", part.getNrSessions());
				if (part.getLabel() != null && !part.getLabel().equals("")) {
					partE.setAttribute("label", part.getLabel());
				}
				Element classesE = document.createElement("classes");
				partE.appendChild(classesE);
				for (ClassUSP classe : part.getClasses()) {
					Element classE = document.createElement("class");
					classesE.appendChild(classE);
					classE.setAttribute("id", classe.getId());
					classE.setAttribute("maxHeadCount", classe.getMaxHeadCount());
					if (classe.getParent() != null && !classe.getParent().equals("")) {
						classE.setAttribute("parent", classe.getParent());
					}
				}
				AllowedSlotsUSP slots = part.getSlot();
				Element slotE = document.createElement("allowedSlots");
				partE.appendChild(slotE);
				slotE.setAttribute("sessionLength", slots.getSessionLength());
				Element daily = document.createElement("dailySlots");
				slotE.appendChild(daily);
				daily.setTextContent(slots.getDailySlots());
				Element days = document.createElement("days");
				slotE.appendChild(days);
				days.setTextContent(slots.getDays());
				Element weeks = document.createElement("weeks");
				slotE.appendChild(weeks);
				weeks.setTextContent(slots.getWeeks());

				AllowedRoomsUSP rooms = part.getRoom();
				Element roomsE = document.createElement("allowedRooms");
				partE.appendChild(roomsE);
				roomsE.setAttribute("sessionRooms", rooms.getSessionRooms());
				for (AllowedRoomUSP room : rooms.getRooms()) {
					Element roomE = document.createElement("room");
					roomsE.appendChild(roomE);
					roomE.setAttribute("refId", room.getRefId());
					if (room.getMandatory() != null && !room.getMandatory().equals("")) {
						roomE.setAttribute("mandatory", room.getMandatory());
					}
				}

				AllowedTeachersUSP teachers = part.getTeacher();
				Element teacherE = document.createElement("allowedTeachers");
				partE.appendChild(teacherE);
				teacherE.setAttribute("sessionTeachers", teachers.getSessionTeachers());
				for (AllowedTeacher teach : teachers.getTeacher()) {
					Element teachE = document.createElement("teacher");
					teacherE.appendChild(teachE);
					teachE.setAttribute("refId", teach.getRefId());
					teachE.setAttribute("nrSessions", teach.getNrSessions());
				}
			}

		}

	}

	private static void setStudents(Document document, Element racine, ArrayList<StudentUSP> students) {
		Element studentS = document.createElement("students");
		racine.appendChild(studentS);
		for (StudentUSP student : students) {
			Element studentE = document.createElement("student");
			studentS.appendChild(studentE);
			studentE.setAttribute("id", student.getId());
			studentE.setAttribute("label", student.getLabel());
			if (student.getCourses().size() > 0) {
				Element courses = document.createElement("courses");
				studentE.appendChild(courses);
				for (String s : student.getCourses()) {
					Element course = document.createElement("course");
					courses.appendChild(course);
					course.setAttribute("refId", s);
				}
			}
		}

	}

	private static void setTeachers(Document document, Element racine, ArrayList<TeacherUSP> teachers) {
		Element teacherS = document.createElement("teachers");
		racine.appendChild(teacherS);
		for (TeacherUSP teacher : teachers) {
			Element teacherE = document.createElement("teacher");
			teacherS.appendChild(teacherE);
			teacherE.setAttribute("id", teacher.getId());
			teacherE.setAttribute("label", teacher.getLabel());
		}

	}

	private static void setRooms(Document document, Element racine, ArrayList<RoomUSP> rooms) {
		Element roomS = document.createElement("rooms");
		racine.appendChild(roomS);
		for (RoomUSP room : rooms) {
			Element roomE = document.createElement("room");
			roomS.appendChild(roomE);
			roomE.setAttribute("id", room.getId());
			roomE.setAttribute("capacity", room.getCapacity());
			roomE.setAttribute("label", room.getLabel());
		}

	}

}
