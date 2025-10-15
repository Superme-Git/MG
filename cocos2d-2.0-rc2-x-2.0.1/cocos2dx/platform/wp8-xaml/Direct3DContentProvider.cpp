/****************************************************************************
Copyright (c) 2013 cocos2d-x.org
Copyright (c) Microsoft Open Technologies, Inc.

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
#include "Direct3DContentProvider.h"
#include "ICSharpCallback.h"
using namespace PhoneDirect3DXamlAppComponent;

Direct3DContentProvider::Direct3DContentProvider(Direct3DInterop^ controller) :
    m_controller(controller)
	, m_FrameCount(0)
	, m_lastTick(0)
	, m_lastFrameTick(0)
{
}

// IDrawingSurfaceContentProviderNative interface
HRESULT Direct3DContentProvider::Connect(_In_ IDrawingSurfaceRuntimeHostNative* host)
{
    m_host = host;
    Initialize();
    m_controller->Connect();

    return S_OK;
}

void Direct3DContentProvider::Disconnect()
{
    m_controller->Disconnect();
    m_host = nullptr;
    m_synchronizedTexture = nullptr;
    m_d3dDevice = nullptr;
    m_d3dContext = nullptr;
    m_renderTarget = nullptr;
    m_renderTargetView = nullptr;
}

HRESULT Direct3DContentProvider::PrepareResources(_In_ const LARGE_INTEGER* presentTargetTime, _Out_ BOOL* contentDirty)
{
    m_controller->PrepareResources(*presentTargetTime);
    *contentDirty = true;

    return S_OK;
}

struct WaitTimeData {
	CONDITION_VARIABLE conditionVariable;
	CRITICAL_SECTION criticalSection;
	WaitTimeData()
	{
		InitializeConditionVariable(&conditionVariable);
		memset(&criticalSection, 0, sizeof(CRITICAL_SECTION));
		criticalSection.DebugInfo = (PRTL_CRITICAL_SECTION_DEBUG)-1;
		criticalSection.LockCount = -1;
	}
};

int WPSleep(unsigned int ms) {
	static WaitTimeData s_wtd;
	SleepConditionVariableCS(&s_wtd.conditionVariable,
		&s_wtd.criticalSection,
		ms);
	if (GetLastError() == ERROR_TIMEOUT)
	{
		SetLastError(0);
	}
	return 0;
}

HRESULT Direct3DContentProvider::GetTexture(_In_ const DrawingSurfaceSizeF* size, _Out_ IDrawingSurfaceSynchronizedTextureNative** synchronizedTexture, _Out_ DrawingSurfaceRectF* textureSubRectangle)
{
	if (m_lastTick != 0) {
		ULONGLONG curTick = GetTickCount64();
		if (curTick > m_lastTick + 1000) {
			FpsHelper::fps = m_FrameCount;
			m_FrameCount = 0;
			m_lastTick = GetTickCount64();
		} else {
			m_FrameCount++;
		}
	} else {
		m_lastTick = GetTickCount64();
	}
	
    if (!size || !synchronizedTexture || !textureSubRectangle) return E_INVALIDARG;

    HRESULT hr = S_OK;

    if (!m_synchronizedTexture ||
        m_renderTargetSize.Width != m_controller->RenderResolution.Width ||
        m_renderTargetSize.Height != m_controller->RenderResolution.Height)
    {
        UpdateForWindowSizeChange(m_controller->RenderResolution.Width, m_controller->RenderResolution.Height);
        hr = InitializeTexture();
    }

    // Draw to the texture.
    if (SUCCEEDED(hr))
    {
        hr = m_synchronizedTexture->BeginDraw();
        
        if (SUCCEEDED(hr))
        {
            m_controller->Draw(m_d3dDevice.Get(), m_d3dContext.Get(), m_renderTargetView.Get());
            hr = m_synchronizedTexture->EndDraw();
        }
    }

    // Set output parameters.
    if (SUCCEEDED(hr))
    {    
        textureSubRectangle->left = 0.0f;
        textureSubRectangle->top = 0.0f;
		textureSubRectangle->right = m_controller->RenderResolution.Height;
		textureSubRectangle->bottom = m_controller->RenderResolution.Width;

        hr = m_synchronizedTexture.CopyTo(synchronizedTexture);

        if (SUCCEEDED(hr))
        {
            hr = m_host->RequestAdditionalFrame();
        }
    }
	if (m_lastFrameTick != 0) {

		int sleeptime = 1000/20 - (GetTickCount64() - m_lastFrameTick);
	/*	if (sleeptime > 0)
			WPSleep(sleeptime);*/
	}
	m_lastFrameTick = GetTickCount64();
    return hr;
}

HRESULT Direct3DContentProvider::InitializeTexture()
{
    return m_host->CreateSynchronizedTexture(m_renderTarget.Get(), &m_synchronizedTexture);
}
