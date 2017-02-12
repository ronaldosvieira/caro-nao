package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RecordSet implements Collection<Row> {
	
	List<Row> data;
	
	public RecordSet() {
		this.data = new ArrayList<Row>();
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return data.contains(o);
	}

	@Override
	public Iterator iterator() {
		return data.iterator();
	}

	@Override
	public Object[] toArray() {
		return data.toArray();
	}

	@Override
	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Row e) {
		return data.add((Row) e);
	}

	@Override
	public boolean remove(Object o) {
		return data.remove(o);
	}

	@Override
	public boolean containsAll(Collection c) {
		return data.containsAll(c);
	}

	@Override
	public boolean addAll(Collection c) {
		return data.addAll(c);
	}

	@Override
	public boolean removeAll(Collection c) {
		return data.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection c) {
		return data.retainAll(c);
	}

	@Override
	public void clear() {
		data.clear();
	}
	
}
