//////////////////////////////////////////////////////////////////////////
// File:		CEGUIBinLayoutFileDefine.h
// Author:	���
// Date:		2016/02/29
//////////////////////////////////////////////////////////////////////////

#ifndef _CEGUIBinLayoutFileDefine_h_
#define _CEGUIBinLayoutFileDefine_h_

namespace CEGUI
{
	namespace BinLayout
	{
		static const char LAYOUT_BIN_FILE_MAGIC[4] = { 'L', 'B', 'F', 'M' };
		static const int LAYOUT_BIN_FILE_VERSION = 1;

		// �������ļ��У��ڵ�����͡�
		// Ϊ�������¼��ݣ��պ�ά��ʱֻ��׷�ӣ������޸ĺ�ɾ��
		enum NodeType
		{
			NT_Window,
			NT_AutoWindow,
			NT_LayoutImport,
			NT_Event,
			NT_Property,
		};
	}
}

#endif