#include "nupimg.h"

namespace Nuclear
{
	void NuclearImg::ResetImgObject()
	{
		m_nRow = 0;
		m_nCol = 0;
		m_nFileSize = 0;
		m_nFileWidth = 0;
		m_nFileHeight = 0;

		m_filefmt = Nuclear::XPIFF_DDS;
		m_texfmt = Nuclear::XPTEXFMT_DXT5;

		m_dwVer = ImgVer_CURVER; // 这个变量初始化为当前版本其实是没道理的

		CenterPoint = Nuclear::NuclearPoint();
		for(int i=0; i<4; i++)
			pos[i] = Nuclear::NuclearPoint();
		m_vectorRct.clear();
		m_Mask.clear();
	}

	NuclearOStream& NuclearImg::marshal(NuclearOStream &os) const
	{ //save
		os << (int)ImgVer_CURVER; //纹理文件格式....int

		os << CenterPoint;
		for(int i=0; i<4; ++i)
			os << pos[i];


		os << m_nRow;
		os << m_nCol;

		int count = (int)m_vectorRct.size();
		os << count;
		for(int i=0; i<count; ++i)
			os << m_vectorRct[i];
		os << m_nFileSize;
		os << m_nFileWidth;
		os << m_nFileHeight;

		//版本4新加的.. 4、5是NuclearRect 6改为NuclearPoint
		count = (int)m_Mask.size();
		os << count;
		for(int i=0; i<count; ++i)
			os << m_Mask[i];
		//以上

		os << m_filefmt;
		os << m_texfmt;
		return os;
	}

	const INuclearStream& NuclearImg::unmarshal(const INuclearStream &os)
	{ //Load
		int dummy;
		os >> dummy;

		m_dwVer = dummy;

		os >> CenterPoint;
		for(int i=0; i<4; i++)
			os >> pos[i];


		os >> m_nRow;
		os >> m_nCol;

		int count = 0;
		os >> count;
		m_vectorRct.clear();

		for(int i=0; i<count; i++)
		{
			NuclearRect rct;
			os >> rct;
			m_vectorRct.push_back(rct);
		}

		os >> m_nFileSize;
		os >> m_nFileWidth;
		os >> m_nFileHeight;

		if( m_dwVer==4 || m_dwVer==5 )
		{
			int count = 0;
			os >> count;
			std::vector<NuclearRect> mask;
			mask.clear();
			for(int i=0; i<count; i++)
			{
				NuclearRect rct;
				os >> rct;
				mask.push_back(rct);
			}
		}
		if(m_dwVer >5)
		{
			int count = 0;
			os >> count;
			m_Mask.clear();
			for(int i=0; i<count; i++)
			{
				NuclearPoint pt;
				os >> pt;
				m_Mask.push_back(pt);
			}
		}

		if(m_dwVer<5)
		{
//			TCHAR TextureFormat[20];
//			TCHAR PicFileFormat[20];
//			os.pop_byte((char*)TextureFormat, 20*sizeof(TCHAR));
//			os.pop_byte((char*)PicFileFormat, 20*sizeof(TCHAR));
            
            assert(false);

			m_filefmt = Nuclear::XPIFF_DDS;
			m_texfmt = Nuclear::XPTEXFMT_DXT5;
		}
		else
		{
			os >> m_filefmt;
			os >> m_texfmt;
		}

		return os;
	}
	
}