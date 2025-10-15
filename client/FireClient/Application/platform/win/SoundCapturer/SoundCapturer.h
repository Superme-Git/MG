/*
	Function:
		一个简单的声音捕捉器。
		需要实现的功能如下：
			1、支持枚举出所有音频输入设备。
			2、提供使用指定音频输入设备录音功能。
			3、提供将录音内容保存到指定文件功能。
	
	History:
		2013.07.03		第一次创建

	Author:
		Colin 
*/

#pragma once
//#include <Guiddef.h>
#include <windows.h>
#include <MMSystem.h>
#include <initguid.h>
#include <dsound.h>
#pragma comment(lib,"winmm.lib")

#include <string>
using std::wstring;

// 音频输入设备枚举函数类型// 参见DirectSoundCaptureEnumerate参数1
typedef BOOL (CALLBACK *PSCENUMCALLBACK)(LPGUID lpGUID, LPCTSTR lpszDesc,LPCTSTR lpszDrvName, LPVOID lpContext);


#define CB_WAVE_FORM_BUFFER	 16384		// 每个存储音频缓冲的字节数大小
class CSoundCapturer
{
public:
	CSoundCapturer(IN PWAVEFORMATEX npdWaveFormatEx = NULL);
	~CSoundCapturer(void);

	// 设置音频输入设备枚举函数
	MMRESULT SetEnumCallBackProc(
		IN PSCENUMCALLBACK npFunAddress,		// 回调函数地址
		IN LPVOID npContext						// 发给回调函数的上下文
		);

	// 获取当前使用的音频设备信息
	

	// 启动录音
	MMRESULT StartRecord(
		IN LPGUID npDevGUID = NULL						// 设备GUID，如果为空，则使用默认的设备
		);

	// 停止录音
	MMRESULT StopRecord();

	// 保存录音内容到文件
	MMRESULT SaveRecordToFile(
		IN wchar_t* nswFileName
		);

	// 获取错误描述
	void GetErrorDescribe(
		IN MMRESULT nhMMResult,
		OUT wstring& noWStrDescrib);

	// 通过DirectSound返回的设备GUID，获取设备相关信息
	static BOOL GetInfoFromDSoundGUID( GUID i_sGUID, DWORD &dwWaveID, wstring &Description, wstring &Module, wstring &Interface);


protected:

	// 用于从GUID获取ID的函数
	static MMRESULT DirectSoundPrivateCreate (OUT LPKSPROPERTYSET * ppKsPropertySet);
	
	
	// 接受waveIn消息的回调函数
	static void CALLBACK WaveInMsgProc(HWAVEIN hwi,UINT uMsg,DWORD_PTR dwInstance,DWORD_PTR dwParam1,DWORD_PTR dwParam2);

private:
	WAVEFORMATEX mdWaveFormatEx;			// PCM格式

	HWAVEIN	mhWaveIn;						// 标识录音设备的句柄
	WAVEHDR	mdWaveHdr1,mdWaveHdr2;			// 标识两个录音缓冲区的头结构

	BYTE* mpDataBuffer;						// 存储数据的Buffer		// 这个buffer要用free释放	
	LONG mlCbBuffer;						// Buffer的大小

	wstring moWstrError;					// 错误信息描述

	bool mbIsStop;							// 标记是否停止录音
};

