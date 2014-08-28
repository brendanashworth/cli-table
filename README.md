cliTable
========

cliTable help to draw table in command line interface.

# usage
## simple usage
~~~~
TextTable table = TextTable();
table.addRow("name", "age");
table.addBar();
table.addRow("personA", 24);
table.addRow("personB", 22);
System.out.println(table.toString());
~~~~


~~~~
+--------+----+
|name    |age |
+--------+----+
|personA |24  |
|personB |22  |
+--------+----+
~~~~

## order by
~~~~
TextTable table = TextTable();
table.addRow("name", "age");
table.addBar();
table.addRow("personA", 24);
table.addRow("personB", 22);
table.orderBy(1, true); //order by first column asc
System.out.println(table.toString());
~~~~

~~~~
+--------+----+
|name    |age |
+--------+----+
|personB |22  |
|personA |24  |
+--------+----+
~~~~

## where
~~~~
TextTable table = TextTable();
table.addRow("name", "age");
table.addBar();
table.addRow("personA", 24);
table.addRow("personB", 22);
table.where(1, new WhereComparator() {
@Override
	public boolean onCompare(Object colData) {
		if((Integer)colData>23)
			return true;
		return false;
	}
});
System.out.println(table.toString());
~~~~

~~~~
+--------+----+
|name    |age |
+--------+----+
|personA |24  |
+--------+----+
~~~~

lastiverse@gmail.com