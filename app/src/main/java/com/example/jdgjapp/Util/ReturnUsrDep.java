package com.example.jdgjapp.Util;

import com.example.jdgjapp.Bean.Depart;
import com.example.jdgjapp.Bean.User;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by mwh on 2018/1/25.
 */

public class ReturnUsrDep {
    public static User returnUsr(){
        User user = DataSupport.findFirst(User.class);
        return user;
    }

    public static List returnDep(){
        List<Depart> departList = DataSupport.findAll(Depart.class);
        return departList;
    }
}
