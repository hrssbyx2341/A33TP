package com.example.x6.a33tp.Date;

import android.content.Context;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/4.
 */

public class TypeManager {
    private Context context;
    private ResultDate resultDate;

    public final static int Y7 = 0;
    public final static int Y8 = 1;
    public final static int Y10 = 2;
    public final static int K7232 = 3;
    public final static int K10232 = 4;
    public final static int K7485 = 5;
    public final static int K10485 = 6;
    public final static int Y5 = 7;

    public final static String STR_Y7 = "Y7";
    public final static String STR_Y8 = "Y8";
    public final static String STR_Y10 = "Y10";
    public final static String STR_K7232 = "K7232";
    public final static String STR_K10232 = "K10232";
    public final static String STR_K7485 = "K7485";
    public final static String STR_K10485 = "K10485";
    public final static String STR_Y5 = "Y5";




    int productType = -1;
    String uuid = "";

    public int getProductType(){
        String str = "";
        try {
            str = resultDate.readLine(ResultDate.TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(str == null){
            return -1;
        }else if(str.equals("")){
            return -1;
        }else if(str.equals(STR_Y7)){
            this.productType = Y7;
        }else if(str.equals(STR_Y5)){
            this.productType = Y5;
        }else if(str.equals(STR_Y8)){
            this.productType = Y8;
        }else if(str.equals(STR_Y10)){
            this.productType = Y10;
        }else if(str.equals(STR_K7232)){
            this.productType = K7232;
        }else if(str.equals(STR_K7485)){
            this.productType = K7485;
        }else if(str.equals(STR_K10232)){
            this.productType = K10232;
        }else if(str.equals(STR_K10485)){
            this.productType = K10485;
        }
        return this.productType;
    }

    private static TypeManager typeManager = null;
    public static TypeManager getTypeManager(Context context1){
        if(typeManager == null)
            typeManager = new TypeManager(context1);
        return typeManager;
    }
    private TypeManager(Context context1){
        this.context = context1;
        resultDate = ResultDate.getResultDate(context);
    }
}
