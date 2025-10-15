#ifndef __Nuclear_PIMG_H
#define __Nuclear_PIMG_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\pobject.h"
#include "..\common\xptypes.h"

namespace Nuclear
{
	class PImg : public PObject
	{
	public:
		enum IMGVER{
			PREVER = 1, //�༭�����ṩ��ת�����°汾�Ĺ���
			FOOVER = 2, //2008-12-05----�Ѵ�ͼ�и��512*512��Сͼ��Ϊdds�ļ���
						//���ص�ͼʱѡ�����ǡ���Ŀ�ﵽ�����ڴ�ʹ�õ�Ŀ�ġ�
			NEWVER = 3, //����ֽ�����..
			VERSION04 = 4, //��������������m_mask
			VERSION05 = 5, //�ļ������ʽ��Ϊ���Ӧ��intֵ..
			VERSION06 = 6, //�ڵ������Ϊ��յĶ����...һ�㲻����10������..
			CURVER = 6,
		};	

	public:
		PImg()
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
		//
		XOStream& marshal(XOStream &) const;
		const XIStream& unmarshal(const XIStream &);
		int GetSourceVersion() { return m_dwVer; }
		void ResetImgObject();//ת���ļ�ʱ����...
		// ��������

		CPOINT CenterPoint; //���� ---���ͼƬ���Ͻǵ�----����������...
		CPOINT pos[4]; //�� �� �� �� 0 1 2 3

		int m_nRow;
		int m_nCol;
		std::vector<CRECT> m_vectorRct;
		int m_nFileSize;
		int m_nFileWidth;
		int m_nFileHeight;
	//	std::vector<CRECT> m_mask; // todo ���ֲ�, ���ڴ�������ʱ��������͸����ʾ
		std::vector<CPOINT> m_mask;

		int m_filefmt;		// �ļ���ʽ
		int m_texfmt;		// �����ʽ

		int m_dwVer; // ����Դ�ļ��İ汾�����Ƕ���İ汾

	};
	
}

#endif