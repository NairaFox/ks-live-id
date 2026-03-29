package com.example.kshook

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.smile.gifmaker") return

        val activityClass = XposedHelpers.findClass(
            "com.yxcorp.gifshow.detail.PhotoDetailActivity",
            lpparam.classLoader
        )

        XposedHelpers.findAndHookMethod(
            activityClass,
            "onResume",
            object : de.robv.android.xposed.XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {

                    val activity = param.thisObject as Activity
                    val intent = activity.intent

                    val data = intent.toString()

                    val regex = Regex("live/(\\d+)")
                    val match = regex.find(data)

                    if (match != null) {

                        val liveId = match.groupValues[1]

                        val clipboard =
                            activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                        val clip = ClipData.newPlainText("liveId", liveId)
                        clipboard.setPrimaryClip(clip)
                    }
                }
            })
    }
}
