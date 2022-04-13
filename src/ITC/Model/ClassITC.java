package ITC.Model;

import java.util.ArrayList;

import Utils.Pair;

public class ClassITC {
	private String id;
	private String limit;
	private String parent;
	private ArrayList<Pair<String, String>> rooms;
	ArrayList<TimesPenaltyITC> times;

	public ClassITC(String id, String limit, ArrayList<Pair<String, String>> rooms, ArrayList<TimesPenaltyITC> times) {
		this.id = id;
		this.limit = limit;
		this.rooms = rooms;
		this.times = times;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public ArrayList<Pair<String, String>> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<Pair<String, String>> rooms) {
		this.rooms = rooms;
	}

	public ArrayList<TimesPenaltyITC> getTimes() {
		return times;
	}

	public void setTimes(ArrayList<TimesPenaltyITC> times) {
		this.times = times;
	}

}
