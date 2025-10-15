#include <iostream>
#include "../Include/LJXMLHelper.hpp"

int main(int argc, char **argv)
{
	std::locale::global(std::locale(""));
	LJXML::LJXML_Doc<> doc;
	doc.LoadFromFile(L"1.xml");
	doc.SaveToFile(L"1B.xml");
	LJXMLHelper::OutPutAllNode(&doc);//Êä³öXMLµÄNodeÊ÷
	system("PAUSE");
}