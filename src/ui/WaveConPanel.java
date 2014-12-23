package ui;

import handler.Handler;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.util.List;


public class WaveConPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	int comboTag = 0; // 开关/配置面板标志
	private int swicthIndex = 0; // XFP 编号
	Handler handler;
	JComboBox<String> xfpComboBox;
	JComboBox<String> chComboBox;
	List<String> chList;
	String[] ComboxModel;
	String xfpString;

	public WaveConPanel(Handler handler, int switchIndex, String[] ComboxModel)
	{
		this.ComboxModel = ComboxModel;
		this.handler = handler;
		this.swicthIndex = switchIndex;
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("OLT Unit" + (switchIndex + 1) + ":");
		panel.add(lblNewLabel, BorderLayout.NORTH);

		xfpComboBox = new JComboBox<String>();
		xfpComboBox.setModel(new DefaultComboBoxModel<>(ComboxModel));
		panel.add(xfpComboBox, BorderLayout.CENTER);
		xfpComboBox.addActionListener(new XfpComboBoxListener());

		chComboBox = new JComboBox<String>();
		panel.add(chComboBox, BorderLayout.SOUTH);
		chComboBox.addActionListener(new ChComboBoxListener());
	}

	class XfpComboBoxListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			xfpString = (String) xfpComboBox.getSelectedItem();
			chList = MainUI.awgMap.getChannels().get(xfpString);
			String[] chStrings = chList.toArray(new String[0]);
			chComboBox.setModel(new DefaultComboBoxModel<>(chStrings));
		}
	}

	class ChComboBoxListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// TODO Auto-generated method stub
			String channel = (String) chComboBox.getSelectedItem();
			int channelId = chList.indexOf(channel);
			handler.setChannel(swicthIndex, channelId, xfpString);
		}

	}
}