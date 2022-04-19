package main;

import ITC.Model.ProblemITC;
import ITC.ReadWrite.ReadITC;

public class MainVersUSP {

	public static void main(String[] args) {
		ProblemITC problem = ReadITC.getProblem(args[0]);
		/*
		 * WriteITC.write(problem, "testApresITC.xml");
		 * 
		 * Timetabling time = ConvertisseurUSP.getTimeTabling(problem);
		 * 
		 * WriteUSP.write(time, "testITCversUSP.xml");
		 */
	}

}
