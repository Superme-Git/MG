#pragma once

namespace MHZX
{
	class IBlockDecoder
	{
	public:
		virtual void Decode(
			unsigned char* pDestPicture, int pictureWidth, int pictureHeight,
			const unsigned char* pSrcBlock, unsigned int size,
			int width, int height, int x_offset, int y_offset ) = 0;

		virtual void Release() = 0;
	};

	class CBlockDecoderFactory
	{
	public:
		static IBlockDecoder* CreateBlockDecoder(int format);
	};
}