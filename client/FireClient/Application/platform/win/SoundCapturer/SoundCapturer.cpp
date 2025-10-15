#include "SoundCapturer.h"

#include <windows.h>
#include <dsconf.h>
#pragma comment(lib, "dsound.lib")

#include <assert.h>

#include "WaveFile.h"

CSoundCapturer::CSoundCapturer(IN PWAVEFORMATEX npdWaveFormatEx)
{
	mhWaveIn = NULL;

	ZeroMemory(&mdWaveHdr1, sizeof(mdWaveHdr1));
	ZeroMemory(&mdWaveHdr2, sizeof(mdWaveHdr1));

	mdWaveHdr1.lpData = (LPSTR)new BYTE[CB_WAVE_FORM_BUFFER];
	ZeroMemory(mdWaveHdr1.lpData, CB_WAVE_FORM_BUFFER);
	mdWaveHdr1.dwBufferLength = CB_WAVE_FORM_BUFFER;
	
	mdWaveHdr2.lpData = (LPSTR)new BYTE[CB_WAVE_FORM_BUFFER];
	ZeroMemory(mdWaveHdr2.lpData, CB_WAVE_FORM_BUFFER);
	mdWaveHdr2.dwBufferLength = CB_WAVE_FORM_BUFFER;

	mpDataBuffer = NULL;
	mlCbBuffer = 0;

	// 默认PCM格式
	if(NULL == npdWaveFormatEx)
	{
		ZeroMemory( &mdWaveFormatEx, sizeof(mdWaveFormatEx));
		mdWaveFormatEx.wFormatTag = WAVE_FORMAT_PCM;	// 通道数，单声道数据用单通道，立体声通道用双通道
		mdWaveFormatEx.nSamplesPerSec = 8000;			// 采样率（HZ），每秒钟采取样本的次数
		mdWaveFormatEx.wBitsPerSample =  16;			// 每个样本的位数，for WAVE_FORMAT_PCM，wBitsPerSample必须等于8或者16
		mdWaveFormatEx.nChannels = 1;					// 通道数，单声道数据用单通道，立体声通道用双通道
		mdWaveFormatEx.nBlockAlign = mdWaveFormatEx.nChannels * (mdWaveFormatEx.wBitsPerSample / 8);// 每个样本的字节数，for WAVE_FORMAT_PCM，nBlockAlign = (nChannels × wBitsPerSample) / 8
		mdWaveFormatEx.nAvgBytesPerSec = mdWaveFormatEx.nBlockAlign * mdWaveFormatEx.nSamplesPerSec;	// 每秒转换数据的字节数，for WAVE_FORMAT_PCM，nAvgBytesPerSec = nSamplesPerSec * nBlockAlign
	}
	else
		memcpy(&mdWaveFormatEx, npdWaveFormatEx, sizeof(WAVEFORMATEX));

	mbIsStop = true;
}


CSoundCapturer::~CSoundCapturer(void)
{
	StopRecord();

	if(NULL != mhWaveIn)
		waveInClose(mhWaveIn);

	if(NULL != mdWaveHdr1.lpData)
		delete [] mdWaveHdr1.lpData;

	if(NULL != mdWaveHdr2.lpData)
		delete [] mdWaveHdr2.lpData;

	if(NULL != mpDataBuffer)
	{
		delete [] mpDataBuffer;
	}
}


// 设置音频输入设备枚举函数
MMRESULT CSoundCapturer::SetEnumCallBackProc(
	IN PSCENUMCALLBACK npFunAddress,		// 回调函数地址
	IN LPVOID npContext						// 发给回调函数的上下文
	)
{
	MMRESULT lhMMResult = MMSYSERR_ERROR;
	do 
	{
		if(NULL == npFunAddress)
			lhMMResult = MMSYSERR_INVALPARAM;

		// 注册声音捕捉设备枚举回调函数
		lhMMResult = DirectSoundCaptureEnumerate((LPDSENUMCALLBACK)npFunAddress,npContext);

	} while (false);
	return lhMMResult;
}


//// 设置当前音频输入设备
//MMRESULT CSoundCapturer::SetSoundCaptureDev(
//	IN UINT nuiDevID						// 设备ID
//	)
//{
//	MMRESULT lhMMResult = MMSYSERR_ERROR;
//	do 
//	{
//		if(NULL != mhWaveIn)
//		{
//			waveInClose(mhWaveIn);
//		}
//
//		waveInOpen(mhWaveIn)
//
//	} while (false);
//	return lhMMResult;
//}


