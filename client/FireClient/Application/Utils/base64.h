#ifndef ___BASE64_H___  
#define ___BASE64_H___  

#include <string>
using namespace std;

namespace Base64
{
    string Encode(const unsigned char* Data, int DataByte);
    string Decode(const char* Data, int DataByte, int& OutByte);
	string YYEncode(const unsigned char* Data, int DataByte);
	string YYDecode(const char* Data, int DataByte, int& OutByte);
};

#endif // ___BASE64_H___  

