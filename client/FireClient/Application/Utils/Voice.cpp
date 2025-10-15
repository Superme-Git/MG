#include <cstring>
#include <cstdio>
#include <stdlib.h>
#ifdef WIN32 
#include <memory.h >
#endif
#include "voice.h"
#include <speex/speex.h>
#include "platform/CCPlatformConfig.h"

///////////////////////////////////wav & speex///////////////////////////////////
//#define JIANGZAO
#ifdef JIANGZAO
#include <speex/speex_preprocess.h>
#endif

// 音质
#define MYQUALITY 8;
// 宽带模式 + 音质8 : 对应的压缩后长度 70
#define COMPRESS_SIZE 70
// 开启知觉增强
#define ENH 1

static int enc_frame_size;
static int dec_frame_size;

static void *enc_state;
static SpeexBits ebits;
static bool is_enc_init = false;

#ifdef JIANGZAO
static SpeexPreprocessState* enc_preprocess_state;
#endif

static void *dec_state;
static SpeexBits dbits;
static bool is_dec_init = false;

void voice_encode_init()
{
	int quality = MYQUALITY;

	enc_state = speex_encoder_init(&speex_wb_mode);
	speex_encoder_ctl(enc_state, SPEEX_SET_QUALITY, &quality);
	speex_encoder_ctl(enc_state, SPEEX_GET_FRAME_SIZE, &enc_frame_size);
    speex_bits_init(&ebits);
	is_enc_init = true;

#ifdef JIANGZAO
	enc_preprocess_state = speex_preprocess_state_init(16, 8000);
	int denoise = 1;
	speex_preprocess_ctl(enc_preprocess_state, SPEEX_PREPROCESS_SET_DENOISE, &denoise);
	int noiseSuppress = -25;
	speex_preprocess_ctl(enc_preprocess_state, SPEEX_PREPROCESS_SET_NOISE_SUPPRESS, &noiseSuppress);
#endif
}

int voice_encode(short* in, int size, char* encoded, int max_buffer_size)
{
	if (!is_enc_init)
	{
		voice_encode_init();
	}

#ifdef JIANGZAO
	speex_preprocess_run(enc_preprocess_state, in);
#endif

	speex_bits_reset(&ebits);
	speex_encode_int(enc_state, in, &ebits);
	int nbBytes = speex_bits_write(&ebits, encoded, max_buffer_size);

	return nbBytes;
}

std::vector<char> encodeWAV2SPEEX(std::vector<short>& datas)
{
	std::vector<char> outDatas;

	voice_encode_init();

	if (enc_frame_size <= 0)
	{
		return outDatas;
	}

	for (size_t i = 0; i < datas.size(); i += enc_frame_size)
	{
		while (datas.size() < i + enc_frame_size)
		{
			datas.push_back(0);
		}

		char encoded[2024];
		memset(encoded, 0, enc_frame_size);
		size_t encoded_count = voice_encode((short*)(&datas[i]), enc_frame_size, encoded, enc_frame_size);

		// add 1 byte frame header
		outDatas.push_back(encoded_count);

		for (size_t j = 0; j < encoded_count; j++)
		{
			outDatas.push_back(encoded[j]);
		}
	}

	voice_encode_release();

	return outDatas;
}

void voice_encode_release()
{
    speex_bits_destroy(&ebits);
    speex_encoder_destroy(enc_state);
	is_enc_init = false;

#ifdef JIANGZAO
	speex_preprocess_state_destroy(enc_preprocess_state);
#endif
}


void voice_decode_init()
{
	int enh = ENH;

    speex_bits_init(&dbits);
    dec_state = speex_decoder_init(&speex_wb_mode);
    speex_decoder_ctl(dec_state, SPEEX_GET_FRAME_SIZE, &dec_frame_size);
	speex_decoder_ctl(dec_state, SPEEX_SET_ENH, &enh);
	is_dec_init = true;

#ifdef JIANGZAO
	enc_preprocess_state = speex_preprocess_state_init(16,11025);
	int denoise = 10;
	speex_preprocess_ctl(enc_preprocess_state, SPEEX_PREPROCESS_SET_DENOISE, &denoise);
	int noiseSuppress = -25;
	speex_preprocess_ctl(enc_preprocess_state, SPEEX_PREPROCESS_SET_NOISE_SUPPRESS, &noiseSuppress);
#endif
}

