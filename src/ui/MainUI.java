package ui;

import handler.AWGMap;
import handler.Constants;
import handler.Handler;

import javax.swing.JFrame;

import java.lang.String;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;


import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;



public class MainUI
{
	private JTabbedPane tabbedPane;
	private ListenerPanel listenerPanel;
	private ConfigPanel configPanel;
	private final static MainUI mainUI = new MainUI();
	static JFrame frmWdmpon;
	private Handler handler = new Handler();
	private static String key = "1234";
	public static AWGMap awgMap;
	private String[] COMs = null;

	/**
	 * Create the application.
	 */
	public static void main(String[] args)
	{
		final JFrame frame = new JFrame();
		JTextField textField;
		final JPasswordField passwordField;
		frame.setVisible(true);
		frame.setTitle("\u767B\u5F55");
		frame.setBounds(100, 100, 210, 184);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setVgap(20);
		panel.add(panel_2);

		JLabel lblNewLabel = new JLabel("\u7528\u6237\u540D\uFF1A");
		panel_2.add(lblNewLabel);

		textField = new JTextField();
		textField.setText("WDMPON");
		panel_2.add(textField);
		textField.setColumns(10);

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_3.getLayout();
		flowLayout_1.setVgap(15);
		panel.add(panel_3);

		JLabel lblNewLabel_1 = new JLabel("\u5BC6  \u7801\uFF1A");
		panel_3.add(lblNewLabel_1);

		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		passwordField.setText("1234");
		panel_3.add(passwordField);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);

