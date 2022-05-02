package Convertion.VersITC;

import java.util.List;

import ITC.Model.TimesPenaltyITC;
import ITC.Model.TimesPenaltysITC;

public class ConvertisseurTimes {

	public static TimesPenaltysITC convertForbiddenToTimes(List<String> forbiddenPeriod, int nbDaysPerWeek, int nbWeeks,
			int nbSesPerWeek) {
		TimesPenaltysITC times = new TimesPenaltysITC();
		int deb = 0;
		int fin = 0;
		for (int i = 0; i < forbiddenPeriod.size() - 1; i++) {
			String begin = forbiddenPeriod.get(i);
			String end = forbiddenPeriod.get(i + 1);
			deb = Integer.parseInt(begin.split("-")[1]) + 1;
			fin = Integer.parseInt(end.split("-")[0]) - 1;
			TimesPenaltyITC time = getTime(deb, fin, nbDaysPerWeek, nbWeeks);
			times.add(time);
		}
		times.join(nbSesPerWeek);
		return times;
	}

	private static TimesPenaltyITC getTime(int deb, int fin, int nbDaysPerWeek, int nbWeeks) {
		int nbDay = Math.floorDiv(deb, 1440);
		int nbWeek = Math.floorDiv(nbDay, nbDaysPerWeek);
		int start = (deb - nbDay * 1440);
		nbDay = nbDay - nbWeek * nbDaysPerWeek;
		String startTime = start / 5 + "";
		String days = "";
		String weeks = "";
		String length = (fin - deb) / 5 + "";
		for (int i = 0; i < nbDaysPerWeek; i++) {
			if (i == nbDay) {
				days += "1";
			} else {
				days += "0";
			}
		}
		for (int i = 0; i < nbWeeks; i++) {
			if (i == nbWeek) {
				weeks += "1";
			} else {
				weeks += "0";
			}
		}
		System.out.println("start " + startTime + " day " + days + " nbWeek " + weeks);
		return new TimesPenaltyITC(days, startTime, length, weeks, "0");
	}

}
