// 本文件定义了Nuclear中mhzx正式版程序的版本号，现为手工编辑，
// 以后此文件可能会自动生成. 
// 如果有必要修改此文件，请联系刘东学或相关人员
//
#ifdef PRODUCT_TIYAN	// 体验版
#error You should not define PRODUCT_TIYAN	before including this file
#else
// 正式版

#define MajorVersion	1
#define MinorVersion	0
#define shareVersion	0
#define NuclearVersion 8947

#endif

// 需要保证换行
