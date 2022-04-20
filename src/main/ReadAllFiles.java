package main;

import java.io.File;

import ITC.ReadWrite.ReadITC;

public class ReadAllFiles {
	public static void main(String[] args) {
		String filename = System.getProperty("user.dir") + "/files";
		;
		File file = new File(filename);
		System.out.println(filename);
		File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory() == true) {
					System.out.println("Dossier: " + files[i].getAbsolutePath());
				} else {
					System.out.println("  Fichier: " + files[i].getName());
					ReadITC.getProblem("files/" + files[i].getName(), true);
				}
			}
		}
		System.out.println(ReadITC.nbFile);
	}

}