// 启动录音
MMRESULT CSoundCapturer::StartRecord(
	IN LPGUID npDevGUID								// 设备GUID，如果为空，则使用默认的设备
	)
{
	MMRESULT lhMMResult = MMSYSERR_ERROR;
	do 
	{
		// 获取设备ID
		UINT luiDevID = WAVE_MAPPER;
		if(NULL != npDevGUID)
		{
			// 通过GUID获取设备ID以及描述信息
			DWORD ldwDevID;
			wstring loWstrDescrib;
			wstring loWstrModule;
			wstring loWstrInterface;
			if(FALSE == GetInfoFromDSoundGUID(*npDevGUID, ldwDevID, loWstrDescrib, loWstrModule, loWstrInterface))
			{
				lhMMResult = MMSYSERR_INVALPARAM;
				break;
			}
			luiDevID = (UINT)ldwDevID;
		}

		// 确保之前的录音设置清空
		if(NULL != mhWaveIn)
		{
			lhMMResult = StopRecord();
			if(MMSYSERR_NOERROR != lhMMResult)
				break;
		}

		// 打开设备
		lhMMResult = waveInOpen(&mhWaveIn, luiDevID, &mdWaveFormatEx, (DWORD)CSoundCapturer::WaveInMsgProc, (DWORD)this, CALLBACK_FUNCTION);
		if(MMSYSERR_NOERROR != lhMMResult)
			break;

		// 准备数据缓冲区
		lhMMResult = waveInPrepareHeader(mhWaveIn, &mdWaveHdr1, sizeof(mdWaveHdr1));
		if(MMSYSERR_NOERROR != lhMMResult)
		{
			break;
		}
		lhMMResult = waveInAddBuffer(mhWaveIn, &mdWaveHdr1, sizeof (mdWaveHdr1));
		if(MMSYSERR_NOERROR != lhMMResult)
		{
			break;
		}

		lhMMResult = waveInPrepareHeader(mhWaveIn, &mdWaveHdr2, sizeof(mdWaveHdr2));
		if(MMSYSERR_NOERROR != lhMMResult)
		{
			break;
		}
		lhMMResult = waveInAddBuffer(mhWaveIn, &mdWaveHdr2, sizeof (mdWaveHdr2));
		if(MMSYSERR_NOERROR != lhMMResult)
		{
			break;
		}

		// 开始录音
		mbIsStop = false;
		lhMMResult = waveInStart(mhWaveIn);
		if(MMSYSERR_NOERROR != lhMMResult)
		{
			break;
		}

	} while (false);
	return lhMMResult;
}

void CALLBACK CSoundCapturer::WaveInMsgProc(HWAVEIN hwi,UINT uMsg,DWORD_PTR dwInstance,DWORD_PTR dwParam1,DWORD_PTR dwParam2)
{
	CSoundCapturer* lpoThis = (CSoundCapturer*)dwInstance;
	assert(NULL != lpoThis);

	MMRESULT lhMMResult = MMSYSERR_ERROR;
	switch(uMsg)
	{
	case WIM_OPEN:			// 该消息在waveInOpen函数内部就到达了，此时HWAVEIN还是无效状态，所以不能在这里准备缓冲区
		{
			// 删除存储的录音缓存
			if(NULL != lpoThis->mpDataBuffer)
			{
				free(lpoThis->mpDataBuffer);
				lpoThis->mpDataBuffer = NULL;
			}

			lpoThis->mlCbBuffer = 0;
		}
		break;
	case WIM_DATA:
		{
			PWAVEHDR lpoWaveHdr = (PWAVEHDR)dwParam1;
			if((WHDR_DONE & lpoWaveHdr->dwFlags) != 1)
				break;

			// 重新分配内存存储数据
			lpoThis->mpDataBuffer = (BYTE*)realloc(lpoThis->mpDataBuffer, lpoThis->mlCbBuffer + lpoWaveHdr->dwBytesRecorded);
			if(NULL == lpoThis->mpDataBuffer)
			{
				break;
			}

			if(0 != memcpy_s(lpoThis->mpDataBuffer + lpoThis->mlCbBuffer,
				lpoWaveHdr->dwBytesRecorded, lpoWaveHdr->lpData, lpoWaveHdr->dwBytesRecorded))
				break;

			lpoThis->mlCbBuffer += lpoWaveHdr->dwBytesRecorded;

			// 如果已经没有停止录音，则循环利用这个缓冲区
			if(false == lpoThis->mbIsStop)
			{
				// 循环利用缓冲区
				lhMMResult = waveInAddBuffer(lpoThis->mhWaveIn, lpoWaveHdr, sizeof(WAVEHDR));
				if(MMSYSERR_NOERROR != lhMMResult)
				{
					break;
				}
			}


		}
		break;
	case WIM_CLOSE:
		{
			// 卸载之前准备的缓冲头
			lhMMResult = waveInUnprepareHeader(lpoThis->mhWaveIn, &lpoThis->mdWaveHdr1, sizeof(lpoThis->mdWaveHdr1));
			if(MMSYSERR_NOERROR != lhMMResult)
			{
				break;
			}

			lhMMResult = waveInUnprepareHeader(lpoThis->mhWaveIn, &lpoThis->mdWaveHdr2, sizeof(lpoThis->mdWaveHdr2));
			if(MMSYSERR_NOERROR != lhMMResult)
			{
				break;
			}
		}
		break;

	default:
		break;
	}
}

