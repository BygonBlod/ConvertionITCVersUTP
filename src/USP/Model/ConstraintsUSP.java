package USP.Model;

import java.util.ArrayList;

public class ConstraintsUSP extends ArrayList<ConstraintUSP> {
	public ConstraintsUSP() {
		super();
	}

	public int containsCons(String name, String type) {
		int res = 0;
		for (ConstraintUSP constraint : this) {
			if (constraint.getName().equals(name) && constraint.getType().equals(type)) {
				return res;
			}
			res++;
		}
		return -1;
	}

}