		JButton loginButton = new JButton("\u767B\u5F55");
		loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				char[] passwordChar = passwordField.getPassword();
				String password = new String(passwordChar);
				if (password.equals(key))
				{
					frame.setVisible(false);
					frmWdmpon.setVisible(true);
				} else
				{
					frame.setTitle("密码错误");
				}

			}
		});
		panel_1.add(loginButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		panel_1.add(cancelButton);
	}

	public static MainUI getMainUI()
	{
		return mainUI;
	}

	private MainUI()
	{
		awgMap = new AWGMap();
		frmWdmpon = new JFrame();
		frmWdmpon.setResizable(false);
		frmWdmpon.setSize(new Dimension(692, 470));
		frmWdmpon.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWdmpon.setTitle("WDM-PON实验系统管理端");
		tabbedPane = new JTabbedPane();
		// 创建面板
		listenerPanel = new ListenerPanel();
		configPanel = new ConfigPanel();

		listenerPanel.setBackground(Color.WHITE);
		configPanel.setBackground(Color.WHITE);
		// 将标签面板加入到选项卡面板对象上
		tabbedPane.addTab("状态监控", null, listenerPanel, null);
		tabbedPane.addTab("配置管理", null, configPanel, null);

		frmWdmpon.getContentPane().add(tabbedPane);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public ConfigPanel getConfigPanel()
	{
		return configPanel;
	}

	public ListenerPanel getListenerPanel()
	{
		return listenerPanel;
	}

	public class ListenerPanel extends JPanel
	{
		/**
		 * 
		 */
		XFPPanel xfpPanel1;
		XFPPanel xfpPanel2;
		XFPPanel xfpPanel3;
		XFPPanel xfpPanel4;
		XFPPanel[] xfpPanels;
		private static final long serialVersionUID = 1L;

		public ListenerPanel()
		{
			setLayout(new BorderLayout(0, 0));

			JPanel panel = new JPanel();
			add(panel, BorderLayout.NORTH);

			final JButton findComButton = new JButton("COM:");
			panel.add(findComButton);
			findComButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					COMs = handler.findCom();
					// TODO Auto-generated method stub
					if (COMs.length != 0)
					{
						handler.setCom(COMs[0]);
					}
				}
			});

			final JComboBox<String> comComboBox = new JComboBox<String>();

			COMs = handler.findCom();
			if (COMs.length != 0)
			{
				handler.setCom(COMs[0]);
				comComboBox.setModel(new DefaultComboBoxModel<>(COMs));
			}
			// 设置默认端口

			panel.add(comComboBox);
			comComboBox.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// TODO Auto-generated method stub

					int index = comComboBox.getSelectedIndex();
					String com = comComboBox.getItemAt(index);
					handler.setCom(com);
				}
			});

			// 连接
			final JButton connectButton = new JButton("\u8FDE\u63A5");
			panel.add(connectButton);
			// 断开
			final JButton disconnectButton = new JButton("\u65AD\u5F00");
			panel.add(disconnectButton);
			// 重新连接
			final JButton reconnectButton = new JButton("\u91CD\u8FDE");
			panel.add(reconnectButton);

			final JLabel tempThroLabel = new JLabel("高温阈值:");
			panel.add(tempThroLabel);

			final JComboBox<Integer> tempsComboBox = new JComboBox<Integer>();
			tempsComboBox.setModel(new DefaultComboBoxModel<>(new Integer[]
			{ 30, 50, 55, 60, 65, 70 }));
			tempsComboBox.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// TODO Auto-generated method stub
					int index = tempsComboBox.getSelectedIndex();
					int tempThro = tempsComboBox.getItemAt(index);
					Constants.TEMP_ALARM_THRO = tempThro;
				}
			});
			panel.add(tempsComboBox);

			connectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (handler.openCom())
					{
						connectButton.setEnabled(false);
						disconnectButton.setEnabled(true);
						reconnectButton.setEnabled(true);
					}
				}
			});
			disconnectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (handler.closeCom())
					{
						disconnectButton.setEnabled(false);
						reconnectButton.setEnabled(false);
						connectButton.setEnabled(true);
					}
				}
			});
			reconnectButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (handler.closeCom() & handler.openCom())
					{
						connectButton.setEnabled(false);
					}
				}
			});
			reconnectButton.setEnabled(false);
			disconnectButton.setEnabled(false);
			JPanel panel_1 = new JPanel();
			add(panel_1, BorderLayout.CENTER);
			panel_1.setLayout(new GridLayout(2, 2, 0, 0));

			xfpPanel1 = new XFPPanel(1);
			panel_1.add(xfpPanel1);
			xfpPanel1.setLayout(new GridLayout(1, 2, 0, 0));

			xfpPanel2 = new XFPPanel(2);
			panel_1.add(xfpPanel2);
			xfpPanel2.setLayout(new GridLayout(1, 2, 0, 0));

			xfpPanel3 = new XFPPanel(3);
			panel_1.add(xfpPanel3);
			xfpPanel3.setLayout(new GridLayout(1, 2, 0, 0));

			xfpPanel4 = new XFPPanel(4);
			panel_1.add(xfpPanel4);
			xfpPanel4.setLayout(new GridLayout(1, 2, 0, 0));

			xfpPanels = new XFPPanel[]
			{ xfpPanel1, xfpPanel2, xfpPanel3, xfpPanel4 };

			JPanel panel_2 = new JPanel();
			add(panel_2, BorderLayout.SOUTH);

			Panel panel_3 = new Panel();
			panel_3.setBackground(Color.GREEN);
			panel_2.add(panel_3);

			Label label = new Label("\u5DF2\u6CE8\u518C");
			panel_2.add(label);

			Panel panel_4 = new Panel();
			panel_4.setBackground(Color.YELLOW);
			panel_2.add(panel_4);

			Label label_1 = new Label("\u53EF\u6CE8\u518C");
			panel_2.add(label_1);

			Panel panel_5 = new Panel();
			panel_5.setBackground(Color.LIGHT_GRAY);
			panel_2.add(panel_5);

			Label label_2 = new Label("\u672A\u6CE8\u518C");
			panel_2.add(label_2);

			Panel panel_6 = new Panel();
			panel_6.setBackground(Color.RED);
			panel_2.add(panel_6);

			Label label_3 = new Label("\u624B\u52A8\u5173\u95ED");
			panel_2.add(label_3);

		}

		public XFPPanel getXfpPanel(int index)
		{
			return xfpPanels[index];
		}
	}

	public class ConfigPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private SwitchPanel switchPanel1;
		private SwitchPanel switchPanel2;
		private SwitchPanel switchPanel3;
		private SwitchPanel switchPanel4;
		private WaveConPanel waveConPanel1;
		private WaveConPanel waveConPanel2;
		private WaveConPanel waveConPanel3;
		private WaveConPanel waveConPanel4;

		public ConfigPanel()
		{
			setLayout(new BorderLayout(0, 0));

			JPanel panel_1 = new JPanel();
			add(panel_1, BorderLayout.CENTER);
			panel_1.setLayout(new BorderLayout(0, 0));

			JPanel panel = new JPanel();
			panel_1.add(panel, BorderLayout.NORTH);
			panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

			JLabel lblNewLabel = new JLabel("\u7528\u6237\uFF1A");
			panel.add(lblNewLabel);

			JPanel panel_2 = new JPanel();
			panel_1.add(panel_2, BorderLayout.CENTER);
			panel_2.setLayout(new GridLayout(0, 2, 0, 0));

			JPanel panel_3 = new JPanel();
			panel_3.setBorder(new TitledBorder(null,
					"ONU\u6CE8\u518C\u914D\u7F6E", TitledBorder.LEFT,
					TitledBorder.TOP, null, null));
			panel_2.add(panel_3);
			panel_3.setLayout(new GridLayout(2, 2, 0, 0));

			String[] switchComboxModel = new String[]
			{ "开启", "关闭" };

			switchPanel1 = new SwitchPanel(handler, 0, switchComboxModel);
			panel_3.add(switchPanel1);

			switchPanel2 = new SwitchPanel(handler, 1, switchComboxModel);
			panel_3.add(switchPanel2);

			switchPanel3 = new SwitchPanel(handler, 2, switchComboxModel);
			panel_3.add(switchPanel3);

			switchPanel4 = new SwitchPanel(handler, 3, switchComboxModel);
			panel_3.add(switchPanel4);

			JPanel panel_5 = new JPanel();
			panel_5.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"),
					"ONU\u6CE2\u957F\u914D\u7F6E", TitledBorder.LEFT,
					TitledBorder.TOP, null, null));
			panel_2.add(panel_5);
			panel_5.setLayout(new GridLayout(2, 2, 0, 0));

			// 设置XFP下拉框选项
			String[] xfpComboxModel = awgMap.getChannels().keySet()
					.toArray(new String[0]);
			waveConPanel1 = new WaveConPanel(handler, 0, xfpComboxModel);
			panel_5.add(waveConPanel1);

			waveConPanel2 = new WaveConPanel(handler, 1, xfpComboxModel);
			panel_5.add(waveConPanel2);

			waveConPanel3 = new WaveConPanel(handler, 2, xfpComboxModel);
			panel_5.add(waveConPanel3);

			waveConPanel4 = new WaveConPanel(handler, 3, xfpComboxModel);
			panel_5.add(waveConPanel4);

		}
	}

}