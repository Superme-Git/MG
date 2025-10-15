using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using CSTools;

namespace ResourceCheck
{
    public partial class MainWindow : Window
    {
        string mOfPath;
        Option mOption;
        UCCommonList mUCTableList;
        UCCommonList mUCEffectList;
        UCCommonList mUCParticleList;
        UCCommonList mUCAnimationList;
        public MainWindow(string[] args)
        {
            int Result = 0;
            InitializeComponent();
            mOfPath = "";
            for (int i = 0; i < args.Length; i++)
            {
                if (args[i] == "OfPath")
                {
                    mOfPath = "../";
                }
            }
            Result = Init();
            if (Result != 0)
            {
                System.Environment.Exit(0);
            }
        }
        private void Window_Closed(object sender, EventArgs e)
        {
            UnInit();
            System.Environment.Exit(0);
        }
        private int Init()
        {
            int Result = 0;
            Result = InitOption();
            InitUI();
            return Result;
        }
        private int InitOption()
        {
            int Result = 0;
            Option.sMainWindow = this;
            mOption = new Option(System.AppDomain.CurrentDomain.SetupInformation.ApplicationBase);
            Result = mOption.Init();
            mOption.mEffectPath = mOfPath + mOption.mEffectPath;
            mOption.mTableFileInfo.mPathNameFull = mOfPath + mOption.mTableFileInfo.mPathNameFull;
            CheckCommon.sOption = mOption;
            if (Result != 0)
            {
                MessageBox.Show("初始化配置文件失败");
            }
            return Result;
        }
        private void InitUI()
        {
            mUCTableList = new UCCommonList();
            mUCTableList.Init("Table");
            TIGTable.Children.Add(mUCTableList);
            mUCEffectList = new UCCommonList();
            mUCEffectList.Init("Effect");
            TIGEffect.Children.Add(mUCEffectList);
            mUCParticleList = new UCCommonList();
            mUCParticleList.Init("Particle");
            TIGParticle.Children.Add(mUCParticleList);
            mUCAnimationList = new UCCommonList();
            mUCAnimationList.Init("Animation");
            TIGAnimation.Children.Add(mUCAnimationList);
            RoutedEventArgs e = null;
            BtnTable_Click(BtnTable, e);
        }
        private void UnInit()
        {
            UnInitOption();
        }
        private void UnInitOption()
        {
            mUCTableList.Free();
            mUCTableList = null;
            mUCEffectList.Free();
            mUCEffectList = null;
            mUCParticleList.Free();
            mUCParticleList = null;
            mUCAnimationList.Free();
            mUCAnimationList = null;
            mOption.Free();
            mOption = null;
        }
        private void BtnTable_Click(object sender, RoutedEventArgs e)
        {
            TITable.IsSelected = true;
            mUCTableList.RefreshList();
        }
        private void BtnEffect_Click(object sender, RoutedEventArgs e)
        {
            TIEffect.IsSelected = true;
            mUCEffectList.RefreshList();
        }
        private void BtnParticle_Click(object sender, RoutedEventArgs e)
        {
            TIParticle.IsSelected = true;
            mUCParticleList.RefreshList();
        }
        private void BtnAnimation_Click(object sender, RoutedEventArgs e)
        {
            TIAnimation.IsSelected = true;
            mUCAnimationList.RefreshList();
        }
        private void BtnSlide_Click(object sender, RoutedEventArgs e)
        {
            Thickness NewMargin;
            if (GDLeft.Width > 20)
            {
                GDLeft.Width = 20;
                BtnSlide.Content = ">";
            }
            else
            {
                GDLeft.Width = 220;
                BtnSlide.Content = "<";
            }
            NewMargin = TCBG.Margin;
            NewMargin.Left = GDLeft.Width;
            TCBG.Margin = NewMargin;
        }
    }
}
