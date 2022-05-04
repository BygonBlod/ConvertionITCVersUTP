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
	private ClassRoomsITC rooms;
	private TimesPenaltysITC times;
	private ArrayList<StudentITC> students;

	public ClassITC(String id, String limit, ClassRoomsITC rooms, TimesPenaltysITC times) {
		this.id = id;
		this.limit = limit;
		this.parent = "";
		this.days = "";
		this.start = "";
		this.weeks = "";
		this.room = "";
		this.rooms = rooms;
		this.times = times;
		this.students = new ArrayList<>();
	}

	public ClassITC(String id, String days, String start, String weeks, String room, ArrayList<StudentITC> students) {
		this.id = id;
		this.limit = "";
		this.days = days;
		this.start = start;
		this.weeks = weeks;
		this.room = room;
		this.students = students;
		this.rooms = new ClassRoomsITC();
		this.times = new TimesPenaltysITC();
	}

	public ArrayList<String> getForbiddenPeriod(int min, int max) {
		ArrayList<String> res = new ArrayList<>();
		ArrayList<TimesPenaltysITC> timesTri = triDay();
		TimesPenaltyITC temp = getTimes().get(0);
		int startForb = min;
		int endForb = 0;
		int day = 0;
		int dayTime = 1440;

		for (TimesPenaltysITC times : timesTri) {
			for (TimesPenaltyITC time : times) {
				int startC = Integer.parseInt(time.getStartUSP());
				int length = Integer.parseInt(time.getLengthUSP());
				endForb = (dayTime * day) + startC - 1;
				res.add(startForb + "-" + endForb);
				startForb = (dayTime * day) + startC + length + 1;

			}
			day++;
		}
		endForb = max;
		res.add(startForb + "-" + endForb);
		return res;
	}

	public ArrayList<TimesPenaltysITC> triDay() {
		ArrayList<TimesPenaltysITC> res = new ArrayList<>();
		TimesPenaltyITC temp = getTimes().get(0);
		if (temp != null) {
			for (int i = 0; i < temp.getWeeks().length(); i++) {// pour chaque semaine
				for (int j = 0; j < temp.getDays().length(); j++) {// pour chaque jour
					TimesPenaltysITC times = new TimesPenaltysITC();
					for (TimesPenaltyITC time : getTimes()) {
						if (time.getWeeks().charAt(i) == '1' && time.getDays().charAt(j) == '1') {
							times.add(time);
						}
					}
					res.add(times);
					// System.out.println("semaines " + (i + 1) + " jours " + (j + 1) + " ou jour "
					// + ((i * 7) + j + 1)
					// + " " + times.size() + " " + res.size());
				}
			}
		}
		return res;
	}

	public boolean checkWeeks() {
		String weeks = getWeeks();
		for (int i = 1; i < weeks.length() - 1; i++) {
			char c1 = weeks.charAt(i - 1);
			char c2 = weeks.charAt(i);
			char c3 = weeks.charAt(i + 1);
			if (c1 == '1' && c3 == '1' && c2 != '1') {
				return false;
			}

		}
		return true;
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

	public ClassRoomsITC getRooms() {
		return rooms;
	}

	public void setRooms(ClassRoomsITC rooms) {
		this.rooms = rooms;
	}

	public TimesPenaltysITC getTimes() {
		return times;
	}

	public void setTimes(TimesPenaltysITC times) {
		this.times = times;
	}

	public ArrayList<StudentITC> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<StudentITC> students) {
		this.students = students;
	}

	public int getNbSessionWeek() {
		int res = 0;
		if (getTimes().size() > 0) {
			TimesPenaltyITC time = getTimes().get(0);
			String days = time.getDays();
			for (int i = 0; i < days.length(); i++) {
				if (days.charAt(i) == '1') {
					res++;
				}
			}
		}
		return res;
	}

	public String getNrSession() {
		if (times.size() > 0) {
			return times.get(0).getNbSes() + "";
		}
		return "";
	}

}
