package USP.Model;

import java.util.ArrayList;

public class RuleUSP {
	private ArrayList<SessionRuleUSP> sessions;
	private ConstraintsUSP constraint;

	public RuleUSP(ArrayList<SessionRuleUSP> sessions, ConstraintsUSP constraint) {
		this.sessions = sessions;
		this.constraint = constraint;
	}

	public ArrayList<SessionRuleUSP> getSessions() {
		return sessions;
	}

	public void setSessions(ArrayList<SessionRuleUSP> sessions) {
		this.sessions = sessions;
	}

	public ConstraintsUSP getConstraint() {
		return constraint;
	}

	public void setConstraint(ConstraintsUSP constraint) {
		this.constraint = constraint;
	}

}
