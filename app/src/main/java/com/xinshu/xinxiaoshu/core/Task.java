package com.xinshu.xinxiaoshu.core;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.xinshu.xinxiaoshu.App;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by chiontang on 2/17/16.
 */
public class Task {

    private static final String TAG = "Task";

    private App context = null;

    public Task(App context) {
        this.context = context;
    }

    /**
     * 创建根目录
     *
     * @return
     */
    public Single<File> makeDirOB() {
        return Single.fromCallable(this::makeDir)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 移动assets中的apk文件到手机目录
     *
     * @return
     */
    public Single<File> moveApkOB() {
        return Single.fromCallable(this::copyWechatFromAssets)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 复制微信的数据库到目录
     *
     * @return
     */
    public Single<File> copySnsDbOB() {
        return Single.fromCallable(this::copySnsDB)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private File makeDir() {
        File extDir = new File(Config.EXT_DIR);
        if (!extDir.exists()) {
            if (!extDir.mkdir()) {
                throw new RuntimeException("创建根目录失败: " + Config.EXT_DIR);
            }
        }
        return extDir;
    }


    private File copyWechatFromAssets() throws IOException {

        File apkDir = new File(Config.EXT_DIR, "/wechat.apk");

        if (!apkDir.exists()) {
            InputStream assetInputStream;
            if (apkDir.exists()) {
                if (!apkDir.delete()) {
                    Log.d("apkOutput", "删除残留的微信apk文件失败,请手动尝试");
                    Toast.makeText(context, "删除残留的微信apk文件失败,请手动尝试", Toast.LENGTH_LONG).show();
                }
            }

            byte[] buf = new byte[1024];

            if (!apkDir.createNewFile()) {
                Log.d("apkOutput", "创建目录: " + apkDir.getPath() + " 失败,请手动尝试");
                throw new RuntimeException("创建目录: " + apkDir.getPath() + "失败");
            }
            assetInputStream = context.getAssets().open("wechat.apk");
            FileOutputStream outAPKStream = new FileOutputStream(apkDir);
            int read;
            while ((read = assetInputStream.read(buf)) != -1) {
                outAPKStream.write(buf, 0, read);
            }
            assetInputStream.close();
            outAPKStream.close();
        }

        return apkDir;
    }

    public void restartWeChat() throws Throwable {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> pids = am.getRunningAppProcesses();
        int pid = -1;
        for (int i = 0; i < pids.size(); i++) {
            ActivityManager.RunningAppProcessInfo info = pids.get(i);
            if (info.processName.equalsIgnoreCase(Config.WECHAT_PACKAGE)) {
                pid = info.pid;
            }
        }
        if (pid != -1) {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
            outputStream.writeBytes("kill " + pid + "\n");
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            outputStream.close();
        }
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(Config.WECHAT_PACKAGE);
        context.startActivity(launchIntent);

    }


    /**
     * <p>
     * 通过调用su，可以复制出微信的SQLite数据库文件到本工具可读写的目录下。
     * 微信朋友圈的SQLite文件在/data/data/com.tencent.mm/MicroMsg/XXXXXXXXXXXXX/SnsMicroMsg.db。
     * 其中，XXXXXXXXXXXXX是微信生成的hash值，每台设备上都可能不一样。由于在Android的shell中没有find或类似的命令，
     * 需要复制出这个SnsMicroMsg.db还得费一点功夫。
     * 最终，采用ls列目录并循环尝试cp的方法强行取得SnsMicroMsg.db。
     * </p>
     */
    private File copySnsDB() throws IOException, InterruptedException {
        String dataDir = Environment.getDataDirectory().getAbsolutePath();
        String destDir = Config.EXT_DIR;

        final File dbDir = new File((destDir + "/SnsMicroMsg.db"));
        if (dbDir.exists()) {
            if (!dbDir.delete()) {
                throw new IOException("删除旧db文件失败,请手动尝试");
            }
        }

        Process su = Runtime.getRuntime().exec("su");
        DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
        outputStream.writeBytes("mount -o remount,rw " + dataDir + "\n");
        outputStream.writeBytes("cd " + dataDir + "/data/" + Config.WECHAT_PACKAGE + "/MicroMsg\n");
        outputStream.writeBytes("ls | while read line; do cp ${line}/SnsMicroMsg.db " + destDir + "/ ; done \n");
        outputStream.writeBytes("sleep 1\n");

        // 还需要修改db文件的权限为777，否则工具无权读取数据库
        outputStream.writeBytes("chmod 777 " + destDir + "/SnsMicroMsg.db\n");
        outputStream.writeBytes("exit\n");
        outputStream.flush();
        outputStream.close();

        // sleep是为了避免稍后偶然性出现的读取数据库失败的情况（可能文件复制不完整或未被去锁？）。
        Thread.sleep(2000);

        return dbDir;
    }

    public Single<Boolean> testRoot() {
        return Single.fromCallable(this::isRoot)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private boolean isRoot() {

        try {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getWeChatVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(Config.WECHAT_PACKAGE, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getWeChatVersion", e);
            return null;
        }
        String wechatVersion = "";
        if (pInfo != null) {
            wechatVersion = pInfo.versionName;
            Config.initWeChatVersion(wechatVersion);
            return wechatVersion;
        }
        return null;
    }

}
