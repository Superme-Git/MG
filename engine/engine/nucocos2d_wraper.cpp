//  cocos2d_wraper.cpp
//  engine

#include <iostream>
#include "nucocos2d_wraper.h"
#include "shaders/CCGLProgram.h"
#include "shaders/ccGLStateCache.h"
#include "shaders/CCShaderCache.h"
#if ((defined WIN32) || (defined _WIN32))
#include "../../../cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/include/SimpleAudioEngine.h"
#include "CCApplication.h"
#endif
#include "utils/FileUtil.h"
#include "utils/Utils.h"
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
#include "../../../client/FireClient/Application/Utils/SystemInfo.h"
#endif
#define NoTouchDuraTime 300000 //5分钟
#define DefaultDarkBrightness 0.01f

#ifdef ANDROID
#include "../../../cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/include/SimpleAudioEngine.h"
#endif

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


using namespace cocos2d;
namespace Nuclear
{
    static EngineLayer* s_EngineLayer = NULL;
    
    bool IsInIpad()
    {
#if (defined WIN32) || (defined _WIN32)
		return false;
#else
#ifndef ANDROID

        CCDirector* pDirector = CCDirector::sharedDirector();
        assert(pDirector);
        return pDirector->getOpenGLView()->isIpad();
#else
		return false;
#endif
#endif
    }
    
    IEngine* GetEnginePtr()
    {
#if (defined WIN32) || (defined _WIN32)
#if (defined WIN7_32)
		EngineApp* pApp = dynamic_cast<EngineApp*>(&cocos2d::CCApplication::sharedApplication());//modify by eagle
#else
		EngineApp* pApp = dynamic_cast<EngineApp*>(cocos2d::CCApplication::sharedApplication());
#endif
#else
        EngineApp* pApp = dynamic_cast<EngineApp*>(&cocos2d::CCApplication::sharedApplication());
#endif
        if (pApp) return pApp->GetEnginePtr();
     
        return NULL;
    }
    
    EngineApp::EngineApp(IEngine* aPEngine)
    :m_PEngine(aPEngine)
    {
        
    }
    
    EngineApp::~EngineApp()
    {
        
    }
    
    bool EngineApp::applicationDidFinishLaunching()
    {
        CCDirector* pDirector = CCDirector::sharedDirector();
#if (defined WIN32) || (defined _WIN32)
#if (defined WIN7_32)
		pDirector->setOpenGLView(CCEGLView::sharedOpenGLView());
#else
		pDirector->setOpenGLView(CCEGLView::sharedOpenGLView());
#endif
#else
        pDirector->setOpenGLView(CCEGLView::sharedOpenGLView());
#endif
        pDirector->setDisplayStats(false);

#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
		pDirector->setAnimationInterval(1.0 / 1000.0);
#elif (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
		pDirector->setAnimationInterval(1.0 / 45.0f);

		if (!pDirector->getOpenGLView()->isIpad())//shield by yyy  --open by liugeng
		{
			pDirector->enableRetinaDisplay(true);
		}
#else
		pDirector->setAnimationInterval(1.0 / 45.0f);//yangbin---setfps android and windows
#endif
        
        CCScene* pScene = CCScene::create();        
        s_EngineLayer = EngineLayer::create();
        pScene->addChild(s_EngineLayer);
        pDirector->runWithScene(pScene);
        
        return true;
    }
    
    void EngineApp::applicationDidEnterBackground()
    { 
#if (defined ANDROID) || ((defined WIN32) || (defined _WIN32))
        GetEnginePtr()->GetApp()->applicationDidEnterBackground();
        CocosDenshion::SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();
		CocosDenshion::SimpleAudioEngine::sharedEngine()->stopAllEffects();
		CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectEnable(true);
#endif        
    }
    
    void EngineApp::applicationWillEnterForeground()
    {
#if (defined ANDROID) || ((defined WIN32) || (defined _WIN32))
        CocosDenshion::SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
        GetEnginePtr()->GetApp()->applicationEnterForeground();
        if (cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()) {
            cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ResetServerTimer");
        }
#endif
    }
    
