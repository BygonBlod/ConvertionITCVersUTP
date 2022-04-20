package main;

import Convertion.VersUSP.ConvertisseurUSP;
import ITC.Model.ProblemITC;
import ITC.ReadWrite.ReadITC;
import ITC.ReadWrite.WriteITC;
import USP.Model.Timetabling;
import USP.ReadWrite.WriteUSP;

public class MainVersUSP {

	public static void main(String[] args) {
		ProblemITC problem = ReadITC.getProblem(args[0], false);

		WriteITC.write(problem, "testApresITC.xml");

		Timetabling time = ConvertisseurUSP.getTimeTabling(problem);

		WriteUSP.write(time, "testITCversUSP.xml");

	}

}
