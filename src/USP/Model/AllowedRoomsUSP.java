package USP.Model;

import java.util.ArrayList;

public class AllowedRoomsUSP extends ArrayList<AllowedRoomUSP> {
	private String sessionRooms;

	public AllowedRoomsUSP(String sessionRooms) {
		this.sessionRooms = sessionRooms;
	}

	public String getSessionRooms() {
		return sessionRooms;
	}

	public void setSessionRooms(String sessionRooms) {
		this.sessionRooms = sessionRooms;
	}

	public boolean containsRoom(String id) {
		for (AllowedRoomUSP room : this) {
			if (room.getRefId().equals(id)) {
				return true;
			}
		}
		return false;
	}

}
