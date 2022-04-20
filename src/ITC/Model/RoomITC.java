package ITC.Model;

import java.util.ArrayList;

public class RoomITC {
	private String id;
	private ArrayList<TravelITC> travel;
	private String capacity;
	private ArrayList<TimesITC> unvailable;

	public RoomITC(String id, ArrayList<TravelITC> travel, String capacity, ArrayList<TimesITC> unvailable) {
		this.id = id;
		this.travel = travel;
		this.capacity = capacity;
		this.unvailable = unvailable;
	}

	public ArrayList<TravelITC> getTravel() {
		return travel;
	}

	public void setTravel(ArrayList<TravelITC> travel) {
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
