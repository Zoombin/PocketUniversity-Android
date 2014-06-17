package com.xyhui.types;

public class PhotoList {
	private int status;
	private String msg;
	private Data data;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		private String id;
	    private String name;
	    private String mTime;
	    private String photoCount;
	    private String userId;
	    private String privacy;
	    private String coverImageId;
	    private Photo []photos;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getmTime() {
			return mTime;
		}
		public void setmTime(String mTime) {
			this.mTime = mTime;
		}
		public String getPhotoCount() {
			return photoCount;
		}
		public void setPhotoCount(String photoCount) {
			this.photoCount = photoCount;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getPrivacy() {
			return privacy;
		}
		public void setPrivacy(String privacy) {
			this.privacy = privacy;
		}
		public String getCoverImageId() {
			return coverImageId;
		}
		public void setCoverImageId(String coverImageId) {
			this.coverImageId = coverImageId;
		}
		public Photo[] getPhotos() {
			return photos;
		}
		public void setPhotos(Photo[] photos) {
			this.photos = photos;
		}
	}
}
