//  CEGUIIosBuildInParser.cpp
//  CEGUI

#include "CEGUIIosBuildInParser.h"
#include <CEGUIXMLHandler.h>
#include <CEGUIXMLAttributes.h>
#include <CEGUI.h>

#import <Foundation/NSXMLParser.h>
//namespace XMLIOHelper
//{
//    inline std::wstring GUIStringToWString(const CEGUI::String& cs)
//    {
//        std::wstring ret(cs.length(),0);
//        for(CEGUI::String::size_type i=0;i!=cs.length();++i){
//            ret[i]=cs[i];
//        }
//        return ret;
//    }
//    inline CEGUI::String WStringToGUIString(const std::wstring& wstr)
//    {
//        int len = WideCharToMultiByte(CP_UTF8, 0, (wchar_t*)wstr.c_str(), -1,
//                                      NULL, NULL, NULL, NULL);
//        
//        char* szBuffer = new char[len];
//        WideCharToMultiByte(CP_UTF8, 0, (wchar_t*)wstr.c_str(), -1,
//                            szBuffer, len, NULL, NULL);
//        
//        CEGUI::String str((CEGUI::utf8*)szBuffer);
//        delete[] szBuffer;
//        return str;
//    }
//    
//}

@interface PWXMLParserDelegate : NSObject<NSXMLParserDelegate> {
    CEGUI::XMLHandler* m_handler;
}

- (id) initWithHandler:(CEGUI::XMLHandler*) handler;
@end

@implementation PWXMLParserDelegate

- (id) initWithHandler:(CEGUI::XMLHandler*) handler
{
    m_handler = handler;
    return self;
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict
{
    //NSLog(@"begin:%@", elementName);
    CEGUI::XMLAttributes attrs;
    
    for (NSString* pKey in attributeDict) {
        NSString* pValue = [attributeDict objectForKey:pKey];
        
//        NSData* pND = [pKey dataUsingEncoding:NSUTF16LittleEndianStringEncoding];
//        NSData* pVD = [pValue dataUsingEncoding:NSUTF16LittleEndianStringEncoding];
//        char* pNameStr = new char[ [pND length] + 1];
//        char* pValueStr = new char[ [pVD length] + 1];
//        memset(pNameStr, 0, [pND length]+1);
//        memset(pValueStr, 0, [pVD length]+1);
//        [pND getBytes:pNameStr];
//        [pVD getBytes:pValueStr];
//        attrs.add(pNameStr, pValueStr);
        attrs.add((unsigned char*)[pKey UTF8String], (unsigned char*)[pValue UTF8String]);
    }
    
    m_handler->elementStart((unsigned char*)[elementName UTF8String], attrs);
}

- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName
{
    //NSLog(@"end:%@", elementName);
    m_handler->elementEnd((unsigned char*)[elementName UTF8String]);
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string
{
    //NSLog(@"string:%@", string);
    m_handler->text((unsigned char*)[string UTF8String]);
}
@end

namespace CEGUI {

    IosBuildInParser::IosBuildInParser(void)
    {
        d_identifierString = "CEGUI::IosBuildInParser - LocoJoy xml parser module";
    }
    
    IosBuildInParser::~IosBuildInParser(void)
    {
    }
    
    void IosBuildInParser::parseXMLFile(XMLHandler& handler, const String& filename, const String& schemaName, const String& resourceGroup)
    {
        //TODO:处理路径
        CEGUI::DefaultResourceProvider* rp = static_cast<CEGUI::DefaultResourceProvider*>(CEGUI::System::getSingleton().getResourceProvider());        
        
        String finnalPath = rp->getResourceGroupDirectory(resourceGroup) + filename;
        NSString* nsFileName = [NSString stringWithUTF8String:finnalPath.c_str()];
        
        NSURL* url = [NSURL fileURLWithPath:nsFileName];
       
        NSXMLParser* parser = [[NSXMLParser alloc] initWithContentsOfURL:url];        
        PWXMLParserDelegate* del = [[PWXMLParserDelegate alloc] initWithHandler:&handler];
        
        [parser setDelegate:del];
        //[parser setShouldProcessNamespace:NO];
        [parser setShouldReportNamespacePrefixes:NO];
        [parser setShouldResolveExternalEntities:NO];
        [parser parse];
        
        NSError* parserError = [parser parserError] ;
        NSCAssert1(parserError == nil, @"fail to parser xml file:%@", nsFileName);
        
        [parser release];
    }
    
    void IosBuildInParser::parseXMLContent(XMLHandler& handler,const String& content)
    {
        //NSCAssert(false, @"parseXMLContent not implement");
        NSData* data = [NSData dataWithBytes:content.c_str() length:content.size()*2];
        NSXMLParser* parser = [[NSXMLParser alloc] initWithData:data];
        PWXMLParserDelegate* del = [[PWXMLParserDelegate alloc] initWithHandler:&handler];
        
        [parser setDelegate:del];
        //[parser setShouldProcessNamespace:NO];
        [parser setShouldReportNamespacePrefixes:NO];
        [parser setShouldResolveExternalEntities:NO];
        [parser parse];
        
//        NSError* parserError = [parser parserError] ;
        
        [parser release];

    }
    
    bool IosBuildInParser::initialiseImpl(void)
    {
        return true;
    }
    
    void IosBuildInParser::cleanupImpl(void)
    {
        
    }
}//end of namespace CEGUI