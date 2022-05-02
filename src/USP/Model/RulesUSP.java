package USP.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RulesUSP extends ArrayList<RuleUSP> {
	public RulesUSP() {
		super();
	}

	public List<String> getForbidenPeriodClass(String id) {
		List<String> res = new ArrayList<>();
		for (RuleUSP rule : this) {
			int cons = rule.getConstraint().containsCons("forbiddenPeriod", "hard");
			if (cons > -1 && rule.getSessions().containsClassID(id) > -1) {
				ConstraintUSP constraint = rule.getConstraint().get(cons);
				String first = constraint.getParameters().getValueParam("first", "slot");
				String last = constraint.getParameters().getValueParam("last", "slot");
				res.add(first + "-" + last);
			}
		}
		return res;
	}

	public List<String> getForbidenRoomsClass(String id) {
		List<String> res = new ArrayList<>();
		for (RuleUSP rule : this) {
			int cons = rule.getConstraint().containsCons("forbiddenRooms", "hard");
			int ses = rule.getSessions().containsClassID(id);
			if (cons > -1 && ses > -1) {
				ConstraintUSP constraint = rule.getConstraint().get(cons);
				String rooms = constraint.getParameters().getValueParam("rooms", "ids");
				if (!rooms.equals("")) {
					String[] splitRoom = rooms.split(",");
					res = Arrays.asList(splitRoom);
				}
			}
		}
		return res;
	}

}
