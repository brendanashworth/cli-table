package org.lasti.cli_table;

public interface WhereComparator {

	/**
	 * Comparative function to return true or false, depending on whether or not it fits the where statement.
	 * @param  colData data of the column
	 * @return         boolean value based on whether or not it should be shown in the table
	 */
	public boolean onCompare(Object colData);
}