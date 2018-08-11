package com.example.kuoky.myapplication.Drawer;

import com.kinvey.android.Client;
import com.kinvey.android.model.User;

public class Common
{
    public static String currentToken="";

    private static String baseURL="https://fcm.googleapis.com/";

    public static User user=null;

    public static Client client=null;
    public static int notification=0;

}