int voice_decode(char* encoded, int size, short* output, int max_buffer_size)
{
	if (!is_dec_init)
	{
		voice_decode_init();
	}

	speex_bits_reset(&dbits);
	speex_bits_read_from(&dbits, encoded, size);
	speex_decode_int(dec_state, &dbits, output);

	return dec_frame_size;
}

std::vector<short>  decodeSPEEX2WAV(std::string& strData)
{
	std::vector<short> datas;

	voice_decode_init();

	// skip 1 byte frame header
	for (size_t i = 0; i < strData.size(); i += 1 + COMPRESS_SIZE)
	{
		size_t srcSize = COMPRESS_SIZE;
		if (i + 1 + COMPRESS_SIZE > strData.size())
			srcSize = strData.size() - (i + 1);

		short decoded[1024];
		size_t decoded_count = voice_decode(&strData[i + 1], srcSize, decoded, dec_frame_size);
		for (size_t j = 0; j < decoded_count; j++)
		{
			datas.push_back(decoded[j]);
		}
	}

	voice_decode_release();

	return datas;
}

void voice_decode_release()
{
	speex_bits_destroy(&dbits);
	speex_decoder_destroy(dec_state);
	is_dec_init = false;

#ifdef JIANGZAO
	speex_preprocess_state_destroy(enc_preprocess_state);
#endif
}

///////////////////////////////////wav & amr///////////////////////////////////
#include <stdio.h>
#include "Amr/amr_enc.h"
#include "Amr/amr_dec.h"

#define Amr_Magic_Number "#!AMR\n"
#define pcm_frame_size 160 // 8khz 8000*0.02=160
#define Max_Amr_Frame_Size 32
#define Amr_Frame_Count_Per_Second 50
int AMREncodeMode[] = {4750, 5150, 5900, 6700, 7400, 7950, 10200, 12200}; // amr 编码方式

typedef struct
{
	char chChunkID[4];
	int nChunkSize;
}XchunkHeader;

typedef struct
{
	short nFormatTag;
	short nChannels;
	int nSamplesPerSec;
	int nAvgBytesPerSec;
	short nBlockAlign;
	short nBitsPerSample;
}WaveFormat;

typedef struct
{
	short nFormatTag;
	short nChannels;
	int nSamplesPerSec;
	int nAvgBytesPerSec;
	short nBlockAlign;
	short nBitsPerSample;
	short nExSize;
}WaveFormatX;

typedef struct
{
	char chRiffID[4];
	int nRiffSize;
	char chRiffFormat[4];
}RiffHeader;

typedef struct
{
	char chFmtID[4];
	int nFmtSize;
	WaveFormat wf;
}FmtBlock;


class Buffer
{
private:
	unsigned char* mpBuffer;
	int mMaxSize;
	int mPos;

public:
	Buffer(void* pBuffer, int maxSize);

	unsigned char* getBuffer() const { return mpBuffer; }
	bool hasMoreData() const { return mPos < mMaxSize; }

	int read(void* pDstBuffer, int eSize, int eCount);
	int read(Buffer& dst, int eSize, int eCount);

	int write(const void* pSrcBuffer, int eSize, int eCount);
	int write(Buffer& src, int eSize, int eCount);

private:
	int processSizeCount(int& eSize, int& eCount);
};

