package ITC.Model;

import java.util.ArrayList;

import Utils.Pair;

public class RoomITC {
	private String id;
	private ArrayList<Pair<String, String>> travel;
	private String capacity;
	private ArrayList<TimesITC> unvailable;

	public RoomITC(String id, ArrayList<Pair<String, String>> travel, String capacity, ArrayList<TimesITC> unvailable) {
		this.id = id;
		this.travel = travel;
		this.capacity = capacity;
		this.unvailable = unvailable;
	}

	public String toString() {
		String s = "";
		s += "id: " + id;
		s += " cap: " + capacity;
		s += " travel :" + travel.toString();
		s += " unavailable:" + unvailable.toString();
		s += "\n";
		return s;
	}

	public ArrayList<Pair<String, String>> getTravel() {
		return travel;
	}

	public void setTravel(ArrayList<Pair<String, String>> travel) {
		this.travel = travel;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public ArrayList<TimesITC> getUnvailable() {
		return unvailable;
	}

	public void setUnvailable(ArrayList<TimesITC> unvailable) {
		this.unvailable = unvailable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
