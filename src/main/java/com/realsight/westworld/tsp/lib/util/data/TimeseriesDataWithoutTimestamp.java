package com.realsight.westworld.tsp.lib.util.data;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.realsight.westworld.tsp.lib.csv.CsvReader;
import com.realsight.westworld.tsp.lib.series.DoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleDoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleStringSeries;
import com.realsight.westworld.tsp.lib.series.StringSeries;
import com.realsight.westworld.tsp.lib.series.TimeSeries;

public class TimeseriesDataWithoutTimestamp {
	private char delimiter = ',';
	private Charset charset = null;
	private Path path = null;
	
	public TimeseriesDataWithoutTimestamp(char delimiter, Charset charset, String path){
		this.delimiter = delimiter;
		this.charset = charset;
		this.path = Paths.get(path);
	}
	
	public TimeseriesDataWithoutTimestamp(String path){
		this(',', Charset.forName("ISO-8859-1"), path);
	}
	
	public TimeseriesDataWithoutTimestamp(Path path){
		this(',', Charset.forName("ISO-8859-1"), path.toString());
	}
	
	public DoubleSeries getPropertyDoubleSeries(String name){
		DoubleSeries res = new DoubleSeries(this.path.getFileName().toString().split(".csv")[0]);
		try {
			CsvReader cr = new CsvReader(path.toString(), delimiter, charset);
			cr.readHeaders();
			if(cr.getIndex(name) == -1)
				throw new IOException("File not exists " + name + ".");
			while(cr.readRecord()){
				Double value = 0.0;
				try {
					value = Double.parseDouble(cr.get(name));
				} catch (NumberFormatException e) { 
					// passed 
				}
				res.add(new TimeSeries.Entry<Double>(value, cr.getCurrentRecord()));
			}
			cr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public DoubleSeries getPropertyDoubleSeries(int columnIndex) throws IOException{
		CsvReader cr = new CsvReader(path.toString(), delimiter, charset);
		cr.readHeaders();
		String name = cr.get(columnIndex);
		cr.close();
		return getPropertyDoubleSeries(name);
	}
	
	public StringSeries getPropertyStringSeries(String name){
		StringSeries res = new StringSeries(this.path.getFileName().toString().split(".csv")[0]);
		try {
			CsvReader cr = new CsvReader(path.toString(), delimiter, charset);
			cr.readHeaders();
			if(cr.getIndex(name) == -1)
				throw new IOException("File not exists " + name + ".");
			while(cr.readRecord()){
				res.add(new TimeSeries.Entry<String>(cr.get(name), cr.getCurrentRecord()));
			}
			cr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public MultipleStringSeries getPropertyStringSeries(){
		Collection<String> names = new ArrayList<String>();
		MultipleStringSeries res = null;
		try {
			CsvReader cr = new CsvReader(path.toString(), delimiter, charset);
			cr.readHeaders();
			for ( int i = 0; i < cr.getHeaderCount(); i++ ) {
				if (cr.getHeader(i).equals("timestamp")) continue;
				names.add(cr.getHeader(i));
			}
			List<StringSeries> datas = new ArrayList<StringSeries>();
			for ( int i = 0; i < cr.getHeaderCount(); i++ ) {
				if (cr.getHeader(i).equals("timestamp")) continue;
				datas.add(new StringSeries(cr.getHeader(i)));
			}
			while(cr.readRecord()){
				for ( int i = 0, j = 0; i < cr.getHeaderCount(); i++ ) {
					if (cr.getHeader(i).equals("timestamp")) continue;
					String value = cr.get(cr.getHeader(i));
					datas.get(j).add(
						new TimeSeries.Entry<String> (value, cr.getCurrentRecord()));
					j++;
				}
			}
			res = new MultipleStringSeries((this.path.toFile()).getName().split(".csv")[0],datas);
			cr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public MultipleDoubleSeries getPropertyDoubleSeries(){
		Collection<String> names = new ArrayList<String>();
		MultipleDoubleSeries res = null;
		try {
			CsvReader cr = new CsvReader(path.toString(), delimiter, charset);
			cr.readHeaders();
			for ( int i = 0; i < cr.getHeaderCount(); i++ ) {
				if (cr.getHeader(i).equals("timestamp")) continue;
				names.add(cr.getHeader(i));
			}
			List<DoubleSeries> datas = new ArrayList<DoubleSeries>();
			for ( int i = 0; i < cr.getHeaderCount(); i++ ) {
				if (cr.getHeader(i).equals("timestamp")) continue;
				datas.add(new DoubleSeries(cr.getHeader(i)));
			}
			while(cr.readRecord()){
				for ( int i = 0, j = 0; i < cr.getHeaderCount(); i++ ) {
					if (cr.getHeader(i).equals("timestamp")) continue;
					Double value = 0.0;
					try {
						value = Double.parseDouble(cr.get(cr.getHeader(i)));
					} catch (NumberFormatException e) {
						// passed
					}
					datas.get(j).add(
						new TimeSeries.Entry<Double> (value, cr.getCurrentRecord()));
					j++;
				}
			}
			res = new MultipleDoubleSeries((this.path.toFile()).getName().split(".csv")[0],datas);
			cr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
