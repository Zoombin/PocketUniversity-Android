package com.xyhui.utils;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mslibs.utils.VolleyLog;
import com.xyhui.types.Game;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "PocketUniDB";

	// Games table name
	private static final String TABLE_GAMES = "games";

	// Games Table Columns names
	private static final String KEY_UID = "uid";
	private static final String KEY_NAME = "name";
	private static final String KEY_PKG_NAME = "pkg_name";
	private static final String KEY_LAUNCH_PATH = "launch_path";
	private static final String KEY_INSTALLED = "installed";
	private static final String KEY_APK_PATH = "apk_path";
	private static final String KEY_DOWNLOAD_URL = "download_url";
	private static final String KEY_ICON_URL = "icon_url";
	private static final String KEY_VERSION_NAME = "version_name";

	private static final String[] COLUMNS = { KEY_UID, KEY_NAME, KEY_PKG_NAME, KEY_LAUNCH_PATH,
			KEY_INSTALLED, KEY_APK_PATH, KEY_DOWNLOAD_URL, KEY_ICON_URL, KEY_VERSION_NAME };

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create game table
		String CREATE_GAME_TABLE = String
				.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY,  %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT , %s TEXT, %s TEXT, %s TEXT)",
						TABLE_GAMES, KEY_UID, KEY_NAME, KEY_PKG_NAME, KEY_LAUNCH_PATH,
						KEY_INSTALLED, KEY_APK_PATH, KEY_DOWNLOAD_URL, KEY_ICON_URL,
						KEY_VERSION_NAME);

		// create games table
		db.execSQL(CREATE_GAME_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older games table if existed
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_GAMES));

		// create fresh games table
		onCreate(db);
	}

	// ---------------------------------------------------------------------

	/**
	 * CRUD operations (create "add", read "get", update, delete) game + get all games + delete all
	 * games
	 */

	public void addOrUpdateGame(Game game) {
		VolleyLog.d("Begin %s", game.toString());
		SQLiteDatabase db = getWritableDatabase();

		db.execSQL(String
				.format("INSERT OR REPLACE INTO %s (%s, %s,%s, %s,%s, %s,%s,%s, %s) VALUES ('%s', '%s','%s', '%s','%s','%s','%s','%s','%s')",
						TABLE_GAMES, KEY_UID, KEY_NAME, KEY_PKG_NAME, KEY_LAUNCH_PATH,
						KEY_INSTALLED, KEY_APK_PATH, KEY_DOWNLOAD_URL, KEY_ICON_URL,
						KEY_VERSION_NAME, game.getUid(), game.getName(), game.getPkgName(),
						game.getLaunchPath(), game.getInstalled(), game.getApkPath(),
						game.getDownloadUrl(), game.getIconUrl(), game.getVersionName()));

		db.close();

		VolleyLog.d("End");
	}

	public void addGame(Game game) {
		VolleyLog.d("Begin %s", game.toString());
		// 1. get reference to writable DB
		SQLiteDatabase db = getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_UID, game.getUid());
		values.put(KEY_NAME, game.getName());
		values.put(KEY_PKG_NAME, game.getPkgName());
		values.put(KEY_LAUNCH_PATH, game.getLaunchPath());
		values.put(KEY_INSTALLED, game.getInstalled());
		values.put(KEY_APK_PATH, game.getApkPath());
		values.put(KEY_DOWNLOAD_URL, game.getDownloadUrl());
		values.put(KEY_ICON_URL, game.getIconUrl());
		values.put(KEY_VERSION_NAME, game.getVersionName());

		// 3. insert
		db.insert(TABLE_GAMES, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column values

		// 4. close
		db.close();
		VolleyLog.d("End");
	}

	public Game getGame(String uid) {
		VolleyLog.d("Begin");
		// 1. get reference to readable DB
		SQLiteDatabase db = getReadableDatabase();

		// 2. build query
		Cursor cursor = db.query(TABLE_GAMES, // a. table
				COLUMNS, // b. column names
				KEY_UID + " = ?", // c. selections
				new String[] { uid }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit

		// 3. if we got results get the first one
		if (cursor != null && cursor.moveToFirst()) {
			// 4. build game object
			Game game = new Game();
			game.setUid(cursor.getString(0));
			game.setName(cursor.getString(1));
			game.setPkgName(cursor.getString(2));
			game.setLaunchPath(cursor.getString(3));
			game.setInstalled(cursor.getString(4));
			game.setApkPath(cursor.getString(5));
			game.setDownloadUrl(cursor.getString(6));
			game.setIconUrl(cursor.getString(7));
			game.setVersionName(cursor.getString(8));

			VolleyLog.d("gameid( %s ):game( %s )", uid, game.toString());

			// 5. return game
			return game;
		} else {
			return null;
		}

	}

	// Get All Games
	public List<Game> getAllGames() {
		VolleyLog.d("Begin");
		List<Game> games = new LinkedList<Game>();

		// 1. build the query
		String query = "SELECT  * FROM " + TABLE_GAMES;

		// 2. get reference to writable DB
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build game and add it to list

		if (cursor.moveToFirst()) {
			do {
				Game game = new Game();
				game.setUid(cursor.getString(0));
				game.setName(cursor.getString(1));
				game.setPkgName(cursor.getString(2));
				game.setLaunchPath(cursor.getString(3));
				game.setInstalled(cursor.getString(4));
				game.setApkPath(cursor.getString(5));
				game.setDownloadUrl(cursor.getString(6));
				game.setIconUrl(cursor.getString(7));
				game.setVersionName(cursor.getString(8));

				// Add game to games
				games.add(game);
			} while (cursor.moveToNext());
		}

		VolleyLog.d(games.toString());

		// return games
		return games;
	}

	// Updating single game
	public int updateGame(Game game) {
		VolleyLog.d("Begin");

		// 1. get reference to writable DB
		SQLiteDatabase db = getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_UID, game.getUid());
		values.put(KEY_NAME, game.getName());
		values.put(KEY_PKG_NAME, game.getPkgName());
		values.put(KEY_LAUNCH_PATH, game.getLaunchPath());
		values.put(KEY_INSTALLED, game.getInstalled());
		values.put(KEY_APK_PATH, game.getApkPath());
		values.put(KEY_DOWNLOAD_URL, game.getDownloadUrl());
		values.put(KEY_ICON_URL, game.getIconUrl());
		values.put(KEY_VERSION_NAME, game.getVersionName());

		// 3. updating row
		int i = db.update(TABLE_GAMES, // table
				values, // column/value
				KEY_UID + " = ?", // selections
				new String[] { game.getUid() }); // selection args

		// 4. close
		db.close();

		VolleyLog.d("End");

		return i;
	}

	// Deleting single game
	public void deleteGame(Game game) {
		VolleyLog.d("Begin");

		// 1. get reference to writable DB
		SQLiteDatabase db = getWritableDatabase();

		// 2. delete
		db.delete(TABLE_GAMES, KEY_UID + " = ?", new String[] { game.getUid() });

		// 3. close
		db.close();

		VolleyLog.d(game.toString());
	}
}
