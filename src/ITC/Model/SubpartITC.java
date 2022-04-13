package ITC.Model;

import java.util.ArrayList;

public class SubpartITC {
	private String id;
	ArrayList<ClassITC> clas;

	public SubpartITC(String id, ArrayList<ClassITC> clas) {
		this.id = id;
		this.clas = clas;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<ClassITC> getClas() {
		return clas;
	}

	public void setClas(ArrayList<ClassITC> clas) {
		this.clas = clas;
	}

}
