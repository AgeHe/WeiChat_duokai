package com.zhushou.weichat.addfriends.wheelview;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class CityWheelAdapter implements WheelAdapter {
	
	private List<String> cities;
	private Context context;
	
	public CityWheelAdapter(Context context, int provinceId){
		this.context = context;
		this.cities = Arrays.asList(context.getResources().getStringArray(provinceId));
	}

	@Override
	public int getItemsCount() {
		return cities == null ? 0 : cities.size();
	}

	@Override
	public String getItem(int index) {
		return index <= cities.size() - 1 ? cities.get(index) : null;
	}

	@Override
	public int getMaximumLength() {
		return 7;
	}
	
	public void setCityList(int provinceId){
		this.cities = Arrays.asList(context.getResources().getStringArray(provinceId));
	}

	@Override
	public String getCurrentId(int index) {
		if (cities==null)
			return "";
//		getValue(context,cities.get(index));
		return getValue(context,cities.get(index));
	}

	public String getValue(Context context, String strText) {
		String strData = "";
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		InputStream inputStream = null;
		// 首先找到xml文件
		factory = DocumentBuilderFactory.newInstance();
		try {
			// 找到xml，并加载文档
			builder = factory.newDocumentBuilder();
			inputStream = context.getResources().getAssets()
					.open("arrays.xml");
			document = builder.parse(inputStream);
			// 找到根Element
			Element root = document.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("item");
			// 遍历根节点所有子节点
			Element cardElement;
			String strName;
			String strValues;
			for (int i = 0; i < nodes.getLength(); i++) {
				cardElement = (Element) (nodes.item(i));
				strName = cardElement.getAttribute("name");
//				Element eValues = (Element) cardElement
//						.getElementsByTagName("item").item(0);
//				strValues = eValues.getFirstChild().getNodeValue();
				strValues = cardElement.getChildNodes().item(0).getNodeValue();
				if (strValues.equals(strText)) {
					strData = strName;
					break;
				}
				if (i == nodes.getLength() - 1) {
					Log.v("", "未查到相关信息。。。");
					strData = "";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return strData;
	}
}
