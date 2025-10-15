#pragma once

template<char ch0, char ch1, char ch2, char ch3>
struct FourCC
{
	enum { value = ( (ch0 << 0) + (ch1 << 8) + (ch2 << 16) + (ch3 << 24) ) };
};

class control
{
	
public:
	control(void);
	~control(void);

	enum{
		IOS_PVR = 1,
		ANDROID_ATC = 2,
		ANDROID_DDS = 4,
		COMMON_ETC = 8,
	};

	enum e_status
	{
		ENone,
		EProcessing,
	};

	enum TargetFormat
	{
		TF_PVR,
		TF_ETC1_RGB8,
		TF_ETC1_RGBA8_AA,
		TF_ETC1_RGBA8_AS,
		TF_ATITC,
		TF_DXT1,
		TF_None
	};

	enum OutputExt
	{
		OE_PVR, // .pvr
		OE_ORIG, // orig name
		OE_End
	};

	void begin(wchar_t* d);
	void abort();
	void begin_convert();
	struct message
	{
		enum message_type{info, error, window};
		message_type msgtype;
		std::wstring msg;
	};

	std::wstring m_strWindowText;
	std::vector<message> m_vMessages;
	std::vector<message> m_vDM;

	std::wstring GetWindowText();
	void AddMessage(const wchar_t* amessage, message::message_type type = message::message_type::info);
	message GetMessage(int idx);
	message FetchFirstMessage();

	int GetMessageCount();
	void ClearMessages();
	volatile e_status m_status;
	std::wstring m_artresDir;
	std::wstring m_targetDir;

	int m_iMode;
	bool m_bSprite;
	bool m_bEffect;
	bool m_bMap;
	bool m_bUI;

	bool m_genMipmaps;
	bool m_dxt1Alpha;
	std::wstring m_inputDir;
	std::wstring m_outputDir;
	TargetFormat m_targetFormat;
	HANDLE m_CompleteEvent;

	bool m_bPvrQualityFast;
	bool m_bPvrQualityNormal;
	bool m_bPvrQualityHigh;
	bool m_bPvrQualityBest;
	bool m_atitc_rgb;
	bool m_atitc_rgba_interpolated;
	bool m_atitc_rgba_explicit;
	bool m_mergeEtc;
	OutputExt m_output_ext;
};

typedef struct PVR_Header_Texture_TAG
{
	unsigned int dwHeaderSize;			/*!< size of the structure */
	unsigned int dwHeight;				/*!< height of surface to be created */
	unsigned int dwWidth;				/*!< width of input surface */
	unsigned int dwMipMapCount;			/*!< number of mip-map levels requested */


	unsigned int dwpfFlags;				/*!< pixel format flags */
	unsigned int dwTextureDataSize;		/*!< Total size in bytes */
	unsigned int dwBitCount;			/*!< number of bits per pixel  */
	unsigned int dwRBitMask;			/*!< mask for red bit */

	unsigned int dwGBitMask;			/*!< mask for green bits */
	unsigned int dwBBitMask;			/*!< mask for blue bits */
	unsigned int dwAlphaBitMask;		/*!< mask for alpha channel */
	unsigned int dwPVR;					/*!< magic number identifying pvr file */
	unsigned int dwNumSurfs;			/*!< the number of surfaces present in the pvr */
} PVR_Texture_Header;

extern control g_control;
void mySystem(const wchar_t* cmd, const wchar_t* arg);
std::wstring mapSrc2Dst(std::wstring src, int mode);


int convert_to_pvr();
int convert_to_etc1();
int convert_to_atitc();
int convert_to_dxt1();
int convert_to_etc1_rgba_aa();
int convert_to_etc1_rgba_as();
