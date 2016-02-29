#ifndef MONITOR_CPP_
#define MONITOR_CPP_

#endif
#include <jni.h>
#include <org_androidpn_client_deamon_NativeWatchClass.h>
#ifndef _JNI_FALSE
#define _JNI_FALSE 0
#endif
/**
 * 全局变量，代表应用程序进程.
 */
ProcessBase *g_process = NULL;

/**
 * 应用进程的UID.
 */
const char* g_userId = NULL;

/**
 * 全局的JNIEnv，子进程有时会用到它.
 */
JNIEnv* g_env = NULL;

char* jstringTostring(JNIEnv* env, jstring jstr) {
	char* rtn = NULL;
	jclass clsstring = env->FindClass("java/lang/String");
	jstring strencode = env->NewStringUTF("utf-8");
	jmethodID mid = env->GetMethodID(clsstring, "getBytes",
			"(Ljava/lang/String;)[B");
	jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
	jsize alen = env->GetArrayLength(barr);
	jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
	if (alen > 0) {
		rtn = (char*) malloc(alen + 1);
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	env->ReleaseByteArrayElements(barr, ba, 0);
	return rtn;
}

/* Header for class org_androidpn_client_deamon_NativeWatchClass */

/*
 * Class:     org_androidpn_client_deamon_NativeWatchClass
 * Method:    createWatcher
 * Signature: (Ljava/lang/String;)Z
 */JNIEXPORT jboolean JNICALL Java_org_androidpn_client_deamon_NativeWatchClass_createWatcher(
		JNIEnv *env, jobject thiz, jstring user) {
	g_process = new Parent(env, thiz);

	g_userId = (const char*) jstringTostring(env, user);

	g_process->catch_child_dead_signal();

	if (!g_process->create_child()) {
		LOGE("<<create child error!>>");

		return JNI_FALSE;
	}

	return JNI_TRUE;
}
;

/*
 * Class:     org_androidpn_client_deamon_NativeWatchClass
 * Method:    connectToMonitor
 * Signature: ()Z
 */JNIEXPORT jboolean JNICALL Java_org_androidpn_client_deamon_NativeWatchClass_connectToMonitor(
		JNIEnv *env, jobject jobj) {
	if (g_process != NULL) {
		if (g_process->create_channel()) {
			return JNI_TRUE;
		}

		return JNI_FALSE;
	}
}
;

/*
 * Class:     org_androidpn_client_deamon_NativeWatchClass
 * Method:    sendMsgToMonitor
 * Signature: (Ljava/lang/String;)I
 */JNIEXPORT jint JNICALL Java_org_androidpn_client_deamon_NativeWatchClass_sendMsgToMonitor(
		JNIEnv *env, jobject jobj, jstring jstring) {

}
;



