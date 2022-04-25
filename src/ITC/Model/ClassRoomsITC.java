package ITC.Model;

import java.util.ArrayList;

public class ClassRoomsITC extends ArrayList<ClassRoomITC> {
	public ClassRoomsITC() {
		super();
	}

	public boolean containsRoom(String id) {
		for (ClassRoomITC room : this) {
			if (room.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

}
