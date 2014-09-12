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
	
	protected char horizontalBar = '-';
	protected char verticalBar = '|';
	protected char plusSign = '+';
	
	private Object[] barObj = new Object[1];

	/**
	 * Creates a {@link TextTable} object.
	 * @return a new {@link TextTable}
	 */
	public TextTable() {
		
	}

	/**
	 * Adds a row to the table.
	 * @param  dataArr the objects to add
	 * @return         the table
	 */
	public TextTable addRow(Object... dataArr) {
		synchronized (modifyLockObj) {
			dataStore.put(indexNum, dataArr);
			indexNum++;
		}

		return this;
	}

	/**
	 * Adds a row to the table, in collection form.
	 * @param  dataColl collection of the objects to add
	 * @return          the table
	 */
	public TextTable addRow(Collection dataColl) {
		synchronized (modifyLockObj) {
			dataStore.put(indexNum, dataColl.toArray());
			indexNum++;
		}

		return this;
	}

	/**
	 * Adds a row to the table.
	 * @param  dataIter the iterable
	 * @return          the table
	 */
	public TextTable addRow(Iterable dataIter) {
		synchronized (modifyLockObj) {
			List<Object> dataList = new ArrayList<Object>();
			for (Object data : dataIter)
				dataList.add(data);

			dataStore.put(indexNum, dataList.toArray());
			indexNum++;
		}

		return this;
	}

	/**
	 * Adds a bar to the table.
	 * @return the table
	 */
	public TextTable addBar() {
		addRow(barObj);
		return this;
	}

	/**
	 * Adds a where statement comparator to the table.
	 * @param  col       number of the column to check on
	 * @param  whereComp the comparator to use for checking
	 * @return           the table
	 */
	public TextTable where(int col, WhereComparator whereComp) {
		synchronized (modifyLockObj) {
			for (int key = 0; key < indexNum; key++) {
				Object[] row = dataStore.get(key);
				if(row != null && !whereComp.onCompare(row[col]))
					dataStore.remove(key);
			}
		}

		return this;
	}

	/**
	 * Orders the table by the column provided.
	 * @param  col the column number
	 * @param  asc whether it should be ascending or not
	 * @return     the table
	 */
	public TextTable orderBy(int col, boolean asc) {
		this.orderBy = new AbstractMap.SimpleEntry<Integer, Boolean>(col, asc);

		return this;
	}

	/**
	 * Orders the table data.
	 */
	private void order() {
		if (this.orderBy == null) 
			return;
		
		synchronized (modifyLockObj) {
			for (int repeat = 0; repeat < indexNum; repeat++) {
				for (int rowIndex = 0; rowIndex < indexNum - 1; rowIndex++) {
					int leftIndex = rowIndex;
					int rightIndex = rowIndex + 1;
					
					if(dataStore.get(leftIndex) == barObj || dataStore.get(rightIndex) == barObj)
						continue;
					if(!(dataStore.get(leftIndex)[orderBy.getKey()] instanceof Comparable) || !(dataStore.get(rightIndex)[orderBy.getKey()] instanceof Comparable))
						continue;
					
					boolean isAsc = orderBy.getValue();
					Comparable leftData = (Comparable) dataStore.get(leftIndex)[orderBy.getKey()];
					Comparable rightData = (Comparable) dataStore.get(rightIndex)[orderBy.getKey()];
					int compResult = leftData.compareTo(rightData);
					if(compResult < 0 && !isAsc) {
						Object[] tmpSwitch = dataStore.get(leftIndex);
						dataStore.put(leftIndex, dataStore.get(rightIndex));
						dataStore.put(rightIndex, tmpSwitch);
					} else if(compResult > 0 && isAsc) {
						Object[] tmpSwitch = dataStore.get(leftIndex);
						dataStore.put(leftIndex, dataStore.get(rightIndex));
						dataStore.put(rightIndex, tmpSwitch);
					}
				}
			}
		}
	}
	
	/**
	 * Gets the column width map.
	 * @return the column width
	 */
	private Map<Integer, Integer> getColWidthMap() {
		Map<Integer, Integer> colWidth = new HashMap<Integer, Integer>();
		// iterate over data store's values
		for (Object[] row : dataStore.values()) {
			if(row != null && row != barObj) {
				for (int col = 0; col < row.length; col++) {
					if (colWidth.get(col) == null || row[col].toString().length() > colWidth.get(col))
						colWidth.put(col, Math.round((float) row[col].toString().length() / 2) * 2);
				}
			}
		}

		return colWidth;
	}
	
	/**
	 * Converts the table to a string value.
	 * @return the string value for the table's values
	 */
	@Override
	public synchronized String toString() {
		this.order();

		Map<Integer, Integer> colWidth = this.getColWidthMap();
		StringBuilder outputSb = new StringBuilder();

		// build top
		outputSb.append(plusSign);
		for (int col = 0; col < colWidth.size(); col++) {
			outputSb.append(TextUtil.repeatCh(horizontalBar, colWidth.get(col)));
			if(col < colWidth.size() - 1)
				outputSb.append(plusSign);
		}

		outputSb.append(plusSign).append("\n");

		// build data
		for (int i = 0; i < indexNum; i++) {
			Object[] row = dataStore.get(i);
			if(row == null)
				continue;

			if(row == barObj) {
				// build bar
				outputSb.append(plusSign);

				for (int col = 0; col < colWidth.size(); col++) {
					outputSb.append(TextUtil.repeatCh(horizontalBar, colWidth.get(col)));
					if(col < colWidth.size() - 1)
						outputSb.append(plusSign);
				}

				outputSb.append(plusSign).append("\n");	
			} else {
				outputSb.append(verticalBar);

				for (int col = 0; col < row.length; col++) {
					String rowData = row[col].toString();
					int repeatBlank = colWidth.get(col) - rowData.length();

					outputSb.append(rowData);
					if(repeatBlank != 0)
						outputSb.append(TextUtil.repeatCh(' ', repeatBlank));

					outputSb.append(verticalBar);
				}

				outputSb.append("\n");
			}
		}

		// build bottom
		outputSb.append(plusSign);
		for (int col = 0; col < colWidth.size(); col++) {
			outputSb.append(TextUtil.repeatCh(horizontalBar, colWidth.get(col)));
			if(col < colWidth.size() - 1)
				outputSb.append(plusSign);
		}

		outputSb.append(plusSign).append("\n");

		return outputSb.toString();
	}
}