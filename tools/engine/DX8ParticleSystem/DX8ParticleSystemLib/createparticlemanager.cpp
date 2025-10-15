#include "..\..\engine\common\xptypes.h"
#include "..\..\engine\particlesystem\iparticlemanager.h"
#include "..\..\engine\common\ifileio.h"
#include "..\..\engine\particlesystem\particlemanager.h"
#include "createparticlemanager.h"

namespace Nuclear
{
	IParticleManager* CreateParticleManager(IBaseRenderer* pBaseRender, IFileIO* pFileIOMan)
	{
		return new ParticleManager(pBaseRender, pFileIOMan);
	}
	void ReleaseParticleManager(IParticleManager* pParticleMan)
	{
		delete pParticleMan;
	}
}
