//////////////////////////////////////////////////////////////////////////
//   任务限制需求设定
//////////////////////////////////////////////////////////////////////////

using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Documents;
using System.Windows.Data;

namespace Knight.MissionEditor
{


    //任务限制需求设定

    public class MissionBaseInfoPanel : StackPanel
    {
        /************************************************************************/
        /*  data                                                                                                */
        //////////////////////////////////////////////////////////////////////////
        //所用控件define here
        public IntNumEditBox IDEditBox;
        public SingleLenEditBox NameEditBox;
        public SingleLenEditBox TypeEditBox;
        public SingleLenEditBox autoEditBox;

        //等级限制
        public IntNumEditBox MinLevelEditBox;
        public IntNumEditBox MaxLevelEditBox;

        //转生限制 
        public IntNumEditBox TransMinLevelEditBox;
        public IntNumEditBox TransMaxLevelEditBox;

        //任务需求
        public IntNumEditBox NeedMissionEditBox;
        public Button AddNeedMissionBT;
        public Button ClearNeedMissionBT;
        public ListBox NeedMissionList;

        //转生需求
        public IntNumEditBox RoleIDEditBox;
        public Button AddRoleIDBT;
        public Button ClearRoleIDBT;
        public ListBox NeedRoleIDList;

        static int IDNUMLENGTH = 6;

        ///////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////

        /*************************************************************************/
        /*         初始化界面                                                                                 */
        /************************************************************************/
        public void Init()
        {
            MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            if (missionInfo != null)
            {
                Binding IDBind = new Binding("MissionInfo");
                IDBind.Source = missionInfo;
                IDBind.Mode = BindingMode.TwoWay;
                IDBind.Path = new PropertyPath("MissionID");
                IDEditBox.SetBinding(TextBox.TextProperty, IDBind);

                Binding NameBind = new Binding("NameBinding");
                NameBind.Source = missionInfo;
                NameBind.Mode = BindingMode.TwoWay;
                NameBind.Path = new PropertyPath("MissionName");
                NameEditBox.SetBinding(TextBox.TextProperty, NameBind);


                Binding MissionTypeBind= new Binding("MissionTypeString");
                MissionTypeBind.Source = missionInfo;
                MissionTypeBind.Mode = BindingMode.TwoWay;
                MissionTypeBind.Path = new PropertyPath("MissionTypeString");
                TypeEditBox.SetBinding(TextBox.TextProperty, MissionTypeBind);

                Binding AutoDoBind = new Binding("AutoDo");
                AutoDoBind.Source = missionInfo;
                AutoDoBind.Mode = BindingMode.TwoWay;
                AutoDoBind.Path = new PropertyPath("AutoDo");
                autoEditBox.SetBinding(TextBox.TextProperty, AutoDoBind);



                Binding MinLevelBind = new Binding("MinLevelBind");
                MinLevelBind.Source = missionInfo.LimitInfo;
                MinLevelBind.Mode = BindingMode.TwoWay;
                MinLevelBind.Path = new PropertyPath("MinLevel");
                MinLevelEditBox.SetBinding(TextBox.TextProperty, MinLevelBind);

                Binding MaxLevelBind = new Binding("MaxLevelBind");
                MaxLevelBind.Source = missionInfo.LimitInfo;
                MaxLevelBind.Mode = BindingMode.TwoWay;
                MaxLevelBind.Path = new PropertyPath("MaxLevel");
                MaxLevelEditBox.SetBinding(TextBox.TextProperty, MaxLevelBind);

                Binding TransMinBind = new Binding("TransMinBind");
                TransMinBind.Source = missionInfo.LimitInfo;
                TransMinBind.Mode = BindingMode.TwoWay;
                TransMinBind.Path = new PropertyPath("TransMinLevel");
                TransMinLevelEditBox.SetBinding(TextBox.TextProperty, TransMinBind);

                Binding TransMaxBind = new Binding("TransMaxBind");
                TransMaxBind.Source = missionInfo.LimitInfo;
                TransMaxBind.Mode = BindingMode.TwoWay;
                TransMaxBind.Path = new PropertyPath("TransMaxLevel");
                TransMaxLevelEditBox.SetBinding(TextBox.TextProperty, TransMaxBind);

                
                NeedMissionList.ItemsSource = missionInfo.LimitInfo.RequestMissionList;
                NeedRoleIDList.ItemsSource = missionInfo.LimitInfo.RequestRoleIDList;      
               
            }
        }
        ////////////////////////////////////////////////////////////////////////////////