// 从WAVE文件中跳过WAVE文件头，直接到PCM音频数据
void skipToPCMAudioData(FILE* fpwave)
{
	RiffHeader riff;
	FmtBlock fmt;
	XchunkHeader chunk;
	WaveFormatX wfx;
	int bDataBlock = 0;

	// 1. 读RIFF头
	fread(&riff, 1, sizeof(RiffHeader), fpwave);

	// 2. 读FMT块 - 如果 fmt.nFmtSize>16 说明需要还有一个附属大小没有读
	fread(&chunk, 1, sizeof(XchunkHeader), fpwave);
	if (chunk.nChunkSize > 16)
	{
		fread(&wfx, 1, sizeof(WaveFormatX), fpwave);
	}
	else
	{
		memcpy(fmt.chFmtID, chunk.chChunkID, 4);
		fmt.nFmtSize = chunk.nChunkSize;
		fread(&fmt.wf, 1, sizeof(WaveFormat), fpwave);
	}

	// 3.转到data块 - 有些还有fact块等。
	while (!bDataBlock)
	{
		fread(&chunk, 1, sizeof(XchunkHeader), fpwave);
		if (!memcmp(chunk.chChunkID, "data", 4))
		{
			bDataBlock = 1;
			break;
		}
		// 因为这个不是data块,就跳过块数据
		fseek(fpwave, chunk.nChunkSize, SEEK_CUR);

		// 循环保护代码
		if (feof(fpwave))
		{
			break;
		}
	}
}

// skip wave header when read wave data
int skipWaveHeader(FILE* fp)
{
	fseek(fp, 40, 0);
	unsigned int dataLength;
	fread(&dataLength, 4, 1, fp);

	return dataLength;
}

// skip caff header when read caff data
int skipCaffHander(FILE* fp)
{
	fseek(fp, 0, 2);
	int dataLength = ftell(fp) - 4092;
	fseek(fp, 4092, 0);
	return dataLength;
}

// 从WAVE文件读一个完整的PCM音频帧
// 返回值: 0-错误 >0: 完整帧大小
int readPCMFrame(short speech[], FILE* fpwave, int nChannels, int nBitsPerSample)
{
	int nRead = 0;
	int x = 0, y = 0;
	//	unsigned short ush1=0, ush2=0, ush=0;

	// 原始PCM音频帧数据
	unsigned char  pcmFrame_8b1[pcm_frame_size];
	unsigned char  pcmFrame_8b2[pcm_frame_size << 1];
	unsigned short pcmFrame_16b1[pcm_frame_size];
	unsigned short pcmFrame_16b2[pcm_frame_size << 1];

	if (nBitsPerSample == 8 && nChannels == 1)
	{

		nRead = fread(pcmFrame_8b1, (nBitsPerSample / 8), pcm_frame_size*nChannels, fpwave);

		for (x = 0; x < pcm_frame_size; x++)
		{
			speech[x] = (short)((short)pcmFrame_8b1[x] << 7);
		}
	}
	else
		if (nBitsPerSample == 8 && nChannels == 2)
		{
			nRead = fread(pcmFrame_8b2, (nBitsPerSample / 8), pcm_frame_size*nChannels, fpwave);
			for (x = 0, y = 0; y < pcm_frame_size; y++, x += 2)
			{
				// 1 - 取两个声道之左声道
				speech[y] = (short)((short)pcmFrame_8b2[x + 0] << 7);
				// 2 - 取两个声道之右声道
				//speech[y] =(short)((short)pcmFrame_8b2[x+1] << 7);
				// 3 - 取两个声道的平均值
				//ush1 = (short)pcmFrame_8b2[x+0];
				//ush2 = (short)pcmFrame_8b2[x+1];
				//ush = (ush1 + ush2) >> 1;
				//speech[y] = (short)((short)ush << 7);
			}
		}
		else
			if (nBitsPerSample == 16 && nChannels == 1)
			{
				nRead = fread(pcmFrame_16b1, (nBitsPerSample / 8), pcm_frame_size*nChannels, fpwave);
				for (x = 0; x < pcm_frame_size; x++)
				{
					speech[x] = (short)pcmFrame_16b1[x + 0];
				}
			}
			else
				if (nBitsPerSample == 16 && nChannels == 2)
				{
					nRead = fread(pcmFrame_16b2, (nBitsPerSample / 8), pcm_frame_size*nChannels, fpwave);
					for (x = 0, y = 0; y < pcm_frame_size; y++, x += 2)
					{
						//speech[y] = (short)pcmFrame_16b2[x+0];
						speech[y] = (short)((int)((int)pcmFrame_16b2[x + 0] + (int)pcmFrame_16b2[x + 1])) >> 1;
					}
				}

	// 如果读到的数据不是一个完整的PCM帧, 就返回0
	if (nRead < pcm_frame_size*nChannels) return 0;

	return nRead;
}


