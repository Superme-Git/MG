#ifndef __Nuclear_MAP_CONST_H
#define __Nuclear_MAP_CONST_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	static const int MAP_LAYER_COUNT = 12;	//��ͼ�������ֱ��ǵر�ˮ1 ˮ2 ��1 ��2 ��3 ��4 ��5 ��6 �� ��1 ��2
	static const int TILES_SMALL_LAYER_COUNT = 4;	//С�ر����
	static const int TILES_MID_LAYER_COUNT = 2;		//�еر����
	static const int TILES_ALL_LAYER_COUNT = 4;		//�ر�������ֱ���1��С�ر� TILES_MID_LAYER_COUNT���еر�1����ر�
	static const int PIC_GROUND_LAYER_COUNT = TILES_SMALL_LAYER_COUNT + TILES_ALL_LAYER_COUNT - 1;//ʵ�ʵر��ܲ���
	static const int PIC_CATCH_LAYER_COUNT = PIC_GROUND_LAYER_COUNT + 2;//���еĵر� + ˮ1+ˮ2
	static const int PIC_ELEMENT_LAYER_COUNT = MAP_LAYER_COUNT - 1;	//�������
	static const int PIC_LAYER_COUNT = PIC_GROUND_LAYER_COUNT + PIC_ELEMENT_LAYER_COUNT;	//ͼƬ�ܲ���
	static const int PIC_FIRST_ELEMENT_ID = PIC_GROUND_LAYER_COUNT;
	static const int MID_ELEMENT_LAYER_ID_FOR_ELEMENT = 8;
	static const int PIC_MID_ELEMENT_LAYER_ID = PIC_FIRST_ELEMENT_ID + MID_ELEMENT_LAYER_ID_FOR_ELEMENT;
}

#endif