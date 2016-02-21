/*
 * monitor.h
 *
 *  Created on: 2016��2��21��
 *      Author: Administrator
 */

#ifndef MONITOR_H_
#define MONITOR_H_

#endif /* MONITOR_H_ */
#include <jni.h>
#include <sys/select.h>
#include <unistd.h>
#include <sys/socket.h>
#include <pthread.h>
#include <signal.h>
#include <sys/wait.h>
#include <android/log.h>
#include <sys/types.h>
#include <sys/un.h>
#include <errno.h>
#include <stdlib.h>

#ifndef LOG_TAG
#define LOG_TAG "Native"
#endif

#ifndef LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)
#endif

/**
 * ����:�Ը��ӽ��̵�һ������
 * @author wangqiang
 * @date 2014-03-14
 */
class ProcessBase {
public:

	ProcessBase();

	/**
	 * ���ӽ���Ҫ���Ĺ�������ͬ,����һ������ӿ��ɸ��ӽ���
	 * �Լ�ȥʵ��.
	 */
	virtual void do_work() = 0;

	/**
	 * ���̿��Ը�����Ҫ�����ӽ���,�������Ҫ�����ӽ���,���Ը�
	 * �˽ӿ�һ����ʵ�ּ���.
	 */
	virtual bool create_child() = 0;

	/**
	 * ��׽�ӽ����������ź�,���û���ӽ��̴˷������Ը�һ����ʵ��.
	 */
	virtual void catch_child_dead_signal() = 0;

	/**
	 * ���ӽ�������֮������������.
	 */
	virtual void on_child_end() = 0;

	/**
	 * �������ӽ���ͨ��ͨ��.
	 */
	bool create_channel();

	/**
	 * ����������ͨ��ͨ��.
	 * @param channel_fd ͨ�����ļ�����
	 */
	void set_channel(int channel_fd);

	/**
	 * ��ͨ����д������.
	 * @param data д��ͨ��������
	 * @param len  д����ֽ���
	 * @return ʵ��д��ͨ�����ֽ���
	 */
	int write_to_channel(void* data, int len);

	/**
	 * ��ͨ���ж�����.
	 * @param data �����ͨ���ж��������
	 * @param len  ��ͨ���ж�����ֽ���
	 * @return ʵ�ʶ������ֽ���
	 */
	int read_from_channel(void* data, int len);

	/**
	 * ��ȡͨ����Ӧ���ļ�������
	 */
	int get_channel() const;

	virtual ~ProcessBase();

protected:

	int m_channel;
};

/**
 * ���ܣ������̵�ʵ��
 * @author wangqiang
 * @date 2014-03-14
 */
class Parent: public ProcessBase {
public:

	Parent(JNIEnv* env, jobject jobj);

	virtual bool create_child();

	virtual void do_work();

	virtual void catch_child_dead_signal();

	virtual void on_child_end();

	virtual ~Parent();

	bool create_channel();

	/**
	 * ��ȡ�����̵�JNIEnv
	 */
	JNIEnv *get_jni_env() const;

	/**
	 * ��ȡJava��Ķ���
	 */
	jobject get_jobj() const;

private:

	JNIEnv *m_env;

	jobject m_jobj;

};

/**
 * �ӽ��̵�ʵ��
 * @author wangqiang
 * @date 2014-03-14
 */
class Child: public ProcessBase {
public:

	Child();

	virtual ~Child();

	virtual void do_work();

	virtual bool create_child();

	virtual void catch_child_dead_signal();

	virtual void on_child_end();

	bool create_channel();

private:

	/**
	 * �������������¼�
	 */
	void handle_parent_die();

	/**
	 * ���������̷��͵���Ϣ
	 */
	void listen_msg();

	/**
	 * ��������������.
	 */
	void restart_parent();

	/**
	 * �������Ը����̵���Ϣ
	 */
	void handle_msg(const char* msg);

	/**
	 * �̺߳�����������⸸�����Ƿ�ҵ�
	 */
	void* parent_monitor();

	void start_parent_monitor();

	/**
	 * ���������������ǰ�������ĳ�Ա������Ϊ�̺߳���ʹ��
	 */
	union {
		void* (*thread_rtn)(void*);

		void* (Child::*member_rtn)();
	} RTN_MAP;
};

