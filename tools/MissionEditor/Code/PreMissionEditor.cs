//////////////////////////////////////////////////////////////////////////
//   前置任务编辑器
//////////////////////////////////////////////////////////////////////////

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Knight.MissionEditor;

namespace Knight.MissionEditor
{
    public class PreMissionEditor : Grid
    {
        //UI控件
        public ComboBox preMissionIDCombo;
        public TextBox txtbox;
        public PreMissionEditor()
        {
            //搞一个4行2列的格子
            for (int i = 0; i < 2; ++i)
            {
                ColumnDefinition coldef = new ColumnDefinition();
                coldef.Width = GridLength.Auto;
                ColumnDefinitions.Add(coldef);
            }

            for (int i = 0; i < 2; ++i)
            {
                RowDefinition rowdef = new RowDefinition();
                rowdef.Height = GridLength.Auto;
                RowDefinitions.Add(rowdef);
            }

            //添加控件
            Label titleLabel = new Label();
            titleLabel.Background = Brushes.AliceBlue;
            titleLabel.Foreground = Brushes.DarkSalmon;
            titleLabel.Margin = new Thickness(5);
            TextBlock txtblk = new TextBlock();
            txtblk.FontSize = 12;
            txtblk.TextAlignment = TextAlignment.Right;

            titleLabel.Content = txtblk;

            txtblk.Inlines.Add(new Italic(new Run("前置任务ID")));

            Children.Add(titleLabel);
            Grid.SetColumn(titleLabel, 0);
            Grid.SetRow(titleLabel, 0);

            //preMissionIDCombo = new ComboBox();
            //preMissionIDCombo.Width = 180;
            //preMissionIDCombo.Margin = new Thickness(5);
            //Children.Add(preMissionIDCombo);
            //Grid.SetColumn(preMissionIDCombo, 1);
            //Grid.SetRow(preMissionIDCombo, 0);
            //preMissionIDCombo.DisplayMemberPath = "MissionID";
            //preMissionIDCombo.SelectionChanged += PreMissionIDChanged;
                        //添加控件
            Label perposeLabel = new Label();
            perposeLabel.Background = Brushes.AliceBlue;
            perposeLabel.Foreground = Brushes.DarkSalmon;
            perposeLabel.Margin = new Thickness(5);
            TextBlock txtblk2 = new TextBlock();
            txtblk2.FontSize = 12;
            txtblk2.TextAlignment = TextAlignment.Right;

            perposeLabel.Content = txtblk2;

            txtblk2.Inlines.Add(new Italic(new Run("前置任务目的")));
            Children.Add(perposeLabel);
            Grid.SetRow(perposeLabel, 1);
            Grid.SetColumn(perposeLabel, 0);
            //

            txtbox = new TextBox();
            txtbox.FontSize = 20;
            txtbox.AcceptsReturn = true;
            txtbox.TextWrapping = TextWrapping.Wrap;
            txtbox.VerticalScrollBarVisibility = ScrollBarVisibility.Auto;
            txtbox.IsReadOnly = true;
            txtbox.Margin = new Thickness(5);
            txtbox.Text = "";
            Children.Add(txtbox);
            txtbox.Width = 200;
            txtbox.Height = 100;
            Grid.SetRow(txtbox, 1);
            Grid.SetColumn(txtbox, 1);

            this.ShowGridLines = true;


            //事件
            GetEditorApp.myApp.CurMissionInfoChanged +=
                new EditorApp.CurMissionInfoChangedHandler(Init);
        }

        //public void PreMissionIDChanged(object sender, SelectionChangedEventArgs args)
        //{
        //    ComboBox item = sender as ComboBox;
        //    MissionInfo mInfo = item.SelectedItem as MissionInfo;
            
        //    if (mInfo == null)
        //        return;

        //    if (mInfo.MissionID == EditorApp.GetCurMissionInfo().MissionID)
        //    {
        //        MessageBox.Show("前置任务ID不能与当前任务重复");
        //        item.SelectedIndex = -1;
        //        txtbox.Text = "";
        //        return;
        //    }
        //    EditorApp.GetCurMissionInfo().LimitInfo.PreMissionID = mInfo.MissionID;
        //    txtbox.Text = mInfo.TaskInfo.PurposeA;      
        //}

        public void Init()
        {
            //set up data bind         
            //MissionInfo mInfo = EditorApp.GetCurMissionInfo();
            //if (mInfo != null)
            //{
            //    //Binding PreMissionIDBind = new Binding("PreMissionIDBind");
            //    //PreMissionIDBind.Source = mInfo.LimitInfo;
            //    //PreMissionIDBind.Mode = BindingMode.TwoWay;
            //    //PreMissionIDBind.Path = new PropertyPath("PreMissionID");
            //    //BindingExpressionBase beb = preMissionIDCombo.SetBinding(TextBlock.TextProperty, PreMissionIDBind);
            //    MissionInfo preMissionInfo;
            //    EditorApp.GetMissionInfoByID(mInfo.LimitInfo.PreMissionID, out preMissionInfo);
            //    if (preMissionInfo != null)
            //        preMissionIDCombo.SelectedItem = preMissionInfo;
            //    else
            //    {
            //        preMissionIDCombo.SelectedIndex = -1;
            //        txtbox.Text = "";
            //    }
            //}         
        }        
    }
}
