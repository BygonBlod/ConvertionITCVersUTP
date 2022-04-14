package USP.Model;

public class AllowedTeacher {
	private String refId;
	private String nrSessions;

	public AllowedTeacher(String refId, String nrSessions) {
		this.refId = refId;
		this.nrSessions = nrSessions;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getNrSessions() {
		return nrSessions;
	}

	public void setNrSessions(String nrSessions) {
		this.nrSessions = nrSessions;
	}

}
