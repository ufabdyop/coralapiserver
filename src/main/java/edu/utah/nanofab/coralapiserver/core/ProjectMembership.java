package edu.utah.nanofab.coralapiserver.core;

public class ProjectMembership {
	private String project;
	private String[] members;
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String[] getMembers() {
		return members;
	}
	public void setMembers(String[] members) {
		this.members = members;
	}
}
