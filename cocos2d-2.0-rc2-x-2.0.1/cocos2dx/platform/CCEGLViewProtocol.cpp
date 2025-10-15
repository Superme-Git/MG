#include "CCEGLViewProtocol.h"
#include "touch_dispatcher/CCTouchDispatcher.h"
#include "touch_dispatcher/CCTouch.h"
#include "CCDirector.h"
#include "cocoa/CCSet.h"
#include "cocoa/CCDictionary.h"
#include "cocoa/CCInteger.h"

NS_CC_BEGIN

static CCTouch* s_pTouches[CC_MAX_TOUCHES] = { NULL };
static unsigned int s_indexBitsUsed = 0;
static CCDictionary s_TouchesIntergerDict;

static int getUnUsedIndex()
{
    int i;
    int temp = s_indexBitsUsed;

    for (i = 0; i < CC_MAX_TOUCHES; i++) {
        if (! (temp & 0x00000001)) {
            s_indexBitsUsed |= (1 <<  i);
            return i;
        }

        temp >>= 1;
	}

    // all bits are used
    return -1;
}

static void removeUsedIndexBit(int index)
{
    if (index < 0 || index >= CC_MAX_TOUCHES) 
    {
        return;
    }

    unsigned int temp = 1 << index;
    temp = ~temp;
    s_indexBitsUsed &= temp;
}

CCEGLViewProtocol::CCEGLViewProtocol()
  : m_pDelegate(NULL)
  , m_fScaleX(1.0f)
  , m_fScaleY(1.0f)
  , m_bNeedScale(false)
  , m_fScreenScaleFactor(1.0f)
{
    strncpy(m_szViewName, "Cocos2d-x Game", sizeof(m_szViewName));
}

CCEGLViewProtocol::~CCEGLViewProtocol()
{

}

void CCEGLViewProtocol::setFrameSize(float width, float height)
{
    m_sSizeInPixel.setSize(width, height);
    m_rcViewPort.size.setSize(width, height);
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	m_obDesignResolutionSize = m_obScreenSize = CCSizeMake(width, height);
#endif
}

#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
CCSize  CCEGLViewProtocol::getVisibleSize() const
{
    if (m_eResolutionPolicy == kResolutionNoBorder)
    {
        return CCSizeMake(m_obScreenSize.width/m_fScaleX, m_obScreenSize.height/m_fScaleY);
    }
    else 
    {
        return m_obDesignResolutionSize;
    }
}

CCPoint CCEGLViewProtocol::getVisibleOrigin() const
{
    if (m_eResolutionPolicy == kResolutionNoBorder)
    {
        return CCPointMake((m_obDesignResolutionSize.width - m_obScreenSize.width/m_fScaleX)/2, 
                           (m_obDesignResolutionSize.height - m_obScreenSize.height/m_fScaleY)/2);
    }
    else 
    {
        return CCPointZero;
    }
}
#endif

const CCSize& CCEGLViewProtocol::getDesignResolutionSize() const
{
    return m_obDesignResolutionSize;
}

CCSize CCEGLViewProtocol::getFrameSize()
{
    return m_sSizeInPixel;
}

float CCEGLViewProtocol::getScaleX() const
{
    return m_fScaleX;
}

float CCEGLViewProtocol::getScaleY() const
{
    return m_fScaleY;
}

