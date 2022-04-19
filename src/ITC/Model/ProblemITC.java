package ITC.Model;

import java.util.ArrayList;

public class ProblemITC {
	private String name;
	private String nrDays;
	private String slotsPerDay;
	private String nrWeeks;

	private OptimizationITC optimization;
	private SolutionITC solution;
	// différentes listes
	private ArrayList<RoomITC> rooms = new ArrayList<>();
	private ArrayList<DistributionITC> distributions = new ArrayList<>();
	private ArrayList<StudentITC> students = new ArrayList<>();
	private ArrayList<CourseITC> courses = new ArrayList<>();

	public ProblemITC(String name, String nrDays, String slotsPerDay, String nrWeeks) {
		this.name = name;
		this.nrDays = nrDays;
		this.slotsPerDay = slotsPerDay;
		this.nrWeeks = nrWeeks;
		optimization = new OptimizationITC();
		solution = new SolutionITC();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNrDays() {
		return nrDays;
	}

	public void setNrDays(String nrDays) {
		this.nrDays = nrDays;
	}

	public String getSlotsPerDay() {
		return slotsPerDay;
	}

	public void setSlotsPerDay(String slotsPerDay) {
		this.slotsPerDay = slotsPerDay;
	}

	public String getNrWeeks() {
		return nrWeeks;
	}

	public void setNrWeeks(String nrWeeks) {
		this.nrWeeks = nrWeeks;
	}

	public OptimizationITC getOptimization() {
		return optimization;
	}

	public void setOptimization(OptimizationITC optimization) {
		this.optimization = optimization;
	}

	public ArrayList<RoomITC> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<RoomITC> rooms) {
		this.rooms = rooms;
	}

	public ArrayList<DistributionITC> getDistributions() {
		return distributions;
	}

	public void setDistributions(ArrayList<DistributionITC> distributions) {
		this.distributions = distributions;
	}

	public ArrayList<StudentITC> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<StudentITC> students) {
		this.students = students;
	}

	public ArrayList<CourseITC> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<CourseITC> courses) {
		this.courses = courses;
	}

	public SolutionITC getSolution() {
		return solution;
	}

	public void setSolution(SolutionITC solution) {
		this.solution = solution;
	}

}
