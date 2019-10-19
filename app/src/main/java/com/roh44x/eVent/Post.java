package com.roh44x.eVent;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.tylersuehr.chips.Chip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {
    public String uid;
    public String author;
    public String title;
    public String description;
    public int goingToNumber = 0;
    public int tagcount;
    public int interestedInNumber = 0;
    public String filePath;
    public Map<String, Boolean> going = new HashMap<>();
    public Map<String, Boolean> interested = new HashMap<>();
    public List<Map<String, String>> tags;
    public String dateEvent;
    public String match;


    public Post(String uid, String author, String title, String description, String filePath, List<Map<String, String>> tags, String dateEvent, String match) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.tags = tags;
        this.dateEvent = dateEvent;
        this.match = match;
    }

    public Post(){}

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("description", description);
        result.put("goingToNumber", goingToNumber);
        result.put("interestedInNumber", interestedInNumber);
        result.put("going", going);
        result.put("interested", interested);
        result.put("filePath", filePath);
        result.put("match", match);

        return result;
    }
}
