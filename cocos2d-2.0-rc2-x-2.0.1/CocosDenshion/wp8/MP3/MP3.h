#include "pch.h"
#include "Mp3Types.h"
#include <vector>
static ReaderConfig global_reader = { sf_unknown, 0, 0, 0 };
static DecoderConfig global_decoder;
static get_audio_global_data global;
int lame_decoder(const byte* mp3Stream, size_t intSize, std::vector<byte>* wavStream);