/***********************************************************************
filename: 	Utils2.h
author:		eagle
purpose:	常用辅助函数
*************************************************************************/
#ifndef __UTILS2_H__
#define __UTILS2_H__

namespace MHSD_UTILS
{
	//程序内字符串资源获取（与客户端信息提示表不同）
	const std::wstring GETSTRING(int id); //lua
	const std::wstring& GetEffectPath(int id); //lua
	const std::wstring& GetTipsMsg(int id);
}

#endif//__UTILS_H__