void CCEGLViewProtocol::setDesignResolutionSize(float width, float height, ResolutionPolicy resolutionPolicy)
{
    CCAssert(resolutionPolicy != kResolutionUnKnown, "should set resolutionPolicy");
    
    if (width == 0.0f || height == 0.0f)
    {
        return;
    }
    
    m_obDesignResolutionSize.setSize(width, height);
    
    m_fScaleX = (float)m_obScreenSize.width / m_obDesignResolutionSize.width;
    m_fScaleY = (float)m_obScreenSize.height / m_obDesignResolutionSize.height;
    
    if (resolutionPolicy == kResolutionNoBorder)
    {
        m_fScaleX = m_fScaleY = MAX(m_fScaleX, m_fScaleY);
    }
    
    if (resolutionPolicy == kResolutionShowAll)
    {
        m_fScaleX = m_fScaleY = MIN(m_fScaleX, m_fScaleY);
    }
    
    if ( resolutionPolicy == kResolutionFixedHeight) {
        m_fScaleX = m_fScaleY;
        m_obDesignResolutionSize.width = ceilf(m_obScreenSize.width/m_fScaleX);
    }
    
    if ( resolutionPolicy == kResolutionFixedWidth) {
        m_fScaleY = m_fScaleX;
        m_obDesignResolutionSize.height = ceilf(m_obScreenSize.height/m_fScaleY);
    }
    
    // calculate the rect of viewport
    float viewPortW = m_obDesignResolutionSize.width * m_fScaleX;
    float viewPortH = m_obDesignResolutionSize.height * m_fScaleY;
    
    m_obViewPortRect.setRect((m_obScreenSize.width - viewPortW) / 2, (m_obScreenSize.height - viewPortH) / 2, viewPortW, viewPortH);
    
    m_eResolutionPolicy = resolutionPolicy;
    
    // reset director's member variables to fit visible rect
    CCDirector::sharedDirector()->m_obWinSizeInPoints = getDesignResolutionSize();
    CCDirector::sharedDirector()->createStatsLabel();
    CCDirector::sharedDirector()->setGLDefaultValues();
}

#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8


#else

void CCEGLViewProtocol::setDesignResolutionSize(float width, float height)
{
    if (width == 0.0f || height == 0.0f)
    {
        return;
    }

    m_sSizeInPoint.setSize(width, height);

    // calculate the factor and the rect of viewport    
    m_fScreenScaleFactor =  MIN((float)m_sSizeInPixel.width / m_sSizeInPoint.width, 
        (float)m_sSizeInPixel.height / m_sSizeInPoint.height);
    float viewPortW = m_sSizeInPoint.width * m_fScreenScaleFactor;
    float viewPortH = m_sSizeInPoint.height * m_fScreenScaleFactor;

    m_rcViewPort.setRect((m_sSizeInPixel.width - viewPortW) / 2, (m_sSizeInPixel.height - viewPortH) / 2, viewPortW, viewPortH);

    CCLOG("m_fScreenScaleFactor = %f", m_fScreenScaleFactor);
    m_bNeedScale = true;  
}
#endif
CCSize CCEGLViewProtocol::getSize()
{
    CCSize size;
    if (m_bNeedScale)
    {
        // retina and scale mode can't be opened at the same time
        CCAssert(CC_CONTENT_SCALE_FACTOR() == 1.0f, "retina and scale mode can't be opened at the same time!");
        size.setSize(m_sSizeInPoint.width, m_sSizeInPoint.height);      
    }
    else
    {
        size.setSize(m_sSizeInPixel.width, m_sSizeInPixel.height);
    }
    return size;
}

CCRect CCEGLViewProtocol::getViewPort()
{
    return m_rcViewPort;
}

void CCEGLViewProtocol::setTouchDelegate(EGLTouchDelegate * pDelegate)
{
    m_pDelegate = pDelegate;
}

float CCEGLViewProtocol::getScreenScaleFactor()
{
    return m_fScreenScaleFactor;
}

bool CCEGLViewProtocol::canSetContentScaleFactor()
{
    return false;
}

void CCEGLViewProtocol::setContentScaleFactor(float contentScaleFactor)
{
    m_fScreenScaleFactor = contentScaleFactor;
}

void CCEGLViewProtocol::setViewPortInPoints(float x , float y , float w , float h)
{
    if (m_bNeedScale)
    {
        CCAssert(CC_CONTENT_SCALE_FACTOR() == 1.0f, "retina and scale mode can't be opened at the same time!");
        float factor = m_fScreenScaleFactor / CC_CONTENT_SCALE_FACTOR();
        glViewport((GLint)(x * factor + m_rcViewPort.origin.x),
            (GLint)(y * factor + m_rcViewPort.origin.y),
            (GLsizei)(w * factor),
            (GLsizei)(h * factor));
    }
    else
    {
        glViewport(
            (GLint)(x*CC_CONTENT_SCALE_FACTOR()),
            (GLint)(y*CC_CONTENT_SCALE_FACTOR()),
            (GLsizei)(w*CC_CONTENT_SCALE_FACTOR()),
            (GLsizei)(h*CC_CONTENT_SCALE_FACTOR()));
    }
}

