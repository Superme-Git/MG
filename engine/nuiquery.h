#ifndef __Nuclear_IQUERY_H__
#define __Nuclear_IQUERY_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <string>
#include <vector>

namespace Nuclear
{

	// 这个类用来暴露一些引擎的内部信息
	class IQuery
	{
	public:
		struct ActionInfo
		{
			int nTime;	 // 正常播放需要的时间，单位毫秒
			int nFrame;  // 帧数
			int nStride; // 步幅，单位象素(仅对于移动动作，其他动作为0)
		};

		struct SpriteLayerInfo
		{
			std::wstring name; // 同时作为 directory name
			std::wstring des;  // 双汉字的描述
		};

	public:
		IQuery() {}
		virtual ~IQuery() { }

	private:
		IQuery(const IQuery&);
		IQuery& operator=(const IQuery&);

	public:
		// 返回动作相关信息
		virtual bool GetActionInfo(const std::wstring &modelname, const std::wstring &actname, IQuery::ActionInfo &info) const = 0;

		// 返回vector的size就是应用的层数，此数据在程序运行期间不会变化，所以只应调用一次
		virtual std::vector<SpriteLayerInfo> GetSpriteLayerInfo(const std::wstring &modelname) const = 0;

	};

}


#endif