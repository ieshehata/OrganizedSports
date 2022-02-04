package com.app.organizedsports.utils;

import com.app.organizedsports.model.QuestionAndAnsModel;
import com.app.organizedsports.model.QuestionsModel;
import com.app.organizedsports.model.UserModel;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

public class SharedData {
    public static int userType = 2; // 1->Admin, 2->user
    public static int state =0 ;//-2=Blocked,-1=Rejected 0=Waiting, 1=Accepted , 2= warning
    public static double longitude;
    public static double latitude;
    public static LatLng currentLatLng;

    public static UserModel currentUser = new UserModel();

    public static UserModel user;

    public static ArrayList<QuestionsModel> allQuestions = new ArrayList<>();
    public static ArrayList<QuestionAndAnsModel> allQuestionAndAns = new ArrayList<>();
    public static String format = "EEE, d MMM yyyy hh:mm a";
    public static String formatTime = "hh:mm a";
    public static String formatDate = "dd/MM/yyyy";
    public static String formatDateTime = "dd/MM/yyyy hh:mm a";
    public static String imageUrl;
    public static final int NOTIFICATION_ID = 1303;
    public static final String PREF_KEY = "login";
    public static final String IS_USER_SAVED = "SAVED_USER";
    public static final String PHONE = "PHONE";
    public static final String PASS = "PASS";
    public static final String USER_TYPE = "USER_TYPE";
}
