package USP.Model;

import java.util.ArrayList;

public class ParametersUSP extends ArrayList<ParameterUSP> {
	public ParametersUSP() {
		super();
	}

	public String getValueParam(String name, String type) {
		for (ParameterUSP param : this) {
			if (param.getName().equals(name) && param.getType().equals(type)) {
				return param.getValue();
			}
		}
		return "";
	}
}
