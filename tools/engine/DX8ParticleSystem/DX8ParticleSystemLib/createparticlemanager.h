#pragma once

namespace Nuclear
{
	IParticleManager* CreateParticleManager(IBaseRenderer* pBaseRender, IFileIO* pFileIOMan);
	void ReleaseParticleManager(IParticleManager* pParticleMan);
}
