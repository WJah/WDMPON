package handler;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;


import rs232.RS232;
import ui.MainUI;

public class Handler
{
	/**
	 * ������ͨ��ģ��
	 */
	private boolean isConnected = false;
	public RS232 rs;
	int xfpId;
	String xfpString;

	public Handler()
	{
		rs = new RS232(this)
		{
			@Override
			public void back(String s1)
			{
				// TODO Auto-generated method stub
			}
		};
	}

	public String[] findCom()
	{
		List<String> list = rs.findComs();
		String[] comsStrings = new String[list.size()];
		list.toArray(comsStrings);
		return comsStrings;
	}

	public boolean openCom()
	{
		return isConnected = rs.openCom();
	}

	public boolean closeCom()
	{
		rs.closeCom();
		isConnected = false;
		return !isConnected;
	}

	// ����ͨ������
	public void setChannel(int XFPId, int channelId, String xfpString)
	{
		if (!isConnected)
		{
			return;
		}
		xfpId = XFPId;
		this.xfpString = xfpString;
		rs.write((byte) (0x21 + 0x10 * XFPId + channelId));// ���ù�ģ��ͨ����
		ChannelGetter channelGetter = new ChannelGetter(XFPId);//�����µ��̣߳���30s�ٶ�ȡ����ͨ��
		channelGetter.start();
		System.err.println((byte) (0x21 + 0x10 * XFPId + channelId));
		setChannelNum(channelId);
	}

	class ChannelGetter extends Thread
	{
		int XFPId;

		public ChannelGetter(int XFPId)
		{
			this.XFPId = XFPId;
		}

		@Override
		public void run()
		{
			try
			{
				Thread.sleep(30 * 1000);
				rs.write((byte) (0x29 + 0x10 * XFPId)); // ��ù�ģ��ͨ����
				System.out.println((byte) (0x29 + 0x10 * XFPId));
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub
		}
	}

	public void setChannelNum(int channelId)
	{
		AWGMap awgMap = new AWGMap();
		Map<String, List<String>> Channels = awgMap.getChannels();
		System.out.println(xfpString);
		List<String> waveStrings = Channels.get(xfpString);
		MainUI.getMainUI().getListenerPanel().getXfpPanel(xfpId)
				.setWavelenthLabel(waveStrings.get(channelId));
	}

	// ���ع�ģ��
	public void xpfSwitch(int xfpId, boolean open)
	{
		if (!isConnected)
		{
			return;
		}
		if (open)
		{
			rs.write((byte) (0x00 + xfpId * 2)); // ����4213
			rs.write((byte) (0x00 + (xfpId * 2 + 1)));// ����1412
		} else
		{
			rs.write((byte) (0x10 + xfpId * 2));// �ر�4213
			rs.write((byte) (0x10 + (xfpId * 2 + 1)));// �ر�1412
		}
	}

	public void setCom(String com)
	{
		rs.setCom(com);
	}

	// ���ü����¶�
	public void getTemperature(Map<Byte, Map<Integer, Byte>> maps)
	{
		Set<Byte> keySet = maps.keySet();
		Byte[] bytes = keySet.toArray(new Byte[0]);
		for (Byte key : bytes)
		{
			Map<Integer, Byte> map = maps.get(key);
			byte INT = map.get(1); // ��������
			byte DEC = map.get(2); // С������
			double temp = INT + (byteToInt(DEC) * 0.0039);
			DecimalFormat df = new DecimalFormat("#.00"); // ������λС��
			int xfpId = (0x0F & key) - 1;
			MainUI.getMainUI().getListenerPanel().getXfpPanel(xfpId)
					.setTemperatureLabel(df.format(temp));
			if (temp > Constants.TEMP_ALARM_THRO) // ���¶ȴ��ڸ��¾�����ֵʱ�رչ�ģ��
			{
				xpfSwitch(xfpId, false);
				MainUI.getMainUI().getListenerPanel().getXfpPanel(xfpId)
						.setHighTempAlarm();
			} else
			{
				MainUI.getMainUI().getListenerPanel().getXfpPanel(xfpId)
						.setNormalTemp();
				;
			}
		}
	}

	private int byteToInt(byte b)
	{
		if (Byte.valueOf(b) < 0)
		{
			return Byte.valueOf(b) + 256;
		}
		return Byte.valueOf(b);
	}

	// ����ONU��ע���ʶ
	public void setONUReg(int xfpid)
	{
		MainUI.getMainUI().getListenerPanel().getXfpPanel(xfpid)
				.setTagColor(Color.GREEN);

	}
}
