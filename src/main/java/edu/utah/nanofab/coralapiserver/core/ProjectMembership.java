package edu.utah.nanofab.coralapiserver.core;

import edu.nanofab.utah.coralapi.collections.Members;

public class ProjectMembership {
	private String project;
	private String[] members;
	public ProjectMembership() {
	}
	public ProjectMembership(String project, Members members) {
		this.project = project;
		this.members = members.getNames();
	}
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
