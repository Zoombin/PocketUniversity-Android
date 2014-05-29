package com.xyhui.types;

public class KeyValuePair {
	public String id;
	public String title;

	public KeyValuePair(String id, String title) {
		this.id = id;
		this.title = title;
	}

	@Override
	public String toString() {
		return title;
	}
}
