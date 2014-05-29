package com.xyhui.types;

import java.util.ArrayList;

public class Topic {
	public String tid;
	public int start_floor;
	public Thread topic;
	public ArrayList<Reply> reply;

	public class Thread {
		public String id;
		public String gid;
		public String uid;
		public String name;
		public String title;
		public String cid;
		public String viewcount;
		public String replycount;
		public String addtime;
		public String replytime;
		public String content;
		public String pid;
		public String face;
	}

	public class Reply {
		public String id;
		public String gid;
		public String uid;
		public String tid;
		public String content;
		public String ctime;
		public String ip;
		public String name;
	}

}
