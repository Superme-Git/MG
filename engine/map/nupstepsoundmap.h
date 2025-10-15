#ifndef __Nuclear_pstepsoundmap_H__
#define __Nuclear_pstepsoundmap_H__
#include "../common/nupobject.h"

namespace Nuclear
{
	typedef unsigned char NuclearStepSound;
	class NuclearStepSoundMap : public NuclearObject
	{
	public:
		static const int GRID_WIDTH = 64;
		static const int GRID_HEIGHT = 64;
		static const int VERSION = 0;
	public:
		NuclearStepSoundMap(void);
		virtual ~NuclearStepSoundMap(void);

	public:
		//以格子为单位
		bool Init(int width, int height);

		///////////////////////////////输入输出///////////////////////////////
		NuclearOStream& marshal(NuclearOStream &os) const;
		const INuclearStream& unmarshal(const INuclearStream &os);

		//马虎一点，就这样吧
		const NuclearStepSound* GetData() const { return m_pGround; }
		NuclearStepSound* GetData() { return m_pGround; }

		//像素坐标
		int GetData(NuclearLocation loc) const;

		int GetWidth() const { return m_nWidth; }
		int GetHeight() const { return m_nHeight; }

		NuclearStepSoundMap* Clone() const;

	private:
		NuclearStepSoundMap& operator= (const NuclearStepSoundMap &x);
		NuclearStepSoundMap(const NuclearStepSoundMap &x);

		virtual bool initial();
		void freeMaps();
		bool allocMaps();

	private:
		NuclearStepSound *m_pGround;
		int m_nWidth;	//以格子为单位
		int m_nHeight;	//以格子为单位

	};
}

#endif