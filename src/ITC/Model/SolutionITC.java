package ITC.Model;

import java.util.ArrayList;

public class SolutionITC {
	private String name;
	private String runtime;
	private String cores;
	private String technique;
	private String author;
	private String institution;
	private String country;
	private ArrayList<ClassITC> classes;

	public SolutionITC(String name, String runtime, String cores, String technique, String author, String institution,
			String country, ArrayList<ClassITC> classes) {
		this.name = name;
		this.runtime = runtime;
		this.cores = cores;
		this.technique = technique;
		this.author = author;
		this.institution = institution;
		this.country = country;
		this.classes = classes;
	}

	public SolutionITC() {
		this.name = "";
		this.runtime = "";
		this.cores = "";
		this.technique = "";
		this.author = "";
		this.institution = "";
		this.country = "";
		this.classes = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getCores() {
		return cores;
	}

	public void setCores(String cores) {
		this.cores = cores;
	}

	public String getTechnique() {
		return technique;
	}

	public void setTechnique(String technique) {
		this.technique = technique;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public ArrayList<ClassITC> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<ClassITC> classes) {
		this.classes = classes;
	}

}
