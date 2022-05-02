package USP.Model;

public class ConstraintUSP {
	private String name;
	private String type;
	private ParametersUSP parameters;

	public ConstraintUSP(String name, String type, ParametersUSP parameters) {
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

	public ParametersUSP getParameters() {
		return parameters;
	}

	public void setParameters(ParametersUSP parameters) {
		this.parameters = parameters;
	}

}