    void EngineApp::setAnimationInterval(double interval)
    {
        cocos2d::CCApplication::setAnimationInterval(interval);
    }
    
    
    EngineTicker::EngineTicker(EngineLayer* aPEngine)
    :m_pEngineLayer(aPEngine)
    {
        
    }
    
    bool EngineTicker::isDone(void)
    {
        return false;
    }
    
    void EngineTicker::step(float dt)
    {
        IEngine * pEngine = GetEnginePtr();
        if (pEngine) {
            pEngine->OnIdle();
        }
        return;
    }

       
    EngineLayer::EngineLayer()
    {
#if defined DEBUG || defined _DEBUG
		m_bShowingTexture = false;
#endif
		m_pTicker = new EngineTicker(this);
        IEngine* pEngine = GetEnginePtr();
        if (pEngine)
        {
            pEngine->SetEngineLayer(this);
        }
		else
        {
            assert(false && "set engine layer fail");
        }

		m_LastTick = Nuclear::GetMilliSeconds();
		m_DuraTime = 0;
		m_bDark = false;
		m_IsTick = true;
		m_IsRunBrightNess = false;
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
		float VersionFloatValue =  MT3SystemInfo::sGetIOSVersionFloatValue();
		m_BrightNessVersion = (VersionFloatValue >= 8.1);
#else
		m_BrightNessVersion = true;
#endif
    }
    
    EngineLayer::~EngineLayer()
    {
        if (m_pTicker) {
            delete m_pTicker;
        }
    }
    
	#define SCALE_POINT(point, scale)\
	do{\
		point.x *= scale;\
		point.y *= scale;\
		point.x *= ((float)pEngine->m_adapter->get_logic_w()/(float)pEngine->m_adapter->get_display_w());\
		point.y *= ((float)pEngine->m_adapter->get_logic_h()/(float)pEngine->m_adapter->get_display_h());\
		point.x -= pEngine->m_adapter->get_display_x();\
		point.y -= pEngine->m_adapter->get_display_y();\
	}while(false)
    

	static CCDictionary s_dic;

	void EngineLayer::ccTouchesBegan(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent)
	{
		CCSetIterator iter = pTouches->begin();
		for (; iter != pTouches->end(); iter++)
		{
			CCTouch* pTouch = (CCTouch*)(*iter);

			if (s_dic.objectForKey(pTouch->getID()))
			{
				continue;
			}
			else{
				s_dic.setObject(pTouch, pTouch->getID());
			}
            
            
            //CCLog("touch id:%d",pTouch->getID());

#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)
			CCPoint pt = pTouch->getLocationInView();
#else
			CCPoint pt = pTouch->locationInView();
#endif
			float fScale = CCDirector::sharedDirector()->getContentScaleFactor();
			Nuclear::IEngine* pEngine = Nuclear::GetEngine();
			SCALE_POINT(pt, fScale);

			float force = pTouch->getForce();

			pEngine->OnWindowsMessage((HWND)pTouch->getID(), WM_LBUTTONDOWN, pt.x, pt.y, &force);

			m_DuraTime = 0;
			bool IsRun = true;
#if defined(_LOCOJOY_SDK_) || defined(_YJ_SDK_)
			if (m_IsRunBrightNess)
				IsRun = true;
			else
				IsRun = false;
#endif
			if (IsRun&&m_BrightNessVersion)
			{
				m_LastTick = Nuclear::GetMilliSeconds();
				if (m_bDark)
				{
					IOS_MHSD_UTILS::SetCurBrightness(m_fCurBrightness);
					m_bDark = false;
				}
			}
			int pid = pTouch->getID();
			if (0 <= pid && pid < MAXPOINT)
				m_Pos[pid] = pt;
		}
	}

