package com.xyhui.utils;

import java.util.ArrayList;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mslibs.utils.JSONUtils;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.City;
import com.xyhui.types.EventCat;
import com.xyhui.types.GroupCats;
import com.xyhui.types.KeyValuePair;
import com.xyhui.types.School;
import com.xyhui.types.UserData;

public class LocalDataManager {
	private PrefUtil mPrefUtil;

	private ArrayList<School> mSchools;
	private ArrayList<City> mCitys;
	private ArrayList<EventCat> mEventCats;
	// private ArrayList<CourseCat> mCourseCats;
	private GroupCats mGroupCats;

	private String mIsVerified;
	private String mIsInited;

	public LocalDataManager() {
		mPrefUtil = new PrefUtil();
	}

	public void setSchools(Context context) {
		if (null == mSchools || mSchools.size() <= 1) {
			new Api(schoolListCB, context).getSchools();
		}
	}

	public ArrayList<School> getSchools() {
		if (null == mSchools || mSchools.size() <= 1) {
			String schools = mPrefUtil.getPreference(Params.LOCAL.SCHOOLLIST);

			mSchools = JSONUtils.fromJson(schools, new TypeToken<ArrayList<School>>() {
			});

			if (null != mSchools) {
				School allschool = new School();
				allschool.name = "选择学校";
				allschool.school = "0";

				mSchools.add(0, allschool);
			}
		}

		return new ArrayList<School>(mSchools);
	}

	public void setCitys(Context context) {
		if (null == mCitys || mCitys.size() <= 1) {
			new Api(cityListCB, context).getCitys();
		}
	}

	public ArrayList<City> getCitys() {
		if (null == mCitys || mCitys.size() <= 1) {
			String citys = mPrefUtil.getPreference(Params.LOCAL.CITYLIST);

			mCitys = JSONUtils.fromJson(citys, new TypeToken<ArrayList<City>>() {
			});

			if (null != mCitys) {
				City allCity = new City();
				allCity.city = "选择城市";
				allCity.id = "0";
				mCitys.add(0, allCity);
			}
		}

		return new ArrayList<City>(mCitys);
	}

	public boolean citySchoolArrived() {
		if (null != mSchools && null != mCitys) {
			return true;
		}
		return false;
	}

	public boolean catDataArrived() {
		if (null != mEventCats && groupCatsArrived()) {
			return true;
		}
		return false;
	}

	public boolean userDataArrived() {
		if (null != mIsVerified && null != mIsInited) {
			return true;
		}
		return false;
	}

	public boolean allDataArrived() {
		if (citySchoolArrived() && catDataArrived() && userDataArrived()) {
			return true;
		}
		return false;
	}

	public void setUserData(Context context) {
		if (!userDataArrived()) {
			new Api(checkUserCB, context).checkUser(PuApp.get().getToken());
		}
	}

	public void setEventCats(Context context) {
		if (null == mEventCats || mEventCats.size() <= 1) {
			new Api(eventCatListCB, context).getEventCats(PuApp.get().getToken());
		}
	}

	public ArrayList<EventCat> getEventCats() {
		if (null == mEventCats || mEventCats.size() <= 1) {
			String eventCats = mPrefUtil.getPreference(Params.LOCAL.EVENT_CAT);

			mEventCats = JSONUtils.fromJson(eventCats, new TypeToken<ArrayList<EventCat>>() {
			});

			EventCat sort = new EventCat();
			sort.title = "选择分类";
			sort.id = "0";
			mEventCats.add(0, sort);
		}

		return mEventCats;
	}

	// public void setCourseCats(Context context) {
	// if (null == mCourseCats || mCourseCats.size() <= 1) {
	// new Api(courseCatListCB, context).getCourseCats(PuApp.get().getToken());
	// }
	// }

	// public ArrayList<CourseCat> getCourseCats() {
	// if (null == mCourseCats || mCourseCats.size() <= 1) {
	// String courseCats = mPrefUtil.getPreference(Params.LOCAL.COURSE_CAT);
	//
	// mCourseCats = JSONUtils.fromJson(courseCats, new TypeToken<ArrayList<CourseCat>>() {
	// });
	//
	// CourseCat sort = new CourseCat();
	// sort.name = "选择分类";
	// sort.id = "0";
	// mCourseCats.add(0, sort);
	// }
	//
	// return mCourseCats;
	// }

