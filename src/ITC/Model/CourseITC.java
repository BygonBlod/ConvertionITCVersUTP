package ITC.Model;

import java.util.ArrayList;

public class CourseITC {
	private String id;
	private ArrayList<ConfigITC> config;

	public CourseITC(String id, ArrayList<ConfigITC> config) {
		this.id = id;
		this.config = config;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<ConfigITC> getConfig() {
		return config;
	}

	public void setConfig(ArrayList<ConfigITC> config) {
		this.config = config;
	}
}
