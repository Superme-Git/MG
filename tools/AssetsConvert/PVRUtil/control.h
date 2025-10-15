#pragma once

#include "filesystem.h"
#include <functional>
template<char ch0, char ch1, char ch2, char ch3>
struct FourCC
{
	enum { value = ( (ch0 << 0) + (ch1 << 8) + (ch2 << 16) + (ch3 << 24) ) };
};

extern bool isExistInExcludePaths(const std::wstring& path);

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
		EPreparing,
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

	enum Convert2
	{
		CT_PVR = 1 << 0,
		CT_ATITC = 1 << 1,
		CT_DXT1 = 1 << 2,
		CT_ETC1 = 1 << 3
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
	void AddPvrMessage( const std::wstring& msg );
	void AddATITCMessage( const std::wstring& msg );
	void AddDXT1Message( const std::wstring& msg );
	void AddETC1Message( const std::wstring& msg );

	typedef void (*AddMsgFunc)(const std::wstring& msg);

	message GetMessage(int idx);
	message FetchFirstMessage();
	std::wstring FetchPvrFirstMessage();
	std::wstring FetchATITCFirstMessage();
	std::wstring FetchDXT1FirstMessage();
	std::wstring FetchETC1FirstMessage();

	int GetMessageCount();
	void ClearMessages();
	volatile e_status m_status;
	volatile e_status m_pvr_convert_status;
	volatile e_status m_atitc_convert_status;
	volatile e_status m_dxt1_convert_status;
	volatile e_status m_etc1_convert_status;
	volatile bool	m_terminate_converting;
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
	unsigned int m_converted_files;
	unsigned int m_need_convert_files;
	DWORD m_convert2;
	std::wstring m_pvr_output_dir;
	std::wstring m_dxt1_output_dir;
	std::wstring m_atitc_output_dir;
	std::wstring m_etc1_output_dir;
	
	std::vector<dir::item*> m_in_dir_items;

	std::vector<std::wstring> m_pvr_message_queue;
	std::vector<std::wstring> m_atitc_message_queue;
	std::vector<std::wstring> m_dxt1_message_queue;
	std::vector<std::wstring> m_etc1_message_queue;

	std::ofstream m_pvr_msg_log;
	std::ofstream m_atitc_msg_log;
	std::ofstream m_dxt1_msg_log;
	std::ofstream m_etc1_msg_log;

	std::function<void (const std::wstring&)> m_MsgFunc;

	float rescale;
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


UINT convert2pvr( LPVOID pArg );
bool __convert2pvr( const std::wstring& quality, const std::wstring& mipmaps, const std::wstring& input_file );
bool __convert2pvr( const std::wstring& quality, const std::wstring& mipmaps, const dir::item& input_item );

UINT convert2atitc( LPVOID pArg );
bool __convert2atitc( const std::wstring& mipmaps, const std::wstring& input_file, bool has_alpha );
bool __convert2atitc( const std::wstring& mipmaps, const dir::item& input_item, bool has_alpha  );

UINT convert2dxt1( LPVOID pArg );
bool __convert2dxt1( const std::wstring& mipmaps, const std::wstring& input_file, bool has_alpha );
bool __convert2dxt1( const std::wstring& mipmaps, const dir::item& input_item, bool has_alpha );

UINT convert2etc1( LPVOID pArg );
bool __convert2etc1( bool mipmaps, const std::wstring& input_file, const std::wstring& output_dir, bool has_alpha );
bool __convert2etc1( bool mipmaps, const dir::item& input_item, const std::wstring& output_dir, bool has_alpha );


bool copy_entier_directory( const std::wstring& src_dir, const std::wstring& dst_dir, void(control::*func)( const std::wstring& )  );
bool copy_entier_directory_mt( const std::wstring& src_dir, const std::wstring& dst_dir, const std::tr1::function<void (const std::wstring&)>& _func );
