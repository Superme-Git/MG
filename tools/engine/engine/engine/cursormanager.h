#ifndef __Nuclear_CURSORMANAGER_H
#define __Nuclear_CURSORMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"

namespace Nuclear
{
	class Engine;

	// ����Ӳ�����
	class CursorManager
	{
	public:
		CursorManager(Engine *pEngine);
		~CursorManager();

	public:

		// ע��Ӳ����꣬��������
		// @resource��ͼƬ·���� offset�ǹ����ͼƬ�ڵ�ƫ�ƣ���С�̶�Ϊ32*32, center�ǹ������
		CursorHandle RegisterCursor(const std::wstring& resource, const CPOINT &offset, const CPOINT &center);
		// �°�Ĺ�꣬����ϵͳAPI������֧��cur ani��ʽ����Դ·��Ϊwindows���·�����߾���·������Դ���ܴ�����
		XPCursorHandle RegisterCursor(const std::wstring& resource);
		// �������
		bool ChangeCursor(CursorHandle hCursor);
		bool ChangeCursor(XPCursorHandle hCursor);
		// �����Ƿ���ʾ���
		bool ShowCursor(bool b);
		// ���ù������
		bool SetCursorPosition(const CPOINT &point);
		// �õ��ر�����
		void GetCursorPosition(CPOINT &point) const;

		// ���ù��ص�
		void OnSetCursor();

	private:
		Engine *m_pEngine;

	private:
		bool m_bUseHardCursor;
		HCURSOR m_hOldCursor;
		XPCursorHandle m_curCursor;
		std::map<CursorHandle, CPOINT> m_mapCursor;

		// ռ��
	private:
		CursorManager(const CursorManager&);
		CursorManager& operator=(const CursorManager&);
	};

};

#endif
