using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace Knight.MissionEditor
{
    public class MiddlePanel : StackPanel
    {
        /************************************************************************/
        /*  data                                                                                                */
        //////////////////////////////////////////////////////////////////////////
        public PreMissionEditor preMissionEditor;
        public MissionBaseInfoPanel missionBaseInfo;

        /************************************************************************/



        /************************************************************************/
        /*         构造函数 ，初始化UI界面                                                             */
        /************************************************************************/
        public MiddlePanel()
        {
            //preMissionEditor = new PreMissionEditor();
            //Children.Add(preMissionEditor);

            //Children.Add(new Separator());

            missionBaseInfo = new MissionBaseInfoPanel();
            Children.Add(missionBaseInfo);
        }
        //////////////////////////////////////////////////////////////////////////

    }
}
