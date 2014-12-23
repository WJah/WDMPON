package ui;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.border.BevelBorder;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;


public class XFPPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TitledBorder xfpTitledBorder;
	private JLabel wavelengthLabel;
	private JLabel temLabel;

	public XFPPanel(int unitId)
	{
		String panelTag = "OLT Unit";
		JPanel panel = new JPanel();
		Font xfpFont = new Font("XFP", Font.BOLD, 15);
		xfpTitledBorder = new TitledBorder(
				UIManager.getBorder("TitledBorder.border"), panelTag + unitId,
				TitledBorder.CENTER, TitledBorder.TOP, xfpFont, Color.GRAY);
		panel.setBorder(xfpTitledBorder);
		add(panel);
		panel.setLayout(new GridLayout(1, 2, 0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		wavelengthLabel = new JLabel("----");
		wavelengthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wavelengthLabel.setFont(new Font("ËÎÌו", Font.PLAIN, 20));
		panel_1.add(wavelengthLabel, BorderLayout.CENTER);

		JLabel label_1 = new JLabel(" \u6CE2\u957F:(nm)");
		label_1.setFont(new Font("Dialog", Font.PLAIN, 16));
		panel_1.add(label_1, BorderLayout.NORTH);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		panel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		temLabel = new JLabel("----");
		temLabel.setHorizontalAlignment(SwingConstants.CENTER);
		temLabel.setFont(new Font("ËÎÌו", Font.PLAIN, 20));
		panel_2.add(temLabel, BorderLayout.CENTER);

		JLabel label_3 = new JLabel(" \u6E29\u5EA6:(\u2103)");
		label_3.setFont(new Font("Dialog", Font.PLAIN, 16));
		panel_2.add(label_3, BorderLayout.NORTH);
	}

	public void setTagColor(Color color)
	{
		xfpTitledBorder.setTitleColor(color);
	}

	public void setTemperatureLabel(String temperature)
	{
		temLabel.setText(temperature);
	}
	
	public void setNolightAlarm()
	{
		wavelengthLabel.getParent().setBackground(Color.GRAY);
	}

	public void setHighTempAlarm()
	{
		temLabel.getParent().setBackground(Color.RED);
	}

	public void setNormalTemp()
	{
		temLabel.getParent().setBackground(null);
	}

	public void setWavelenthLabel(String wavelength)
	{
		wavelengthLabel.setText(wavelength);
	}

}
