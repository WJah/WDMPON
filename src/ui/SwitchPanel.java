package ui;

import handler.Handler;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwitchPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private int swicthIndex = 0; // XFP 编号
	JComboBox<String> comboBox;
	Handler handler;

	public SwitchPanel(Handler handler, int switchIndex, String[] ComboxModel)
	{
		this.handler = handler;
		this.swicthIndex = switchIndex;
		JPanel panel = new JPanel();
		add(panel);
		String tag = "OLT Unit";
		JLabel label = new JLabel(tag + (switchIndex + 1) + "\uFF1A");
		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(ComboxModel));
		comboBox.addActionListener(this);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(label);

		panel.add(comboBox);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		int key = comboBox.getSelectedIndex();
		MainUI mainUI = MainUI.getMainUI();
		XFPPanel xfpPanel = mainUI.getListenerPanel().getXfpPanel(swicthIndex);
		switch (key)
		{
		case 0: // 开启光模块
			xfpPanel.setTagColor(Color.GRAY);
			handler.xpfSwitch(swicthIndex, true);
			break;

		case 1: // 关闭光模块
			xfpPanel.setTagColor(Color.RED);
			handler.xpfSwitch(swicthIndex, false);
			break;
		}

	}
}