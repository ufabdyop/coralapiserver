package edu.utah.nanofab.coralapiserver.core;

public class DisableRequest {
	public String member;
	public String item;
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
}
