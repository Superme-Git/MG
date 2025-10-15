#include "ResolutionAdapter.h"
#include <engine/nuengine.h>

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

static const float c_aspec_min = 1.0f;
static const float c_aspec_max = 2.0f;

#ifdef WIN32
static int c_width_max = 1200;
static int c_height_max = 720;
#else
static const int c_width_min = 1080;//1200;//1080 // 960;
static const int c_height_min = 720;// 640;

static const int c_width_max = 1080;//1200;//1080 // 1280;
static const int c_height_max = 720;// 800;
#endif // WIN32


ResolutionAdapter::ResolutionAdapter()
{ }

bool ResolutionAdapter::init()
{
	GLint vp[4];
	glGetIntegerv(GL_VIEWPORT, vp);
#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	m_screenW = vp[2];
	m_screenH = vp[3];
#elif defined _OS_IOS
    m_screenW = (vp[2] > vp[3] ? vp[2]: vp[3]);
    m_screenH = (vp[2] > vp[3] ? vp[3]: vp[2]);
#else
	m_screenW = vp[2];
	m_screenH = vp[3];
#endif

//MT的需求
#ifdef WIN32
	std::FILE *fp = std::fopen("frameResolutionSize.txt", "r");
	if (fp)
	{
		fseek(fp, 0, SEEK_END);
		long size = ftell(fp);
		fseek(fp, 0, SEEK_SET);
		char *buffer = new char[size + 1];
		memset(buffer, 0, size + 1);
		size = fread(buffer, sizeof(char), size, fp);
		char *p = strtok(buffer, " ");
		c_width_max = atoi(p);
		p = strtok(NULL, " ");
		c_height_max = atoi(p);
		delete[] buffer;	// yeqing 2015-10-19
		fclose(fp);
	}
#endif // WIN32

	if (m_screenW != c_width_max || m_screenH != c_height_max)
	{
		float w = (float)c_width_max / m_screenW;
		float h = (float)c_height_max / m_screenH;
		float r = (w > h ? w : h);
		m_logicW = r*m_screenW;
		m_logicH = r*m_screenH;
	}
	else
	{
		m_logicW = m_screenW;
		m_logicH = m_screenH;
	}

    m_displayX = 0;
    m_displayY = 0;
    
    m_displayW = m_screenW;
    m_displayH = m_screenH;
    
    m_screenUIW = m_screenW;
    m_screenUIH = m_screenH;
    
    m_logicUIW = m_logicW;
    m_logicUIH = m_logicH;
    
    m_displayUIX = m_displayX;
    m_displayUIY = m_displayY;
    
    m_displayUIW = m_screenW;
    m_displayUIH = m_screenH;
    
    LOGD("m_logicW ------ %d", m_logicW);
    LOGD("m_logicH ------ %d", m_logicH);
    LOGD("m_screenW ----- %d", m_screenW);
    LOGD("m_screenH ----- %d", m_screenH);
    LOGD("m_displayX ---- %d", m_displayX);
    LOGD("m_displayY ---- %d", m_displayY);
    LOGD("m_displayW ---- %d", m_displayW);
    LOGD("m_displayH ---- %d", m_displayH);
    LOGD("m_logicUIW ---- %d", m_logicUIW);
    LOGD("m_logicUIH ---- %d", m_logicUIH);
    LOGD("m_screenUIW --- %d", m_screenUIW);
    LOGD("m_screenUIH --- %d", m_screenUIH);
    LOGD("m_displayUIX -- %d", m_displayUIX);
    LOGD("m_displayUIY -- %d", m_displayUIY);
    LOGD("m_displayUIW -- %d", m_displayUIW);
    LOGD("m_displayUIH -- %d", m_displayUIH);
    return true;
}

void ResolutionAdapter::SetScreenHeight(int h)
{
	m_screenUIH = h;
}

void ResolutionAdapter::SetDisplayOffsetY(int y)
{
	m_displayUIY = y;
}

int ResolutionAdapter::GetDisplayHeight()
{
	return m_displayUIH;
}

int ResolutionAdapter::GetDisplayWidth()
{
	return m_displayUIW;
}

int ResolutionAdapter::GetDisplayOffsetY()
{
	return m_displayUIY;
}

int ResolutionAdapter::GetDisplayOffsetX()
{
	return m_displayUIX;
}

int ResolutionAdapter::GetScreenHeight()
{
	return m_screenUIH;
}

int ResolutionAdapter::GetScreenWidth()
{
	return m_screenUIW;
}

int ResolutionAdapter::GetLogicHeight()
{
	return m_logicUIH;
}

int ResolutionAdapter::GetLogicWidth()
{
	return m_logicUIW;
}

int ResolutionAdapter::get_display_h()
{
	return m_displayH;
}

int ResolutionAdapter::get_display_w()
{
	return m_displayW;
}

int ResolutionAdapter::get_display_y()
{
	return m_displayY;
}

int ResolutionAdapter::get_display_x()
{
	return m_displayX;
}

int ResolutionAdapter::get_logic_h()
{
	return m_logicH;
}

int ResolutionAdapter::get_logic_w()
{
	return m_logicW;
}

int ResolutionAdapter::get_screen_h()
{
	return m_screenH;
}

int ResolutionAdapter::get_screen_w()
{
    return m_screenW;
}

