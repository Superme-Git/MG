package com.pwrd.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.javadocking.DockingManager;
import com.javadocking.dock.FloatDock;
import com.javadocking.dock.Position;
import com.javadocking.dock.BorderDock;
import com.javadocking.dock.CompositeLineDock;
import com.javadocking.dock.SplitDock;
import com.javadocking.dock.TabDock;
import com.javadocking.dock.docker.BorderDocker;
import com.javadocking.dock.factory.CompositeToolBarDockFactory;
import com.javadocking.dock.factory.ToolBarDockFactory;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.StateActionDockable;
import com.javadocking.dockable.DockableState;
import com.javadocking.dockable.DockingMode;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;
import com.javadocking.model.DockModel;
import com.javadocking.model.FloatDockModel;
import com.javadocking.util.LookAndFeelUtil;
import com.javadocking.visualizer.DockingMinimizer;
import com.javadocking.visualizer.FloatExternalizer;
import com.javadocking.visualizer.SingleMaximizer;
import com.pwrd.dbx.DbxManager;
import com.pwrd.definition.DatabaseMetaDataWrapper;
import com.pwrd.resources.ConstantIcons;
import com.pwrd.ui.event.AddDbxActionListener;
import com.pwrd.ui.event.ExecuteActionListener;
import com.pwrd.ui.event.FindActionListener;
import com.pwrd.ui.event.RemoveDbxActionListener;
import com.pwrd.ui.event.XTableMouseListener;
import com.pwrd.ui.text.CaretInfoPanel;

public class XdbBrowserFrame extends JFrame
{
	private static final long serialVersionUID = 5836933840042493705L;
	
	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(XdbBrowserFrame.class);
	
	public static LAF[] lafs;
	private static XdbBrowserFrame browser;
	
	private XTablePanel xtablePanel;
	private XBeanPanel xbeanPanel;
	// private CfgPanel cfgPanel;
	// private CfgStrChangePanel cfgStrChangePanel;
	
	private FindPanel findPanel;
	private PropertiesPanel propertiesPanel;
	private ConsolePanel consolePanel;
	private ValueDetailPanel valueDetailPanel;
	// private QueryResultPanel queryResultPanel;
	private TabDock topLeftTabDock;
	private TabDock topMiddleTabDock; // The TabDock for QueryResultPanel
	
	private int position = 0;
	
	private CaretInfoPanel caretInfoPanel;

