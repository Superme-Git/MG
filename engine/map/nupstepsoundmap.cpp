#include "../common/nulog.h"
#include "nupstepsoundmap.h"


namespace Nuclear
{
	NuclearStepSoundMap::NuclearStepSoundMap(void) : m_pGround(NULL), m_nWidth(0), m_nHeight(0)
	{
	}

	NuclearStepSoundMap::~NuclearStepSoundMap(void)
	{
		freeMaps();
	}

	NuclearOStream& NuclearStepSoundMap::marshal(NuclearOStream &os) const
	{
		const char fileHead[4] = {'S','S','M','P'};
		os.push_byte(fileHead, sizeof(char) * 4);
		os << VERSION;
		os << m_nWidth;
		os << m_nHeight;
		os.push_byte(m_pGround, m_nWidth * m_nHeight);
		return os;
	}

	const INuclearStream& NuclearStepSoundMap::unmarshal(const INuclearStream &os)
	{
		char fileHead[4];
		os.pop_byte(fileHead, sizeof(char) * 4);
		if (fileHead[0] != 'S' || fileHead[1] != 'S' || fileHead[2] != 'M' || fileHead[3] != 'P' )
		{
			XPLOG_ERROR(L"ssmp fileHead Error\n");
			throw Exception();
		}
		int version;
		os >> version;
		os >> m_nWidth;
		os >> m_nHeight;
		if (!initial())
		{
			XPLOG_ERROR(L"ssmp init Error\n");
			throw Exception();
		}
		os.pop_byte(m_pGround, m_nWidth * m_nHeight);
		return os;
	}

	bool NuclearStepSoundMap::initial()
	{
		freeMaps();
		return allocMaps();
	}

	bool NuclearStepSoundMap::Init(int width, int height)
	{
		if (width == 0 || height == 0)
			return false;
		m_nWidth = width;
		m_nHeight = height;
		return initial();
	}

	void NuclearStepSoundMap::freeMaps()
	{
		if (m_pGround != NULL)
			XFree(m_pGround);
		m_pGround = NULL;
	}

	bool NuclearStepSoundMap::allocMaps()
	{
		XPASSERT(m_pGround == NULL && L"m_pGround == NULL");
		int size = m_nWidth * m_nHeight;
		if (size == 0)
			return false;
		m_pGround = static_cast<NuclearStepSound*>(XAlloc(size));
		if (m_pGround == NULL)
			return false;
		memset(m_pGround, 0, size);
		return true;
	}

	int NuclearStepSoundMap::GetData(NuclearLocation loc) const
	{
		loc.x /= GRID_WIDTH;
		loc.y /= GRID_HEIGHT;
		if (loc.x >= m_nWidth || loc.x < 0 || loc.y >= m_nHeight || loc.y < 0 || (!m_pGround))
			return 0;
		return m_pGround[loc.y * m_nWidth + loc.x];
	}

	NuclearStepSoundMap* NuclearStepSoundMap::Clone() const
	{
		NuclearStepSoundMap* pResult = new NuclearStepSoundMap();
		pResult->Init(m_nWidth, m_nHeight);
		int size = m_nWidth * m_nHeight;
        if (pResult->m_pGround)
        {
            memcpy(pResult->m_pGround, m_pGround, size);
        }

		return pResult;
	}
}
