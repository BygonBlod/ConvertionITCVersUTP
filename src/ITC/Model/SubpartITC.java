package ITC.Model;

import java.util.ArrayList;

public class SubpartITC {
	private String id;
	ClassesITC clas;

	public SubpartITC(String id, ClassesITC clas) {
		this.id = id;
		this.clas = clas;
	}

	public TimesPenaltysITC getAllTimes() {
		TimesPenaltysITC res = new TimesPenaltysITC();
		for (ClassITC classe : clas) {
			for (TimesPenaltyITC time : classe.getTimes()) {
				if (!res.containsTime4(time.getDays(), time.getWeeks(), time.getStart(), time.getLength())) {
					res.add(time);
				}
			}
		}
		return res;
	}

	public boolean sameTimesLSD(String i) {
		TimesPenaltysITC times = getAllTimes();
		String length = "";
		String start = "";
		String days = "";
		for (TimesPenaltyITC time : times) {
			if (length.equals("")) {
				length = time.getLength();
				start = time.getStart();
				days = time.getDays();
			} else {
				if (!length.equals(time.getLength()) || !start.equals(time.getStart())
						|| !days.equals(time.getDays())) {
					// if sur les consécutives
					return false;
				}
			}
		}
		return true;
	}

	public String getWeeksLSD() {
		ArrayList<Integer> s = new ArrayList<>();
		TimesPenaltysITC times = getAllTimes();
		if (times.checkAllWeeks()) {
			for (TimesPenaltyITC time : times) {
				ArrayList<Integer> timeWeek = time.getWeeksUSP();
				for (int i = 0; i < timeWeek.size(); i++) {
					if (!s.contains(timeWeek.get(i))) {
						s.add(timeWeek.get(i));
					}
				}
			}
		}
		s.sort(null);
		String res = "";
		for (int st : s) {
			res += st + ",";
		}
		if (res.length() > 0) {
			res = res.substring(0, res.length() - 1);
		}
		return res;
	}

	public int getLastWeek(TimesPenaltysITC times) {
		int max = 0;
		for (TimesPenaltyITC time : times) {
			if (max < time.getLastWeeks()) {
				max = time.getLastWeeks();
			}
		}
		return max;
	}

	public int getFirstWeek(TimesPenaltysITC times) {
		int min = 0;
		for (TimesPenaltyITC time : times) {
			if (min == 0) {
				min = time.getWeeks().length();
			}
			int temp = time.getFirstWeeks();
			if (min > temp && temp != -1) {
				min = temp;
			}
		}
		return min;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ClassesITC getClas() {
		return clas;
	}

	public void setClas(ClassesITC clas) {
		this.clas = clas;
	}
}
