package com.xinshu.xinxiaoshu.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiontang on 2/8/16.
 */
public class SnsInfo {
    public static final String TAG = "SnsInfo";

    public String id = "";
    public String authorName = "";
    public String content = "";
    public String authorId = "";
    public List<Like> likes = new ArrayList<>();
    public List<Comment> comments = new ArrayList<>();
    public List<String> mediaList = new ArrayList<>();
    public String rawXML = "";
    public long timestamp = 0;
    public boolean ready = false;
    public boolean isCurrentUser = false;
    public boolean selected = true;

    public void print() {
        Log.d(TAG, "================================");
        Log.d(TAG, "id: " + this.id);
        Log.d(TAG, "Author: " + this.authorName);
        Log.d(TAG, "Content: " + this.content);
        Log.d(TAG, "Likes:");
        for (int i = 0; i < likes.size(); i++) {
            Log.d(TAG, likes.get(i).userName);
        }
        Log.d(TAG, "Comments:");
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            Log.d(TAG, "CommentAuthor: " + comment.authorName + "; CommentContent: " + comment.content + "; ToUser: " + comment.toUser);
        }
        Log.d(TAG, "Media List:");
        for (int i = 0; i < mediaList.size(); i++) {
            Log.d(TAG, mediaList.get(i));
        }
    }

    public SnsInfo clone() {
        SnsInfo newSns = new SnsInfo();
        newSns.id = this.id;
        newSns.authorName = this.authorName;
        newSns.content = this.content;
        newSns.authorId = this.authorId;
        newSns.likes = new ArrayList<>(this.likes);
        newSns.comments = new ArrayList<>(this.comments);
        newSns.mediaList = new ArrayList<>(this.mediaList);
        newSns.rawXML = this.rawXML;
        newSns.timestamp = this.timestamp;
        return newSns;
    }

    public void clear() {
        id = "";
        authorName = "";
        content = "";
        authorId = "";
        likes.clear();
        comments.clear();
        mediaList.clear();
        rawXML = "";
    }

}
