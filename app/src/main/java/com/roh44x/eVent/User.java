package com.roh44x.eVent;

import com.google.firebase.database.Exclude;
import com.tylersuehr.chips.Chip;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public String username;
    public String email;
    public List<Map<String, String>> tags;
    public Boolean hasSignedUp;
    public Map<String, Double> coefList;

public User(){}
    public User(String username, String email,List<Map<String, String>> tags, Boolean hasSignedUp)
    {
        this.username = username;
        this.email = email;
        this.tags = tags;
        this.hasSignedUp = hasSignedUp;
    }

}
