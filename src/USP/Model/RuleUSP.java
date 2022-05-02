package USP.Model;

public class RuleUSP {
	private SessionsRuleUSP sessions;
	private ConstraintsUSP constraint;

	public RuleUSP(SessionsRuleUSP sessions, ConstraintsUSP constraint) {
		this.sessions = sessions;
		this.constraint = constraint;
	}

	public SessionsRuleUSP getSessions() {
		return sessions;
	}

	public void setSessions(SessionsRuleUSP sessions) {
		this.sessions = sessions;
	}

	public ConstraintsUSP getConstraint() {
		return constraint;
	}

	public void setConstraint(ConstraintsUSP constraint) {
		this.constraint = constraint;
	}

}
