package USP.Model;

public class ClassUSP {
	private String id;
	private String maxHeadCount;
	private String parent;

	public ClassUSP(String id, String maxHeadCount) {
		this.id = id;
		this.maxHeadCount = maxHeadCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMaxHeadCount() {
		return maxHeadCount;
	}

	public void setMaxHeadCount(String maxHeadCount) {
		this.maxHeadCount = maxHeadCount;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

}