// WAVE音频采样频率是8khz 
// 音频样本单元数 = 8000*0.02 = 160 (由采样频率决定)
// 声道数 1 : 160
//        2 : 160*2 = 320
// bps决定样本(sample)大小
// bps = 8 --> 8位 unsigned char
//       16 --> 16位 unsigned short
int encodeWAVE2AMR(const char* pchWAVEFilename, const char* pchAMRFileName, int nChannels, int nBitsPerSample)
{
	FILE* fpwave;
	FILE* fpamr;

	/* input speech vector */
	short speech[160];

	/* counters */
	int byte_counter, frames = 0, bytes = 0;

	/* pointer to encoder state structure */
	void *enstate;

	/* requested mode */
	enum Mode req_mode = MR515;
	int dtx = 0;

	/* bitstream filetype */
	unsigned char amrFrame[Max_Amr_Frame_Size];

	fpwave = fopen(pchWAVEFilename, "rb");
	if (fpwave == NULL)	return 0;

	// 创建并初始化amr文件
	fpamr = fopen(pchAMRFileName, "wb");
	if (fpamr == NULL)	return 0;

	/* write magic number to indicate single channel AMR file storage format */
	bytes = fwrite(Amr_Magic_Number, sizeof(char), strlen(Amr_Magic_Number), fpamr);

	/* skip to pcm audio data*/
	//skipToPCMAudioData(fpwave);
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	skipWaveHeader(fpwave);
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	skipCaffHander(fpwave);
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	skipWaveHeader(fpwave);
#else
	skipToPCMAudioData(fpwave);
#endif

	enstate = encoder_interface_init(dtx);

	while (1)
	{
		// read one pcm frame
		if (!readPCMFrame(speech, fpwave, nChannels, nBitsPerSample)) break;

		frames++;

		/* call encoder */
		byte_counter = encoder_interface_encode(enstate, req_mode, speech, amrFrame, 0);

		bytes += byte_counter;
		fwrite(amrFrame, sizeof(unsigned char), byte_counter, fpamr);
	}

	encoder_interface_exit(enstate);

	fclose(fpamr);
	fclose(fpwave);

	return frames;
}


void writeWAVEFileHeader(FILE* fpwave, int nFrame)
{
	char tag[10] = "";

	// 1. 写RIFF头
	RiffHeader riff;
	strcpy(tag, "RIFF");
	memcpy(riff.chRiffID, tag, 4);
	riff.nRiffSize = 4                                     // WAVE
		+ sizeof(XchunkHeader)               // fmt 
		+ sizeof(WaveFormatX)           // WaveFormatX
		+ sizeof(XchunkHeader)               // DATA
		+ nFrame * 160 * sizeof(short);    //
	strcpy(tag, "WAVE");
	memcpy(riff.chRiffFormat, tag, 4);
	fwrite(&riff, 1, sizeof(RiffHeader), fpwave);

	// 2. 写FMT块
	XchunkHeader chunk;
	WaveFormatX wfx;
	strcpy(tag, "fmt ");
	memcpy(chunk.chChunkID, tag, 4);
	chunk.nChunkSize = sizeof(WaveFormatX);
	fwrite(&chunk, 1, sizeof(XchunkHeader), fpwave);
	memset(&wfx, 0, sizeof(WaveFormatX));
	wfx.nFormatTag = 1;
	wfx.nChannels = 1; // 单声道
	wfx.nSamplesPerSec = 8000; // 8khz
	wfx.nAvgBytesPerSec = 16000;
	wfx.nBlockAlign = 2;
	wfx.nBitsPerSample = 16; // 16位
	fwrite(&wfx, 1, sizeof(WaveFormatX), fpwave);

	// 3. 写data块头
	strcpy(tag, "data");
	memcpy(chunk.chChunkID, tag, 4);
	chunk.nChunkSize = nFrame * 160 * sizeof(short);
	fwrite(&chunk, 1, sizeof(XchunkHeader), fpwave);
}

