package com.xyhui.utils;

public class Params {

	public static class INTENT_EXTRA {
		public static final String EVENT_COMMENT_EDIT = "intent.event.comment.edit";
		public static final String EVENT_TOID = "intent.event.toid";
		public static final String EVENT_ID = "intent.event.id";
		public static final String WEIBO_EDIT = "intent.weibo.edit";
		public static final String WEIBO_ITEM = "intent.weibo.item";
		public static final String WEIBO = "intent.weibo";
		public static final String WEIBO_ID = "intent.weibo.id";
		public static final String WEIBO_FORWARD = "intent.weibo.forward";
		public static final String WEIBO_DATA = "intent.weibo.data";
		public static final String WEIBO_FORWARD_DATA = "intent.weibo.forward.data";
		public static final String WEIBO_TOPIC_ID = "intent.weibo.topic.id";
		public static final String SCHOOL = "intent.school";
		public static final String CITY = "intent.city";
		public static final String USER = "intent.user";
		public static final String USER_ID = "intent.user.id";
		public static final String PHOTO_PRIVACY = "intent.photo.privacy";
		public static final String ALBUM_ID = "intent.album.id";
		public static final String GROUP_ID = "intent.group.id";
		public static final String BLOG_ID = "intent.blog.id";
		public static final String EVENT_COMMENT_TOID = "intent.event.comment.toid";
		public static final String DOC_ID = "intent.doc.id";
		public static final String DOC = "intent.doc";
		public static final String MESSAGE_LISTID = "intent.message.listid";
		public static final String MESSAGE_UID = "intent.message.uid";
		public static final String WEBVIEW_TITLE = "intent.webview.title";
		public static final String WEBVIEW_URL = "intent.webview.url";
		public static final String WEBVIEW_TYPE = "intent.webview.type";
		public static final String WEBVIEW_MECHANISM = "intent.webview.mechanism";

		public static final String ANNOUNCE_ID = "intent.announce";
		public static final String ANNOUNCE_TYPE = "intent.announce.type";

		public static final String FRIENDLIST_USERID = "intent.friendlist.userid";
		public static final String FRIENDLIST_TYPE = "intent.friendlist.type";

		public static final String GROUP = "intent.group";

		public static final String MSG_TYPE = "intent.message.type";

		public static final String USERNAME = "intent.user.name";
		public static final String USERSEX = "intent.user.sex";

		public static final String MAP = "intent.map";
		public static final String EVENT = "intent.event";
		public static final String EVENTID = "intent.event.id";
		public static final String COURSE = "intent.course";
		public static final String COURSEID = "intent.course.id";
		public static final String QRCODE = "intent.qrcode";
		public static final String EVENTNEWS_ID = "intent.event.news.id";
		public static final String URL = "intent.url";
		public static final String GROUPMEMBER = "intent.group.member";
		public static final String LOCATION = "location";
		public static final String ATTEND_CODE = "attend.code";
		public static final String DONATE_ID = "attend.donate.id";
	}

	public static class INTENT_VALUE {
		public static final int WEIBO_NEW = 1;
		public static final int WEIBO_REPLY = 2;
		public static final int WEIBO_FORWARD = 3;
		public static final int WEIBO_BLOG = 4;
		public static final int WEIBO_BLOGREPLY = 5;

		public static final int FRIENDLIST_FOLLOWS = 1;
		public static final int FRIENDLIST_FOLLOWED = 2;
	}

	public static class REQUEST_CODE {
		public static final int SCHOOL = 701;
		public static final int USER = 601;
		public static final int TAKE_PHOTO = 990;
		public static final int GET_PHOTO = 991;
	}

	public static class INTENT_ACTION {
		public static final String REFRESH = "action.refresh";

		public static final String BLOGLIST = "action.refresh.bloglist";
		public static final String EVENTLIST = "action.refresh.eventlist";
		public static final String WEIBOLIST = "action.refresh.weibolist";
		public static final String ALBUMLIST = "action.refresh.albumlist";
		public static final String PHOTOLIST = "action.refresh.photolist";
		public static final String BLOGVIEW = "action.refresh.blogview";
		public static final String WEIBOVIEW = "action.refresh.weiboview";
	}

	public static class LOCAL {
		public static final String TOKEN = "local.token";
		public static final String SECRET = "local.secret";
		public static final String UID = "local.uid";
		public static final String UNAME = "local.uname";
		public static final String NICKNAME = "local.nickname";

		public static final String SCHOOLLIST = "local.school.list";
		public static final String SCHOOLID = "local.school.id";
		public static final String SCHOOLNAME = "local.school.name";
		public static final String SCHOOLEMAIL = "local.school.email";

		public static final String DEPARTID = "local.depart.id";
		public static final String DEPARTNAME = "local.depart.name";

		public static final String CITYLIST = "local.city.list";
		public static final String CITYID = "local.city.id";
		public static final String CITYNAME = "local.city.name";

		public static final String COURSE_CAT = "local.cat.course";
		public static final String EVENT_CAT = "local.cat.event";
		public static final String EVENT_ORG = "local.org.event";
		public static final String GROUP_CAT = "local.cat.group";

		public static final String ANNOUNCETIME = "local.announce.time";
		public static final String NOTICE_OFFICIAL_LIST = "local.official.list";
		public static final String NOTICE_OFFICIAL_LIST1 = "local.official.list1";
		public static final String NOTICE_OFFICIAL_LIST2 = "local.official.list2";
		public static final String NOTICE_SCHOOL_LIST = "local.school.list";
		public static final String NOTICE_SCHOOL_LIST1 = "local.school.list1";
		public static final String NOTICE_SCHOOL_LIST2 = "local.school.list2";
		public static final String NOTICE_SCHOOL_LIST3 = "local.school.list3";
		public static final String NOTICE_SCHOOL_LIST4 = "local.school.list4";
		public static final String NOTICE_SCHOOL_LIST0 = "local.school.list5";

		public static final String MOBILE = "local.mobile";
		public static final String EMAIL = "local.email";

		public static final String VERSION = "local.version";

		public static final String EDIT_MESSAGE = "local.msg";
	}

	public static class JPush {
		// 设置别名标签超时
		public final static int STATE_TIMEOUT = 6002;
		// 成功设置别名标签
		public final static int STATE_OK = 0;

		public final static String PUSH_EXTRA = "push_extra";

		// 私信
		public static final String TYPE_PRIVETE_MSG = "1";
	}

}
