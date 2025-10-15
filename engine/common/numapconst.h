#ifndef __Nuclear_MAP_CONST_H
#define __Nuclear_MAP_CONST_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	static const int XPMAP_LAYER_COUNT = 13;	//��ͼ�������ֱ��ǵر�ˮ1 ˮ2 ��1 ��2 ��3 ��4 ��5 ��6 ��1 ��2 ��1 ��2
	static const int XPTILES_SMALL_LAYER_COUNT = 4;	//С�ر����
	static const int XPTILES_MID_LAYER_COUNT = 2;		//�еر����
	static const int XPTILES_ALL_LAYER_COUNT = 4;		//�ر�������ֱ���1��С�ر� TILES_MID_LAYER_COUNT���еر�1����ر�
	static const int XPPIC_GROUND_LAYER_COUNT = XPTILES_SMALL_LAYER_COUNT + XPTILES_ALL_LAYER_COUNT - 1;//ʵ�ʵر��ܲ���
	static const int XPPIC_CATCH_LAYER_COUNT = XPPIC_GROUND_LAYER_COUNT + 2;//���еĵر� + ˮ1+ˮ2
	static const int XPPIC_ELEMENT_LAYER_COUNT = XPMAP_LAYER_COUNT - 1;	//�������
	static const int XPPIC_LAYER_COUNT = XPPIC_GROUND_LAYER_COUNT + XPPIC_ELEMENT_LAYER_COUNT;	//ͼƬ�ܲ���
	static const int XPPIC_FIRST_ELEMENT_ID = XPPIC_GROUND_LAYER_COUNT;
	static const int XPMID1_ELEMENT_LAYER_ID_FOR_ELEMENT = 8;
	static const int XPMID2_ELEMENT_LAYER_ID_FOR_ELEMENT = 9;
	static const int XPPIC_MID1_ELEMENT_LAYER_ID = XPPIC_FIRST_ELEMENT_ID + XPMID1_ELEMENT_LAYER_ID_FOR_ELEMENT;
	static const int XPPIC_MID2_ELEMENT_LAYER_ID = XPPIC_FIRST_ELEMENT_ID + XPMID2_ELEMENT_LAYER_ID_FOR_ELEMENT;
	static const int XPSORTED_LAYER_COUNT = 2;	//��Ҫ����Ĳ���
}

#endif