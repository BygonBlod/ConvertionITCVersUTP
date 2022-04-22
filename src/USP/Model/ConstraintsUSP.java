package USP.Model;

import java.util.ArrayList;

public class ConstraintsUSP extends ArrayList<ConstraintUSP> {
	public ConstraintsUSP() {
		super();
	}

	public boolean containsCons(String name, String type) {
		for (ConstraintUSP constraint : this) {
			if (constraint.getName().equals(name) && constraint.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}

}