const int myRound(const double x)
{
	return((int)(x + 0.5));
}

// 根据帧头计算当前帧大小
int caclAmrFrameSize(unsigned char frameHeader)
{
	int mode;
	int temp1 = 0;
	int temp2 = 0;
	int frameSize;

	temp1 = frameHeader;

	// 编码方式编号 = 帧头的3-6位
	temp1 &= 0x78; // 0111-1000
	temp1 >>= 3;

	mode = AMREncodeMode[temp1];

	// 计算amr音频数据帧大小
	// 原理: amr 一帧对应20ms，那么一秒有50帧的音频数据
	temp2 = myRound((double)(((double)mode / (double)Amr_Frame_Count_Per_Second) / (double)8));

	frameSize = myRound((double)temp2 + 0.5);
	return frameSize;
}

// 读第一个帧 - (参考帧)
// 返回值: 0-出错; 1-正确
int readAMRFrameFirst(FILE* fpamr, unsigned char frameBuffer[], int* stdFrameSize, unsigned char* stdFrameHeader)
{
	// 先读帧头
	fread(stdFrameHeader, 1, sizeof(unsigned char), fpamr);
	if (feof(fpamr)) return 0;

	// 根据帧头计算帧大小
	*stdFrameSize = caclAmrFrameSize(*stdFrameHeader);

	// 读首帧
	frameBuffer[0] = *stdFrameHeader;
	fread(&(frameBuffer[1]), 1, (*stdFrameSize - 1)*sizeof(unsigned char), fpamr);
	if (feof(fpamr)) return 0;

	return 1;
}

// 返回值: 0-出错; 1-正确
int readAMRFrame(FILE* fpamr, unsigned char frameBuffer[], int stdFrameSize, unsigned char stdFrameHeader)
{
	int bytes = 0;
	unsigned char frameHeader; // 帧头
	memset(frameBuffer, 0, sizeof(frameBuffer));

	// 读帧头
	// 如果是坏帧(不是标准帧头)，则继续读下一个字节，直到读到标准帧头
	while (1)
	{
		bytes = fread(&frameHeader, 1, sizeof(unsigned char), fpamr);
		if (feof(fpamr)) return 0;
		if (frameHeader == stdFrameHeader) break;
	}

	// 读该帧的语音数据(帧头已经读过)
	frameBuffer[0] = frameHeader;
	bytes = fread(&(frameBuffer[1]), 1, (stdFrameSize - 1)*sizeof(unsigned char), fpamr);
	if (feof(fpamr)) return 0;

	return 1;
}

// 将Wav音量放大到不失真的最大（噪音也同时放大）
std::vector<short> amplifyWavVolume(const std::vector<short>& datas)
{
	std::vector<short> amplifiedData;
	std::vector<short>::const_iterator it;

	// 统计最大振幅
	short maxAmplitude = 1;
	for (it = datas.begin(); it != datas.end(); ++it)
	{
		short curAmplitude = *it;
		if (curAmplitude > maxAmplitude) {
			maxAmplitude = curAmplitude;
		}
	}

	// 计算不失真的最大放大倍数
	float multiple = 0x7fff / (float)maxAmplitude;

//#if CC_TARGET_PLATFORM != CC_PLATFORM_WIN32
	multiple -= 0.6f;
	multiple = multiple < 1.0f ? 1.0f : multiple;
	multiple = multiple > 1.8f ? 1.8f : multiple;
//#endif

	// 将振幅统一放大
	for (it = datas.begin(); it != datas.end(); ++it)
	{
		short resultAmplitude = (*it) * multiple;
		if (resultAmplitude > 0x7fff) {
			resultAmplitude = 0x7fff;
		}
		amplifiedData.push_back(resultAmplitude);
	}

	return amplifiedData;
}

