#pragma once
#include "..\common\xptypes.h"
#include "..\common\pobject.h"

namespace Nuclear
{
	class PSpriteMaze :
		public PObject
	{
	public:
		const static int VERSION		 = 2;//版本
		const static int VERSION_NOLTPOS = 1;//版本
	private:
		CPOINT m_leftTopPos;		//从物件原点到障碍左上角的向量（向上向左为正）
		int	   m_nMazeWidth;		//障碍宽（以格子为单位）
		int	   m_nMazeHeight;		//障碍高（以格子为单位）
		char*  m_pMaze;				//障碍们

	public:
		virtual bool Init(int width, int height);	//（以格子为单位）
		virtual bool Init(int width, int height, const CPOINT& ltpos);	//（宽高以格子为单位，ltpos以像素为单位）
		virtual XOStream& marshal(XOStream &) const;
		virtual const XIStream& unmarshal(const XIStream &);

		const CPOINT &GetLeftTopPos() const { return m_leftTopPos; }
		int GetWidth() const { return m_nMazeWidth; }	//障碍宽（以格子为单位）
		int GetHeight() const { return m_nMazeHeight; }	//障碍高（以格子为单位）
		char* GetMaze() const { return m_pMaze; }



	public:
		PSpriteMaze(void);
		virtual ~PSpriteMaze(void);
	};
}