package com.roh44x.eVent;

import com.google.firebase.database.Exclude;
import com.tylersuehr.chips.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public String username;
    public String email;
    public List<? extends Chip> tags;
    public Map<String, Boolean> interested;
    public Map<String, Boolean> going;


    public User(){}
    public User(String username, String email, List<? extends Chip> tags){
        this.username = username;
        this.email = email;
        this.tags = tags;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("interested", interested);
        result.put("going", going);
        return result;
    }
}
