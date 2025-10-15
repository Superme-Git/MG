#pragma once
#include "..\common\pobject.h"
#include "..\common\xptypes.h"

namespace Nuclear
{

class pslgroupobject : public PObject
{
public:
	pslgroupobject(void);
	virtual ~pslgroupobject(void);

public:
	enum PSLGVER
	{
		VERSION01 = 1, //可用的第一个版本..以前的都没有记录下psl之间的相对位置关系..
		CURVER = 1,
	};	

	struct PSLGROUP
	{
		std::wstring m_strPslName;//带扩展名.psl  存放顺序就是绘制顺序...
		CPOINT m_PSLpos; ////相对于组特效摆放位置的相对坐标//水平右为x正, 垂直上为y正.
	};

public:
	XOStream& marshal(XOStream &) const;
	const XIStream& unmarshal(const XIStream &);

public:
	std::vector<PSLGROUP> m_vectorPSLs;												

private:
	pslgroupobject(const pslgroupobject&);
	pslgroupobject& operator=(const pslgroupobject&);
};

}
