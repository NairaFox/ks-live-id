package com.user.kwaihelper;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class KwaiHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.smile.gifmaker")) return;

        XposedHelpers.findAndHookMethod(
            "com.yxcorp.gifshow.detail.PhotoDetailActivity", 
            lpparam.classLoader, 
            "onResume", 
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Activity activity = (Activity) param.thisObject;
                    Intent intent = activity.getIntent();
                    if (intent != null) {
                        String intentString = intent.toUri(0);
                        Matcher matcher = Pattern.compile("live/(\\d+)").matcher(intentString);
                        if (matcher.find()) {
                            String liveId = matcher.group(1);
                            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            if (cm != null) {
                                cm.setText(liveId);
                                Toast.makeText(activity, "🎯 云端编译版提取成功: " + liveId, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        );
    }
}
