#include "iarithmetics.h"
#include "simplearithmetic.h"

namespace TMan
{
	IArithmetics* ArithmeticsFactory::GetArithmetics(TManArithemticsType t, int poolSize, IAllocator *pAllocator)
	{
		switch (t)
		{
		case TMAT_SIMPLE:
			return new SimpleArithmetic(poolSize, pAllocator);
			break;
		}
		return 0;
	}

	void ArithmeticsFactory::DestoryArithmetics(IArithmetics * pArithmetics)
	{
		delete pArithmetics;
	}
}