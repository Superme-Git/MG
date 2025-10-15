// 本文件定义了engine和gameclient中所有需要定义的版本号，现为手工编辑，
// 以后此文件可能会自动生成. 
// 如果有必要修改此文件，请联系刘东学或相关人员
//
#include "product.inl"
#ifdef PRODUCT_TIYAN	// 体验版
#include "version_tiyan.inl"
#elif defined PRODUCT_CHT	// 台湾繁体中文版
#include "version_cht.inl"
#else		// 正式版
#include "version_formal.inl"
#endif

// 需要保证换行
