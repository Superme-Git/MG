#include "../common/nulog.h"
#include "nucommonfuncs.h"
#include <utils/StringUtil.h>
#include <platform/platform_types.h>

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

namespace Nuclear
{
	DWORD FtoDW( float f )
	{
		return *((DWORD*)&f); 
	}

	float PointToLineMinDis(const NuclearPoint& pt, const LINE& line)
	{
		//点pt到线段line的最短距离
		POINT pta, ptb, ptc;
		pta.x = line.pt0.x;
		pta.y = line.pt0.y;
		ptb.x = line.pt1.x;
		ptb.y = line.pt1.y;
		ptc.x = pt.x;
		ptc.y = pt.y;

		POINT ptab, ptac;
		ptab.x = ptb.x - pta.x;
		ptab.y = ptb.y - pta.y;
		ptac.x = ptc.x - pta.x;
		ptac.y = ptc.y - pta.y;

		float f = (float)(ptab.x * ptac.x + ptab.y * ptac.y);
		if(f < 0)
			return ComputerDistance(pta, ptc);
		
		float d = (float)(ptab.x * ptab.x + ptab.y * ptab.y);
		if( f > d )
			return ComputerDistance(ptb, ptc);

		f = f/d;

		POINT D; //点在线段上的投影点坐标
		D.x = (int)(pta.x + f*ptab.x);
		D.y = (int)(pta.y + f*ptab.y);
		return ComputerDistance(D, ptc);
	}

	bool PtInLine(const NuclearPoint& pt, const LINE& line) 
	{
		bool bresult = ( (line.pt1.x - pt.x)*(line.pt0.x - pt.x)>=0 ) || ( (line.pt1.y - pt.y)*(line.pt0.y - pt.y)>=0 );
		if(bresult)
			return false; //点pt在以line为对角线的矩形区域外...先快速地粗略排除这种情况

		//数学上的斜率相等
		//但是计算机图形上的离散点很难出现斜率的绝对相等---误差到底取多少合适？
		POINT pt0, pt1, pt2;
		pt0.x = line.pt0.x;
		pt0.y = line.pt0.y;
		pt1.x = line.pt1.x;
		pt1.y = line.pt1.y;
		pt2.x = pt.x;
		pt2.y = pt.y;

		float dis = ComputerDistance(pt0, pt1);
		float dis1 = ComputerDistance(pt0, pt2);
		float dis2 = ComputerDistance(pt1, pt2);
		return( fabs(dis-dis1-dis2)<1.1f ); //离散的像素点...距离误差是很大的...
	}

	float ComputerDistance(POINT pt1, POINT pt2)
	{
		float dis = powf((float)(pt1.x - pt2.x), 2.0f) + powf((float)(pt1.y - pt2.y), 2.0f);
		dis = sqrt(dis);
		return dis;
	}

	POINT Computer3BezierCurvePoint(float u, POINT p0, POINT p1, POINT p2, POINT p3)
	{
		float coef0 = powf(1.0f-u, 3.0f);
		float coef1 = 3.0f * u * powf(1.0f-u, 2);
		float coef2 = 3 * powf(u, 2.0f) * (1-u);
		float coef3 = powf(u, 3.0f);

		POINT pt;
		pt.x = (long)(coef0*p0.x + coef1*p1.x + coef2*p2.x + coef3*p3.x);
		pt.y = (long)(coef0*p0.y + coef1*p1.y + coef2*p2.y + coef3*p3.y);
		return pt;
	}

	bool IsEqualFloat(float fNum1, float fNum2)
	{
		if( fabs(fNum1-fNum2)<MIN_FLOAT )
		{
			return true;
		}
		else
			return false;
	}

	/****************************************************************************
	* PluckFirstField: pulls the first "field" off of str and puts it in dest  *
	* (uses delim as a delimiter)                                              *
	****************************************************************************/
	void PluckFirstField(LPWSTR str, LPWSTR dest, int maxlen, LPCWSTR delim)					
	{
		LPWSTR endpos;
		int i;

		if( !wcslen(delim) )
		{
			wcscpy(dest, str);
			return;
		}
		endpos = wcsstr(str, delim);
		if(!endpos)
		{
			wcscpy(dest, str);
			return;
		}
		i = (int)(endpos - str);

		memset(dest, 0, maxlen);
		memcpy(dest, str, i*sizeof(wchar_t));

		wcscpy( str, &str[i+wcslen(delim)] );
	}

	void ResetCell5Byte(wchar_t cell[6])
	{
		XPASSERT(cell);
		for(int i=0; i<5; i++)
		{
			wchar_t ch = cell[i];
			if( !(ch>=L'0' && ch<=L'9') )
				cell[i] = ' ';
		}
	}

