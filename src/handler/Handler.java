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
	 * 处理串口通信模块
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

	// 设置通道波长
	public void setChannel(int XFPId, int channelId, String xfpString)
	{
		if (!isConnected)
		{
			return;
		}
		xfpId = XFPId;
		this.xfpString = xfpString;
		rs.write((byte) (0x21 + 0x10 * XFPId + channelId));// 设置光模块通道号
		ChannelGetter channelGetter = new ChannelGetter(XFPId);//启动新的线程，隔30s再读取波长通道
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
				rs.write((byte) (0x29 + 0x10 * XFPId)); // 获得光模块通道号
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

	// 开关光模块
	public void xpfSwitch(int xfpId, boolean open)
	{
		if (!isConnected)
		{
			return;
		}
		if (open)
		{
			rs.write((byte) (0x00 + xfpId * 2)); // 开启4213
			rs.write((byte) (0x00 + (xfpId * 2 + 1)));// 开启1412
		} else
		{
			rs.write((byte) (0x10 + xfpId * 2));// 关闭4213
			rs.write((byte) (0x10 + (xfpId * 2 + 1)));// 关闭1412
		}
	}

	public void setCom(String com)
	{
		rs.setCom(com);
	}

	// 设置监听温度
	public void getTemperature(Map<Byte, Map<Integer, Byte>> maps)
	{
		Set<Byte> keySet = maps.keySet();
		Byte[] bytes = keySet.toArray(new Byte[0]);
		for (Byte key : bytes)
		{
			Map<Integer, Byte> map = maps.get(key);
			byte INT = map.get(1); // 整数部分
			byte DEC = map.get(2); // 小数部分
			double temp = INT + (byteToInt(DEC) * 0.0039);
			DecimalFormat df = new DecimalFormat("#.00"); // 保留两位小数
			int xfpId = (0x0F & key) - 1;
			MainUI.getMainUI().getListenerPanel().getXfpPanel(xfpId)
					.setTemperatureLabel(df.format(temp));
			if (temp > Constants.TEMP_ALARM_THRO) // 当温度大于高温警报阈值时关闭光模块
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

	// 设置ONU已注册标识
	public void setONUReg(int xfpid)
	{
		MainUI.getMainUI().getListenerPanel().getXfpPanel(xfpid)
				.setTagColor(Color.GREEN);

	}
}
