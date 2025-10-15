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
		//�Ը���Ϊ��λ
		bool Init(int width, int height);

		///////////////////////////////�������///////////////////////////////
		NuclearOStream& marshal(NuclearOStream &os) const;
		const INuclearStream& unmarshal(const INuclearStream &os);

		//��һ�㣬��������
		const NuclearStepSound* GetData() const { return m_pGround; }
		NuclearStepSound* GetData() { return m_pGround; }

		//��������
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
		int m_nWidth;	//�Ը���Ϊ��λ
		int m_nHeight;	//�Ը���Ϊ��λ

	};
}

#endif