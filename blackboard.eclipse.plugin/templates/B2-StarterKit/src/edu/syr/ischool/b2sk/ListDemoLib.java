package edu.syr.ischool.b2sk;

import java.util.*;

public class ListDemoLib {
	
	public static String Serialize( List<String> ls, String delimiter)
	{
		String result = "";
		for (String s : ls) {
			if (result.isEmpty())
			{
				result = s;
			} else {
				result += delimiter + s;
			}
		}
		return result;
	}

	public static List<String> DeSerialize( String ls, String delimiter)
	{
		List<String> result = new ArrayList<String>();
		String[] sArray = ls.split(delimiter);		
		for (String s : sArray) {
			if (!s.isEmpty()) { result.add(s); }
		}
		return result;
	}

}
