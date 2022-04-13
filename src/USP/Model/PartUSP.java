package USP.Model;

import java.util.ArrayList;

public class PartUSP {
	private String id;
	private String nrSessions;
	private String label;
	private ArrayList<ClassUSP> classes;
	private AllowedSlotsUSP slot;
	private AllowedRoomsUSP room;
	private AllowedTeachersUSP teacher;

	public PartUSP(String id, String nrSessions, String label, ArrayList<ClassUSP> classes, AllowedSlotsUSP slot,
			AllowedRoomsUSP room, AllowedTeachersUSP teacher) {
		this.id = id;
		this.nrSessions = nrSessions;
		this.label = label;
		this.classes = classes;
		this.slot = slot;
		this.room = room;
		this.teacher = teacher;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNrSessions() {
		return nrSessions;
	}

	public void setNrSessions(String nrSessions) {
		this.nrSessions = nrSessions;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ArrayList<ClassUSP> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<ClassUSP> classes) {
		this.classes = classes;
	}

	public AllowedSlotsUSP getSlot() {
		return slot;
	}

	public void setSlot(AllowedSlotsUSP slot) {
		this.slot = slot;
	}

	public AllowedRoomsUSP getRoom() {
		return room;
	}

	public void setRoom(AllowedRoomsUSP room) {
		this.room = room;
	}

	public AllowedTeachersUSP getTeacher() {
		return teacher;
	}

	public void setTeacher(AllowedTeachersUSP teacher) {
		this.teacher = teacher;
	}

}
