package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import ITC.ReadWrite.ReadITC;

public class ReadAllFiles {
	public static void main(String[] args) {
		Path fichier = Paths.get("multipleConfig.txt");
		try {
			Files.write(fichier, new ArrayList<>(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filename = System.getProperty("user.dir") + "/files";
		File file = new File(filename);
		System.out.println(filename);
		File[] files = file.listFiles();
		int nbFic = 0;
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory() == true) {
					System.out.println("Dossier: " + files[i].getAbsolutePath());
				} else {
					nbFic += 1;
					System.out.println("  Fichier: " + files[i].getName());
					ReadITC.getProblem("files/" + files[i].getName(), true);
				}
			}
		}
		System.out.println(ReadITC.nbFile + " / " + nbFic);
	}

}
