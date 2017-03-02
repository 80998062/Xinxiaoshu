package com.xinshu.xinxiaoshu.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xinshu.xinxiaoshu.models.SnsInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chiontang on 2/12/16.
 */
public class SnsReader {

    private static final String TAG = "SnsReader";
    private static final String DB_PATH = Config.EXT_DIR + "/SnsMicroMsg.db";
    private final Parser parser;
    private final Task task;
    private SQLiteDatabase database;

    private String currentUserId = null;


    public SnsReader(Task task, Parser parser) {
        this.parser = parser;
        this.task = task;

    }


    public Single<SQLiteDatabase> copyAndOpenDB() {
        return task.copySnsDbOB().flatMap(file -> getDB())
                .doOnSuccess(sqLiteDatabase -> Log.d(TAG, "copyAndOpenDB SUCCEED!"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<SQLiteDatabase> getDB() {
        return Single.fromCallable(this::openDatabase)
                .doOnSuccess(sqLiteDatabase -> Log.d(TAG, "getDB SUCCEED!"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private SQLiteDatabase openDatabase() {
        database = SQLiteDatabase.openDatabase(DB_PATH, null, 0);
        return database;
    }

    public Single<List<SnsInfo>> distinctTimelineOB() {
        return Single.fromCallable(this::distinctTimeline)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<SnsInfo> distinctTimeline() {
        List<SnsInfo> snsInfos = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from SnsInfo group by userName ORDER BY createTime", null);

        while (cursor.moveToNext()) {
            try {
                final SnsInfo item = getSnsInfo(cursor);
                snsInfos.add(item);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        cursor.close();
        return snsInfos;
    }


//    private void getCurrentUserIdFromDatabase(SQLiteDatabase database) throws Throwable {
//        Cursor cursor = database.query("snsExtInfo2", new String[]{"userName"}, "ROWID=?", new String[]{"1"}, "", "", "", "1");
//        if (cursor.moveToNext()) {
//            this.currentUserId = cursor.getString(cursor.getColumnIndex("userName"));
//        }
//        cursor.close();
//        Log.d("wechatmomentstat", "Current userID=" + this.currentUserId);
//    }

//    public ArrayList<SnsInfo> getTimelineByUid(@NotNull String userName) throws Exception {
//        Log.d(TAG, "getTimelineByUid");
//        long start = System.currentTimeMillis();
//        Log.d(TAG, "getTimelineByUid start @ " + start);
//        String dbPath = Config.EXT_DIR + "/SnsMicroMsg.db";
//        if (!new File(dbPath).exists()) {
//            Log.e("wechatmomentstat", "DB file not found");
//            throw new Exception("DB file not found");
//        }
//        snsList.clear();
//        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbPath, null, 0);
//        // do something
//        final String query = String.format("select * from SnsInfo where userName in (\"%s\")", userName);
//        Cursor cursor = database.rawQuery(query, null);
//        while (cursor.moveToNext()) {
//            try {
//                addSnsInfoFromCursor(cursor);
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//                throw new Exception(throwable);
//            }
//        }
//
//        cursor.close();
//        database.close();
//        Log.d(TAG, "getTimelineByUid costs " + (System.currentTimeMillis() - start) / 1000 + "seconds");
//
//        return snsList;
//    }

    private SnsInfo getSnsInfo(Cursor cursor) throws Throwable {
        byte[] snsDetailBin = cursor.getBlob(cursor.getColumnIndex("content"));
        byte[] snsObjectBin = cursor.getBlob(cursor.getColumnIndex("attrBuf"));
        SnsInfo newSns = parser.parseSnsAllFromBin(snsDetailBin, snsObjectBin);

        String stringSeq = cursor.getString(cursor.getColumnIndex("stringSeq"));

        newSns.stringSeq = stringSeq;

        for (int i = 0; i < newSns.comments.size(); i++) {
            if (newSns.comments.get(i).authorId.equals(this.currentUserId)) {
                newSns.comments.get(i).isCurrentUser = true;
            }
        }

        for (int i = 0; i < newSns.likes.size(); i++) {
            if (newSns.likes.get(i).userId.equals(this.currentUserId)) {
                newSns.likes.get(i).isCurrentUser = true;
            }
        }

        return newSns;
    }


}
