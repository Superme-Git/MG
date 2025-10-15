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
using System.Windows.Shapes;

namespace QuestInput
{
    /// <summary>
    /// InsertOpenDialog.xaml 的交互逻辑
    /// </summary>
    public partial class InsertOpenDialog : Window
    {
        private string[] dialogs = { 
                                   "商会", 
                                   "摆摊",
                                   "公会技能",
                                   "商城",
                                   "充值",
                                   "装备打造",
                                   "宝石合成",
                                   "装备修理",
                                   "装备镶嵌",
                                   "背包",
                                   "仓库",
                                   "人物属性",
                                   "人物信息",
                                   "人物加点",
                                   "宠物属性",
                                   "宠物炼妖",
                                   "宠物培养",
                                   "宠物图鉴",
                                   "职业技能",
                                   "人物专精",
                                   "队伍",
                                   "副本",
                                   "挂机界面",
                                   "伙伴助战",
                                   "分页签",
                                   "炼药",
                                   "临时背包",
                                   "活力兑现",
                                   "npc商店",
                                   "宠物商店",
                                   "宠物仓库",
                                   "任务上交宠物",
                                   "任务上交物品",
                                   "普通挖宝",
                                   "高级挖宝",
                                   "宠物技能学习",
                                   "宠物放生",
                                   "重置属性",
                                   "死亡提醒",
                                   "任务",
                                   "活动",
                                   "好友",
                                   "排行榜",
                                   "聊天",
                                   "活动周历",
                                   "升级大礼包",
                                   "宠物技能修炼",
                                   "洗点界面",
                                   "添加好友",
                                   "礼包兑换码",
                                   "染色",
                                   "角斗场单人切磋",
                                   "角斗场多人切磋",
                                   "角斗场观战",
                                   "染色保存方案",
                                   "公会",
                                   "队伍申请",
                                   "下战书",
                                   "生死战观战",
                                   "生死决斗榜",
                                   "决斗规则",
                                   "暗夜马戏团",
                                   "分享",
                                   };

        public delegate void passValueHandler(object sender, PassValuesEventArgs e);

        public event passValueHandler PassValuesEvent;
        public InsertOpenDialog()
        {
            InitializeComponent();

            WindowStartupLocation = WindowStartupLocation.CenterScreen;

            IO_tbTextShow.Foreground = (Brush)new BrushConverter().ConvertFromString(MainWindow.getSelectedColor());

            for (int i = 0; i < dialogs.Count(); i++)
            {
                ComboBoxItem item = new ComboBoxItem();
                item.Content = dialogs[i];
                IO_cbChooseDD.Items.Add(item);
            }
        }

        private void HandleInsertOpendialogSureClick(object sender, RoutedEventArgs e)
        {
            String rValue = "<U t= did= c= ></U>";
            string tText = IO_tbTextShow.Text;
            string cText = IO_tbTextShow.Foreground.ToString();
            int did = IO_cbChooseDD.SelectedIndex + 1;

            int posText = rValue.IndexOf("t=");
            rValue = rValue.Insert(posText + 2, "\"" + tText + "\"");

            int posColor = rValue.IndexOf("c=");
            rValue = rValue.Insert(posColor + 2, "\"" + cText.Substring(1, 8) + "\"");

            int posDid = rValue.IndexOf("did=");
            rValue = rValue.Insert(posDid + 4, "\"" + did + "\"");

            PassValuesEventArgs args = new PassValuesEventArgs(tText, rValue);
            PassValuesEvent(this, args);
            Close();
        }
    }
}
