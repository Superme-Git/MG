#ifndef _VideoEngineJni_h
#define _VideoEngineJni_h

extern "C"
{
	extern void createVideoActivityJni();
	extern void destroyVideoActivityJni();
	extern int createVideoWidgetJNI();
	extern void removeVideoWidgetJNI(int index);
	extern void setVideoURLJNI(int index, int sourceType, const char* szUrl);
	extern void startVideoJNI(int index);
	extern void pauseVideoJNI(int index);
	extern void resumeVideoJNI(int index);
	extern void stopVideoJNI(int index);
	extern void seekVideoToJNI(int index, int pos);
	extern void setVideoRectJNI(int index, int x, int y, int width, int height);
	extern void setVideoVisibleJni(int index, bool bVisible);
	extern void setVideoKeepRatioEnabledJni(int index, bool bEnable);
	extern void setFullScreenEnabledJni(int index, bool bEnable, int width, int height);
}

#endif
