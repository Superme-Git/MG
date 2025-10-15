//////////////////////////////////////////////////////////////////////////
// File:		CEGUIBinLayoutFileDefine.h
// Author:	杨春雷
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

		// 二进制文件中，节点的类型。
		// 为保持向下兼容，日后维护时只能追加，不能修改和删除
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