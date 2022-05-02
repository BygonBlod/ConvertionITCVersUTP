package USP.Model;

import java.util.ArrayList;

public class SessionsRuleUSP extends ArrayList<SessionRuleUSP> {
	public SessionsRuleUSP() {
		super();
	}

	public int containsClassID(String id) {
		int res = 0;
		for (SessionRuleUSP session : this) {
			if (session.getAttributeName().equals("id") && session.getGroupBy().equals("class")
					&& session.getIn().equals(id)) {
				return res;
			}
			res++;
		}
		return -1;
	}
}
