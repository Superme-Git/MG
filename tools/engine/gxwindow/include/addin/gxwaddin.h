//
// gxwindow��չ���ͷ�ļ�, ʹ�ÿ�ʱֻ�������ͷ�ļ�
//
#pragma once


// �����ʹ��pfs, ��򿪸ú�
// Ŀǰ��ʱ���xmltextdecoder��Generator/xmlioloader
// �Ժ��������ʱ����ͬʱ֧������ģʽ
// #define USE_NAIVEFILE



#include "../gxwindow.h"

#include "dbclick.h"
#include "Generator/uiloader.h"
#include "Generator/xmlioloader.h"
#include "qqbar.h"

#include "inc/tableex.h"
#include "inc/textdecoder.h"

#ifdef USE_NAIVEFILE
#include "xmltextdecoder.h"
#else
#include "xmlio/xmltextdecoder.h"
#endif