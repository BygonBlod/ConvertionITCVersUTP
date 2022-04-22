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

public class WriteUSP {

	public static void write(Timetabling time, String filename) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.newDocument();
			final Element racine = document.createElement("timetabling");
			document.appendChild(racine);
			racine.setAttribute(Value_USP.Attibute_Name, time.getName());
			racine.setAttribute(Value_USP.Attibute_NrDaysPerWeek, time.getNrDaysPerWeek());
			racine.setAttribute(Value_USP.Attibute_NrSlotsPerDay, time.getNrSlotsPerDay());
			racine.setAttribute(Value_USP.Attibute_NrWeeks, time.getNrWeeks());
			racine.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			racine.setAttribute("xsi:schemaLocation", "test");// à changer par usp_timetabling_v0_2.xsd

			setRooms(document, racine, time.getRooms());
			setTeachers(document, racine, time.getTeachers());
			setCourses(document, racine, time.getCourses());
			setStudents(document, racine, time.getStudents());
			setRules(document, racine, time.getRules());
			setSolution(document, racine, time.getSolution());

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

	private static void setSolution(Document document, Element racine, SolutionUSP solution) {
		Element solutionE = document.createElement(Value_USP.Time_Solution);
		racine.appendChild(solutionE);
		Element groupsE = document.createElement(Value_USP.Solution_Groups);
		solutionE.appendChild(groupsE);
		for (GroupSolutionUSP group : solution.getGroups()) {
			Element groupE = document.createElement(Value_USP.Groups_Group);
			groupsE.appendChild(groupE);
			groupE.setAttribute(Value_USP.Attibute_Id, group.getId());
			groupE.setAttribute(Value_USP.Attibute_HeadCount, group.getHeadCount());
			Element studentsE = document.createElement(Value_USP.Group_Students);
			groupE.appendChild(studentsE);
			for (String std : group.getStudents()) {
				Element studentE = document.createElement(Value_USP.SolutionStudents_Student);
				studentsE.appendChild(studentE);
				studentE.setAttribute(Value_USP.Attibute_RefId, std);
			}
			Element classesE = document.createElement(Value_USP.Group_Classes);
			groupE.appendChild(classesE);
			for (String cla : group.getClasses()) {
				Element classE = document.createElement(Value_USP.SolutionClasses_Class);
				classesE.appendChild(classE);
				classE.setAttribute(Value_USP.Attibute_RefId, cla);
			}
		}
		Element sessionsE = document.createElement(Value_USP.Solution_Sessions);
		solutionE.appendChild(sessionsE);
		for (SessionSolutionUSP ses : solution.getSessions()) {
			Element sessionE = document.createElement(Value_USP.SolutionSessions_Session);
			sessionsE.appendChild(sessionE);
			sessionE.setAttribute(Value_USP.Attibute_Class, ses.getClasse());
			sessionE.setAttribute(Value_USP.Attibute_Rank, ses.getRank());
			if (ses.getSlot() != null && !ses.getSlot().equals("")) {
				sessionE.setAttribute(Value_USP.Attibute_Slot, ses.getSlot());
			}
			if (ses.getRooms() != null && !ses.getRooms().equals("")) {
				sessionE.setAttribute(Value_USP.Attibute_Rooms, ses.getRooms());
			}
			if (ses.getTeachers() != null && !ses.getTeachers().equals("")) {
				sessionE.setAttribute(Value_USP.Attibute_Teachers, ses.getTeachers());
			}
		}
	}