// 停止录音
MMRESULT CSoundCapturer::StopRecord()
{
	mbIsStop = true;
	if(NULL != mhWaveIn)
	{
		MMRESULT lhMMRestult = waveInStop(mhWaveIn);
		if(MMSYSERR_NOERROR != lhMMRestult)
			return lhMMRestult;

		// 注意reset函数会使得所有的buffer都处于done状态，所以这里要加上标记，在数据处理的地方不再加入缓冲区
		lhMMRestult = waveInReset(mhWaveIn);
		if(MMSYSERR_NOERROR != lhMMRestult)
			return lhMMRestult;

		// 注意，只有所有Buffer都return了才能成功
		lhMMRestult = waveInClose(mhWaveIn);
		if (MMSYSERR_NOERROR != lhMMRestult)
			return lhMMRestult;

		mhWaveIn = NULL;
	}
	return MMSYSERR_NOERROR;
}


// 保存录音内容到文件
MMRESULT CSoundCapturer::SaveRecordToFile(
	IN wchar_t* nswFileName
	)
{
	MMRESULT lhMMResult = MMSYSERR_ERROR;
	do 
	{
		if(NULL == mpDataBuffer || 0 == mlCbBuffer)
		{
			break;
		}

		// 创建文件
		CWaveFile loWaveFile;
		lhMMResult = loWaveFile.Open(nswFileName, &mdWaveFormatEx, WAVEFILE_WRITE);
		if(MMSYSERR_NOERROR != lhMMResult)
		{
			loWaveFile.Close();
			break;
		}

		// 写入数据
		UINT luiDataWrote;
		lhMMResult = loWaveFile.Write(mlCbBuffer, mpDataBuffer, &luiDataWrote);
		if(MMSYSERR_NOERROR != lhMMResult)
		{
			break;
		}

		// 关闭文件
		lhMMResult = loWaveFile.Close();
		
	} while (false);
	return lhMMResult;
}

// 获取错误描述
void CSoundCapturer::GetErrorDescribe(
	IN MMRESULT nhMMResult,
	OUT wstring& noWStrDescrib)
{
	wchar_t* lswErrText = new wchar_t[MAXERRORLENGTH];
	assert(NULL != lswErrText);

	ZeroMemory(lswErrText, MAXERRORLENGTH);
	MMRESULT lhMMResult = waveInGetErrorTextW(nhMMResult, lswErrText, MAXERRORLENGTH);
	assert(lhMMResult == MMSYSERR_NOERROR);
	noWStrDescrib = lswErrText;

	delete [] lswErrText;
}

//////////////////////////////////////////////////////////////////////////
// 通过GUID获取设备描述信息