	static
	{
		lafs = new LAF[4];

		lafs[0] = new LAF("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", LAF.THEME_DEAULT);
		lafs[1] = new LAF("Mac", "javax.swing.plaf.mac.MacLookAndFeel", LAF.THEME_DEAULT);
		lafs[2] = new LAF("Metal", "javax.swing.plaf.metal.MetalLookAndFeel", LAF.THEME_DEAULT);
		lafs[3] = new LAF("Nimbus", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel", LAF.THEME_DEAULT);
		
		// Set the first enabled look and feel.
		for (int index = 0; index < lafs.length; index++)
		{
			try 
			{
				if (lafs[index].isSupported())
				{
					lafs[index].setSelected(true);
					UIManager.setLookAndFeel(lafs[index].getClassName());
					break;
				}
		    } catch (Exception e) { }
		}
		 
		// Remove the borders from the split panes and the split pane dividers.
		LookAndFeelUtil.removeAllSplitPaneBorders();
		
		browser = new XdbBrowserFrame();
	}
	
	public static XdbBrowserFrame getInstance()
	{
		return browser;
	}
	
	private XdbBrowserFrame()
	{
		super.setLayout(new BorderLayout());
		
		FloatDockModel dockModel = new FloatDockModel();
		dockModel.addOwner("mainframe", this);
		
		DockingManager.setDockModel(dockModel);
		
		xtablePanel = new XTablePanel(DatabaseMetaDataWrapper.getInstance().getTables());
		xbeanPanel = new XBeanPanel(DatabaseMetaDataWrapper.getInstance().getBeans());
		// cfgPanel = new CfgPanel();
		// cfgStrChangePanel = new CfgStrChangePanel();
		findPanel = new FindPanel();
		propertiesPanel = new PropertiesPanel();
		// QueryPanel queryPanel = new QueryPanel();
		// resultPanel = new ResultPanel();
		consolePanel = new ConsolePanel();
		valueDetailPanel = new ValueDetailPanel();
		// queryResultPanel = new QueryResultPanel();
		
		xtablePanel.setPreferredSize(new Dimension(320, 500));
		propertiesPanel.setPreferredSize(new Dimension(320, 200));
		
		caretInfoPanel = new CaretInfoPanel();
		/** Ìí¼ÓÊÂ¼þ¼àÌý */
		xtablePanel.addTreeSelectionListener(propertiesPanel.getXTableTreeSelectionListener());
		xtablePanel.addMouseListener(new XTableMouseListener());
		findPanel.addFindActionListener(new FindActionListener());
		consolePanel.addCaretListener(caretInfoPanel);
		// queryResultPanel.getQueryPanel().addCaretListener(caretInfoLabel);
		// queryResultPanel.getResultPanel().addResultTableMouseListener(valueDetailPanel.getResultTableMouseListener());
		
		Dockable xtableDockable = new DefaultDockable("xtable", xtablePanel, "XTables", ConstantIcons.getIcon("xtable"), DockingMode.ALL);
		Dockable xbeanDockable = new DefaultDockable("xbean", xbeanPanel, "XBeans", ConstantIcons.getIcon("xbean"), DockingMode.ALL);
		// Dockable cfgDockable = new DefaultDockable("cfg", cfgPanel, "Configuration", null, DockingMode.ALL);
		// Dockable cfgStrChangeDockable = new DefaultDockable("cfgchange", cfgStrChangePanel, "ChangeString", null, DockingMode.ALL);
		Dockable findDockable = new DefaultDockable("find", findPanel, "Find", ConstantIcons.getIcon("find"), DockingMode.ALL);
		Dockable propertiesDockable = new DefaultDockable("properties", propertiesPanel, "Properties", null, DockingMode.ALL);
		Dockable valueDetailDockable = new DefaultDockable("valuedetail", valueDetailPanel, "Value Detail", null, DockingMode.ALL);
		Dockable consoleDockable = new DefaultDockable("console", consolePanel, "Console", null, DockingMode.ALL);
		xtableDockable = addAllActions(xtableDockable);
		xbeanDockable = addAllActions(xbeanDockable);
		findDockable = addAllActions(findDockable);
		propertiesDockable = addAllActions(propertiesDockable);
		Dockable queryResultDockable = createQueryResultDockable(position + 1);
		valueDetailDockable = addAllActions(valueDetailDockable);
		consoleDockable = addAllActions(consoleDockable);
		
		topLeftTabDock = new TabDock();
		TabDock middleLeftTabDock = new TabDock();
		TabDock bottomLeftTabDock = new TabDock();
		topMiddleTabDock = new TabDock();
		TabDock topRightTabDock = new TabDock();
		TabDock bottomRightTabDock = new TabDock();
		
		topLeftTabDock.addDockable(xtableDockable, new Position(0));
		topLeftTabDock.addDockable(xbeanDockable, new Position(1));
		// topLeftTabDock.addDockable(cfgDockable, new Position(2));
		// topLeftTabDock.addDockable(cfgStrChangeDockable, new Position(3));
		topLeftTabDock.setSelectedDockable(xtableDockable);
		middleLeftTabDock.addDockable(findDockable, new Position(0));
		bottomLeftTabDock.addDockable(propertiesDockable, new Position(0));
		
		topMiddleTabDock.addDockable(queryResultDockable, new Position(position ++));
		topRightTabDock.addDockable(valueDetailDockable, new Position(0));
		
		bottomRightTabDock.addDockable(consoleDockable, new Position(0));
		
		/** (XTables, XBeans) and Find */
		SplitDock topLeftSplitDock = new SplitDock();
		topLeftSplitDock.addChildDock(topLeftTabDock, new Position(Position.TOP));
		topLeftSplitDock.addChildDock(middleLeftTabDock, new Position(Position.BOTTOM));
		topLeftSplitDock.setDividerLocation(500);
		
		/** (XTables, XBeans, Find) and Properties */
		SplitDock leftSplitDock = new SplitDock();
		leftSplitDock.addChildDock(topLeftSplitDock, new Position(Position.TOP));
		leftSplitDock.addChildDock(bottomLeftTabDock, new Position(Position.BOTTOM));
		leftSplitDock.setDividerLocation(580);
		
		/*** QueryResult and ValueDetail */
		SplitDock topRightSplitDock = new SplitDock();
		topRightSplitDock.addChildDock(topMiddleTabDock, new Position(Position.LEFT));
		topRightSplitDock.addChildDock(topRightTabDock, new Position(Position.RIGHT));
		topRightSplitDock.setDividerLocation(640);
		
		/** Console */
		SplitDock bottomRightSplitDock = new SplitDock();
		bottomRightSplitDock.addChildDock(bottomRightTabDock, new Position(Position.CENTER));
		
		/** (QueryResult, ValueDetail) and Console */
		SplitDock rightSplitDock = new SplitDock();
		rightSplitDock.addChildDock(topRightSplitDock, new Position(Position.TOP));
		rightSplitDock.addChildDock(bottomRightSplitDock, new Position(Position.BOTTOM));
		rightSplitDock.setDividerLocation(620);
		
		SplitDock totalSplitDock = new SplitDock();
		totalSplitDock.addChildDock(leftSplitDock, new Position(Position.LEFT));
		totalSplitDock.addChildDock(rightSplitDock, new Position(Position.RIGHT));
		totalSplitDock.setDividerLocation(320);
		dockModel.addRootDock("totoalSplitDock", totalSplitDock, this);
		
		SingleMaximizer maximizePanel = new SingleMaximizer(totalSplitDock);
		dockModel.addVisualizer("maximizer", maximizePanel, this);
		
		BorderDock minimizerBorderDock = new BorderDock(new ToolBarDockFactory());
		minimizerBorderDock.setMode(BorderDock.MODE_MINIMIZE_BAR);
		minimizerBorderDock.setCenterComponent(maximizePanel);
		BorderDocker borderDocker = new BorderDocker();
		borderDocker.setBorderDock(minimizerBorderDock);
		DockingMinimizer minimizer = new DockingMinimizer(borderDocker);
		dockModel.addVisualizer("minimizer", minimizer, this);
		
		dockModel.addVisualizer("externalizer", new FloatExternalizer(this), this);
		
		BorderDock toolBarBorderDock = new BorderDock(new CompositeToolBarDockFactory(), minimizerBorderDock);
		toolBarBorderDock.setMode(BorderDock.MODE_TOOL_BAR);
		CompositeLineDock compositeToolBarDock1 = new CompositeLineDock(CompositeLineDock.ORIENTATION_HORIZONTAL, false,
				new ToolBarDockFactory(), DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
		CompositeLineDock compositeToolBarDock2 = new CompositeLineDock(CompositeLineDock.ORIENTATION_VERTICAL, false,
				new ToolBarDockFactory(), DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
		toolBarBorderDock.setDock(compositeToolBarDock1, Position.TOP);
		toolBarBorderDock.setDock(compositeToolBarDock2, Position.LEFT);

		// Add this dock also as root dock to the dock model.
		dockModel.addRootDock("toolBarBorderDock", toolBarBorderDock, this);
		
		// Add the tool bar border dock to this panel.
		getContentPane().add(toolBarBorderDock, BorderLayout.CENTER);
		
		JToolBar toolBar = createToolBar();
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
		getContentPane().add(caretInfoPanel, BorderLayout.PAGE_END);
		
		setMenuBar();
	}
	
	private Dockable addAllActions(Dockable dockable)
	{
		assert dockable != null;
		Dockable wrapper = new StateActionDockable(dockable, new DefaultDockableStateActionFactory(), DockableState.statesClosed());
		wrapper = new StateActionDockable(wrapper, new DefaultDockableStateActionFactory(), DockableState.statesAllExceptClosed());
		return wrapper;
	}
	
	private JToolBar createToolBar()
	{
		JToolBar toolBar = new JToolBar();
		final JComboBox dbxComboBox = new JComboBox();
		for(String dbxName : DbxManager.getInstance().getDbxNames())
			dbxComboBox.addItem(dbxName);
		dbxComboBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Object obj = dbxComboBox.getSelectedItem();
				if(obj != null)
					DbxManager.getInstance().setCurrentDbx(obj.toString());
				else
					DbxManager.getInstance().setCurrentDbx(null);
			}
		});
		dbxComboBox.setMaximumSize(new Dimension(150, 40));
		
		JButton addDbxBtn = new JButton(ConstantIcons.getIcon("dbxadd"));
		addDbxBtn.setToolTipText("Add a Dbx");
		addDbxBtn.addActionListener(new AddDbxActionListener(dbxComboBox));
		
		JButton removeDbxBtn = new JButton(ConstantIcons.getIcon("dbxremove"));
		removeDbxBtn.setToolTipText("Remove the current Dbx");
		removeDbxBtn.addActionListener(new RemoveDbxActionListener(dbxComboBox));
		
		// toolBar.addSeparator();
		JButton executeBtn = new JButton(ConstantIcons.getIcon("execute"));
		executeBtn.setToolTipText("Execute the xql statement");
		executeBtn.addActionListener(new ExecuteActionListener());
		// executeBtn.setMnemonic(KeyEvent.VK_F5);
		// executeBtn.seta
		toolBar.add(dbxComboBox);
		toolBar.add(addDbxBtn);
		toolBar.add(removeDbxBtn);
		toolBar.addSeparator();
		toolBar.add(executeBtn);
		
		return toolBar;
	}
	