void CCEGLViewProtocol::setScissorInPoints(float x , float y , float w , float h)
{
    if (m_bNeedScale)
    {
        CCAssert(CC_CONTENT_SCALE_FACTOR() == 1.0f, "retina and scale mode can't be opened at the same time!");
        float factor = m_fScreenScaleFactor / CC_CONTENT_SCALE_FACTOR();
        glScissor((GLint)(x * factor + m_rcViewPort.origin.x),
            (GLint)(y * factor + m_rcViewPort.origin.y),
            (GLsizei)(w * factor),
            (GLsizei)(h * factor));
    }
    else
    {
        glScissor(
            (GLint)(x * CC_CONTENT_SCALE_FACTOR()), 
            (GLint)(y * CC_CONTENT_SCALE_FACTOR()),
            (GLsizei)(w * CC_CONTENT_SCALE_FACTOR()),
            (GLsizei)(h * CC_CONTENT_SCALE_FACTOR()));
    }
}

float CCEGLViewProtocol::getMainScreenScale()
{
    return -1.0f;
}

void CCEGLViewProtocol::setViewName(const char* pszViewName)
{
    if (pszViewName != NULL && strlen(pszViewName) > 0)
    {
        strncpy(m_szViewName, pszViewName, sizeof(m_szViewName));
    }
}

const char* CCEGLViewProtocol::getViewName()
{
    return m_szViewName;
}

void CCEGLViewProtocol::handleTouchesBegin(int num, int ids[], float xs[], float ys[])
{
    CCSet set;
    for (int i = 0; i < num; ++i)
    {
        int id = ids[i];
        float x = xs[i];
        float y = ys[i];

        CCInteger* pIndex = (CCInteger*)s_TouchesIntergerDict.objectForKey(id);
        int nUnusedIndex = 0;

        // it is a new touch
        if (pIndex == NULL)
        {
            nUnusedIndex = getUnUsedIndex();

            // The touches is more than MAX_TOUCHES ?
            if (nUnusedIndex == -1) {
                CCLOG("The touches is more than MAX_TOUCHES, nUnusedIndex = %d", nUnusedIndex);
                return;
            }

            CCTouch* pTouch = s_pTouches[nUnusedIndex] = new CCTouch();
            if (m_bNeedScale)
            {
                pTouch->setTouchInfo(nUnusedIndex, (x - m_rcViewPort.origin.x) / m_fScreenScaleFactor, 
                    (y - m_rcViewPort.origin.y) / m_fScreenScaleFactor);
            }
            else
            {
                pTouch->setTouchInfo(nUnusedIndex, x, y);
            }
            CCInteger* pInterObj = new CCInteger(nUnusedIndex);
            s_TouchesIntergerDict.setObject(pInterObj, id);
            set.addObject(pTouch);
            pInterObj->release();
        }
    }

    if (set.count() == 0)
    {
        CCLOG("touchesBegan: count = 0");
        return;
    }

    m_pDelegate->touchesBegan(&set, NULL);
}

#define MOVE_MIN_DIST 5

