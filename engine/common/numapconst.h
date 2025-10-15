#ifndef __Nuclear_MAP_CONST_H
#define __Nuclear_MAP_CONST_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	static const int XPMAP_LAYER_COUNT = 13;	//地图层数，分别是地表，水1 水2 低1 低2 低3 低4 低5 低6 中1 中2 高1 高2
	static const int XPTILES_SMALL_LAYER_COUNT = 4;	//小地表层数
	static const int XPTILES_MID_LAYER_COUNT = 2;		//中地表层数
	static const int XPTILES_ALL_LAYER_COUNT = 4;		//地表层数，分别是1个小地表 TILES_MID_LAYER_COUNT个中地表，1个大地表
	static const int XPPIC_GROUND_LAYER_COUNT = XPTILES_SMALL_LAYER_COUNT + XPTILES_ALL_LAYER_COUNT - 1;//实际地表总层数
	static const int XPPIC_CATCH_LAYER_COUNT = XPPIC_GROUND_LAYER_COUNT + 2;//所有的地表 + 水1+水2
	static const int XPPIC_ELEMENT_LAYER_COUNT = XPMAP_LAYER_COUNT - 1;	//物件层数
	static const int XPPIC_LAYER_COUNT = XPPIC_GROUND_LAYER_COUNT + XPPIC_ELEMENT_LAYER_COUNT;	//图片总层数
	static const int XPPIC_FIRST_ELEMENT_ID = XPPIC_GROUND_LAYER_COUNT;
	static const int XPMID1_ELEMENT_LAYER_ID_FOR_ELEMENT = 8;
	static const int XPMID2_ELEMENT_LAYER_ID_FOR_ELEMENT = 9;
	static const int XPPIC_MID1_ELEMENT_LAYER_ID = XPPIC_FIRST_ELEMENT_ID + XPMID1_ELEMENT_LAYER_ID_FOR_ELEMENT;
	static const int XPPIC_MID2_ELEMENT_LAYER_ID = XPPIC_FIRST_ELEMENT_ID + XPMID2_ELEMENT_LAYER_ID_FOR_ELEMENT;
	static const int XPSORTED_LAYER_COUNT = 2;	//需要排序的层数
}

#endif