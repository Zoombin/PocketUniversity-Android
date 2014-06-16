package com.xyhui.api;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.RequestParams;
import com.mslibs.utils.VolleyLog;

public class Api extends BaseApi {

	// 0:网站 1:手机网页版 2:android 3:iphone
	private static final String DEVICE = "2";

	public Api(CallBack callback, Context context) {
		super(callback, context);
	}

	public void getSchools() {
		RequestParams params = new RequestParams();
		Client.get("Sitelist", "getSchools", params, handler);
	}

	public void getUserSchool(RequestParams params) {
		Client.get("User", "getUserSchool", params, handler);
	}

	public void getCitys() {
		RequestParams params = new RequestParams();
		Client.get("Sitelist", "getCitys", params, handler);
	}

	public void getVersion() {
		RequestParams params = new RequestParams();
		Client.get("Sitelist", "version", params, handler);
	}

	public void topFollowUser() {
		RequestParams params = new RequestParams();
		Client.get("Sitelist", "topFollowUser", params, handler);
	}

	public void friends_timeline(RequestParams params, int count, int page) {
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Statuses", "friends_timeline", params, handler);
	}

	public void user_timeline(RequestParams params, String user_id, int count,
			int page) {
		params.put("user_id", user_id);
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Statuses", "user_timeline", params, handler);
	}

	public void showweibo(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Statuses", "show", params, handler);
	}

	public void comments(RequestParams params, String id, int count, int page) {
		params.put("id", id);
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Statuses", "comments", params, handler);
	}

	public void mentions(RequestParams params, int count, int page) {
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Statuses", "mentions", params, handler);
	}

	public void comments_timeline(RequestParams params, int count, int page) {
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Statuses", "comments_timeline", params, handler);
	}

	public void comments_receive_me(RequestParams params, int count, int page) {
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Statuses", "comments_receive_me", params, handler);
	}

	public void message(RequestParams params, int count, int page) {
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Message", "box", params, handler);
	}

	public void showmessage(RequestParams params, String id, int count, int page) {
		params.put("id", id);
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Message", "show", params, handler);
	}

	public void sendmessage(RequestParams params, String id, String content) {
		params.put("to_uid", id);
		params.put("content", content);
		Client.get("Message", "create", params, handler);
	}

	public void showuser(RequestParams params, String user_id) {
		params.put("user_id", user_id);
		Client.get("User", "show", params, handler);
	}

	public void showuserbyname(RequestParams params, String user_name) {

		params.put("user_name", user_name);
		Client.get("User", "show", params, handler);
	}

	public void update(RequestParams params, String content) {
		params.put("content", content);
		params.put("from", DEVICE);
		Client.get("Statuses", "update", params, handler);
	}

