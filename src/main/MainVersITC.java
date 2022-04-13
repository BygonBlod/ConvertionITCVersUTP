package main;

import USP.Model.Timetabling;
import USP.ReadWrite.ReadUSP;
import USP.ReadWrite.WriteUSP;

public class MainVersITC {
	public static void main(String[] args) {
		Timetabling time = ReadUSP.getTimeTabling(args[0]);
		WriteUSP.write(time, "fileUSP.xml");
	}

}
