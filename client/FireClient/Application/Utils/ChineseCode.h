#ifndef __FireClient__ChineseCode__
#define __FireClient__ChineseCode__


class ChineseCode {
public:
    static const char* getConvertCode(const char* code);
private:
    static const char codestable[][4];
};

#endif /* defined(__FireClient__ChineseCode__) */
