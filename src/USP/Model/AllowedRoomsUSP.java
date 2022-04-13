package USP.Model;

import java.util.ArrayList;

public class AllowedRoomsUSP {
	private String sessionRooms;
	private ArrayList<String> rooms;

	public AllowedRoomsUSP(String sessionRooms, ArrayList<String> rooms) {
		this.sessionRooms = sessionRooms;
		this.rooms = rooms;
	}

	public String getSessionRooms() {
		return sessionRooms;
	}

	public void setSessionRooms(String sessionRooms) {
		this.sessionRooms = sessionRooms;
	}

	public ArrayList<String> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<String> rooms) {
		this.rooms = rooms;
	}

}
