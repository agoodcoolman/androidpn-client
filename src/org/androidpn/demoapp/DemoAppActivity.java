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
package org.androidpn.demoapp;

import org.androidpn.client.ServiceManager;
import org.androidpn.client.XmppManager;
import org.androidpn.client.XmppPush;
import org.androidpn.client.message.MessageManager;
import org.androidpn.client.message.XmppVideoMessage;
import org.androidpn.client.uitls.NetUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * This is an androidpn client demo application.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class DemoAppActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("DemoAppActivity", "onCreate()...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   
        final ServiceManager initialize = XmppPush.initialize(this);
        // Settings
        Button okButton = (Button) findViewById(R.id.btn_settings);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                ServiceManager.viewNotificationSettings(DemoAppActivity.this);
            	XmppVideoMessage chatMessage = null;
            	/*if (XmppPush.newUsername.endsWith("01")) {
            		// 互相发送.如果是1号就发给2号,如果2号就发给三号
            		chatMessage = MessageManager.getInstance().getChatMessage(
                			XmppVideoMessage.Android_client_02_jid, "");
            	} else {
            		chatMessage = MessageManager.getInstance().getChatMessage(
                			XmppVideoMessage.Android_client_01_jid, "");
            	}*/
            	
            	if (initialize.getNotificationService() != null ) {
//            		initialize.getNotificationService().sendMessage(chatMessage);
            		Log.d("DemoAppActivity", "发送出去的" + chatMessage.toXML());
            	}
            	
            }
        });    
        /*if (NetUtils.isNetWorkConnection(this)) {
        	
        } else {
        	Toast.makeText(this, "网络连接未开启", Toast.LENGTH_SHORT).show();
        }*/
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	XmppPush.onStart();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	XmppPush.onPause();
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	XmppPush.onDestroy();
    }

}