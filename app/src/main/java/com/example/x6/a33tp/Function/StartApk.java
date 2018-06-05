package com.example.x6.a33tp.Function;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 24179 on 2018/4/10.
 */

public class StartApk {
    /*
    启动软件 proname 为程序名字
    */
    public void startPro(String proname,Context context){
        String string = getAllAppNames(proname,context);
        if(string.equals("")){
            Toast.makeText(context,proname+" 没有安装",Toast.LENGTH_SHORT).show();
        }else{
            doStartApplicationWithPackageName(string,context);
        }
    }

    /* 根据程序名获取包名 */
    private String getAllAppNames(String name, Context context){
        String packageName = "";
        String appName = "";
        PackageManager pm=context.getPackageManager();
        List<PackageInfo> list2=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : list2) {
            appName=packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            packageName=packageInfo.packageName;
            Log.i("L", "应用的名字:"+appName);
            Log.i("L", "应用的包名字:"+packageName);
            if(name.equals(appName)){
                return packageName;
            }
        }
        return "";
    }
    /* 根据包名启动程序 */
    private void doStartApplicationWithPackageName(String packagename, Context context) {
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            String packageName = resolveinfo.activityInfo.packageName;
            String className = resolveinfo.activityInfo.name;
            Log.e("Activity名",className);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }
}