typedef WINUSERAPI MMRESULT (WINAPI *LPFNDLLGETCLASSOBJECT) (const CLSID &, const IID &, void **);
MMRESULT CSoundCapturer::DirectSoundPrivateCreate (OUT LPKSPROPERTYSET * ppKsPropertySet) 
{ 
	HMODULE                 hLibDsound              = NULL; 
	LPFNDLLGETCLASSOBJECT   pfnDllGetClassObject    = NULL; 
	LPCLASSFACTORY          pClassFactory           = NULL; 
	LPKSPROPERTYSET         pKsPropertySet          = NULL; 
	MMRESULT                 lhMMResult                      = DS_OK; 

	// Load dsound.dll 
	hLibDsound = LoadLibrary(TEXT("dsound.dll")); 

	if(!hLibDsound) 
	{ 
		lhMMResult = DSERR_GENERIC; 
	} 

	// Find DllGetClassObject 
	if(SUCCEEDED(lhMMResult)) 
	{ 
		pfnDllGetClassObject = 
			(LPFNDLLGETCLASSOBJECT)GetProcAddress ( hLibDsound, "DllGetClassObject" ); 


		if(!pfnDllGetClassObject) 
		{ 
			lhMMResult = DSERR_GENERIC; 
		} 
	} 

	// Create a class factory object     
	if(SUCCEEDED(lhMMResult)) 
	{ 
		lhMMResult = pfnDllGetClassObject (CLSID_DirectSoundPrivate, IID_IClassFactory, (LPVOID *)&pClassFactory ); 
	} 

	// Create the DirectSoundPrivate object and query for an IKsPropertySet 
	// interface 
	if(SUCCEEDED(lhMMResult)) 
	{ 
		lhMMResult = pClassFactory->CreateInstance ( NULL, IID_IKsPropertySet, (LPVOID *)&pKsPropertySet ); 
	} 

	// Release the class factory 
	if(pClassFactory) 
	{ 
		pClassFactory->Release(); 
	} 

	// Handle final success or failure 
	if(SUCCEEDED(lhMMResult)) 
	{ 
		*ppKsPropertySet = pKsPropertySet; 
	} 
	else if(pKsPropertySet) 
	{ 
		pKsPropertySet->Release(); 
	} 

	FreeLibrary(hLibDsound); 

	return lhMMResult; 
} 

 //Get the device information about a DirectSound GUID.  Description and Module is retrieved on a regular
 //DSound enumeration, but the Interface and the corresponding WaveID is not.
 //Thank you to anonymous person on DirectX team who demonstrated that I must call Get() twice in this case.
BOOL CSoundCapturer::GetInfoFromDSoundGUID( GUID i_sGUID, DWORD &dwWaveID, wstring &Description, wstring &Module, wstring &Interface)
{ 
	LPKSPROPERTYSET         pKsPropertySet = NULL; 
	MMRESULT                 lhMMResult; 
	BOOL					retval = FALSE;

	PDSPROPERTY_DIRECTSOUNDDEVICE_DESCRIPTION_DATA psDirectSoundDeviceDescription = NULL; 
	DSPROPERTY_DIRECTSOUNDDEVICE_DESCRIPTION_DATA sDirectSoundDeviceDescription;

	memset(&sDirectSoundDeviceDescription,0,sizeof(sDirectSoundDeviceDescription)); 
	lhMMResult = DirectSoundPrivateCreate( &pKsPropertySet ); 
	if(SUCCEEDED(lhMMResult)) 
	{ 
		ULONG ulBytesReturned = 0;
		sDirectSoundDeviceDescription.DeviceId = i_sGUID; 

		// On the first call the final size is unknown so pass the size of the struct in order to receive
		// "Type" and "DataFlow" values, ulBytesReturned will be populated with bytes required for struct+strings.
		lhMMResult = pKsPropertySet->Get(DSPROPSETID_DirectSoundDevice, 
			DSPROPERTY_DIRECTSOUNDDEVICE_DESCRIPTION, 
			NULL, 
			0, 
			&sDirectSoundDeviceDescription, 
			sizeof(sDirectSoundDeviceDescription), 
			&ulBytesReturned
			); 

		if (ulBytesReturned)
		{
			// On the first call it notifies us of the required amount of memory in order to receive the strings.
			// Allocate the required memory, the strings will be pointed to the memory space directly after the struct.
			psDirectSoundDeviceDescription = (PDSPROPERTY_DIRECTSOUNDDEVICE_DESCRIPTION_DATA)new BYTE[ulBytesReturned];
			*psDirectSoundDeviceDescription = sDirectSoundDeviceDescription;

			lhMMResult = pKsPropertySet->Get(DSPROPSETID_DirectSoundDevice, 
				DSPROPERTY_DIRECTSOUNDDEVICE_DESCRIPTION, 
				NULL, 
				0, 
				psDirectSoundDeviceDescription, 
				ulBytesReturned, 
				&ulBytesReturned
				); 

			dwWaveID  = psDirectSoundDeviceDescription->WaveDeviceId;
			Description = psDirectSoundDeviceDescription->Description;
			Module = psDirectSoundDeviceDescription->Module;
			Interface = psDirectSoundDeviceDescription->Interface;
			delete [] psDirectSoundDeviceDescription;
			retval = TRUE;
		}

		pKsPropertySet->Release(); 
	} 

	return retval; 
} 

//////////////////////////////////////////////////////////////////////////