        private void AddNeedMissionOnClick(object sender, RoutedEventArgs args)
        {
            string str = NeedMissionEditBox.Text;

            if (str.Length != IDNUMLENGTH)
            {
                MessageBox.Show("任务ID需要是6位数字");
            }
            else
            {
                MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
                MissionIDOC missionID = new MissionIDOC();
                missionID.MissionID = str;
                missionInfo.LimitInfo.RequestMissionList.Add(missionID);
            }
        }

        private void ClearNeedMissionOnClick(object sender, RoutedEventArgs args)
        {
            //MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            //missionInfo.LimitInfo.RequestMissionList.Clear();
            MissionIDOC missionID = NeedMissionList.SelectedItem as MissionIDOC;

            if (missionID == null)
                MessageBox.Show("请先选择一个要删除的任务");
            else
            {
                MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
                missionInfo.LimitInfo.RequestMissionList.Remove(missionID);
            }

        }


        private void AddRoleIDOnClick(object sender, RoutedEventArgs args)
        {
            string str = RoleIDEditBox.Text;
            if (str.Length > 0)
            {
                MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
                AutoString Aus = new AutoString();
                Aus.String = str;
                missionInfo.LimitInfo.RequestRoleIDList.Add(Aus);
            }
            else
            {
                MessageBox.Show("请输入转生等级");
            }
        }

        private void ClearRoleIDOnClick(object sender, RoutedEventArgs args)
        {
            //MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
            //missionInfo.LimitInfo.RequestRoleIDList.Clear();
            AutoString Aus = NeedRoleIDList.SelectedItem as AutoString;

            if (Aus == null)
                MessageBox.Show("请先选择一个要删除的Role ID");
            else
            {
                MissionInfo missionInfo = EditorApp.GetCurMissionInfo();
                missionInfo.LimitInfo.RequestRoleIDList.Remove(Aus);
            }
        }


