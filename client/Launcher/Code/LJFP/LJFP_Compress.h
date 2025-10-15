#ifndef LJFP_Compress_H
#define LJFP_Compress_H

#include "LJFP_Var.h"
#include "LJFP_MiniZ.h"

void Test()
{
	mz_ulong uiSrc = 8;
	unsigned char* pSrc = NULL;
	pSrc = (unsigned char*)malloc(uiSrc);
	memset(pSrc, 0, uiSrc);
	for (int i = 0; i < uiSrc; i++)
	{
		pSrc[i] = i;
	}
	pSrc[0] = 0xb2;
	pSrc[1] = 0xe2;
	pSrc[2] = 0xca;
	pSrc[3] = 0xd4;
	pSrc[4] = 0xd6;
	pSrc[5] = 0xd0;
	pSrc[6] = 0xce;
	pSrc[7] = 0xc4;

	std::vector<int> vSrc;
	std::string sSrc;
	vSrc.resize(uiSrc);
	sSrc.resize(uiSrc);
	for (int i = 0; i < uiSrc; i++)
	{
		vSrc[i] = pSrc[i];
		sSrc[i] = vSrc[i];
	}


	mz_ulong uiDst = 1000;
	unsigned char* pDst = NULL;
	pDst = (unsigned char*)malloc(uiDst);
	memset(pDst, 0, uiDst);
	mz_compress(pDst, &uiDst, pSrc, uiSrc);
	std::vector<int> vDst;
	std::string sDst;
	vDst.resize(uiDst);
	sDst.resize(uiDst);
	for (int i = 0; i < uiDst; i++)
	{
		vDst[i] = pDst[i];
		sDst[i] = vDst[i];
	}

	mz_ulong uiDst2 = 1000;
	unsigned char* pDst2 = NULL;
	pDst2 = (unsigned char*)malloc(uiDst2);
	memset(pDst2, 0, uiDst2);
	//tinfl_decompress_mem_to_mem(pDst, uiDst, pSrc, 100, 0);
	mz_uncompress(pDst2, &uiDst2, pDst, uiDst);
	std::vector<int> vDst2;
	std::string sDst2;
	vDst2.resize(uiDst2);
	sDst2.resize(uiDst2);
	for (int i = 0; i < uiDst2; i++)
	{
		vDst2[i] = pDst2[i];
		sDst2[i] = vDst2[i];
	}

	delete pSrc;
	delete pDst;
	delete pDst2;
}


#endif //LJFP_Compress_H