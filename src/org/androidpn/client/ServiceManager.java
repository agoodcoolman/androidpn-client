/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import java.util.Properties;

import org.androidpn.client.NotificationService.NotificationServiceIBinder;
import org.androidpn.client.NotificationService.ServiceInterface;
import org.androidpn.client.heart.HeartManager;
import org.androidpn.client.uitls.LogUtil;
import org.androidpn.client.uitls.NetUtils;
import org.androidpn.client.uitls.TaskUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

/** 
 * This class is to manage the notificatin service and to load the configuration.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class ServiceManager {

    private static final String LOGTAG = LogUtil
            .makeLogTag(ServiceManager.class);

    private Context context;

    private SharedPreferences sharedPrefs;

    private Properties props;

    private String version = "0.5.0";

    private String apiKey;

    private String xmppHost;

    private String xmppPort;

    private NotificationServiceIBinder notificationSerivice;
    
    private String callbackActivityPackageName;

    private String callbackActivityClassName;

    public ServiceManager(Context context) {
        this.context = context;
        if (context instanceof Activity) {
            Log.i(LOGTAG, "Callback Activity...");
            Activity callbackActivity = (Activity) context;
            callbackActivityPackageName = callbackActivity.getPackageName();
            callbackActivityClassName = callbackActivity.getClass().getName();
        }

        //        apiKey = getMetaDataValue("ANDROIDPN_API_KEY");
        //        Log.i(LOGTAG, "apiKey=" + apiKey);
        //        //        if (apiKey == null) {
        //        //            Log.e(LOGTAG, "Please set the androidpn api key in the manifest file.");
        //        //            throw new RuntimeException();
        //        //        }

        props = loadProperties();
        apiKey = props.getProperty("apiKey", "");
        xmppHost = props.getProperty("xmppHost", "127.0.0.1");
        xmppPort = props.getProperty("xmppPort", "5222");
        
        
        Log.i(LOGTAG, "apiKey=" + apiKey);
        Log.i(LOGTAG, "xmppHost=" + xmppHost);
        Log.i(LOGTAG, "xmppPort=" + xmppPort);
        
        
        sharedPrefs = context.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(Constants.API_KEY, apiKey);
        editor.putString(Constants.VERSION, version);
        editor.putString(Constants.XMPP_HOST, xmppHost);
        editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
        editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME,
                callbackActivityPackageName);
        editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME,
                callbackActivityClassName);
        editor.commit();
        Log.i(LOGTAG, "sharedPrefs=" + sharedPrefs.toString());
    }

    public void startService() {
        Thread serviceThread = new Thread(new Runnable() {
            
			@SuppressLint("NewApi")
			@Override
            public void run() {
            	synchronized (context) {
            		if (NetUtils.isNetWorkConnection(context)) {
            			NetUtils.networkConnectionType(context);
                		// 有网络连接才进行连接
                		Intent intent = NotificationService.getIntent();
                        intent.setPackage(context.getPackageName());
                        intent.setAction("org.androidpn.client.NotificationService");
//                        context.startService(intent);
                        context.bindService(intent, sconnection, Context.BIND_AUTO_CREATE);
                	}
                }
			}
            	
        });
        serviceThread.start();
    }

    
    public void onStart() {
    	frontTaskActivity();
    }
    
    public void onPause () {
    	backgroundTaskActivity();
    }
    // 应用程序在前台时
    private void frontTaskActivity() {
    	Log.i(LOGTAG, "serviceManager frontTaskActivity...");
    	boolean appForgroud = TaskUtils.isAppForgroud(context);
    	if (appForgroud) {
    		if (notificationSerivice != null)
    			notificationSerivice.frontTask();
    	} 
    }
    // 应用程序在后台时候
    private void backgroundTaskActivity() {
    	Log.i(LOGTAG, "serviceManager backgroundTaskActivity...");
    	// 后台任务时
    	if (!TaskUtils.isAppForgroud(context)) {
    		if (notificationSerivice != null)
    		notificationSerivice.backgroundTask();
    	} 
    }
    
    public void stopService() {
        Intent intent = NotificationService.getIntent();
        context.stopService(intent);
        context.unbindService(sconnection);
    }

    // 获得meta的值
    private String getMetaDataValue(String name, String def) {
        String value = getMetaDataValue(name);
        return (value == null) ? def : value;
    }

    private String getMetaDataValue(String name) {
        Object value = null;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context
                    .getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (NameNotFoundException e) {
            throw new RuntimeException(
                    "Could not read the name in the manifest file.", e);
        }
        if (value == null) {
            throw new RuntimeException("The name '" + name
                    + "' is not defined in the manifest file's meta data.");
        }
        return value.toString();
    }

    
    
    private Properties loadProperties() {
        
        Properties props = new Properties();
        try {
            int id = context.getResources().getIdentifier("androidpn", "raw",
                    context.getPackageName());
            props.load(context.getResources().openRawResource(id));
        } catch (Exception e) {
            Log.e(LOGTAG, "Could not find the properties file.", e);
            // e.printStackTrace();
        }
        return props;
    }

        public String getVersion() {
            return version;
        }
    
        public String getApiKey() {
            return apiKey;
        }

    public void setNotificationIcon(int iconId) {
        Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.NOTIFICATION_ICON, iconId);
        editor.commit();
    }

    public void viewNotificationSettings() {
        Intent intent = new Intent().setClass(context,
                NotificationSettingsActivity.class);
        context.startActivity(intent);
    }

    public static void viewNotificationSettings(Context context) {
        Intent intent = new Intent().setClass(context,NotificationSettingsActivity.class);
        context.startActivity(intent);
    }
    
    public NotificationServiceIBinder getNotificationService() {
    	return notificationSerivice;
    }
    
    /* 绑定service监听*/  
    ServiceConnection sconnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(LOGTAG, "ServiceConnection onServiceConnected ...");
			// 连接
			notificationSerivice = (NotificationServiceIBinder)service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 断开连接
			
		}  
       
    };  
    
}