        public MissionBaseInfoPanel()
        {
            // 构造控件
            MLable titleLable = new MLable("任务限制需求设定");
            Children.Add(titleLable);
            //////////////////////////////////////////////////////////////////////////

            //ID and Name
            StackPanel IDPanel = new StackPanel();
            IDPanel.Orientation = Orientation.Horizontal;
            IDPanel.Children.Add(new MLable("任务ID"));

            //Text input For ID
            IDEditBox = new IntNumEditBox(IDNUMLENGTH);
            IDPanel.Children.Add(IDEditBox);  

            //Text input For Name
            IDPanel.Children.Add(new MLable("任务名称"));
            NameEditBox = new SingleLenEditBox();
            IDPanel.Children.Add(NameEditBox);

            Children.Add(IDPanel);
            //////////////////////////////////////////////////////////////////////////

            //Type 
            StackPanel TypePanel = new StackPanel();
            TypePanel.Orientation = Orientation.Horizontal;

            TypePanel.Children.Add(new MLable("任务类型"));

            TypeEditBox = new SingleLenEditBox();
            TypePanel.Children.Add(TypeEditBox);


            Children.Add(TypePanel);
            //////////////////////////////////////////////////////////////////////////
            StackPanel autoPanel = new StackPanel();
            autoPanel.Orientation = Orientation.Horizontal;

            autoPanel.Children.Add(new MLable("自动"));

            autoEditBox = new SingleLenEditBox();
            autoPanel.Children.Add(autoEditBox);


            Children.Add(autoPanel);

            //////////////////////////////////////////////////////////////////////////

            //等级限制
            StackPanel LevelPanel = new StackPanel();
            LevelPanel.Orientation = Orientation.Horizontal;

            LevelPanel.Children.Add(new MLable("等级限制"));

            MinLevelEditBox = new IntNumEditBox(3);
            MaxLevelEditBox = new IntNumEditBox(3);

            LevelPanel.Children.Add(MinLevelEditBox);
            LevelPanel.Children.Add(new MLable("至"));
            LevelPanel.Children.Add(MaxLevelEditBox);

            Children.Add(LevelPanel);
            //////////////////////////////////////////////////////////////////////////

            //转生等级限制
            StackPanel TransPanel = new StackPanel();
            TransPanel.Orientation = Orientation.Horizontal;

            TransPanel.Children.Add(new MLable("转生等级限制"));

            TransMinLevelEditBox = new IntNumEditBox(1);
            TransMaxLevelEditBox = new IntNumEditBox(1);

            TransPanel.Children.Add(TransMinLevelEditBox);
            TransPanel.Children.Add(new MLable("至"));
            TransPanel.Children.Add(TransMaxLevelEditBox);

            Children.Add(TransPanel);
            //////////////////////////////////////////////////////////////////////////

            //任务需求
            StackPanel NeedMissionPanel = new StackPanel();
            NeedMissionPanel.Orientation = Orientation.Horizontal;

            NeedMissionPanel.Children.Add(new MLable("任务需求"));

            NeedMissionEditBox = new IntNumEditBox(6);
            NeedMissionPanel.Children.Add(NeedMissionEditBox);

            AddRoleIDBT = new Button();
            AddRoleIDBT.Content = "添加";
            AddRoleIDBT.Click += AddNeedMissionOnClick;
            AddRoleIDBT.Margin = new Thickness(5);
            AddRoleIDBT.Width = 68;
            NeedMissionPanel.Children.Add(AddRoleIDBT);

            ClearNeedMissionBT = new Button();
            ClearNeedMissionBT.Content = "删除";
            ClearNeedMissionBT.Click += ClearNeedMissionOnClick;
            ClearNeedMissionBT.Margin = new Thickness(5);
            ClearNeedMissionBT.Width = 68;
            NeedMissionPanel.Children.Add(ClearNeedMissionBT);



            Children.Add(NeedMissionPanel);

            NeedMissionList = new ListBox();
            NeedMissionList.Height = 70;
            NeedMissionList.Margin = new Thickness(16.18);
            NeedMissionList.HorizontalContentAlignment = HorizontalAlignment.Right;
            Children.Add(NeedMissionList);
            //////////////////////////////////////////////////////////////////////////

            //角色需求

            StackPanel RoleIDPanel = new StackPanel();
            RoleIDPanel.Orientation = Orientation.Horizontal;

            RoleIDPanel.Children.Add(new MLable("角色需求"));

            RoleIDEditBox = new IntNumEditBox(3);
            RoleIDPanel.Children.Add(RoleIDEditBox);

            AddNeedMissionBT = new Button();
            AddNeedMissionBT.Content = "添加";
            AddNeedMissionBT.Click += AddRoleIDOnClick;
            AddNeedMissionBT.Margin = new Thickness(5);
            AddNeedMissionBT.Width = 68;
            RoleIDPanel.Children.Add(AddNeedMissionBT);

            ClearRoleIDBT = new Button();
            ClearRoleIDBT.Content = "删除";
            ClearRoleIDBT.Click += ClearRoleIDOnClick;
            ClearRoleIDBT.Margin = new Thickness(5);
            ClearRoleIDBT.Width = 68;
            RoleIDPanel.Children.Add(ClearRoleIDBT);



            Children.Add(RoleIDPanel);

            NeedRoleIDList = new ListBox();
            NeedRoleIDList.Height = 70;
            NeedRoleIDList.Margin = new Thickness(16.18);
            NeedRoleIDList.HorizontalContentAlignment = HorizontalAlignment.Right;
            Children.Add(NeedRoleIDList);


            //事件
            GetEditorApp.myApp.CurMissionInfoChanged += 
                new EditorApp.CurMissionInfoChangedHandler(Init);

        }
    }
}
