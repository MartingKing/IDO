package com.bsyun.ido.ui;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookTest implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.bsyun.ido")) {
            XposedBridge.log(" has Hooked!");
            Class clazz = loadPackageParam.classLoader.loadClass("com.bsyun.ido.ui.GalleryFragment");
            XposedHelpers.findAndHookMethod("com.bsyun.ido.ui.GalleryFragment", loadPackageParam.classLoader, "toastMessage", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult("破解成功——by 一夜梦惊人");
                }
            });
        }
    }
}
