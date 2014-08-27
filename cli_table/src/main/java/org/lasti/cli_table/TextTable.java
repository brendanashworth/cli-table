package org.lasti.cli_table;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TextTable {
	private int indexNum = 0;
	private Map<Integer, Object[]> dataStore = new HashMap<Integer, Object[]>();
	private Entry<Integer, Boolean> orderBy = null;
	private Object modifyLockObj = new Object();
	protected char bar_6_12 = '│';
	protected char bar_3_9 = '─';
	protected char bar_3_6 = '┌';
	protected char bar_6_9 = '┐';
	protected char bar_9_12 = '┘';
	protected char bar_3_12 = '└';
	protected char bar_3_6_12 = '├';
	protected char bar_3_6_9 = '┬';
	protected char bar_6_9_12 = '┤';
	protected char bar_3_9_12 = '┴';
	protected char bar_3_6_9_12 = '┼';
	private Object[] barObj = new Object[1];

	public TextTable addRow(Object... dataArr) {
		synchronized (modifyLockObj) {
			dataStore.put(indexNum, dataArr);
			indexNum++;
		} // sync
		return this;
	} // addRow

	public TextTable addRow(Collection dataColl) {
		synchronized (modifyLockObj) {
			dataStore.put(indexNum, dataColl.toArray());
			indexNum++;
		} // sync
		return this;
	} // addRow

	public TextTable addRow(Iterable dataIter) {
		synchronized (modifyLockObj) {
			List<Object> dataList = new ArrayList<Object>();
			for (Object data : dataIter)
				dataList.add(data);
			dataStore.put(indexNum, dataList.toArray());
			indexNum++;
		} // sync
		return this;
	} // addRow

	public TextTable addBar() {
		addRow(barObj);
		return this;
	} // addBar

	public TextTable where(int col, WhereComparator whereComp) {
		synchronized (modifyLockObj) {
			for (int key = 0; key < indexNum; key++) {
				Object[] row=dataStore.get(key);
				if(row!=null && !whereComp.onCompare(row[col]))
					dataStore.remove(key);
			} //for i
		} // sync
		return this;
	} // where

	public TextTable orderBy(int col, boolean asc) {
		this.orderBy = new AbstractMap.SimpleEntry<Integer, Boolean>(col, asc);
		return this;
	} // orderBy

	private void order(){
		if (this.orderBy == null) 
			return;
		
		synchronized (modifyLockObj) {
			for (int repeat = 0; repeat < indexNum; repeat++) {
				for (int rowIndex = 0; rowIndex < indexNum-1; rowIndex++) {
					int leftIndex=rowIndex;
					int rightIndex=rowIndex+1;
					
					if(dataStore.get(leftIndex)==barObj || dataStore.get(rightIndex)==barObj)
						continue;
					if(!(dataStore.get(leftIndex)[orderBy.getKey()] instanceof Comparable) || !(dataStore.get(rightIndex)[orderBy.getKey()] instanceof Comparable))
						continue;
					
					boolean isAsc=orderBy.getValue();
					Comparable leftData=(Comparable) dataStore.get(leftIndex)[orderBy.getKey()];
					Comparable rightData=(Comparable) dataStore.get(rightIndex)[orderBy.getKey()];
					int compResult=leftData.compareTo(rightData);
					if(compResult<0 && !isAsc){
						Object[] tmpSwitch=dataStore.get(leftIndex);
						dataStore.put(leftIndex, dataStore.get(rightIndex));
						dataStore.put(rightIndex, tmpSwitch);
					} else if(compResult>0 && isAsc){
						Object[] tmpSwitch=dataStore.get(leftIndex);
						dataStore.put(leftIndex, dataStore.get(rightIndex));
						dataStore.put(rightIndex, tmpSwitch);
					} //if
				} //for i
			} //for repeat
		} //sync
	} //order
	
	@Override
	public synchronized String toString() {
		order();

		Map<Integer, Integer> colWidth=new HashMap<Integer, Integer>();
		for (Object[] row : dataStore.values()){
			if(row!=null && row!=barObj) {
				for (int col = 0; col < row.length; col++) {
					if(colWidth.get(col)==null || row.toString().length()>colWidth.get(col)){
						colWidth.put(col, Math.round((float)row[col].toString().length()/2)*2 );
					} //if
				} //for col
			} //if
		} //for 
		
		StringBuilder outputSb = new StringBuilder();
		
		//build top
		outputSb.append(bar_3_6);
		for (int col = 0; col < colWidth.size(); col++) {
			outputSb.append(TextUtil.repeatCh(bar_3_9, Math.round(colWidth.get(col)/2)));
			if(col<colWidth.size()-1)
				outputSb.append(bar_3_6_9);
		} //for i
		outputSb.append(bar_6_9).append("\n");
	
		//build data
		for (int i = 0; i < indexNum; i++) {
			Object[] row=dataStore.get(i);
			if(row==null)
				continue;
				
			if(row==barObj){
				//build bar
				outputSb.append(bar_3_6_12);
				for (int col = 0; col < colWidth.size(); col++) {
					outputSb.append(TextUtil.repeatCh(bar_3_9, Math.round(colWidth.get(col)/2)));
					if(col<colWidth.size()-1)
						outputSb.append(bar_3_6_9_12);
				} //for i
				outputSb.append(bar_6_9_12).append("\n");	
			}else{
				outputSb.append(bar_6_12);
				for (int col = 0; col < row.length; col++) {
					String rowData=row[col].toString();
					int repeatBlank=colWidth.get(col)-rowData.length();
					outputSb.append(rowData);
					if(repeatBlank!=0){
						outputSb.append(TextUtil.repeatCh(' ', repeatBlank));
					} //if
					outputSb.append(bar_6_12);
				} //for col
				outputSb.append("\n");
			} //if
		} //for i
		
		//build bottom
		outputSb.append(bar_3_12);
		for (int col = 0; col < colWidth.size(); col++) {
			outputSb.append(TextUtil.repeatCh(bar_3_9, Math.round(colWidth.get(col)/2)));
			if(col<colWidth.size()-1)
				outputSb.append(bar_3_9_12);
		} //for i
		outputSb.append(bar_9_12).append("\n");
		
		return outputSb.toString();
	} // toString
} // class