	public boolean groupCatsArrived() {
		return (null != mGroupCats && null != mGroupCats.dpart && null != mGroupCats.school
				&& null != mGroupCats.year && null != mGroupCats.category);
	}

	public void setGroupCats(Context context) {
		if (!groupCatsArrived()) {
			new Api(groupCatListCB, context).groupCategory(PuApp.get().getToken());
		}
	}

	public GroupCats getGroupCats() {
		if (!groupCatsArrived()) {
			String groupCats = mPrefUtil.getPreference(Params.LOCAL.GROUP_CAT);

			mGroupCats = JSONUtils.fromJson(groupCats, GroupCats.class);

			KeyValuePair kv = new KeyValuePair("0", "选择部落");
			mGroupCats.dpart.add(0, kv);
			kv = new KeyValuePair("0", "选择院系");
			mGroupCats.school.add(0, kv);
			mGroupCats.year.add(0, "选择年级");
			kv = new KeyValuePair("0", "选择分类");
			mGroupCats.category.add(0, kv);
		}

		return mGroupCats;
	}

	public boolean isVerified() {
		return "1".equals(mIsVerified);
	}

	public boolean isInited() {
		return "1".equals(mIsInited);
	}

	public void clearData() {
		mCitys = null;
		mSchools = null;
		// mCourseCats = null;
		mEventCats = null;
		mGroupCats = null;
		mIsVerified = null;
		mIsInited = null;
	}

	CallBack schoolListCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			mPrefUtil.setPreference(Params.LOCAL.SCHOOLLIST, response);

			mSchools = JSONUtils.fromJson(response, new TypeToken<ArrayList<School>>() {
			});

			if (null != mSchools) {
				School allschool = new School();
				allschool.name = "选择学校";
				allschool.school = "0";

				mSchools.add(0, allschool);
			}
		}
	};

	CallBack cityListCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			mPrefUtil.setPreference(Params.LOCAL.CITYLIST, response);

			mCitys = JSONUtils.fromJson(response, new TypeToken<ArrayList<City>>() {
			});

			if (null != mCitys) {
				City allCity = new City();
				allCity.city = "选择城市";
				allCity.id = "0";
				mCitys.add(0, allCity);
			}
		}
	};

	CallBack checkUserCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			UserData data = JSONUtils.fromJson(response, UserData.class);

			mIsVerified = data.is_valid;
			mIsInited = data.is_init;
		}
	};

	CallBack groupCatListCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			mPrefUtil.setPreference(Params.LOCAL.GROUP_CAT, response);

			mGroupCats = JSONUtils.fromJson(response, GroupCats.class);

			KeyValuePair kv = new KeyValuePair("0", "选择部落");
			mGroupCats.dpart.add(0, kv);
			kv = new KeyValuePair("0", "选择院系");
			mGroupCats.school.add(0, kv);
			mGroupCats.year.add(0, "选择年级");
			kv = new KeyValuePair("0", "选择分类");
			mGroupCats.category.add(0, kv);
		}
	};

	// CallBack courseCatListCB = new CallBack() {
	// @Override
	// public void onSuccess(String response) {
	// mPrefUtil.setPreference(Params.LOCAL.COURSE_CAT, response);
	//
	// mCourseCats = JSONUtils.fromJson(response, new TypeToken<ArrayList<CourseCat>>() {
	// });
	//
	// CourseCat sort = new CourseCat();
	// sort.name = "选择分类";
	// sort.id = "0";
	// mCourseCats.add(0, sort);
	// }
	// };

	CallBack eventCatListCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			mPrefUtil.setPreference(Params.LOCAL.EVENT_CAT, response);

			mEventCats = JSONUtils.fromJson(response, new TypeToken<ArrayList<EventCat>>() {
			});

			EventCat sort = new EventCat();
			sort.title = "选择分类";
			sort.id = "0";
			mEventCats.add(0, sort);
		}
	};
}
