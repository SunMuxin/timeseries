package com.realsight.westworld.tsp.lib.util.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;

import com.realsight.westworld.tsp.lib.series.DoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleDoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleStringSeries;
import com.realsight.westworld.tsp.lib.series.StringSeries;
import com.realsight.westworld.tsp.lib.series.TimeSeries;
import com.realsight.westworld.tsp.lib.util.Util;

public class StockData {
	
	private Path stockidPath = null;
	
	public StockData(){
		this.stockidPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "stocks.xml");
	}
	
	@SuppressWarnings("unchecked")
	public MultipleStringSeries stockidset(){
		StringSeries stock_ids = new StringSeries("stock-id");
		StringSeries method_ids = new StringSeries("method-id");
		StringSeries start_timestamps = new StringSeries("start-timestamp");
		try {
			Document doc = new SAXReader().read(stockidPath.toFile());
			Element root = doc.getRootElement();
			Iterator<Element> subs = root.elementIterator();
			Long id = 1L;
			while(subs.hasNext()) {
				Element sub = subs.next();
				stock_ids.add(new TimeSeries.Entry<String>(sub.elementText("stock-id"), id));
				method_ids.add(new TimeSeries.Entry<String>(sub.elementText("method-id"), id));
				start_timestamps.add(new TimeSeries.Entry<String>(sub.elementText("start-timestamp"), id));
				id += 1L;
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MultipleStringSeries("stockidset", stock_ids, method_ids, start_timestamps);
	}
	
	public DoubleSeries downLoadFromUrl(String url) throws IOException{
		DoubleSeries res = new DoubleSeries(url);
		CloseableHttpClient client =  HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse resp = client.execute(get);
		if (resp.getStatusLine().getStatusCode() != 200)
			return res;
		HttpEntity entity = resp.getEntity();
		String str = EntityUtils.toString(entity);
		Scanner sin = new Scanner(str);
//		HttpEntity entity = resp.getEntity();
		entity.getContent();
		StringBuffer bs = new StringBuffer();
		while(sin.hasNext()) {
			bs.append(sin.nextLine());
		}
		sin.close();
		try {
			JSONArray jsonArray = new JSONArray(bs.toString());
			JSONArray datas = jsonArray.getJSONObject(0).getJSONArray("hq");
			for (int i = 0; i < datas.length(); i++) {
				System.out.println(datas.getJSONArray(i));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if (datas.getJSONArray(i).getString(0).contains("/")){
					sdf = new SimpleDateFormat("yyyy/MM/dd");
				}
				try {
					Date date = sdf.parse(datas.getJSONArray(i).getString(0).trim());
					Long timestamp = date.getTime();
					Double value = Double.parseDouble(datas.getJSONArray(i).getString(2).trim());
					res.add(new TimeSeries.Entry<Double>(value, timestamp));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		res.sort();
		res.reverse();
		resp.close();
		client.close();
		System.out.println(url+" download success"); 
		return res;
	}

	public DoubleSeries history_data(String id) throws IOException {
		String url = "http://q.stock.sohu.com/hisHq?code=cn_"+id+"&start=20000101&end=20170529";
		return downLoadFromUrl(url);
	}
	
	public static void main(String[] args) throws IOException {
		Path root = Paths.get(System.getProperty("user.dir"), "data", "stock_data");
		MultipleStringSeries res = new StockData().stockidset();
		for (int i = 0; i < res.size(); i+=1) {
			DoubleSeries data = (new StockData()).history_data(res.getValue("stock-id", i));
			Util.writeCsv(new MultipleDoubleSeries(res.getValue("stock-id", i)+".csv", data),
					root);
		}
	}
}
