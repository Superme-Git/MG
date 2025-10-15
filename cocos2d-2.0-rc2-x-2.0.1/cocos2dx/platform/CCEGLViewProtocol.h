#ifndef __CCEGLVIEWPROTOCOL_H__
#define __CCEGLVIEWPROTOCOL_H__

#include "ccTypes.h"
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8

#endif

enum ResolutionPolicy
{
    // The entire application is visible in the specified area without trying to preserve the original aspect ratio.
    // Distortion can occur, and the application may appear stretched or compressed.
    kResolutionExactFit,
    // The entire application fills the specified area, without distortion but possibly with some cropping,
    // while maintaining the original aspect ratio of the application.
    kResolutionNoBorder,
    // The entire application is visible in the specified area without distortion while maintaining the original
    // aspect ratio of the application. Borders can appear on two sides of the application.
    kResolutionShowAll,
    // The application takes the height of the design resolution size and modifies the width of the internal
    // canvas so that it fits the aspect ratio of the device
    // no distortion will occur however you must make sure your application works on different
    // aspect ratios
    kResolutionFixedHeight,
    // The application takes the width of the design resolution size and modifies the height of the internal
    // canvas so that it fits the aspect ratio of the device
    // no distortion will occur however you must make sure your application works on different
    // aspect ratios
    kResolutionFixedWidth,
    
    kResolutionUnKnown,
};

NS_CC_BEGIN

#define CC_MAX_TOUCHES  20

class EGLTouchDelegate;
class CCSet;

/**
 * @addtogroup platform
 * @{
 */

class CC_DLL CCEGLViewProtocol
{
public:
    CCEGLViewProtocol();
    virtual ~CCEGLViewProtocol();

    virtual void    end() = 0;
    virtual bool    isOpenGLReady() = 0;
    virtual void    swapBuffers() = 0;
	virtual void    setIMEKeyboardState(bool bOpen) = 0;

    virtual CCRect  getViewPort();
    virtual CCSize  getSize();
    virtual void    setFrameSize(float width, float height);
	virtual CCSize  getFrameSize();
	virtual int		getShaderLevel(){ return 0; };
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	/**
     * Set the design resolution size.
     * @param width Design resolution width.
     * @param height Design resolution height.
     * @param resolutionPolicy The resolution policy desired, you may choose:
     *                         [1] kResolutionExactFit Fill screen by stretch-to-fit: if the design resolution ratio of width to height is different from the screen resolution ratio, your game view will be stretched.
     *                         [2] kResolutionNoBorder Full screen without black border: if the design resolution ratio of width to height is different from the screen resolution ratio, two areas of your game view will be cut.
     *                         [3] kResolutionShowAll  Full screen with black border: if the design resolution ratio of width to height is different from the screen resolution ratio, two black borders will be shown.
     */
    
#else
    
#endif
    virtual void setDesignResolutionSize(float width, float height, ResolutionPolicy resolutionPolicy);
    virtual void    setDesignResolutionSize(float width, float height);
    virtual void    setTouchDelegate(EGLTouchDelegate * pDelegate);
    virtual float   getScreenScaleFactor();
    virtual bool    canSetContentScaleFactor();
    virtual void    setContentScaleFactor(float contentScaleFactor);
    virtual void    setViewPortInPoints(float x , float y , float w , float h);
    virtual void    setScissorInPoints(float x , float y , float w , float h);
    virtual float   getMainScreenScale();
    virtual void    setViewName(const char* pszViewName);
    const char*     getViewName();

    /** handle touch events by default, if you want to custom your handles, please override these functions */
    virtual void    handleTouchesBegin(int num, int ids[], float xs[], float ys[]);
    virtual void    handleTouchesMove(int num, int ids[], float xs[], float ys[]);
    virtual void    handleTouchesEnd(int num, int ids[], float xs[], float ys[]);
    virtual void    handleTouchesCancel(int num, int ids[], float xs[], float ys[]);
    
    virtual void    handleLongPress(int num, float xs[], float ys[], int state);
    virtual void    handleClick(float xPos,float yPos);
    virtual void    handleDoubleClick(float xPos,float yPos);
    virtual void    handleSlide(int dir,float xStart,float yStart, float v);
    virtual void    handleDrag(int dir,float xPos,float yPos, float v_x,float v_y);
    
    /**
     * Get scale factor of the horizontal direction.
     */
    float getScaleX() const;
    
    /**
     * Get scale factor of the vertical direction.
     */
    float getScaleY() const;
    
    /** Get design resolution size.
     *  Default resolution size is the same as 'getFrameSize'.
     */
    virtual const CCSize&  getDesignResolutionSize() const;
    
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8

	/**
     * Get the visible area size of opengl viewport.
     */
    virtual CCSize getVisibleSize() const;

    /**
     * Get the visible origin point of opengl viewport.
     */
    virtual CCPoint getVisibleOrigin() const;

#endif

private:
    void getSetOfTouchesEndOrCancel(CCSet& set, int num, int ids[], float xs[], float ys[]);
protected:
    bool m_bNeedScale;
    EGLTouchDelegate* m_pDelegate;
    float  m_fScreenScaleFactor;
    CCSize m_sSizeInPixel;
    CCSize m_sSizeInPoint;
    CCRect m_rcViewPort;
    char m_szViewName[50];
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8

#endif
    // real screen size
    CCSize m_obScreenSize;
    // resolution size, it is the size appropriate for the app resources.
    CCSize m_obDesignResolutionSize;
    // the view port size
    CCRect m_obViewPortRect;
    
    ResolutionPolicy m_eResolutionPolicy;
    
    float  m_fScaleX;
    float  m_fScaleY;
};

// end of platform group
/// @}

NS_CC_END

#endif /* __CCEGLVIEWPROTOCOL_H__ */