void CCEGLViewProtocol::handleTouchesMove(int num, int ids[], float xs[], float ys[])
{
    CCSet set;
    for (int i = 0; i < num; ++i)
    {
        int id = ids[i];
        float x = xs[i];
        float y = ys[i];

        CCInteger* pIndex = (CCInteger*)s_TouchesIntergerDict.objectForKey(id);
        if (pIndex == NULL) {
            CCLOG("if the index doesn't exist, it is an error");
            return;
        }

        CCLOGINFO("Moving touches with id: %d, x=%f, y=%f", id, x, y);
        CCTouch* pTouch = s_pTouches[pIndex->getValue()];
        if (pTouch)
        {
            CCPoint touchPt = pTouch->locationInView();
            if (m_bNeedScale)
            {
                x = (x - m_rcViewPort.origin.x) / m_fScreenScaleFactor;
                y = (y - m_rcViewPort.origin.y) / m_fScreenScaleFactor;
            }
            
			if (fabs(touchPt.x - x) > MOVE_MIN_DIST || fabs(touchPt.y - y) > MOVE_MIN_DIST)
            {
                pTouch->setTouchInfo(pIndex->getValue(), x, y);
                set.addObject(pTouch);
            }
        }
        else
        {
            // It is error, should return.
            CCLOG("Moving touches with id: %d error", id);
            return;
        }
    }

    if (set.count() == 0)
    {
        //CCLOG("touchesMoved: count = 0");
        return;
    }

    m_pDelegate->touchesMoved(&set, NULL);
}

void CCEGLViewProtocol::getSetOfTouchesEndOrCancel(CCSet& set, int num, int ids[], float xs[], float ys[])
{
    for (int i = 0; i < num; ++i)
    {
        int id = ids[i];
        float x = xs[i];
        float y = ys[i];

        CCInteger* pIndex = (CCInteger*)s_TouchesIntergerDict.objectForKey(id);
        if (pIndex == NULL)
        {
            CCLOG("if the index doesn't exist, it is an error");
            return;
        }
        /* Add to the set to send to the director */
        CCTouch* pTouch = s_pTouches[pIndex->getValue()];        
        if (pTouch)
        {
            CCLOGINFO("Ending touches with id: %d, x=%f, y=%f", id, x, y);

            if (m_bNeedScale)
            {
                pTouch->setTouchInfo(pIndex->getValue(), (x - m_rcViewPort.origin.x) / m_fScreenScaleFactor,
                    (y - m_rcViewPort.origin.y) / m_fScreenScaleFactor);
            }
            else
            {
                pTouch->setTouchInfo(pIndex->getValue(), x, y);
            }
            
            set.addObject(pTouch);

            // release the object
            pTouch->release();
            s_pTouches[pIndex->getValue()] = NULL;
            removeUsedIndexBit(pIndex->getValue());

            s_TouchesIntergerDict.removeObjectForKey(id);

        } 
        else
        {
            CCLOG("Ending touches with id: %d error", id);
            return;
        } 

    }

    if (set.count() == 0)
    {
        CCLOG("touchesEnded or touchesCancel: count = 0");
        return;
    }
}

void CCEGLViewProtocol::handleTouchesEnd(int num, int ids[], float xs[], float ys[])
{
    CCSet set;
    getSetOfTouchesEndOrCancel(set, num, ids, xs, ys);
    m_pDelegate->touchesEnded(&set, NULL);
}

void CCEGLViewProtocol::handleTouchesCancel(int num, int ids[], float xs[], float ys[])
{
    CCSet set;
    getSetOfTouchesEndOrCancel(set, num, ids, xs, ys);
    m_pDelegate->touchesCancelled(&set, NULL);
}

void CCEGLViewProtocol::handleLongPress(int num, float xs[], float ys[], int state) {
    m_pDelegate->longPress(num, xs, ys, state);
}

void CCEGLViewProtocol::handleClick(float xPos, float yPos)
{
    m_pDelegate->handleClick(xPos, yPos);
    
}


void CCEGLViewProtocol::handleDoubleClick(float xPos,float yPos)
{
    m_pDelegate->handleDoubleClick(xPos, yPos);
    
}
void CCEGLViewProtocol::handleSlide(int dir,float xStart,float yStart, float v)
{
    m_pDelegate->handleSlide(dir, xStart, yStart, v);
    
}
void CCEGLViewProtocol::handleDrag(int state,float xPos,float yPos, float v_x,float v_y)
{
    m_pDelegate->handleDrag(state, xPos, yPos, v_x,v_y);
    
}


NS_CC_END
