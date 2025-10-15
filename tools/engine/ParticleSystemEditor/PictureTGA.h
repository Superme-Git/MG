#pragma once

class CPictureTGA
{
public:
	CPictureTGA(void);
	virtual ~CPictureTGA(void);

public:
	bool GetWidthHeight(int &nWidth, int &nHeight);
	bool ReadRGBAData(BYTE* pDest);
	bool Open(LPCTSTR strFilePath);
	void Release();

private:
	int mWidth, mHeight;
	BYTE* mpPixels;
};