	public void upload(RequestParams params, String content, String path) {
		params.put("content", content);

		if (!TextUtils.isEmpty(path)) {
			VolleyLog.d("upload image: %s ", path);
			File myFile = new File(path);
			try {
				params.put("pic", myFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		params.put("from", DEVICE);
		Client.get("Statuses", "upload", params, handler);
	}
	
	public void delweibo(RequestParams params, String weiboid) {
		params.put("id", weiboid);
		Client.get("Statuses", "destroy", params, handler);
	}

	public void comment(RequestParams params, String reply_comment_id,
			String weibo_id, String comment_content, String transpond) {
		params.put("reply_comment_id", reply_comment_id);
		params.put("weibo_id", weibo_id);
		params.put("comment_content", comment_content);
		params.put("transpond", transpond);
		params.put("from", DEVICE);
		Client.get("Statuses", "comment", params, handler);
	}

	public void eventcomment(RequestParams params, String eid, String comment,
			String toId) {
		params.put("eid", eid);
		params.put("comment", comment);
		params.put("toId", toId);
		Client.get("event", "addComment", params, handler);
	}

	public void repost(RequestParams params, String reply_weibo_id,
			String content, String transpond_id) {
		params.put("reply_weibo_id", reply_weibo_id);
		params.put("content", content);
		params.put("transpond_id", transpond_id);
		params.put("from", DEVICE);
		Client.get("Statuses", "repost", params, handler);
	}

	public void following(RequestParams params, String user_id, int count,
			int page) {
		params.put("user_id", user_id);
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Statuses", "following", params, handler);
	}

	public void followers(RequestParams params, String user_id, int count,
			int page) {
		params.put("user_id", user_id);
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Statuses", "followers", params, handler);
	}

	public void searchuser(RequestParams params, String sid, String dpartId,
			String keyword, int count, int page) {
		params.put("sid", sid);
		params.put("sid1", dpartId);
		params.put("key", keyword);
		params.put("count", "" + count);
		params.put("page", "" + page);
		Client.get("Statuses", "searchuser", params, handler);
	}

	public void recommendUser(RequestParams params, String sid, String departId) {
		params.put("sid", sid);
		params.put("departId", departId);
		Client.get("Statuses", "recommendUser", params, handler);
	}

	public void login(String username, String password, String client) {
		RequestParams params = new RequestParams();
		params.put("email", username);
		params.put("password", password);
		params.put("client", client);
		Client.get("Sitelist", "authorize", params, handler);
	}

	public void register(String sid, String number, String nickname,
			String password, String repassword) {
		RequestParams params = new RequestParams();
		params.put("sid", sid);
		params.put("number", number);
		params.put("nickname", nickname);
		params.put("repassword", repassword);
		params.put("password", password);
		Client.get("Sitelist", "register", params, handler);
	}

	public void getAnnounceList(RequestParams params, String school, int cid,
			int limit, int offset) {
		params.put("school", school);
		params.put("cid", "" + cid);
		params.put("limit", "" + limit);
		params.put("offset", "" + offset);
		Client.get("User", "getAnnounceList", params, handler);
	}

	public void getAnnounce(RequestParams params, String id, String format) {
		params.put("id", id);
		params.put("new", format);
		Client.get("User", "getAnnounce", params, handler);
	}

	public void getAnnounceNew(RequestParams params, String school, long time) {
		params.put("school", school);
		params.put("time", time + "");
		Client.get("User", "getAnnounceNew", params, handler);
	}

	public void follow(RequestParams params, String user_id) {
		params.put("user_id", user_id);
		Client.get("Friendships", "create", params, handler);
	}

	public void unfollow(RequestParams params, String user_id) {
		params.put("user_id", user_id);
		Client.get("Friendships", "destroy", params, handler);
	}

	public void fav(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Favorites", "create", params, handler);
	}

	public void unfav(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Favorites", "destroy", params, handler);
	}

	public void addZan(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Heart", "create", params, handler);
	}

	public void delZan(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Heart", "destroy", params, handler);
	}

	public void indexZan(RequestParams params, int count, int page) {
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Heart", "index", params, handler);
	}

	public void isfav(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Favorites", "isFavorite", params, handler);
	}

	public void groupsearch(RequestParams params, String keyword, int count,
			int page) {
		params.put("key", keyword);
		params.put("count", count + "");
		params.put("p", page + "");
		Client.get("Group", "search", params, handler);
	}

	public void allgroup(RequestParams params, String[] ids, int count, int page) {
		params.put("dpart", ids[0]);
		params.put("sid1", ids[1]);
		params.put("year", ids[2]);
		params.put("sort", ids[3]);
		params.put("count", count + "");
		params.put("p", page + "");
		Client.get("Group", "allgroup", params, handler);
	}

	public void mygroup(RequestParams params, int count, int page) {
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Group", "mygroup", params, handler);
	}

	public void groupCategory(RequestParams params) {
		Client.get("Group", "groupCategory", params, handler);
	}

	public void group(RequestParams params, String gid) {
		params.put("gid", gid);
		Client.get("Group", "group", params, handler);
	}

	public void createGroup(RequestParams params, String name, String sid,
			String cid, String info, String tag, String pic) {

		params.put("cid0", cid);
		params.put("name", name);
		params.put("tags", tag);
		params.put("school0", sid);
		if (!TextUtils.isEmpty(pic)) {
			VolleyLog.d("upload image: %s", pic);
			File myFile = new File(pic);
			try {
				params.put("logo", myFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		params.put("intro", info);

		Client.get("Group", "create", params, handler);

	}

	public void modifyGroup(RequestParams params, String gid, String name,
			String sid, String cid, String info, String tag, String pic) {

		params.put("gid", gid);
		params.put("cid0", cid);
		params.put("name", name);
		params.put("intro", info);
		params.put("tags", tag);
		params.put("school0", sid);
		if (!TextUtils.isEmpty(pic)) {
			VolleyLog.d("upload image: %s", pic);
			File myFile = new File(pic);
			try {
				params.put("logo", myFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		params.put("intro", info);

		Client.get("Group", "modify", params, handler);

	}

	public void groupmember(RequestParams params, String gid, String ismember,
			int count, int page) {
		params.put("gid", gid);
		params.put("ismember", ismember);
		params.put("count", count + "");
		params.put("p", page + "");
		Client.get("Group", "member", params, handler);
	}

	public void grouptopic(RequestParams params, String gid, int count, int page) {
		params.put("gid", gid);
		params.put("count", count + "");
		params.put("p", page + "");
		Client.get("Group", "topic", params, handler);
	}

	public void groupdoc(RequestParams params, String gid) {
		params.put("gid", gid);
		Client.get("Group", "doc", params, handler);
	}

	public void joingroup(RequestParams params, String gid) {
		params.put("gid", gid);
		Client.get("Group", "joinGroup", params, handler);
	}

	public void quitgroup(RequestParams params, String gid) {
		params.put("gid", gid);
		Client.get("Group", "quitGroup", params, handler);
	}

	public void delgroup(RequestParams params, String gid) {
		params.put("gid", gid);
		Client.get("Group", "delGroup", params, handler);
	}

	public void groupannounce(RequestParams params, String gid, String announce) {
		params.put("gid", gid);
		params.put("announce", announce);
		Client.get("Group", "announce", params, handler);
	}

	public void groupinvite(RequestParams params, String gid, String ids) {
		params.put("gid", gid);
		params.put("ids", ids);
		Client.get("Group", "invite", params, handler);
	}

	public void viewtopic(RequestParams params, String tid, int count, int page) {
		params.put("tid", tid);
		params.put("count", count + "");
		params.put("p", page + "");
		Client.get("Group", "viewtopic", params, handler);
	}

	public void replytopic(RequestParams params, String gid, String tid,
			String content) {
		params.put("gid", gid);
		params.put("tid", tid);
		params.put("content", content);
		Client.get("Group", "replytopic", params, handler);
	}

	public void newtopic(RequestParams params, String gid, String title,
			String content, String flash, String pic) {
		params.put("gid", gid);
		params.put("title", title);
		params.put("flash", flash);
		params.put("content", content);

		if (!TextUtils.isEmpty(pic)) {
			VolleyLog.d("upload image: %s", pic);
			File myFile = new File(pic);
			try {
				params.put("image", myFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		Client.get("Group", "newtopic", params, handler);
	}

	public void myinfo(RequestParams params) {
		Client.get("Group", "user", params, handler);
	}

	public void mygroupcount(RequestParams params) {
		Client.get("Group", "mygroupcount", params, handler);
	}

	public void uploadfile(RequestParams params, String gid, String file) {
		params.put("gid", gid);

		if (!TextUtils.isEmpty(file)) {
			VolleyLog.d("upload file: %s", file);
			File myFile = new File(file);
			try {
				params.put("uploadfile", myFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		Client.get("Group", "uploadfile", params, handler);
	}

	public void profileupdate(RequestParams params, String nickname,
			String sex, File file) {
		params.put("nickname", nickname);
		params.put("sex", sex);

		if (!TextUtils.isEmpty(file.getAbsolutePath())) {
			VolleyLog.d("upload avatar: %s", file.getAbsolutePath());
			try {
				params.put("face", file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		Client.get("UserProfile", "update", params, handler);
	}

	public void profilepassword(RequestParams params, String oldpassword,
			String password, String repassword) {
		params.put("oldpassword", oldpassword);
		params.put("password", password);
		params.put("repassword", repassword);
		Client.get("UserProfile", "doModifyPassword", params, handler);
	}

	public void ditus(RequestParams params, String school, int count, int page) {
		params.put("school", school);
		params.put("count", count + "");
		params.put("p", page + "");
		Client.get("Group", "ditus", params, handler);
	}

	public void ditu(RequestParams params, String id, int count, int page) {
		params.put("id", id);
		params.put("count", count + "");
		params.put("p", page + "");
		Client.get("Group", "ditu", params, handler);
	}
	

	public void EventUploadImg(RequestParams params, String eventid, String path) {
		params.put("id", eventid);

		if (!TextUtils.isEmpty(path)) {
			VolleyLog.d("upload image: %s ", path);
			File myFile = new File(path);
			try {
				params.put("pic", myFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		Client.get("event", "addPhoto", params, handler);
	}


	public void getRecommEventList(RequestParams params, int count) {
		params.put("count", count + "");
		Client.get("Event", "recommEventList", params, handler);
	}

	public void getEventList(RequestParams params, String school, String cid,
			String keyword, int count, int page) {
		params.put("school", school);
		// params.put("cid", cid);
		params.put("orgId", cid);
		params.put("keyword", keyword);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Event", "eventList", params, handler);
	}

	public void getRecommendEventList(RequestParams params, String school,
			int count, int page) {
		params.put("school", school);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Event", "recommList", params, handler);
	}

	public void getMyEventList(RequestParams params, String action, int count,
			int page) {
		params.put("action", action);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Event", "myEventList", params, handler);
	}

	public void getEvent(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Event", "event", params, handler);
	}

	public void getEventNewsList(RequestParams params, String id, int count,
			int page) {

		params.put("id", id);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Event", "newsList", params, handler);
	}

	public void getEventCommentsList(RequestParams params, String id,
			int count, int page) {
		params.put("eid", id);
		params.put("count", count + "");
		params.put("p", page + "");
		Client.get("Event", "commentList", params, handler);
	}

	public void getDeleteComment(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Event", "delComment", params, handler);
	}

	public void getEventPhotoList(RequestParams params, String id, int count,
			int page) {

		params.put("id", id);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Event", "photoList", params, handler);
	}

	public void getPlayerList(RequestParams params, String id, String key,
			int count, int page) {

		params.put("id", id);
		params.put("key", key);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Event", "playerList", params, handler);
	}

	public void vote(RequestParams params, String pid, String eid) {
		params.put("pid", pid);
		params.put("eid", eid);
		Client.get("Event", "vote", params, handler);
	}

	public void joinEvent(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Event", "join", params, handler);
	}
	public void grade(RequestParams params, String id, int score) {
		params.put("id", id);
		params.put("score", score);
		Client.get("Event", "note", params, handler);
	}

	public void cancelJoinEvent(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Event", "cancelEvent", params, handler);
	}

	public void userAttend(RequestParams params, String code) {
		params.put("code", code);
		Client.get("Event", "userAttend", params, handler);
	}

	public void adminAttend(RequestParams params, String code, String uid) {
		params.put("code", code);
		params.put("uid", uid);
		Client.get("Event", "adminAttend", params, handler);
	}

	public void isChecked(RequestParams params, String code, String uid) {
		params.put("code", code);
		params.put("uid", uid);
		Client.get("Event", "isChecked", params, handler);
	}

	public void getEventCats(RequestParams params) {
		Client.get("Event", "CatList", params, handler);
	}

	public void getCourseCats(RequestParams params) {
		Client.get("Course", "CatList", params, handler);
	}

	public void getCourseList(RequestParams params, String action,
			String school, String cid, String keyword, int count, int page) {
		params.put("action", action);
		params.put("school", school);
		params.put("cid", cid);
		params.put("keyword", keyword);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Course", "courseList", params, handler);
	}

	public void getMyCourseList(RequestParams params, String action, int count,
			int page) {
		getCourseList(params, action, "0", "0", "", count, page);
	}

	public void getCourse(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Course", "course", params, handler);
	}

	public void joinCourse(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Course", "joinCourse", params, handler);
	}

	public void getNews(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Event", "news", params, handler);
	}

	public void eventFav(RequestParams params, String id, String type) {
		params.put("id", id);
		params.put("type", type);

		Client.get("Event", "fav", params, handler);
	}

	public void getMobile(RequestParams params) {
		Client.get("Statuses", "getMobile", params, handler);
	}

	public void mobileBind(RequestParams params, String mobile, String pass) {
		params.put("mobile", mobile);
		params.put("pass", pass);
		Client.get("Statuses", "mobileBind2", params, handler);
	}

	public void mobileCode(RequestParams params, String mobile) {
		params.put("mobile", mobile);
		Client.get("User", "mobileCode", params, handler);
	}

	public void userMobileBind(RequestParams params, String mobile, String code) {
		params.put("mobile", mobile);
		params.put("code", code);
		Client.get("User", "mobileBind", params, handler);
	}

	public void passwordEmail(RequestParams params) {
		Client.get("User", "passwordEmail", params, handler);
	}

	public void checkUser(RequestParams params) {
		Client.get("User", "checkUser", params, handler);
	}

	public void emailCode(RequestParams params, String email) {
		params.put("email", email);
		Client.get("User", "emailCode", params, handler);
	}

	public void emailBind(RequestParams params, String email, String code) {
		params.put("email", email);
		params.put("code", code);
		Client.get("User", "emailBind", params, handler);
	}

	public void initUser(RequestParams params, String email, String password) {
		params.put("email", email);
		params.put("password", password);
		Client.get("User", "initUser", params, handler);
	}

	public void memberAction(RequestParams params, String gid, String uid,
			String action) {
		params.put("gid", gid);
		params.put("uid", uid);
		params.put("action", action);
		Client.get("Group", "memberAction", params, handler);
	}

	public void recommendTopic(RequestParams params) {
		Client.get("Statuses", "recommendTopic", params, handler);
	}

	public void topic(RequestParams params, String k, int count, int page) {
		params.put("k", k);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Statuses", "topic", params, handler);
	}

	public void mytopic(RequestParams params, String k, int count, int page) {
		params.put("k", k);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Statuses", "mytopic", params, handler);
	}

	public void weibo(RequestParams params, int count, int page) {
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Statuses", "weibo", params, handler);
	}

	public void getNoticeList(RequestParams params, int cid, int count, int page) {
		params.put("cid", cid + "");
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("User", "getNoticeList", params, handler);
	}

	public void getNotice(RequestParams params, String id) {
		params.put("id", id);
		Client.get("User", "getNotice", params, handler);
	}

	public void eventLucky(RequestParams params, String id) {
		params.put("eventId", id);
		Client.get("Event", "lucky", params, handler);
	}

	public void eventLuckyConfirm(RequestParams params, String id) {
		params.put("eid", id);
		Client.get("Event", "confirm", params, handler);
	}

	public void shakeYY(RequestParams params) {
		Client.get("yy", "yy", params, handler);
	}

	public void shakeStatus(RequestParams params) {
		Client.get("yy", "status", params, handler);
	}

	public void addEvent(RequestParams params) {
		Client.get("Event", "addEvent", params, handler);
	}

	public void csOrga(RequestParams params, String sid) {
		params.put("sid", sid);
		Client.get("Event", "csOrga", params, handler);
	}

	public void doAddEvent(RequestParams params, File logo, String title,
			String audit_uid, String address, String typeId, String sTime,
			String eTime, String deadline, String gid, String limitCount,
			String description, String sid) {
		try {
			params.put("cover", logo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		params.put("title", title);
		params.put("audit_uid", audit_uid);
		params.put("address", address);
		params.put("typeId", typeId);
		params.put("sTime", sTime);
		params.put("eTime", eTime);
		params.put("deadline", deadline);
		if (null != gid) {
			params.put("gid", gid);
		}
		params.put("limitCount", limitCount);
		params.put("description", description);
		params.put("sid", sid);
		Client.get("Event", "doAddEvent", params, handler);
	}

	public void partner(RequestParams params, int count, int page) {
		params.put("count", count + "");
		params.put("p", page + "");
		Client.get("Statuses", "partner", params, handler);
	}

	public void eventReset(RequestParams params, String eventId) {
		params.put("id", eventId);
		Client.get("Event", "eventReset", params, handler);
	}

	public void jsend(RequestParams params, String uid, String content,
			String title, String extra) {
		params.put("uid", uid);
		params.put("content", content);
		params.put("title", title);
		params.put("extras", extra);
		Client.get("Message", "jsend", params, handler);
	}

	public void lyLucky(RequestParams params) {
		Client.get("Statuses", "lyLucky", params, handler);
	}

	public void getVolunteerUpdate() {
		Client.getVolunteer(handler);
	}

	public void getGameList() {
		Client.getGame(handler);
	}

	public void getOrgList(RequestParams params, String sid) {
		params.put("sid", sid);
		Client.get("Event", "getOrgList", params, handler);
	}

	public void cancelEvent(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Event", "cancelEvent", params, handler);
	}

	public void trainSort(RequestParams params) {
		Client.get("Train", "cat", params, handler);
	}

	public void trainArea(RequestParams params, String pid) {
		if (null != pid) {
			// 返回城市列表
			params.put("pid", pid);
		} // else 返回省份列表
		Client.get("Train", "area", params, handler);
	}

	public void courseList(RequestParams params, String provinceId,
			String city, String catId, int count, int page) {
		params.put("provinceId", provinceId);
		params.put("city", city);
		params.put("catId", catId);
		params.put("count", count + "");
		params.put("page", page + "");
		Client.get("Train", "courseList", params, handler);
	}

	public void course(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Train", "course", params, handler);
	}

	public void collectList(RequestParams params, int mPerpage, int page) {
		params.put("count", mPerpage + "");
		params.put("page", page + "");
		Client.get("Train", "collectList", params, handler);
	}

	public void collect(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Train", "collect", params, handler);
	}

	public void cancelCollect(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Train", "cancelCollect", params, handler);
	}

	public void getDepart(RequestParams params, String sid) {
		params.put("sid", sid);
		Client.get("Sitelist", "getDepart", params, handler);
	}

	// place 表示广告的七个位置中的一个 （5-11）
	public void adList(RequestParams params, String sid, String place) {
		params.put("sid", sid);
		params.put("place", place);
		Client.get("Message", "adList", params, handler);
	}

	public void userHomeInfo(RequestParams params, String sid, long time) {
		params.put("school", sid);
		params.put("time", time + "");
		Client.get("User", "userHomeInfo", params, handler);
	}

	public void showByUid(RequestParams params, String uid, int page, int count) {
		params.put("uid", uid);
		params.put("page", page + "");
		params.put("count", count + "");
		Client.get("Message", "showByUid", params, handler);
	}

	public void lyCj(RequestParams params) {
		Client.get("Statuses", "lyCj", params, handler);
	}

	public void citySchool(RequestParams params, String type, String id) {
		params.put("type", type);
		params.put("id", id);
		Client.get("Donate", "citySchool", params, handler);
	}

	public void donate(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Donate", "donate", params, handler);
	}

	public void myDonateBuyer(RequestParams params) {
		Client.get("Donate", "myDonateBuyer", params, handler);
	}

	public void myDonate(RequestParams params) {
		Client.get("Donate", "myDonate", params, handler);
	}

	public void payment(RequestParams params, String id) {
		params.put("id", id);
		Client.get("Donate", "payment", params, handler);
	}

	public void catList(RequestParams params) {
		Client.get("Donate", "catList", params, handler);
	}

	public void donateList(RequestParams params, String province,
			String cityId, String sid, String sid1, String catId, String price,
			int page, int count) {
		params.put("province", province);
		params.put("cityId", cityId);
		params.put("sid", sid);
		params.put("sid1", sid1);
		params.put("catId", catId);
		params.put("price", price);
		params.put("page", page + "");
		params.put("count", count + "");
		Client.get("Donate", "donateList", params, handler);
	}
	
	/******************相册****************/
	public void getAlbumList(RequestParams params,String uid,int page,int count) {
		params.put("uid", uid );
		params.put("p", page + "");
		params.put("count", count + "");
		Client.get("Album", "albumList", params, handler);
	}
}
