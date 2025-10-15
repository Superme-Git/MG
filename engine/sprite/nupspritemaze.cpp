#include "../common/nulog.h"
#include "nupspritemaze.h"

namespace Nuclear
{
	PSpriteMaze::PSpriteMaze(void) : m_leftTopPos(0,0), m_nMazeWidth(0), m_nMazeHeight(0), m_pMaze(NULL)
	{
	}

	PSpriteMaze::~PSpriteMaze(void)
	{
		if (m_pMaze)
			XFree(m_pMaze);
	}

	bool PSpriteMaze::Init(int width, int height, const NuclearPoint& ltpos)
	{
		if (width == 0 || height == 0)
			return false;
		if (m_pMaze)
			XFree(m_pMaze);
		m_pMaze = static_cast<char*>(XAlloc(width * height));
		XPASSERT(m_pMaze != NULL);
		memset(m_pMaze, 0, width * height);
		m_nMazeWidth = width;
		m_nMazeHeight = height;
		m_leftTopPos = ltpos;
		return true;
	}

	bool PSpriteMaze::Init(int width, int height)
	{
		if (width == 0 || height == 0)
			return false;
		if (m_pMaze)
			XFree(m_pMaze);
		m_pMaze = static_cast<char*>(XAlloc(width * height));
		XPASSERT(m_pMaze != NULL);
		memset(m_pMaze, 0, width * height);
		m_nMazeWidth = width;
		m_nMazeHeight = height;
		//先默认原点在中心吧
		m_leftTopPos.x = static_cast<int>(width / 2) * 24 + 12;//格子的宽24
		m_leftTopPos.y = static_cast<int>(height / 2) * 16 + 8;//格子的高16
		return true;
	}
	
	NuclearOStream& PSpriteMaze::marshal(NuclearOStream &os) const
	{
		XPASSERT(m_pMaze != NULL);
		const char fileHead[4] = {'S','M','A','Z'};
		os.push_byte(fileHead, sizeof(char) * 4);
		os << VERSION;
		os << m_nMazeWidth;
		os << m_nMazeHeight;
		os << m_leftTopPos;
		os.push_byte(m_pMaze, m_nMazeWidth * m_nMazeHeight);
		return os;
	}
	
	const INuclearStream& PSpriteMaze::unmarshal(const INuclearStream &os)
	{
		int width, height, version;
		NuclearPoint ltpos;
		char fileHead[4];
		os.pop_byte(fileHead, sizeof(char) * 4);

		if (fileHead[0] != 'S' || fileHead[1] != 'M' || fileHead[2] != 'A' || fileHead[3] != 'Z' )
			throw Exception();
		os >> version;
		os >> width;
		os >> height;
		bool r = false;
		if (version > VERSION_NOLTPOS)
		{
			os >> ltpos;
			r = Init(width, height, ltpos);
		} else {
			r = Init(width, height);
		}
		XPASSERT(r);
		os.pop_byte(m_pMaze, sizeof(char) * width * height);
		return os;
	}
}