package com.roh44x.eVent;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Post {
    public String uid;
    public String author;
    public String title;
    public String description;
    public int goingToNumber = 0;
    public int interestedInNumber = 0;
    public Map<String, Boolean> going = new HashMap<>();
    public Map<String, Boolean> interested = new HashMap<>();

    public Post(String uid, String author, String title, String description) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.description = description;
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

        return result;
    }
}