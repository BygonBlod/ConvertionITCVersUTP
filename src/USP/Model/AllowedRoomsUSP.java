package USP.Model;

import java.util.ArrayList;

public class AllowedRoomsUSP {
	private String sessionRooms;
	private ArrayList<AllowedRoomUSP> rooms;

	public AllowedRoomsUSP(String sessionRooms, ArrayList<AllowedRoomUSP> rooms) {
		this.sessionRooms = sessionRooms;
		this.rooms = rooms;
	}

	public String getSessionRooms() {
		return sessionRooms;
	}

	public void setSessionRooms(String sessionRooms) {
		this.sessionRooms = sessionRooms;
	}

	public ArrayList<AllowedRoomUSP> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<AllowedRoomUSP> rooms) {
		this.rooms = rooms;
	}

}
