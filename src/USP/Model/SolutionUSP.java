package USP.Model;

import java.util.ArrayList;

public class SolutionUSP {
	private ArrayList<GroupSolutionUSP> groups;
	private ArrayList<SessionSolutionUSP> sessions;

	public SolutionUSP(ArrayList<GroupSolutionUSP> groups, ArrayList<SessionSolutionUSP> sessions) {
		this.groups = groups;
		this.sessions = sessions;
	}

	public SolutionUSP() {
		groups = new ArrayList<>();
		sessions = new ArrayList<>();
	}

	public ArrayList<GroupSolutionUSP> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<GroupSolutionUSP> groups) {
		this.groups = groups;
	}

	public ArrayList<SessionSolutionUSP> getSessions() {
		return sessions;
	}

	public void setSessions(ArrayList<SessionSolutionUSP> sessions) {
		this.sessions = sessions;
	}

}
