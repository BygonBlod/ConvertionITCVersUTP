package USP.Model;

import java.util.ArrayList;

public class ConstraintUSP {
	private String name;
	private String type;
	private ArrayList<ParameterUSP> parameters;

	public ConstraintUSP(String name, String type, ArrayList<ParameterUSP> parameters) {
		this.name = name;
		this.type = type;
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<ParameterUSP> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<ParameterUSP> parameters) {
		this.parameters = parameters;
	}

}
