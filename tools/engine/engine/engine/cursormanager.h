#ifndef __Nuclear_CURSORMANAGER_H
#define __Nuclear_CURSORMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"

namespace Nuclear
{
	class Engine;

	// 管理硬件光标
	class CursorManager
	{
	public:
		CursorManager(Engine *pEngine);
		~CursorManager();

	public:

		// 注册硬件光标，返回其句柄
		// @resource是图片路径， offset是光标在图片内的偏移，大小固定为32*32, center是光标中心
		CursorHandle RegisterCursor(const std::wstring& resource, const CPOINT &offset, const CPOINT &center);
		// 新版的光标，调用系统API，仅仅支持cur ani格式，资源路径为windows相对路径或者绝对路径，资源不能打进包里。
		XPCursorHandle RegisterCursor(const std::wstring& resource);
		// 更换光标
		bool ChangeCursor(CursorHandle hCursor);
		bool ChangeCursor(XPCursorHandle hCursor);
		// 设置是否显示光标
		bool ShowCursor(bool b);
		// 设置光标坐标
		bool SetCursorPosition(const CPOINT &point);
		// 得到关标坐标
		void GetCursorPosition(CPOINT &point) const;

		// 设置光标回调
		void OnSetCursor();

	private:
		Engine *m_pEngine;

	private:
		bool m_bUseHardCursor;
		HCURSOR m_hOldCursor;
		XPCursorHandle m_curCursor;
		std::map<CursorHandle, CPOINT> m_mapCursor;

		// 占坑
	private:
		CursorManager(const CursorManager&);
		CursorManager& operator=(const CursorManager&);
	};

};

#endif
