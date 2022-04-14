package USP.Model;

public class AllowedRoomUSP {
	private String refId;
	private String mandatory;

	public AllowedRoomUSP(String refId, String mandatory) {
		this.refId = refId;
		this.mandatory = mandatory;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

}
