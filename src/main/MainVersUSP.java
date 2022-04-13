package main;

import ITC.Model.ProblemITC;
import ITC.ReadWrite.ReadITC;
import ITC.ReadWrite.WriteITC;

public class MainVersUSP {

	public static void main(String[] args) {
		ProblemITC problem = ReadITC.getProblem(args[0]);
		System.out.println("rooms " + problem.getRooms().size());
		System.out.println("distributions " + problem.getDistributions().size());
		System.out.println("students " + problem.getStudents().size());
		System.out.println("courses " + problem.getCourses().size());
		// essai d'écriture
		WriteITC.write(problem, "fileITC.xml");

		ProblemITC problem2 = ReadITC.getProblem("fileITC.xml");
		System.out.println("rooms " + problem2.getRooms().size());
		System.out.println("distributions " + problem2.getDistributions().size());
		System.out.println("students " + problem2.getStudents().size());
		System.out.println("courses " + problem2.getCourses().size());

		WriteITC.write(problem2, "fileITC2.xml");
	}

}