// 将AMR文件解码成WAVE文件
int decodeAMR2WAV(const char* pchAMRFileName, const char* pchWAVEFilename)
{
	FILE* fpamr = NULL;
	FILE* fpwave = NULL;
	char magic[8] = {0};
	void * destate;
	int nFrameCount = 0;
	int stdFrameSize;
	unsigned char stdFrameHeader;

	unsigned char amrFrame[Max_Amr_Frame_Size];
	short pcmFrame[pcm_frame_size];

	fpamr = fopen(pchAMRFileName, "rb");
	if (fpamr == NULL)	return 0;

	// 检查amr文件头
	fread(magic, sizeof(char), strlen(Amr_Magic_Number), fpamr);
	if (strncmp(magic, Amr_Magic_Number, strlen(Amr_Magic_Number)))
	{
		fclose(fpamr);
		return 0;
	}

	fpwave = fopen(pchWAVEFilename, "wb");
	if (fpwave == NULL)	return 0;

	writeWAVEFileHeader(fpwave, nFrameCount);

	// 记录所有的Wav音频数据
	std::vector<short> datas;

	/* init decoder */
	destate = decoder_interface_init();

	// 读第一帧 - 作为参考帧
	memset(amrFrame, 0, sizeof(amrFrame));
	memset(pcmFrame, 0, sizeof(pcmFrame));
	readAMRFrameFirst(fpamr, amrFrame, &stdFrameSize, &stdFrameHeader);

	// 解码一个AMR音频帧成PCM数据
	decoder_interface_decode(destate, amrFrame, pcmFrame, 0);
	nFrameCount++;

	//fwrite(pcmFrame, sizeof(short), pcm_frame_size, fpwave);
	for (int i = 0; i < pcm_frame_size; i++) {
		datas.push_back(pcmFrame[i]);
	}

	// 逐帧解码AMR并写到WAVE文件里
	while (1)
	{
		memset(amrFrame, 0, sizeof(amrFrame));
		memset(pcmFrame, 0, sizeof(pcmFrame));
		if (!readAMRFrame(fpamr, amrFrame, stdFrameSize, stdFrameHeader)) break;

		// 解码一个AMR音频帧成PCM数据 (8k-16b-单声道)
		decoder_interface_decode(destate, amrFrame, pcmFrame, 0);
		nFrameCount++;

		//fwrite(pcmFrame, sizeof(short), pcm_frame_size, fpwave);
		for (int i = 0; i < pcm_frame_size; i++) {
			datas.push_back(pcmFrame[i]);
		}
	}

	// 将Wav音量放大到不失真的最大（噪音也同时放大）
	std::vector<short> amplifiedData = amplifyWavVolume(datas);

	// 写入文件
	for (size_t i = 0; i < amplifiedData.size(); i++) {
		fwrite(&(amplifiedData[i]), sizeof(short), 1, fpwave);
	}

	decoder_interface_exit(destate);

	fseek(fpwave, 0, SEEK_SET);
	writeWAVEFileHeader(fpwave, nFrameCount);
	fclose(fpwave);

	return nFrameCount;
}

int getAMRTotalFrames(const char* pchAMRFileName)
{
	FILE *fpamr = fopen(pchAMRFileName, "rb");
	if (fpamr == NULL)	return 0;

	int stdFrameSize;
	unsigned char stdFrameHeader;
	unsigned char amrFrame[Max_Amr_Frame_Size];
	memset(amrFrame, 0, sizeof(amrFrame));
	readAMRFrameFirst(fpamr, amrFrame, &stdFrameSize, &stdFrameHeader);

	unsigned long nSize = 0;
	fseek(fpamr, 0, SEEK_END);
	nSize = ftell(fpamr);
	fseek(fpamr, 0, SEEK_SET);

	int nFrameCount = nSize / stdFrameSize;
	fclose(fpamr);

	return nFrameCount;
}
