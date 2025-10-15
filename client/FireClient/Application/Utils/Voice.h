#ifndef VOICE_H
#define VOICE_H
#include <vector>
#include <string>

/////////////////////////////////////begin/////////////////////////////////////
///////////////////////////////////wav2speex///////////////////////////////////
void voice_encode_init();
int voice_encode(short* in, int size, char* encoded, int max_buffer_size);
std::vector<char> encodeWAV2SPEEX(std::vector<short>& datas);
void voice_encode_release();
///////////////////////////////////speex2wav///////////////////////////////////
void voice_decode_init();
int voice_decode(char* encoded, int size, short* output, int max_buffer_size);
std::vector<short>  decodeSPEEX2WAV(std::string& strData);
void voice_decode_release();

///////////////////////////////////wav2amr///////////////////////////////////
// WAVE音频采样频率是8khz 
// 音频样本单元数 = 8000*0.02 = 160 (由采样频率决定)
// 声道数 1 : 160
//        2 : 160*2 = 320
// bps决定样本(sample)大小
// bps = 8 --> 8位 unsigned char
//       16 --> 16位 unsigned short
int encodeWAVE2AMR(const char* pchWAVEFilename, const char* pchAMRFileName, int nChannels, int nBitsPerSample);

///////////////////////////////////amr2wav///////////////////////////////////
// 将AMR文件解码成WAVE文件
int decodeAMR2WAV(const char* pchAMRFileName, const char* pchWAVEFilename);

// 获得ARM文件的总帧数
int getAMRTotalFrames(const char* pchAMRFileName);
/////////////////////////////////////end/////////////////////////////////////

#endif //define VOICE_H
