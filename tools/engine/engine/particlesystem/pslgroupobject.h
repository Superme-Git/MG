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
		VERSION01 = 1, //���õĵ�һ���汾..��ǰ�Ķ�û�м�¼��psl֮������λ�ù�ϵ..
		CURVER = 1,
	};	

	struct PSLGROUP
	{
		std::wstring m_strPslName;//����չ��.psl  ���˳����ǻ���˳��...
		CPOINT m_PSLpos; ////���������Ч�ڷ�λ�õ��������//ˮƽ��Ϊx��, ��ֱ��Ϊy��.
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