	public static XTablePanel getXTablePanel()
	{
		return getInstance().xtablePanel;
	}
	
	public static XBeanPanel getXBeanPanel()
	{
		return getInstance().xbeanPanel;
	}
	
	public static FindPanel getFindPanel()
	{
		return getInstance().findPanel;
	}
	
	public static PropertiesPanel getPropertiesPanel()
	{
		return getInstance().propertiesPanel;
	}
	
	public static ConsolePanel getConsolePanel()
	{
		return getInstance().consolePanel;
	}
	
	public static ValueDetailPanel getValueDetailPanel()
	{
		return getInstance().valueDetailPanel;
	}
	
	public static TabDock getQueryResultTabDock()
	{
		return getInstance().topMiddleTabDock;
	}
	
	public static TabDock getXTableXBeanTabDock()
	{
		return getInstance().topLeftTabDock;
	}
	
	private Dockable createQueryResultDockable(int index)
	{
		QueryResultPanel queryResultPanel = new QueryResultPanel();
		queryResultPanel.getQueryPanel().addCaretListener(caretInfoPanel);
		queryResultPanel.getResultPanel().addResultTableMouseListener(valueDetailPanel.getResultTableMouseListener());
		Dockable queryResultDockable = new DefaultDockable("queryresult" + index, queryResultPanel, "Query and Result " + index, null, DockingMode.ALL);
		return addAllActions(queryResultDockable);
	}
	
