#ifndef LJFP_Var_H
#define LJFP_Var_H

#include "stdlib.h"
#include <vector>
#include <string>
#include <list>
#include <map>
#include <fstream>

typedef unsigned int(*CRC32_Func)(unsigned int crc, const unsigned char* ptr, size_t buf_len);
typedef int(*Zip_Func)(unsigned char *pDest, unsigned int *pDest_len, const unsigned char *pSource, unsigned int source_len, int level);
typedef int(*UnZip_Func)(unsigned char *pDest, unsigned int *pDest_len, const unsigned char *pSource, unsigned int source_len);

typedef void(*SMS4_Func)(unsigned char* inBuff, unsigned char* ouBuff, unsigned int uiSize, std::string strPassword);
typedef void(*DeSMS4_Func)(unsigned char* inBuff, unsigned char* ouBuff, unsigned int uiSize, std::string strPassword);


#endif //LJFP_Var_H