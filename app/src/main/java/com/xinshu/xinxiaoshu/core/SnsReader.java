package com.xinshu.xinxiaoshu.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.annotations.NonNull;
import com.xinshu.xinxiaoshu.models.SnsInfo;

import org.greenrobot.greendao.annotation.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

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

//    selectrom SnsInfo where uid = 'bingwen391423' AND
//            ((stringSeq <= a) or (stringSeq>=b and stringSeq<=c) or (stringSeq>= d))


    public Single<List<SnsInfo>> timelineByUsernameOB(@NotNull String userName) {
        return Single.fromCallable(() -> getTimelineByUsername(userName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<SnsInfo> getTimelineByUsername(@NotNull String userName) throws Exception {
        List<SnsInfo> snsInfos = new ArrayList<>();

        final String query = String.format("select distinct from SnsInfo where userName = \'%s\'", userName);

        Cursor cursor = database.rawQuery(query, null);
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

    private SnsInfo getSnsInfo(Cursor cursor) throws Throwable {
        byte[] snsDetailBin = cursor.getBlob(cursor.getColumnIndex("content"));
        byte[] snsObjectBin = cursor.getBlob(cursor.getColumnIndex("attrBuf"));
        SnsInfo newSns = parser.parseSnsAllFromBin(snsDetailBin, snsObjectBin);

        newSns.stringSeq = cursor.getString(cursor.getColumnIndex("stringSeq"));

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


    public Single<JSONArray> convert2JSONOB(@NonNull final List<SnsInfo> snsList) {
        return Single.fromCallable(() -> convertToJSON(snsList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * <p>
     * 保存为JSON文件<p/>
     *
     * @param snsList
     * @return
     * @throws Exception
     */

    private JSONArray convertToJSON(List<SnsInfo> snsList) throws Exception {

        final JSONArray snsListJSON = new JSONArray();

        for (int snsIndex = 0; snsIndex < snsList.size(); snsIndex++) {
            SnsInfo currentSns = snsList.get(snsIndex);

            if (!currentSns.ready) {
                continue;
            }


            JSONObject snsJSON = new JSONObject();
            JSONArray commentsJSON = new JSONArray();
            JSONArray likesJSON = new JSONArray();
            JSONArray mediaListJSON = new JSONArray();

            snsJSON.put("isCurrentUser", currentSns.isCurrentUser);
            snsJSON.put("snsId", currentSns.id);
            snsJSON.put("authorName", currentSns.authorName);
            snsJSON.put("authorId", currentSns.authorId);
            snsJSON.put("content", currentSns.content);
            snsJSON.put("stringSeq", currentSns.stringSeq);

            for (int i = 0; i < currentSns.comments.size(); i++) {
                JSONObject commentJSON = new JSONObject();
                commentJSON.put("isCurrentUser", currentSns.comments.get(i).isCurrentUser);
                commentJSON.put("authorName", currentSns.comments.get(i).authorName);
                commentJSON.put("authorId", currentSns.comments.get(i).authorId);
                commentJSON.put("content", currentSns.comments.get(i).content);
                commentJSON.put("toUserName", currentSns.comments.get(i).toUser);
                commentJSON.put("toUserId", currentSns.comments.get(i).toUserId);
                commentsJSON.put(commentJSON);
            }

            snsJSON.put("comments", commentsJSON);

            for (int i = 0; i < currentSns.likes.size(); i++) {
                JSONObject likeJSON = new JSONObject();
                likeJSON.put("isCurrentUser", currentSns.likes.get(i).isCurrentUser);
                likeJSON.put("userName", currentSns.likes.get(i).userName);
                likeJSON.put("userId", currentSns.likes.get(i).userId);
                likesJSON.put(likeJSON);
            }
            snsJSON.put("likes", likesJSON);


            for (int i = 0; i < currentSns.mediaList.size(); i++) {
                mediaListJSON.put(currentSns.mediaList.get(i));
            }
            snsJSON.put("mediaList", mediaListJSON);
            snsJSON.put("rawXML", currentSns.rawXML);
            snsJSON.put("timestamp", currentSns.timestamp);

            snsListJSON.put(snsJSON);
        }

//        File jsonFile = new File(fileName);
//
//        if (!jsonFile.exists()) {
//            jsonFile.createNewFile();
//        }
//
//
//        FileWriter fw = new FileWriter(jsonFile.getAbsoluteFile());
//        BufferedWriter bw = new BufferedWriter(fw);
//        bw.write(snsListJSON.toString());
//        bw.close();


        return snsListJSON;
    }

}