	void EngineLayer::ccTouchesMoved(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent)
	{
        Nuclear::IEngine* pEngine = Nuclear::GetEngine();
        
        float fScale = CCDirector::sharedDirector()->getContentScaleFactor();
        
		CCTouch* tmp[CC_MAX_TOUCHES];
		for (int i = 0; i < CC_MAX_TOUCHES; i++) {
            tmp[i] = NULL;
        }
		pEngine->OnWindowsMessage(0, 990, 0, 0);

		CCSetIterator iter = pTouches->begin();
		for (; iter != pTouches->end(); iter++)
		{
			CCTouch* pTouch = (CCTouch*)(*iter);

#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)
			CCPoint pt = pTouch->getLocationInView();
#else
			CCPoint pt = pTouch->locationInView();
#endif
			
			SCALE_POINT(pt, fScale);

			float force = pTouch->getForce();

			pEngine->OnWindowsMessage(0, WM_MOUSEMOVE, pt.x, pt.y, &force);
            
            tmp[pTouch->getID()] = pTouch;
		}
        
        if (tmp[0] && tmp[1]) {
            CCPoint tmp1 = tmp[0]->locationInView();
            CCPoint tmp2 = tmp[1]->locationInView();
            SCALE_POINT(tmp1, fScale);
            SCALE_POINT(tmp2, fScale);
            
            if (ccpDistance(tmp1, tmp2) - ccpDistance(m_Pos[0], m_Pos[1]) > 10 ) {
                if (ccpDistance(m_Pos[0], tmp1) >= 20 && ccpDistance(m_Pos[1], tmp2) >= 20) {//开
                    pEngine->OnWindowsMessage(0, 998, 0, 0);
                    m_Pos[0] = tmp1;
                    m_Pos[1] = tmp2;
                }
            }
            if (ccpDistance(tmp1, tmp2) - ccpDistance(m_Pos[0], m_Pos[1]) < -10 ) {
                if (ccpDistance(m_Pos[0], tmp1) >= 20 && ccpDistance(m_Pos[1], tmp2) >= 20) {//合
                    pEngine->OnWindowsMessage(0, 999, 0, 0);
                    m_Pos[0] = tmp1;
                    m_Pos[1] = tmp2;
                }
            }
        }
	}
    

	void EngineLayer::ccTouchesEnded(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent)
	{
		CCSetIterator iter = pTouches->begin();
		for (; iter != pTouches->end(); iter++)
		{
			CCTouch* pTouch = (CCTouch*)(*iter);

#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)
			CCPoint pt = pTouch->getLocationInView();
#else
			CCPoint pt = pTouch->locationInView();
#endif 
			float fScale = CCDirector::sharedDirector()->getContentScaleFactor();
			Nuclear::IEngine* pEngine = Nuclear::GetEngine();
			SCALE_POINT(pt, fScale);

			float force = pTouch->getForce();

			pEngine->OnWindowsMessage((HWND)pTouch->getID(), WM_LBUTTONUP, pt.x, pt.y, &force);

			s_dic.removeObjectForKey(pTouch->getID());

			int pid = pTouch->getID();
			if (0 <= pid && pid < MAXPOINT)
				m_Pos[pid] = CCPoint(0, 0);
		}
	}

	void EngineLayer::ccTouchesCancelled(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent)
	{
		ccTouchesEnded(pTouches, pEvent);
	}
    
    void EngineLayer::ccLongPress(int num, float xs[], float ys[], int state)
	{
        if (num <= 0)
            return;
        
        CCPoint pt(xs[0], ys[0]);
        float fScale = CCDirector::sharedDirector()->getContentScaleFactor();
        Nuclear::IEngine* pEngine = Nuclear::GetEngine();
        SCALE_POINT(pt, fScale);
        
        pEngine->OnWindowsMessage((HWND)state, WM_LONGPRESS, pt.x, pt.y);
    }
    
    void EngineLayer::handleClick(float xPos, float yPos)
    {
        CCPoint pt(xPos,yPos);
        float fScale = CCDirector::sharedDirector()->getContentScaleFactor();
        Nuclear::IEngine* pEngine = Nuclear::GetEngine();
        SCALE_POINT(pt, fScale);
        
        printf("click pos %d,%d \n",(int)pt.x,(int)pt.y);
        pEngine->OnWindowsMessage(0, WM_CLICK, pt.x, pt.y);
    }
    
