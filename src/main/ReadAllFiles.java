package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ITC.Model.ProblemITC;
import ITC.ReadWrite.ReadITC;

public class ReadAllFiles {
	public static void main(String[] args) {
		Path fichier = Paths.get("multipleConfig.txt");
		try {
			Files.write(fichier, new ArrayList<>(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Path fichier2 = Paths.get("nbContraintes.txt");
		try {
			Files.write(fichier2, new ArrayList<>(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Path fichier3 = Paths.get("nbInstancesSlots.txt");
		try {
			Files.write(fichier3, new ArrayList<>(), Charset.forName("UTF-8"));
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
					ProblemITC problem = ReadITC.getProblem("files/" + files[i].getName(), true);
					// Timetabling time = ConvertisseurUSP.getTimeTabling(problem, true, false);
				}
			}
		}
		System.out.println(ReadITC.nbFile + " / " + nbFic);

		HashMap<String, Integer> hash = ReadITC.nbContraintesMax;
		HashMap<String, ArrayList<String>> filesContraintes = ReadITC.nbContraintesFiles;
		System.out.println(filesContraintes.toString());
		HashMap<String, Integer> x = hash.entrySet().stream().sorted((i1, i2) -> i1.getKey().compareTo(i2.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (z1, z2) -> z1, LinkedHashMap::new));
		System.out.println(x.toString());
		ArrayList<String> lignes = new ArrayList<>();
		Path fichierContraintes = Paths.get("nbContraintesMax.txt");
		Set<String> keys = x.keySet();
		try {
			for (String s : keys) {
				lignes.add(s + " - " + x.get(s));
				lignes.add("files:");
				for (String fileC : filesContraintes.get(s)) {
					lignes.add(" " + fileC);
				}
				lignes.add("");
			}

			Files.write(fichierContraintes, lignes, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
