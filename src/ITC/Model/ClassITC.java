package ITC.Model;

import java.util.ArrayList;

public class ClassITC {
	private String id;
	private String limit;
	private String parent;
	private String days;
	private String start;
	private String weeks;
	private String room;
	private ArrayList<ClassRoomITC> rooms;
	private ArrayList<TimesPenaltyITC> times;
	private ArrayList<StudentITC> students;

	public ClassITC(String id, String limit, ArrayList<ClassRoomITC> rooms, ArrayList<TimesPenaltyITC> times) {
		this.id = id;
		this.limit = limit;
		this.rooms = rooms;
		this.times = times;
	}

	public ClassITC(String id, String days, String start, String weeks, String room, ArrayList<StudentITC> students) {
		this.id = id;
		this.days = days;
		this.start = start;
		this.weeks = weeks;
		this.room = room;
		this.students = students;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public ArrayList<ClassRoomITC> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<ClassRoomITC> rooms) {
		this.rooms = rooms;
	}

	public ArrayList<TimesPenaltyITC> getTimes() {
		return times;
	}

	public void setTimes(ArrayList<TimesPenaltyITC> times) {
		this.times = times;
	}

	public ArrayList<StudentITC> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<StudentITC> students) {
		this.students = students;
	}

}