    void EngineLayer::handleDoubleClick(float xPos, float yPos)
    {
        CCPoint pt(xPos,yPos);
        float fScale = CCDirector::sharedDirector()->getContentScaleFactor();
        Nuclear::IEngine* pEngine = Nuclear::GetEngine();
        SCALE_POINT(pt, fScale);
        
        printf("doubleclick pos %d,%d \n",(int)pt.x,(int)pt.y);
        pEngine->OnWindowsMessage(0, WM_DOUBLECLICK, pt.x, pt.y);
    }
    
    void EngineLayer::handleSlide(int dir, float xStart, float yStart, float v)
    {
        CCPoint pt(xStart,yStart);
        float fScale = CCDirector::sharedDirector()->getContentScaleFactor();
        Nuclear::IEngine* pEngine = Nuclear::GetEngine();
        SCALE_POINT(pt, fScale);
        
        printf("slide pos %d,%d \n",(int)pt.x,(int)pt.y);
        pEngine->OnWindowsMessage((HWND)dir, WM_SLIDE, pt.x, pt.y);
    }
    
    void EngineLayer::handleDrag(int state, float xStart, float yStart, float v_x,float v_y)
    {
        CCPoint pt(xStart,yStart);
        if (state==3) {
            pt.x=v_x;
            pt.y=v_y;
        }
        float fScale = CCDirector::sharedDirector()->getContentScaleFactor();
        Nuclear::IEngine* pEngine = Nuclear::GetEngine();
        SCALE_POINT(pt, fScale);
        
         printf("drag pos %d,%d \n",(int)pt.x,(int)pt.y);
        pEngine->OnWindowsMessage((HWND)state, WM_DRAG, pt.x, pt.y);
    }
    
    bool EngineLayer::init()
    {
        if ( !CCLayer::init() )
        {
            return false;
        }

		setTouchEnabled(true);

		m_fCurBrightness = IOS_MHSD_UTILS::GetCurBrightness();

        return true;
    }

	void EngineLayer::registerWithTouchDispatcher(void)
	{
		CCDirector::sharedDirector()->getTouchDispatcher()->addStandardDelegate(this, 0);
	}
    
    void EngineLayer::onEnter()
    {
        CCLayer::onEnter();
        this->runAction(m_pTicker);
    }
    
    void EngineLayer::onExit()
    {
        CCLayer::onExit();
    }
    
    void EngineLayer::draw(void)
    {
        CCLayer::draw();
        
        CCSize size = CCDirector::sharedDirector()->getWinSizeInPixels();        
        Nuclear::IEngine* pEngine = Nuclear::GetEngine();
        
        float lh = pEngine->m_adapter->get_logic_h();
        float lw = pEngine->m_adapter->get_logic_w();
        float sh = pEngine->m_adapter->get_screen_h();
        
        float zeye = 0.f;
        if(IsInIpad())
        {
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)
             zeye = sh / 1.1566f;
#else
             zeye = sh / 1.1566f/ CC_CONTENT_SCALE_FACTOR();
#endif
        }
		else
        {
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
			zeye = sh/2/0.57735;
#else
            zeye = sh/2/0.57735;
#endif
        }
        
        float s = lh/sh;
        zeye = zeye * s;
        
        float midx = lw*0.5f;
        float midy = lh*0.5f;
       
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		glViewport(pEngine->m_adapter->get_display_x(), pEngine->m_adapter->get_display_y(), pEngine->m_adapter->get_display_w(), pEngine->m_adapter->get_display_h());
#else
        glViewport(pEngine->m_adapter->get_display_x(), pEngine->m_adapter->get_display_y(), pEngine->m_adapter->get_display_w(), pEngine->m_adapter->get_display_h());
#endif
		GLint vp[4];
        glGetIntegerv(GL_VIEWPORT, vp);
        
        kmMat4 matrixPerspective, matrixLookup;
        
