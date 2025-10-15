#ifndef __Nuclear_PIMG_H__
#define __Nuclear_PIMG_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nupobject.h"
#include "../common/nuxptypes.h"

namespace Nuclear
{
	class NuclearImg : public NuclearObject
	{
	public:
		enum eIMGVER
		{
			ImgVer_PREVER = 1, //�༭�����ṩ��ת�����°汾�Ĺ���
			ImgVer_FOOVER = 2, //2008-12-05----�Ѵ�ͼ�и��512*512��Сͼ��Ϊdds�ļ���
						//���ص�ͼʱѡ�����ǡ���Ŀ�ﵽ�����ڴ�ʹ�õ�Ŀ�ġ�
			ImgVer_ImgVer_NEWVER = 3, //����ֽ�����..
			ImgVer_VERSION04 = 4, //��������������m_Mask
			ImgVer_VERSION05 = 5, //�ļ������ʽ��Ϊ���Ӧ��intֵ..
			ImgVer_VERSION06 = 6, //�ڵ������Ϊ��յĶ����...һ�㲻����10������..
			ImgVer_CURVER = 6,
		};	

	public:
		NuclearImg()
		{
			m_nRow = 0;
			m_nCol = 0;
			m_nFileSize = 0;
			m_nFileWidth = 0;
			m_nFileHeight = 0;

			m_filefmt = Nuclear::XPIFF_DDS;
			m_texfmt = Nuclear::XPTEXFMT_DXT5;

			m_dwVer = ImgVer_CURVER; // ���������ʼ��Ϊ��ǰ�汾��ʵ��û�����

			CenterPoint = Nuclear::NuclearPoint();
			for(int i=0; i<4; i++)
				pos[i] = Nuclear::NuclearPoint();
			m_vectorRct.clear();
			m_Mask.clear();
		}

		NuclearOStream& marshal(NuclearOStream &) const;
		const INuclearStream& unmarshal(const INuclearStream &);
		int GetSourceVersion() { return m_dwVer; }
		void ResetImgObject();//ת���ļ�ʱ����...

		NuclearPoint CenterPoint; //���� ---���ͼƬ���Ͻǵ�----����������...
		NuclearPoint pos[4]; //�� �� �� �� 0 1 2 3

		int m_nRow;
		int m_nCol;
		std::vector<NuclearRect> m_vectorRct;
		int m_nFileSize;
		int m_nFileWidth;
		int m_nFileHeight;
		std::vector<NuclearPoint> m_Mask;

		int m_filefmt;		// �ļ���ʽ
		int m_texfmt;		// �����ʽ

		int m_dwVer; // ����Դ�ļ��İ汾�����Ƕ���İ汾
	};
	
}

#endif