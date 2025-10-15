package com.pwrd.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pwrd.JavaLibraryLoader;
import com.pwrd.log.XLogger;

public class GsdLauncher extends JFrame
{
	private static final long serialVersionUID = -1173301754582515120L;
	
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(GsdLauncher.class);
	
	private JButton browseBtn;
    private JButton okBtn;
    private JButton cancelBtn;
    private JLabel gsdLabel;
    private JTextArea infoText;
    private JTextField gsdText;
    
	public GsdLauncher()
	{
		setTitle("Gsd Launcher");
		setBounds(400, 300, 410, 220);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		initComponents(panel);
		setContentPane(panel);
		browseBtn.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(chooser.showOpenDialog(GsdLauncher.this) == JFileChooser.APPROVE_OPTION) 
				{
					File file = chooser.getSelectedFile();
					gsdText.setText(file.getAbsolutePath());
				}
			}
		});
		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) 
			{
				String filePath = gsdText.getText();
				if(JavaLibraryLoader.load(filePath))
				{
					// java库载入成功后，应该检测下该库是否是gsd.jar
					try 
					{
						Class.forName("xtable._DatabaseMetaData_");
						// 如果没有抛出异常，表明载入了正确的gsd.jar
						startup();
						GsdLauncher.this.setVisible(false);
					} 
					catch (ClassNotFoundException e) 
					{
						// swallow
						// e.printStackTrace();
						JOptionPane.showMessageDialog(GsdLauncher.this, filePath + " isn't gsd.jar", "Incorrect java library", JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(GsdLauncher.this, filePath + " isn't a java library", "Invalid java library", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
			
		});
	}

	private void initComponents(JPanel panel) 
	{
		gsdLabel = new JLabel();
		gsdText = new JTextField();
		browseBtn = new JButton();
		infoText = new JTextArea();
		okBtn = new JButton();
		cancelBtn = new JButton();

		gsdLabel.setText("Gsd library: ");

		browseBtn.setText("Browse");

		infoText.setColumns(20);
		infoText.setEditable(false);
		infoText.setRows(3);
		infoText.setText("Select a gsd library.\nXdb Browser depends on a gsd library to launch its UI,\nplease select a gsd library for this session.");
		infoText.setName("jTextArea1");

		okBtn.setText("Ok");
		okBtn.setMaximumSize(new java.awt.Dimension(69, 23));
		okBtn.setMinimumSize(new java.awt.Dimension(69, 23));
		okBtn.setName("jButton2");

		cancelBtn.setText("Cancel");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(panel);
		panel.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap(231, Short.MAX_VALUE)
								.addComponent(okBtn,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										69,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18).addComponent(cancelBtn)
								.addGap(23, 23, 23))
				.addComponent(infoText, javax.swing.GroupLayout.DEFAULT_SIZE,
						410, Short.MAX_VALUE)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(gsdLabel)
								.addGap(8, 8, 8)
								.addComponent(gsdText,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										229, Short.MAX_VALUE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(browseBtn).addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(infoText,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										73,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(24, 24, 24)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														gsdText,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(browseBtn)
												.addComponent(gsdLabel))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														okBtn,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(cancelBtn))
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
	}
	
	static void startup()
	{
		/*XdbDefinition xdbDef = XdbDefinition.getInstance();
		try 
		{
			xdbDef.parse("resources/xdb.xml");
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}*/
				
		JFrame frame = XdbBrowserFrame.getInstance();
		frame.setTitle("Xdb Browser");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		XLogger.startup();
	}
}
