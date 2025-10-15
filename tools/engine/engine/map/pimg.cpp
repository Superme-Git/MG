#include "stdafx.h"
#include "pimg.h"

namespace Nuclear
{
	void PImg::ResetImgObject()
	{
		m_nRow = 0;
		m_nCol = 0;
		m_nFileSize = 0;
		m_nFileWidth = 0;
		m_nFileHeight = 0;

		m_filefmt = Nuclear::XPIFF_DDS;
		m_texfmt = Nuclear::XPTEXFMT_DXT5;

		m_dwVer = CURVER; // ���������ʼ��Ϊ��ǰ�汾��ʵ��û�����

		CenterPoint = Nuclear::CPOINT();
		for(int i=0; i<4; i++)
			pos[i] = Nuclear::CPOINT();
		m_vectorRct.clear();
		m_mask.clear();
	}

	XOStream& PImg::marshal(XOStream &os) const
	{ //save
		os << (int)CURVER; //�����ļ���ʽ....int

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

		//�汾4�¼ӵ�.. 4��5��CRECT 6��ΪCPOINT
		count = (int)m_mask.size();
		os << count;
		for(int i=0; i<count; ++i)
			os << m_mask[i];
		//����

		os << m_filefmt;
		os << m_texfmt;
		return os;
	}

	const XIStream& PImg::unmarshal(const XIStream &os)
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
			CRECT rct;
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
			std::vector<CRECT> mask;
			mask.clear();
			for(int i=0; i<count; i++)
			{
				CRECT rct;
				os >> rct;
				mask.push_back(rct);
			}
		}
		if(m_dwVer >5)
		{
			int count = 0;
			os >> count;
			m_mask.clear();
			for(int i=0; i<count; i++)
			{
				CPOINT pt;
				os >> pt;
				m_mask.push_back(pt);
			}
		}

		if(m_dwVer<5)
		{
			TCHAR TextureFormat[20];
			TCHAR PicFileFormat[20];
			os.pop_byte((char*)TextureFormat, 20*sizeof(TCHAR));
			os.pop_byte((char*)PicFileFormat, 20*sizeof(TCHAR));

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