package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RecordSet extends ArrayList<Row> {
	private static final long serialVersionUID = -5358156452541746165L;
	
	public int find(String column, Object value) {
		for (int i = 0; i < this.size(); ++i) {
			if (this.get(i).get(column).equals(value)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public boolean contains(String column, Object value) {
		return this.find(column, value) != -1;
	}
}