	private void setMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		// fileMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
		fileMenu.getAccessibleContext().setAccessibleDescription("The File Menu");
		menuBar.add(fileMenu);
		JMenuItem addMenuItem = new JMenuItem("Add Query Console");
		addMenuItem.setMnemonic(KeyEvent.VK_A);
		addMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		addMenuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Dockable dockable = createQueryResultDockable(position + 1);
				topMiddleTabDock.addDockable(dockable, new Position(position ++));
				topMiddleTabDock.setSelectedDockable(dockable);
			}
			
		});
		JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_E);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
		exitMenuItem.getAccessibleContext().setAccessibleDescription("Exit the application");
		exitMenuItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						System.exit(0);
					}
				});
		fileMenu.add(addMenuItem);
		fileMenu.add(exitMenuItem);
		
		JMenu lookAndFeelMenu = new JMenu("Look and Feel");
		lookAndFeelMenu.setMnemonic(KeyEvent.VK_L);
		lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("The Lool and Feel Menu");
		menuBar.add(lookAndFeelMenu);
		ButtonGroup group = new ButtonGroup();
		for (int index = 0; index < lafs.length; index++)
		{
			LafMenuItem lafMenuItem = new LafMenuItem(lafs[index]);
			lookAndFeelMenu.add(lafMenuItem);
			group.add(lafMenuItem);
		}
		
		JMenu runMenu = new JMenu("Run");
		runMenu.setMnemonic(KeyEvent.VK_R);
		menuBar.add(runMenu);
		JMenuItem executeItem = new JMenuItem("Execute");
		executeItem.setMnemonic(KeyEvent.VK_X);
		executeItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
		executeItem.addActionListener(new ExecuteActionListener());
		runMenu.add(executeItem);
		
		JMenu toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		toolsMenu.getAccessibleContext().setAccessibleDescription("The Tools Menu");
		menuBar.add(toolsMenu);
		JMenuItem conflictCheckItem = new JMenuItem("Conflict check");
		conflictCheckItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				DbxSelectionDlg dlg = new DbxSelectionDlg(XdbBrowserFrame.this);
				dlg.setVisible(true);
			}
			
		}
		);
		JMenuItem timerkitItem = new JMenuItem("Timerkit");
		timerkitItem.setMnemonic(KeyEvent.VK_K);
		timerkitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.ALT_MASK));
		timerkitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				TimerKitDlg timerKit = new TimerKitDlg(XdbBrowserFrame.this, false);
				timerKit.setVisible(true);
			}
			
		});
		JMenuItem objBrowseItem = new JMenuItem("Object Browser");
		objBrowseItem.setMnemonic(KeyEvent.VK_O);
		objBrowseItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		objBrowseItem.addActionListener(new ActionListener() {

			// private ObjectBrowserDlg dlg;
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// if(dlg == null)
				ObjectBrowserDlg dlg = new ObjectBrowserDlg(XdbBrowserFrame.this, false);
				dlg.setVisible(true);
			}
		});
		JMenuItem jmxConsoleItem = new JMenuItem("JMX Console");
		jmxConsoleItem.setMnemonic(KeyEvent.VK_J);
		jmxConsoleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.ALT_MASK));
		jmxConsoleItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Process process;
				try
				{
					Runtime.getRuntime().exec("cmd /c start xbrowse-jmx.bat", null, new File(System.getProperty("user.dir")));
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				} 
			}
			
		});
		JMenuItem serializationConsoleItem = new JMenuItem("Serialization Console");
		serializationConsoleItem.setMnemonic(KeyEvent.VK_S);
		serializationConsoleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		serializationConsoleItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					Runtime.getRuntime().exec("cmd /c start xbrowse-serializable.bat", null, new File(System.getProperty("user.dir")));
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				} 
			}
			
		});
		toolsMenu.add(conflictCheckItem);
		toolsMenu.add(timerkitItem);
		toolsMenu.add(objBrowseItem);
		toolsMenu.add(jmxConsoleItem);
		toolsMenu.add(serializationConsoleItem);
		
		setJMenuBar(menuBar);
	}
	
	private void setLookAndFeel(LAF laf)
	{
		try 
		{
	        UIManager.setLookAndFeel(laf.getClassName());
	        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
	        LAF.setTheme(lookAndFeel, laf.getTheme());
	        UIManager.setLookAndFeel(lookAndFeel);
	        
	        // Iterate over the owner windows of the dock model.
	        DockModel dockModel = DockingManager.getDockModel();
	        for (int index = 0; index < dockModel.getOwnerCount(); index++)
	        {
	        	
	        	// Set the LaF on the owner.
	        	Window owner = dockModel.getOwner(index);
	        	SwingUtilities.updateComponentTreeUI(owner);
	        	
	        	// Set the LaF on the floating windows.
	        	FloatDock floatDock = dockModel.getFloatDock(owner);
	        	for (int childIndex = 0; childIndex < floatDock.getChildDockCount(); childIndex++)
	        	{
	        		Component floatingComponent = (Component)floatDock.getChildDock(childIndex);
	        		SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(floatingComponent));
	        	}
	        	
	        	for(int i = 0; i < topMiddleTabDock.getDockableCount(); ++ i)
	        	{
	        		SwingUtilities.updateComponentTreeUI(topMiddleTabDock.getDockable(i).getContent());
	        	}
	        	
	        	/** Set the LaF on all the dockable components.*/
	        	SwingUtilities.updateComponentTreeUI(xtablePanel);
	        	SwingUtilities.updateComponentTreeUI(xbeanPanel);	
	        	SwingUtilities.updateComponentTreeUI(findPanel);
	        	SwingUtilities.updateComponentTreeUI(propertiesPanel);
	        	SwingUtilities.updateComponentTreeUI(consolePanel);
	        	SwingUtilities.updateComponentTreeUI(valueDetailPanel);
	        	SwingUtilities.updateComponentTreeUI(caretInfoPanel);
	        }
	    } 
		catch (Exception e) 
	    { 
			e.printStackTrace();
	    }

	}
	
	private class LafMenuItem extends JRadioButtonMenuItem
	{
		private static final long serialVersionUID = 6078933877308680096L;

		public LafMenuItem(LAF laf)
		{
			super(laf.getTitle());
			
			// Is this look and feel supported?
			if (laf.isSupported())
			{
				LafListener lafItemListener = new LafListener(laf);
				addActionListener(lafItemListener);
			}
			else
			{
				setEnabled(false);
			}
			
			if (laf.isSelected())
			{
				setSelected(true);
			}
		}
	}
	
	private class LafListener implements ActionListener
	{

		// Fields.
		private LAF laf;
		
		// Constructors.
		public LafListener(LAF laf)
		{
			this.laf = laf;
		}
		
		// Implementations of ItemListener.
		public void actionPerformed(ActionEvent arg0)
		{
			for (int index = 0; index < lafs.length; index++)
			{
				lafs[index].setSelected(false);
			}
			setLookAndFeel(laf);
			laf.setSelected(true);
		}
		
	}
}
