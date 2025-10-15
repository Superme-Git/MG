#ifndef __Nuclear_MAP_CONST_H
#define __Nuclear_MAP_CONST_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	static const int MAP_LAYER_COUNT = 12;	//地图层数，分别是地表，水1 水2 低1 低2 低3 低4 低5 低6 中 高1 高2
	static const int TILES_SMALL_LAYER_COUNT = 4;	//小地表层数
	static const int TILES_MID_LAYER_COUNT = 2;		//中地表层数
	static const int TILES_ALL_LAYER_COUNT = 4;		//地表层数，分别是1个小地表 TILES_MID_LAYER_COUNT个中地表，1个大地表
	static const int PIC_GROUND_LAYER_COUNT = TILES_SMALL_LAYER_COUNT + TILES_ALL_LAYER_COUNT - 1;//实际地表总层数
	static const int PIC_CATCH_LAYER_COUNT = PIC_GROUND_LAYER_COUNT + 2;//所有的地表 + 水1+水2
	static const int PIC_ELEMENT_LAYER_COUNT = MAP_LAYER_COUNT - 1;	//物件层数
	static const int PIC_LAYER_COUNT = PIC_GROUND_LAYER_COUNT + PIC_ELEMENT_LAYER_COUNT;	//图片总层数
	static const int PIC_FIRST_ELEMENT_ID = PIC_GROUND_LAYER_COUNT;
	static const int MID_ELEMENT_LAYER_ID_FOR_ELEMENT = 8;
	static const int PIC_MID_ELEMENT_LAYER_ID = PIC_FIRST_ELEMENT_ID + MID_ELEMENT_LAYER_ID_FOR_ELEMENT;
}

#endif