	private static void setRules(Document document, Element racine, ArrayList<RuleUSP> rules) {
		Element ruleS = document.createElement(Value_USP.Time_Rules);
		racine.appendChild(ruleS);
		for (RuleUSP rule : rules) {
			Element ruleE = document.createElement(Value_USP.Rules_Rule);
			ruleS.appendChild(ruleE);
			for (SessionRuleUSP ses : rule.getSessions()) {
				Element sesE = document.createElement(Value_USP.Rule_Sessions);
				ruleE.appendChild(sesE);
				if (ses.getGroupBy() != null && !ses.getGroupBy().equals("")) {
					sesE.setAttribute(Value_USP.Attibute_GroupBy, ses.getGroupBy());
				}
				if (ses.getAttributeName() != null && !ses.getAttributeName().equals("")) {
					sesE.setAttribute(Value_USP.Attibute_AttributeName, ses.getAttributeName());
				}
				if (ses.getSessionsMask() != null && !ses.getSessionsMask().equals("")) {
					sesE.setAttribute(Value_USP.Attibute_SessionsMask, ses.getSessionsMask());
				}
				if (ses.getIn() != null && !ses.getIn().equals("")) {
					sesE.setAttribute(Value_USP.Attibute_In, ses.getIn());
				}
				if (ses.getNotIn() != null && !ses.getNotIn().equals("")) {
					sesE.setAttribute(Value_USP.Attibute_NotIn, ses.getNotIn());
				}
				if (ses.getFilter() != null) {
					ArrayList<FilterUSP> filters = ses.getFilter();
					for (FilterUSP filter : filters) {
						Element filterE = document.createElement(Value_USP.Sessions_Filter);
						sesE.appendChild(filterE);
						if (filter.getType() != null && !filter.getType().equals("")) {
							filterE.setAttribute(Value_USP.Attibute_Type, filter.getType());
						}
						if (filter.getAttributeName() != null && !filter.getAttributeName().equals("")) {
							filterE.setAttribute(Value_USP.Attibute_AttributeName, filter.getAttributeName());
						}
						if (filter.getIn() != null && !filter.getIn().equals("")) {
							filterE.setAttribute(Value_USP.Attibute_In, filter.getIn());
						}
						if (filter.getNotIn() != null && !filter.getNotIn().equals("")) {
							filterE.setAttribute(Value_USP.Attibute_NotIn, filter.getNotIn());
						}
					}
				}
			}
			for (ConstraintUSP cons : rule.getConstraint()) {
				Element consE = document.createElement(Value_USP.Rule_Constraint);
				ruleE.appendChild(consE);
				consE.setAttribute(Value_USP.Attibute_Name, cons.getName());
				consE.setAttribute(Value_USP.Attibute_Type, cons.getType());
				consE.setTextContent(" \n");
				if (cons.getParameters().size() > 0) {
					Element parameters = document.createElement(Value_USP.Constraint_Parameters);
					consE.appendChild(parameters);
					for (ParameterUSP param : cons.getParameters()) {
						Element paramE = document.createElement(Value_USP.Prameters_Parameter);
						parameters.appendChild(paramE);
						paramE.setAttribute(Value_USP.Attibute_Name, param.getName());
						if (param.getType() != null && !param.getType().equals("")) {
							paramE.setAttribute(Value_USP.Attibute_Type, param.getType());
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
		Element courseS = document.createElement(Value_USP.Time_Coursess);
		racine.appendChild(courseS);
		for (CourseUSP course : courses) {
			Element coursE = document.createElement(Value_USP.Courses_Course);
			courseS.appendChild(coursE);
			coursE.setAttribute(Value_USP.Attibute_Id, course.getId());
			if (course.getLabel() != null && !course.getLabel().equals("")) {
				coursE.setAttribute(Value_USP.Attibute_Label, course.getLabel());
			}
			for (PartUSP part : course.getParts()) {
				Element partE = document.createElement(Value_USP.Course_Part);
				coursE.appendChild(partE);
				partE.setAttribute(Value_USP.Attibute_Id, part.getId());
				partE.setAttribute(Value_USP.Attibute_NrSessions, part.getNrSessions());
				if (part.getLabel() != null && !part.getLabel().equals("")) {
					partE.setAttribute(Value_USP.Attibute_Label, part.getLabel());
				}
				Element classesE = document.createElement(Value_USP.Part_Classes);
				partE.appendChild(classesE);
				for (ClassUSP classe : part.getClasses()) {
					Element classE = document.createElement(Value_USP.Classes_Class);
					classesE.appendChild(classE);
					classE.setAttribute(Value_USP.Attibute_Id, classe.getId());
					classE.setAttribute(Value_USP.Attibute_MaxHeadCount, classe.getMaxHeadCount());
					if (classe.getParent() != null && !classe.getParent().equals("")) {
						classE.setAttribute(Value_USP.Attibute_Parent, classe.getParent());
					}
				}
				AllowedSlotsUSP slots = part.getSlot();
				Element slotE = document.createElement(Value_USP.Part_AllowedSlots);
				partE.appendChild(slotE);
				slotE.setAttribute("sessionLength", slots.getSessionLength());
				Element daily = document.createElement(Value_USP.AllowedSlots_DailySlots);
				slotE.appendChild(daily);
				daily.setTextContent(slots.getDailySlots());
				Element days = document.createElement(Value_USP.AllowedSlots_Days);
				slotE.appendChild(days);
				days.setTextContent(slots.getDays());
				Element weeks = document.createElement(Value_USP.AllowedSlots_Weeks);
				slotE.appendChild(weeks);
				weeks.setTextContent(slots.getWeeks());

				AllowedRoomsUSP rooms = part.getRoom();
				Element roomsE = document.createElement(Value_USP.Part_AllowedRooms);
				partE.appendChild(roomsE);
				roomsE.setAttribute(Value_USP.Attibute_SessionRooms, rooms.getSessionRooms());
				for (AllowedRoomUSP room : rooms) {
					Element roomE = document.createElement(Value_USP.AllowedRooms_Room);
					roomsE.appendChild(roomE);
					roomE.setAttribute(Value_USP.Attibute_RefId, room.getRefId());
					if (room.getMandatory() != null && !room.getMandatory().equals("")) {
						roomE.setAttribute(Value_USP.Attibute_Mandatory, room.getMandatory());
					}
				}

				AllowedTeachersUSP teachers = part.getTeacher();
				Element teacherE = document.createElement(Value_USP.Part_AllowedTeachers);
				partE.appendChild(teacherE);
				teacherE.setAttribute(Value_USP.Attibute_SessionTeachers, teachers.getSessionTeachers());
				for (AllowedTeacher teach : teachers.getTeacher()) {
					Element teachE = document.createElement(Value_USP.AllowedTeachers_Teacher);
					teacherE.appendChild(teachE);
					teachE.setAttribute(Value_USP.Attibute_RefId, teach.getRefId());
					teachE.setAttribute(Value_USP.Attibute_NrSessions, teach.getNrSessions());
				}
			}

		}

	}

	private static void setStudents(Document document, Element racine, ArrayList<StudentUSP> students) {
		Element studentS = document.createElement(Value_USP.Time_Students);
		racine.appendChild(studentS);
		for (StudentUSP student : students) {
			Element studentE = document.createElement(Value_USP.Students_Student);
			studentS.appendChild(studentE);
			studentE.setAttribute(Value_USP.Attibute_Id, student.getId());
			if (!student.getLabel().equals("")) {
				studentE.setAttribute(Value_USP.Attibute_Label, student.getLabel());
			}
			if (student.getCourses().size() > 0) {
				Element courses = document.createElement(Value_USP.Student_Courses);
				studentE.appendChild(courses);
				for (String s : student.getCourses()) {
					Element course = document.createElement(Value_USP.StudentCourses_Course);
					courses.appendChild(course);
					course.setAttribute(Value_USP.Attibute_RefId, s);
				}
			}
		}

	}

	private static void setTeachers(Document document, Element racine, ArrayList<TeacherUSP> teachers) {
		Element teacherS = document.createElement(Value_USP.Time_Teachers);
		racine.appendChild(teacherS);
		for (TeacherUSP teacher : teachers) {
			Element teacherE = document.createElement(Value_USP.Teachers_Teacher);
			teacherS.appendChild(teacherE);
			teacherE.setAttribute(Value_USP.Attibute_Id, teacher.getId());
			if (!teacher.getLabel().equals("")) {
				teacherE.setAttribute(Value_USP.Attibute_Label, teacher.getLabel());
			}
		}

	}

	private static void setRooms(Document document, Element racine, ArrayList<RoomUSP> rooms) {
		Element roomS = document.createElement(Value_USP.Time_Rooms);
		racine.appendChild(roomS);
		for (RoomUSP room : rooms) {
			Element roomE = document.createElement(Value_USP.Rooms_Room);
			roomS.appendChild(roomE);
			roomE.setAttribute(Value_USP.Attibute_Id, room.getId());
			roomE.setAttribute(Value_USP.Attibute_Capacity, room.getCapacity());
			if (!room.getLabel().equals("")) {
				roomE.setAttribute(Value_USP.Attibute_Label, room.getLabel());
			}
		}

	}

}
