package USP.Model;

import java.util.ArrayList;

public class Timetabling {
	private String name;
	private String nrWeeks;
	private String nrDaysPerWeek;
	private String nrSlotsPerDay;
	private ArrayList<RoomUSP> rooms = new ArrayList<>();
	private ArrayList<TeacherUSP> teachers = new ArrayList<>();
	private ArrayList<CourseUSP> courses = new ArrayList<>();
	private ArrayList<StudentUSP> students = new ArrayList<>();
	private RulesUSP rules = new RulesUSP();
	private ArrayList<EquipmentUSP> equipments = new ArrayList<>();
	private SolutionUSP solution;

	public Timetabling(String name, String nrWeeks, String nrDaysPerWeek, String nrSlotsPerDay) {
		this.name = name;
		this.nrWeeks = nrWeeks;
		this.nrDaysPerWeek = nrDaysPerWeek;
		this.nrSlotsPerDay = nrSlotsPerDay;
		this.solution = new SolutionUSP();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNrWeeks() {
		return nrWeeks;
	}

	public void setNrWeeks(String nrWeeks) {
		this.nrWeeks = nrWeeks;
	}

	public String getNrDaysPerWeek() {
		return nrDaysPerWeek;
	}

	public void setNrDaysPerWeek(String nrDaysPerWeek) {
		this.nrDaysPerWeek = nrDaysPerWeek;
	}

	public String getNrSlotsPerDay() {
		return nrSlotsPerDay;
	}

	public void setNrSlotsPerDay(String nrSlotsPerDay) {
		this.nrSlotsPerDay = nrSlotsPerDay;
	}

	public ArrayList<RoomUSP> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<RoomUSP> rooms) {
		this.rooms = rooms;
	}

	public ArrayList<TeacherUSP> getTeachers() {
		return teachers;
	}

	public void setTeachers(ArrayList<TeacherUSP> teachers) {
		this.teachers = teachers;
	}

	public ArrayList<CourseUSP> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<CourseUSP> courses) {
		this.courses = courses;
	}

	public ArrayList<StudentUSP> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<StudentUSP> students) {
		this.students = students;
	}

	public RulesUSP getRules() {
		return rules;
	}

	public void setRules(RulesUSP rules) {
		this.rules = rules;
	}

	public ArrayList<EquipmentUSP> getEquipments() {
		return equipments;
	}

	public void setEquipments(ArrayList<EquipmentUSP> equipments) {
		this.equipments = equipments;
	}

	public SolutionUSP getSolution() {
		return solution;
	}

	public void setSolution(SolutionUSP solution) {
		this.solution = solution;
	}

}