	bool GetRightSideFromValueMap(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, LPWSTR dest, int destsize)
	{
		std::wstring str;

		//允许文件中找不到某些参数
		if(valuemap.find(strName) != valuemap.end()) 
		{
#ifdef ANDROID
            wcsncpy_x(dest, valuemap[strName].c_str(), destsize/sizeof(wchar_t));
#else
            wcsncpy(dest, valuemap[strName].c_str(), destsize/sizeof(wchar_t));
#endif
			
			return true;
		}
		return false;
	}

	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, int &value)
	{
		wchar_t buf[256];
		bool bres = GetRightSideFromValueMap(valuemap, strName, buf, sizeof(buf));
		if(bres)
			value = _wtoi((LPCWSTR)buf);
	}

	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, long &value)
	{
		wchar_t buf[256];
		bool bres = GetRightSideFromValueMap(valuemap, strName, buf, sizeof(buf));
		if(bres)
			value = _wtol((LPCWSTR)buf);
	}

	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, float &value)
	{
		wchar_t buf[256];
		bool bres = GetRightSideFromValueMap(valuemap, strName, buf, sizeof(buf));
		if(bres)
			value = (float)_wtof(buf);
		//else
	}

	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, wchar_t value[256])
	{
		wchar_t buf[256];
		bool bres = GetRightSideFromValueMap(valuemap, strName, buf, sizeof(buf)); 
		if(bres)
			memcpy(value, buf, sizeof(buf));
	}

    static void to_upper(wchar_t* str)
    {
        if (!str) return;
        if (str[0] == 0) return;
        
        const int dist = L'a' - L'A';
        while (str[0]) {
            if(str[0] >= L'a' && str[0] <= L'z')
            {
                str[0] -= dist;
            }
            str++;
        }
    }
    
	void ReadBlendModeProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, int &value)
	{
		wchar_t buf[256];
		bool bres = GetRightSideFromValueMap(valuemap, strName, buf, sizeof(buf));
		if(!bres)
			return;

		for(int i=0; i<NUMBLENDINGMODES; i++)
		{
            to_upper(buf);
			if( !wcscmp(g_BlendingModes[i].name, buf) )
			{
				value = g_BlendingModes[i].mode;
				return;
			}
		}
		XPTRACE(L"ReadBlendModeProperty()--可使用的参数没有找到\n");
	}

	void ReadProperty(std::map<std::wstring, std::wstring> &valuemap, LPCWSTR strName, PATHPOINT &value)
	{
		wchar_t buf[256];
		bool bres = GetRightSideFromValueMap(valuemap, strName, buf, sizeof(buf));

		wchar_t cell[6];
		memset(cell, 0, 6*sizeof(wchar_t));
		int index = 0;
		memcpy(cell, buf+5*index, 5*sizeof(wchar_t));
		value.pt.x = _wtoi((LPCWSTR)cell);
		index++;

		memcpy(cell, buf+5*index, 5*sizeof(wchar_t));
		value.pt.y = _wtoi((LPCWSTR)cell);
		index++;

		memcpy(cell, buf+5*index, 5*sizeof(wchar_t));
		value.m_bCurve = (_wtoi((LPCWSTR)cell));
		index++;

		memcpy(cell, buf+5*index, 5*sizeof(wchar_t));
		value.m_bLink = (_wtoi((LPCWSTR)cell));
		index++;

		memcpy(cell, buf+5*index, 5*sizeof(wchar_t));
		value.pre_ControlPt.x = _wtoi((LPCWSTR)cell);
		index++;

		memcpy(cell, buf+5*index, 5*sizeof(wchar_t));
		value.pre_ControlPt.y = _wtoi((LPCWSTR)cell);
		index++;

		memcpy(cell, buf+5*index, 5*sizeof(wchar_t));
		value.next_ControlPt.x = _wtoi((LPCWSTR)cell);
		index++;

		memcpy(cell, buf+5*index, 5*sizeof(wchar_t));
		value.next_ControlPt.y = _wtoi((LPCWSTR)cell);
		index++;
	}

	void TransformPoint(std::vector<POINT>& vectorFullPathPoint, float fPathZoom, float fPathSpin, int nPathMirrorType)
	{
		int cnt = (int)vectorFullPathPoint.size();
		fPathSpin = TORADIANS(fPathSpin);
		float posx = 0.0f;
		float posy = 0.0f;
		for(int i=0; i<cnt; i++)
		{
			POINT pt = vectorFullPathPoint[i];

			pt.x = (int)(pt.x *fPathZoom);
			pt.y = (int)(pt.y *fPathZoom);

			posx = (float)pt.x;
			posy = (float)pt.y;
			pt.x = (int)( posx*cos(fPathSpin) - posy*sin(fPathSpin) );
			pt.y = (int)( posx*sin(fPathSpin) + posy*cos(fPathSpin) );
			switch(nPathMirrorType)
			{
			case PATHMIRRORTYPE_NONE:
				break;
			case PATHMIRRORTYPE_LR:
				pt.x *= -1;
				break;
			case PATHMIRRORTYPE_TB:
				pt.y *= -1;
				break;
			case PATHMIRRORTYPE_LRTB:
				pt.x *= -1;
				pt.y *= -1;
				break;
			default:
			    break;
			}

			vectorFullPathPoint[i] = pt;
		}
	}

	int NormalizeParam(int data, int nMin, int nMax)
	{
		data = data<nMin ? nMin:data;
		data = data>nMax ? nMax:data;
		return data;
	}

	float NormalizeParam(float data, float fMin, float fMax)
	{
		data = data<fMin ? fMin:data;
		data = data>fMax ? fMax:data;
		return data;
	}

	void WriteData(std::ofstream& FS, int Value)
	{
		FS.write((char*)&Value, sizeof(int));
	}
	void WriteData(std::ofstream& FS, float Value)
	{
		FS.write((char*)&Value, sizeof(float));
	}
	void WriteData(std::ofstream& FS, std::string Value)
	{
		int iCount = Value.size();
		WriteData(FS, iCount);
		std::string TmpStr = Value;
		FS.write((char*)TmpStr.c_str(), iCount * sizeof(char));
	}
	void WriteData(std::ofstream& FS, std::wstring Value)
	{
		int iCount = Value.size();
		WriteData(FS, iCount);
		std::wstring TmpWStr = Value;
		FS.write((char*)TmpWStr.c_str(), iCount * sizeof(wchar_t));
	}
	void WriteDataBlendMode(std::ofstream& FS, int Value)
	{
		for (int i = 0; i < NUMBLENDINGMODES; i++)
		{
			if (g_BlendingModes[i].mode == Value)
			{
				WriteData(FS, ws2s(g_BlendingModes[i].name));
				return;
			}
		}
		WriteData(FS, "");
	}

	void ReadData(std::ifstream& FS, int& Value)
	{
		FS.read((char*)&Value, sizeof(int));
	}
	void ReadData(std::ifstream& FS, float& Value)
	{
		FS.read((char*)&Value, sizeof(float));
	}
	void ReadData(std::ifstream& FS, std::string& Value)
	{
		int iCount;
		ReadData(FS, iCount);
		std::string Result;
		Result.resize(iCount);
		FS.read((char*)Result.c_str(), iCount * sizeof(char));
		Value = Result;
	}
	void ReadData(std::ifstream& FS, std::wstring& Value)
	{
		int iCount;
		ReadData(FS, iCount);
		std::wstring Result;
		Result.resize(iCount);
		FS.read((char*)Value.c_str(), iCount * sizeof(wchar_t));
		Value = Result;
	}
	void ReadDataBlendMode(std::ifstream& FS, int& Value)
	{
		std::string ResultS;
		ReadData(FS, ResultS);
		std::wstring ResultWS;
		ResultWS = s2ws(ResultS);
		for (int i = 0; i < NUMBLENDINGMODES; i++)
		{
			if (ResultWS.compare(g_BlendingModes[i].name) == 0)
			{
				Value = g_BlendingModes[i].mode;
				return;
			}
		}
		Value = 0;
	}

	void ReadData2(INuclearStream& FS, int& Value)
	{
		FS >> Value;
	}
	void ReadData2(INuclearStream& FS, float& Value)
	{
		FS >> Value;
	}
	void ReadData2(INuclearStream& FS, std::string& Value)
	{
		int iCount;
		ReadData2(FS, iCount);
		std::string Result;
		Result.resize(iCount);
		FS.pop_byte((char*)Result.c_str(), iCount * sizeof(char));
		Value = Result;
	}
	void ReadData2(INuclearStream& FS, std::wstring& Value)
	{
		int iCount;
		ReadData2(FS, iCount);
		std::wstring Result;
		Result.resize(iCount);
		FS.pop_byte((char*)Value.c_str(), iCount * sizeof(wchar_t));
		Value = Result;
	}
	void ReadDataBlendMode2(INuclearStream& FS, int& Value)
	{
		std::string ResultS;
		ReadData2(FS, ResultS);
		std::wstring ResultWS;
		ResultWS = s2ws(ResultS);
		for (int i = 0; i < NUMBLENDINGMODES; i++)
		{
			if (ResultWS.compare(g_BlendingModes[i].name) == 0)
			{
				Value = g_BlendingModes[i].mode;
				return;
			}
		}
		Value = 0;
	}

}