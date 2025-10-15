#ifndef __Nuclear_COMMONFUNCS_H__
#define __Nuclear_COMMONFUNCS_H__

#if _MSC_VER > 1000
#pragma once
#endif


#include "nuparticlesyscommon.h"
#include "../common/nuport.h"
#include "../common/nuxmarshal.h"
#include <math.h>
#include <fstream>


namespace Nuclear
{
	const int NUMPARTICLES = 2000;
	const float MIN_FLOAT = 0.00001f;

	//查表powf(fAgeCoef, fvar);//fAgeCoef:0.0f--1.0f,  fvar:0.0f--2.0f粒子系统用
	//var分成20份, 生命值分成40份
	extern float XP_POWF[11][42];

	DWORD FtoDW( float f );

	float PointToLineMinDis(const NuclearPoint& pt, const LINE& line); //点到线段的最短距离
	bool PtInLine(const NuclearPoint& pt, const LINE& line); //点是否在线段上

	float ComputerDistance(POINT pt1, POINT pt2);
	POINT Computer3BezierCurvePoint(float u, POINT p0, POINT p1, POINT p2, POINT p3);

	bool IsEqualFloat(float fNum1, float fNum2);

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

		if( fMax-fMin < FIRE_EPSILON ) return(fMin);
		float fRandom = (float)rand() / (float)RAND_MAX;
		return( (fRandom * (fMax-fMin))+fMin );
	}

	inline float GetPowfLerpValue(float dis, float var)
	{
		//粒子系统专用--因为针对粒子特效的需要做了特殊处理，这里算出来的并不是标准的pow	
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
	* PluckFirstField: pulls the first "field" off of str and puts it in dest  *
	* (uses delim as a delimiter)                                              *
	****************************************************************************/
	void TransformPoint(std::vector<POINT>& vectorFullPathPoint, float fPathZoom, float fPathSpin, int nPathMirrorType);//解码后缩放+旋转
	
	void WriteProperty(FILE *file, LPCWSTR strName, int value);
	void WriteProperty(FILE *file, LPCWSTR strName, float value);
	void WriteProperty(FILE *file, LPCWSTR strName, LPCWSTR value);
	void WriteBlendModeProperty(FILE *file, LPCWSTR strName, int value);
	void WriteProperty(FILE *file, LPCWSTR strName, Nuclear::PATHPOINT value);
	void ResetCell5Byte(wchar_t cell[6]);

	bool GetRightSideFromValueMap(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, LPWSTR dest, int destsize);
	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, int &value);
	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, long &value);
	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, float &value);
	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, wchar_t value[256]);
	void ReadBlendModeProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, int &value);
	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, PATHPOINT &value);

	int NormalizeParam(int data, int nMin, int nMax); //把data限制在min和max之间
	float NormalizeParam(float data, float fMin, float fMax);

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

	void ReadData2(INuclearStream& FS, int& Value);
	void ReadData2(INuclearStream& FS, float& Value);
	void ReadData2(INuclearStream& FS, std::string& Value);
	void ReadData2(INuclearStream& FS, std::wstring& Value);
	void ReadDataBlendMode2(INuclearStream& FS, int& Value);

};

#endif
