package USP.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RulesUSP extends ArrayList<RuleUSP> {
	public RulesUSP() {
		super();
	}

	public RulesUSP getForbidenPeriodClass(String id) {
		RulesUSP rules = new RulesUSP();
		for (RuleUSP rule : this) {
			if (rule.getConstraint().containsCons("ForbiddenPeriod", "hard") > -1
					&& rule.getSessions().containsClassID(id) > -1) {
				rules.add(rule);
			}
		}
		return rules;
	}

	public List<String> getForbidenRoomsClass(String id) {
		List<String> res = new ArrayList<>();
		for (RuleUSP rule : this) {
			int cons = rule.getConstraint().containsCons("forbiddenRooms", "hard");
			int ses = rule.getSessions().containsClassID(id);
			if (cons > -1 || ses > -1) {
				// System.out.println(cons + " " + ses);
			}
			if (cons > -1 && ses > -1) {
				System.out.println("rentrer");
				ConstraintUSP constraint = rule.getConstraint().get(cons);
				ArrayList<ParameterUSP> parameters = constraint.getParameters();
				for (ParameterUSP param : parameters) {
					if (param.getName().equals("rooms") && param.getType().equals("ids")) {
						String[] splitRoom = param.getValue().split(",");
						res = Arrays.asList(splitRoom);
					}
				}
			}
		}
		return res;
	}

}