        kmGLMatrixMode(KM_GL_PROJECTION);
        kmGLLoadIdentity();
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		kmGLMultMatrix(CCEGLView::sharedOpenGLView()->getOrientationMatrix());
#endif
        // issue #1334
        kmMat4PerspectiveProjection( &matrixPerspective, 60, (GLfloat)lw/lh, 0.1f, zeye*2);
        
        kmGLMultMatrix(&matrixPerspective);
        
        kmGLMatrixMode(KM_GL_MODELVIEW);
        kmGLLoadIdentity();
        kmVec3 eye, center, up;
        kmVec3Fill( &eye, midx, midy, -zeye );
        kmVec3Fill( &center, midx, midy, 0.0f );
        kmVec3Fill( &up, 0.0f, -1.0f, 0.0f);
        kmMat4LookAt(&matrixLookup, &eye, &center, &up);
        kmGLMultMatrix(&matrixLookup);
        
        cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColor);
        pEngine->Draw();
#if defined DEBUG || defined _DEBUG 
		if (m_bShowingTexture)
			drawTexture(m_uTexName);
#endif
        cocos2d::CCShaderCache::sharedShaderCache()->popShader();
        assert(cocos2d::CCShaderCache::sharedShaderCache()->getSaderStackDepth() == 0);
        CCDirector::sharedDirector()->setProjection(kCCDirectorProjection3D);
#if defined(_LOCOJOY_SDK_) || defined(_YJ_SDK_)
		if (m_IsTick&&m_IsRunBrightNess&&m_BrightNessVersion)
#else
		if (m_IsTick&&m_BrightNessVersion)
#endif // _LOCOJOY_SDK_
	   {
		   int64_t tick = Nuclear::GetMilliSeconds();
		   if (!m_bDark)
		   {
			   m_DuraTime += (tick - m_LastTick);
		   }
		   m_LastTick = tick;
		   if (!m_bDark && m_DuraTime > NoTouchDuraTime)
		   {
			   this->m_fCurBrightness = IOS_MHSD_UTILS::GetCurBrightness();
			   IOS_MHSD_UTILS::SetCurBrightness(DefaultDarkBrightness);
			   CCLOG("=====================Screem Drak==================");
			   m_bDark = true;
			   m_DuraTime = 0;
			   m_LastTick = Nuclear::GetMilliSeconds();
		   }
	   }
  }
#if defined DEBUG || defined _DEBUG  
	void EngineLayer::drawTexture(GLuint texName) 
	{
		ccGLBindTexture2D(texName);
		GLfloat    coordinates[] = {   
				1, 0,
                1, 1,
                0, 0,
                0, 1,};
        GLuint width = 0x200, height = 0x200;
        GLfloat        vertices[] = {
			width,        0,
            width,        height,
            0,            0,
            0,            height,
        };
		unsigned int color[4];
		color[0] = color[1] = color[2] = color[3] = 0;
		ccGLBlendFunc(GL_ONE, GL_ZERO);
		cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionColor);
		glVertexAttribPointer(cocos2d::kCCVertexAttrib_Position, 2, GL_FLOAT, GL_FALSE, 0, vertices);
        glVertexAttribPointer(cocos2d::kCCVertexAttrib_Color, 4, GL_UNSIGNED_BYTE, GL_TRUE, 0, color);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		cocos2d::CCShaderCache::sharedShaderCache()->popShader();

		color[0] = color[1] = color[2] = color[3] = 0xFFFFFFFF;
		ccGLBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColor);
		glVertexAttribPointer(cocos2d::kCCVertexAttrib_Position, 2, GL_FLOAT, GL_FALSE, 0, vertices);
        glVertexAttribPointer(cocos2d::kCCVertexAttrib_TexCoords, 2, GL_FLOAT, GL_FALSE, 0, coordinates);
        glVertexAttribPointer(cocos2d::kCCVertexAttrib_Color, 4, GL_UNSIGNED_BYTE, GL_TRUE, 0, color);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		cocos2d::CCShaderCache::sharedShaderCache()->popShader();
	}
#endif
    EngineLayer* EngineLayer::GetEngineLayer()
    {
        assert(s_EngineLayer != NULL);        
        return s_EngineLayer;
    }
    
}//namespace Nuclear
