//  cocos2d_wraper.h
//  engine

#ifndef __Nuclear_cocos2d_wraper_H__
#define __Nuclear_cocos2d_wraper_H__

#include <cocos2d.h>
#include "nuengine.h"
namespace cocos2d
{
    class CCGLProgram;
}
#define MAXPOINT 5
namespace Nuclear
{
    class EngineLayer;
    IEngine* GetEnginePtr();
    extern void RunCocosScene();

	bool IsInIpad();

    class EngineApp : public cocos2d::CCApplication
    {
    private:
        IEngine* m_PEngine;
    public:
        EngineApp(IEngine* aPEngine);
        ~EngineApp();
        
        IEngine* GetEnginePtr(){
            return m_PEngine;}
        
        virtual bool applicationDidFinishLaunching();
        virtual void applicationDidEnterBackground();
        virtual void applicationWillEnterForeground();
        virtual void setAnimationInterval(double interval);
    };
    
    class EngineTicker : public cocos2d::CCAction
    {
        EngineLayer* m_pEngineLayer;
    public:
        EngineTicker(EngineLayer* aPEngine);
        
        virtual bool isDone(void);
        virtual void step(float dt);
    };
    
    class EngineLayer : public cocos2d::CCLayer
    {
    private:
        EngineTicker* m_pTicker;
        cocos2d::CCPoint m_Pos[MAXPOINT];
    public:
        EngineLayer();
        virtual ~EngineLayer();

		virtual void registerWithTouchDispatcher(void);
		virtual void ccTouchesBegan(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent);
		virtual void ccTouchesMoved(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent);
		virtual void ccTouchesEnded(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent);
		virtual void ccTouchesCancelled(cocos2d::CCSet *pTouches, cocos2d::CCEvent *pEvent);
        
        virtual void ccLongPress(int num, float xs[], float ys[], int state);
        virtual void handleClick(float xPos,float yPos);
        virtual void handleDoubleClick(float xPos,float yPos);
        virtual void handleSlide(int dir,float xStart,float yStart, float v);
        virtual void handleDrag(int dir,float xPos,float yPos, float v_x,float v_y);
        virtual bool init();
        virtual void onEnter();
        virtual void onExit();
        virtual void draw(void);

		static EngineLayer* GetEngineLayer();
         
#if defined DEBUG || defined _DEBUG        
		void drawTexture(GLuint texName);
		void setShowTextureName(GLuint texName) { m_uTexName = texName; }
		void showTexture(bool show) { m_bShowingTexture = show; }
		bool isShowTexture() { return m_bShowingTexture; }
        
	private:
		bool	m_bShowingTexture;
		GLuint	m_uTexName;
        
	public:
#endif
		int64_t m_LastTick;
		unsigned int m_DuraTime;
		float m_fCurBrightness;
		bool  m_bDark;
		bool m_IsTick;
		bool m_IsRunBrightNess;
		bool m_BrightNessVersion;
        
        LAYER_CREATE_FUNC(EngineLayer);
    };
    
    
}//namespace Nuclear

#endif
