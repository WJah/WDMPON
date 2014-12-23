package handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AWGMap
{
	Map<String, List<String>> Channels = new HashMap<String, List<String>>();
	List<String> C205;
	List<String> C275;
	List<String> C355;
	List<String> C305;

	public AWGMap()
	{
		C205 = new ArrayList<String>();
		C205.add("1561.01");
		C205.add("1560.61");
		C205.add("1560.18");
		C205.add("1559.79");
		C205.add("1559.38");
		C205.add("1558.98");
		C205.add("1558.58");
		C205.add("1558.17");

		C275 = new ArrayList<String>();
		C275.add("1555.34");
		C275.add("1554.94");
		C275.add("1554.54");
		C275.add("1554.13");
		C275.add("1553.73");
		C275.add("1553.32");
		C275.add("1552.92");
		C275.add("1552.52");

		C305 = new ArrayList<String>();
		C305.add("1552.92");
		C305.add("1552.52");
		C305.add("1552.12");
		C305.add("1551.72");
		C305.add("1551.32");
		C305.add("1550.92");
		C305.add("1550.51");
		C305.add("1550.12");

		C355 = new ArrayList<String>();
		C355.add("1548.91");
		C355.add("1548.51");
		C355.add("1548.12");
		C355.add("1547.72");
		C355.add("1547.32");
		C355.add("1546.92");
		C355.add("1546.51");
		C355.add("1546.11");

		Channels.put("FTLX4213C205", C205);
		Channels.put("FTLX4213C275", C275);
		Channels.put("FTLX4213C355", C355);
		Channels.put("FTLX4213C305", C305);

	}

	public Map<String, List<String>> getChannels()
	{
		return Channels;
	}

}