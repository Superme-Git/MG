#pragma once
#include "..\common\pobject.h"

namespace Nuclear
{
	typedef unsigned char StepSoundGT;
	class PStepSoundMap :
		public PObject
	{
	public:
		static const int GRID_WIDTH = 64;
		static const int GRID_HEIGHT = 64;
		static const int VERSION = 0;
	private:
		StepSoundGT *m_pGround;
		int m_nWidth;	//�Ը���Ϊ��λ
		int m_nHeight;	//�Ը���Ϊ��λ

	public:
		PStepSoundMap(void);
		virtual ~PStepSoundMap(void);

	private:
		virtual bool Init();
		void FreeMaps();
		bool AllocMaps();

	public:
		//�Ը���Ϊ��λ
		bool Init(int width, int height);

		///////////////////////////////�������
		XOStream& marshal(XOStream &os) const;
		const XIStream& unmarshal(const XIStream &os);

		//��һ�㣬��������
		const StepSoundGT* GetData() const { return m_pGround; }
		StepSoundGT* GetData() { return m_pGround; }

		//��������
		int GetData(Location loc) const;

		int GetWidth() const { return m_nWidth; }
		int GetHeight() const { return m_nHeight; }

		PStepSoundMap* Clone() const;

		//ռ��
	private:
		PStepSoundMap& operator= (const PStepSoundMap &x);
		PStepSoundMap(const PStepSoundMap &x);

	};

}
