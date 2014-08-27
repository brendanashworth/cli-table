package org.lasti.cli_table;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextTableTest {
	@Test
	public void test_simpleTest1(){
		TextTable table=new TextTable();
		table.addRow("test1", "test2", "test3");
		table.addBar();
		table.addRow("test1", "test2", "test3");
		String expectResult="┌───┬───┬───┐\n"+
									"│test1 │test2 │test3 │\n"+
									"├───┼───┼───┤\n"+
									"│test1 │test2 │test3 │\n"+
									"└───┴───┴───┘\n";
		assertTrue(expectResult.equals(table.toString()));
	}
	
	@Test
	public void test_where1(){
		TextTable table=new TextTable();
		table.addRow("jone", 23);
		table.addRow("tom", 11);
		table.addRow("james", 44);
		table.where(1, new WhereComparator() {
			@Override
			public boolean onCompare(Object colData) {
				if((Integer)colData>40)
					return true;
				return false;
			}
		});
		String expectResult="┌───┬─┐\n"+
									"│james │44│\n"+
									"└───┴─┘\n";
		assertTrue(expectResult.equals(table.toString()));
	}
	
	@Test
	public void test_orderBy1(){
		TextTable table=new TextTable();
		table.addRow("jone", 23);
		table.addRow("tom", 11);
		table.addRow("james", 44);
		table.orderBy(1, false);
		String expectResult="┌──┬─┐\n"+
									"│james│44│\n"+
									"│jone│23│\n"+
									"│tom │11│\n"+
									"└──┴─┘\n";
		assertTrue(expectResult.equals(table.toString()));
	}
	
	@Test
	public void test_orderBy2(){
		TextTable table=new TextTable();
		table.addRow(3);
		table.addRow(2);
		table.addRow(8);
		table.addBar();
		table.addRow(4);
		table.addRow(2);
		table.addRow(5);
		table.orderBy(0, true);
		String expectResult="┌─┐\n"+
									"│2 │\n"+
									"│3 │\n"+
									"│8 │\n"+
									"├─┤\n"+
									"│2 │\n"+
									"│4 │\n"+
									"│5 │\n"+
									"└─┘\n";
		assertTrue(expectResult.equals(table.toString()));
	}
}
