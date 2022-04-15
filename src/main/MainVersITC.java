package main;

import Convertion.VersITC.ConvertisseurITC;
import ITC.Model.ProblemITC;
import ITC.ReadWrite.WriteITC;
import USP.Model.Timetabling;
import USP.ReadWrite.ReadUSP;

public class MainVersITC {
	public static void main(String[] args) {
		Timetabling time = ReadUSP.getTimeTabling(args[0]);

		// WriteUSP.write(time, "testApres.xml");

		ProblemITC problem = ConvertisseurITC.getProblem(time);
		WriteITC.write(problem, "testUSPversITC.xml");
	}

}
