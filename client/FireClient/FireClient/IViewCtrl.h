//  IViewCtrl.h
//  FireClient

#ifndef FireClient_IViewCtrl_h
#define FireClient_IViewCtrl_h


class CEditViewControl
{
  
public:
    static void show(float top, float height,float aniTime);
    static void hide(float aniTime);
    
//    static CEditViewControl g_EditViewControl;
//    static CEditViewControl* GetVCPtr(){ return &g_EditViewControl; }
};

//CEditViewControl g_EditViewControl;

#endif
