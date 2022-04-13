package USP.Model;

import java.util.ArrayList;

public class RuleUSP {
	private ArrayList<SessionRuleUSP> sessions;
	private ArrayList<ConstraintUSP> constraint;

	public RuleUSP(ArrayList<SessionRuleUSP> sessions, ArrayList<ConstraintUSP> constraint) {
		this.sessions = sessions;
		this.constraint = constraint;
	}

	public ArrayList<SessionRuleUSP> getSessions() {
		return sessions;
	}

	public void setSessions(ArrayList<SessionRuleUSP> sessions) {
		this.sessions = sessions;
	}

	public ArrayList<ConstraintUSP> getConstraint() {
		return constraint;
	}

	public void setConstraint(ArrayList<ConstraintUSP> constraint) {
		this.constraint = constraint;
	}

}
