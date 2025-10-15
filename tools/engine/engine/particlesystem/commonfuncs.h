#ifndef __Nuclear_COMMONFUNCS_H
#define __Nuclear_COMMONFUNCS_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "particlesyscommon.h"
#include <math.h>
#include <fstream>
#include "..\common\xmarshal.h"

namespace Nuclear
{



const int NUMPARTICLES = 2000;
const float MIN_FLOAT = 0.00001f;

//���powf(fAgeCoef, fvar);//fAgeCoef:0.0f--1.0f,  fvar:0.0f--2.0f����ϵͳ��
//var�ֳ�20��, ����ֵ�ֳ�40��
extern float XP_POWF[11][42];

DWORD FtoDW( FLOAT f );

float PointToLineMinDis(const CPOINT& pt, const LINE& line); //�㵽�߶ε���̾���
bool PtInLine(const CPOINT& pt, const LINE& line); //���Ƿ����߶���

float ComputerDistance(POINT pt1, POINT pt2);
POINT Computer3BezierCurvePoint(float u, POINT p0, POINT p1, POINT p2, POINT p3);

bool IsEqualFloat(float fNum1, float fNum2);
//int RandomNumber(int iMin, int iMax);
//float RandomNumber(float fMin, float fMax);


inline int RandomNumber(int iMin, int iMax)
{
	if(iMin > iMax)
	{
		int temp = iMin;
		iMin = iMax;
		iMax = temp;
	}

	if (iMin == iMax) return(iMin);
	return( (rand() % (iMax-iMin+1)) +iMin );
}

inline float RandomNumber(float fMin, float fMax)
{
	if(fMin > fMax)
	{
		float temp = fMin;
		fMin = fMax;
		fMax = temp;
	}

	if( fMax-fMin < XP_EPSILON ) return(fMin);
	float fRandom = (float)rand() / (float)RAND_MAX;
	return( (fRandom * (fMax-fMin))+fMin );
}

__forceinline float GetPowfLerpValue(float dis, float var)
{
	//����ϵͳר��--��Ϊ���������Ч����Ҫ�������⴦������������Ĳ����Ǳ�׼��pow
	
	bool need = var>0.5f;
	if(need)
	{
		dis = 1.0f - dis;
		var = 1.0f - var;
	}

	dis *= 40;
	int ndis = (int)dis;
	int nvar = (int)(var*20);
	float fcoef = dis - ndis;

	float dis1 = XP_POWF[nvar][ndis++];
	dis1 += fcoef*(XP_POWF[nvar][ndis] - dis1);

	if(need)
		return 1.0f-dis1;

	return dis1;
}

void PluckFirstField(LPWSTR str, LPWSTR dest, int maxlen, LPCWSTR delim);
/****************************************************************************
*                                                                          *
* PluckFirstField: pulls the first "field" off of str and puts it in dest  *
* (uses delim as a delimiter)                                              *
*                                                                          *
****************************************************************************/
void TransformPoint(std::vector<POINT>& vectorFullPathPoint, float fPathZoom, float fPathSpin, int nPathMirrorType);
//���������+��ת

void WriteProperty(FILE *file, LPCWSTR strName, int value);
void WriteProperty(FILE *file, LPCWSTR strName, float value);
void WriteProperty(FILE *file, LPCWSTR strName, LPCWSTR value);
void WriteBlendModeProperty(FILE *file, LPCWSTR strName, int value);
void WriteProperty(FILE *file, LPCWSTR strName, Nuclear::PATHPOINT value);
void ResetCell5Byte(TCHAR cell[6]);

bool GetRightSideFromValueMap(std::map<std::wstring, std::wstring> &valuemap, 
							  LPCWSTR strName, LPWSTR dest, int destsize);
void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, int &value);
void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, long &value);
void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, float &value);
void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, TCHAR value[256]);
void ReadBlendModeProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, int &value);
void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, PATHPOINT &value);

int NormalizeParam(int data, int nMin, int nMax); //��data������min��max֮��
float NormalizeParam(float data, float fMin, float fMax);

// std::wstring towstring(const int& i);
// std::wstring towstring(const unsigned int& i);
// std::wstring towstring(const unsigned long& i);
// std::wstring towstring(const double& f);
// std::wstring tohexwstring(const int& i);	
// int toint(const std::wstring& str);
// int hextoint(const std::wstring& str);

void WriteData(std::ofstream& FS, int Value);
void WriteData(std::ofstream& FS, float Value);
void WriteData(std::ofstream& FS, std::string Value);
void WriteData(std::ofstream& FS, std::wstring Value);
void WriteDataBlendMode(std::ofstream& FS, int Value);

void ReadData(std::ifstream& FS, int& Value);
void ReadData(std::ifstream& FS, float& Value);
void ReadData(std::ifstream& FS, std::string& Value);
void ReadData(std::ifstream& FS, std::wstring& Value);
void ReadDataBlendMode(std::ifstream& FS, int& Value);

void ReadData2(XIStream& FS, int& Value);
void ReadData2(XIStream& FS, float& Value);
void ReadData2(XIStream& FS, std::string& Value);
void ReadData2(XIStream& FS, std::wstring& Value);
void ReadDataBlendMode2(XIStream& FS, int& Value);

std::string ws2s(std::wstring WS);
std::wstring s2ws(std::string WS);


};

#endif