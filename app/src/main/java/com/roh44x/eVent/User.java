package com.roh44x.eVent;

import com.tylersuehr.chips.Chip;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String email;
    public List<? extends Chip> tags;

    public User(){}
    public User(String username, String email, List<? extends Chip> tags){
        this.username = username;
        this.email = email;
        this.tags = tags;
    }
}
