package rs232;

import handler.Constants;
import handler.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;


public abstract class RS232 implements SerialPortEventListener
{
	private CommPortIdentifier portId;// ���ڹ���˿�
	private SerialPort sp;// ���ڴ���ͨ��
	private int BaudRate = 9600;
	private int WordSize = 8;
	private int StopBit = 1;
	private int Pariy = 0;
	private boolean state = true;
	private String com = null;
	private Handler handler;
	private Map<Byte, Map<Integer, Byte>> tempMaps = new HashMap<Byte, Map<Integer, Byte>>();
	private Map<Integer, Byte> tempMap; // �¶ȵ�������С������
	private Byte tempMapKey;// XFP��ʶ
	private int tempMapCur = 0; // �¶ȴ��������α�
	private int chCur = 0; // ͨ�����������α�
	private int chNum = 0;

	// ///////////////////��ʼ��//////////////////////////////////////
	public RS232(Handler handler)
	{
		this.handler = handler;
		// TODO Auto-generated constructor stub
	}

	// ////////////////����Ϣ/////////////////////////////////////////
	public String read()
	{
		String s = null;
		try
		{
			InputStream inp = sp.getInputStream();
			if (inp.available() > 0)
			{
				byte[] readBuffer = new byte[inp.available()];
				inp.read(readBuffer);
				for (int i = 0; i < readBuffer.length; i++)
				{
					if (tempMapCur == 3)
					{
						tempMapCur = 0;
						tempMaps.put(tempMapKey, tempMap);
						handler.getTemperature(tempMaps);
					}
					/**
					 * ��ȡ�¶ȴ���tempMaps
					 */
					if ((readBuffer[i] == Constants.TEMP_TAG1
							|| readBuffer[i] == Constants.TEMP_TAG2
							|| readBuffer[i] == Constants.TEMP_TAG3 || readBuffer[i] == Constants.TEMP_TAG4)
							&& tempMapCur == 0)
					{
						chCur = 0;// �жϲ�����ȡ
						tempMapKey = readBuffer[i];
						tempMap = new HashMap<Integer, Byte>();
						tempMapCur++;
					} else if (tempMapCur == 1)
					{
						tempMap.put(tempMapCur, readBuffer[i]);
						tempMapCur++;
					} else if (tempMapCur == 2)
					{
						tempMap.put(tempMapCur, readBuffer[i]);
						tempMapCur++;
					}

					/**
					 * ��ȡ����
					 */
					if (readBuffer[i] == Constants.CH_READ_TAG && chCur == 0)
					{
						tempMapCur = 0; // �ж��¶ȶ�ȡ
						chCur++;
						System.out.println("tag" + readBuffer[i]);
					} else if (chCur == 1)
					{
						System.out.println("chnum" + readBuffer[i]);
						chNum = readBuffer[i];
						handler.setChannelNum(chNum);
						chCur = 0;
					}
					System.err.println("read:" + readBuffer[i]);
					
					//�����Ƿ�ע��
					if ((readBuffer[i] == Constants.ONU1_REG_TAG
							|| readBuffer[i] == Constants.ONU2_REG_TAG
							|| readBuffer[i] == Constants.ONU3_REG_TAG || readBuffer[i] == Constants.ONU4_REG_TAG)
							&& chCur == 0 && tempMapCur == 0)
					{
						handler.setONUReg(readBuffer[i] & 0x03 - 1);
					}
					System.out.println(readBuffer[i]);
				}

			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;

	}

	// /////////////////д��Ϣ////////////////////////////////////////
	public void write(byte b)
	{
		if (state == true)
		{
			try
			{
				OutputStream out = sp.getOutputStream();
				out.write(b);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// ��ڰ�˿�
	public ArrayList<String> findComs()
	{
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> tempCommPortIdentifiers = CommPortIdentifier
				.getPortIdentifiers();
		ArrayList<String> comsList = new ArrayList<String>();
		while (tempCommPortIdentifiers.hasMoreElements())
		{
			CommPortIdentifier commPortIdentifier = tempCommPortIdentifiers
					.nextElement();
			String comNameString = commPortIdentifier.getName();
			String comString = comNameString.substring(0, 3);
			if (comString.equals("COM"))
			{
				comsList.add(comNameString);
			}
		}
		return comsList;
	}

	// /////////////////�򿪶˿�//////////////////////////////////////
	public boolean openCom()
	{
		try
		{
			portId = CommPortIdentifier.getPortIdentifier(com);
			System.out.println("portId:" + com);
			sp = (SerialPort) portId.open("WDMPON", 2000);
			sp.addEventListener(this);
			sp.notifyOnDataAvailable(true);

		} catch (PortInUseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Port in use!");
			return false;
		} catch (TooManyListenersException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Too many listeners");
			return false;
		} catch (NoSuchPortException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("Can't find " + com);
			return false;
		}
		return true;
	}

	// /////////////////�رն˿�//////////////////////////////////////
	public void closeCom()
	{
		sp.close();
	}

	// /////////////////���ò����ʵ���Ϣ///////////////////////////
	public void setParamas(int aBaudRate, int aWordSize, int aStopBit,
			int aPariy)
	{
		BaudRate = aBaudRate;
		Pariy = aPariy;
		StopBit = aStopBit;
		WordSize = aWordSize;
		if (state == true)
		{
			try
			{
				sp.setSerialPortParams(BaudRate, WordSize, StopBit, Pariy);
			} catch (UnsupportedCommOperationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void serialEvent(SerialPortEvent event)
	{
		// TODO Auto-generated method stub
		switch (event.getEventType())
		{
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			// read();
			back(read());
			break;
		}
	}

	// /////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////
	public int getBaudRate()
	{
		return BaudRate;
	}

	// /////////////////////////////////////////////////////////
	public int getWordSize()
	{
		return WordSize;
	}

	// /////////////////////////////////////////////////////////
	public int getStopBit()
	{
		return StopBit;
	}

	// /////////////////////////////////////////////////////////
	public int getPariy()
	{
		return Pariy;
	}

	// /////////////////////////////////////////////////////////
	public String getCom()
	{
		return com;
	}

	public void setCom(String com)
	{
		this.com = com;
	}

	public String toString()
	{
		return new String("");

	}

	public abstract void back(String s1);
}
