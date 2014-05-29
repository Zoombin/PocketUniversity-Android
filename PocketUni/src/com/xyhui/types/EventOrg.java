package com.xyhui.types;

import java.util.ArrayList;

public class EventOrg {
	public ArrayList<EventCat> zhuzhi;
	public ArrayList<EventCat> bumen;
	public ArrayList<EventCat> school;

	public ArrayList<EventCat> getList() {
		ArrayList<EventCat> total = new ArrayList<EventCat>();

		if (null != zhuzhi) {
			for (EventCat eventCat : zhuzhi) {
				eventCat.id = "-" + eventCat.id;
			}
			total.addAll(zhuzhi);
		}

		if (null != bumen) {
			for (EventCat eventCat : bumen) {
				eventCat.id = "-" + eventCat.id;
			}
			total.addAll(bumen);
		}

		if (null != school) {
			total.addAll(school);
		}

		return total;
	}
}
