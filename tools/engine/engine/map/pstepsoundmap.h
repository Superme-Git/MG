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
		int m_nWidth;	//以格子为单位
		int m_nHeight;	//以格子为单位

	public:
		PStepSoundMap(void);
		virtual ~PStepSoundMap(void);

	private:
		virtual bool Init();
		void FreeMaps();
		bool AllocMaps();

	public:
		//以格子为单位
		bool Init(int width, int height);

		///////////////////////////////输入输出
		XOStream& marshal(XOStream &os) const;
		const XIStream& unmarshal(const XIStream &os);

		//马虎一点，就这样吧
		const StepSoundGT* GetData() const { return m_pGround; }
		StepSoundGT* GetData() { return m_pGround; }

		//像素坐标
		int GetData(Location loc) const;

		int GetWidth() const { return m_nWidth; }
		int GetHeight() const { return m_nHeight; }

		PStepSoundMap* Clone() const;

		//占坑
	private:
		PStepSoundMap& operator= (const PStepSoundMap &x);
		PStepSoundMap(const PStepSoundMap &x);

	};

}
