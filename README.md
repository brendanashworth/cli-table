# cli-table

> cli-table helps with drawing tables in command line interfaces.

# Usage
```java
TextTable table = new TextTable();
table.addRow("Name", "Age");
table.addBar();
table.addRow("Gracie Weber", 24);
table.addRow("Lexi O'Conner", 22);

System.out.println(table.toString());
```

```
+--------------+----+
|Name          |Age |
+--------------+----+
|Gracie Weber  |24  |
|Lexi O'Conner |22  |
+--------------+----+
```

## Order by statement
```java
TextTable table = new TextTable();

table.addRow("Name", "Age");
table.addBar();
table.addRow("Gracie Weber", 24);
table.addRow("Lexi O'Conner", 22);
table.orderBy(1, true); // order by first column, ascending

System.out.println(table.toString());
```

~~~~
+--------------+----+
|Name          |Age |
+--------------+----+
|Lexi O'Conner |22  |
|Gracie Weber  |24  |
+--------------+----+
~~~~

## Where statement
```java
TextTable table = new TextTable();

table.addRow("Name", "Age");
table.addBar();
table.addRow("Gracie Weber", 24);
table.addRow("Lexi O'Conner", 22);
table.where(1, new WhereComparator() {
	@Override
	public boolean onCompare(Object colData) {
		return (Integer) colData > 23;
	}
});

System.out.println(table.toString());
```

```
+-------------+----+
|Name         |Age |
+-------------+----+
|Gracie Weber |24  |
+-------------+----+
```
