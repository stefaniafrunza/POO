import java.util.*;

class ArrayMap<K, V> extends AbstractMap<K, V> {
	
	class ArrayMapEntry<K, V> implements Map.Entry<K, V>, Comparable<K> {

		public K key;
		public V value;

		public ArrayMapEntry(K key, V value) {
			this.key = key;
			this.value = value;
		} 

		public K getKey() {
			return this.key;
		}

		public V getValue() {
			return this.value;
		}

		public V setValue(V value) {
			V old = this.value;
			this.value = (V)value;
			return old;
		}

		public String toString() {
			String result = "\nValue: " + this.value + "\nKey: " + this.key;
			return result;
		}

		public boolean equals(Object o1) {
			if(!(o1 instanceof ArrayMapEntry))
				return false;
			ArrayMapEntry<K, V> o = (ArrayMapEntry<K, V>) o1;
			return ((this.getKey() == null ? o.getKey() == null : this.getKey().equals(o.getKey())) &&
				(this.getValue() == null ? o.getValue() == null : this.getValue().equals(o.getValue())));
		}

		public int hashCode() {
			int keyHash = (this.getKey() == null ? 0 : this.getKey().hashCode());
			int valueHash = (this.getValue() == null ? 0 : this.getValue().hashCode());
			return keyHash ^ valueHash;

		}

		public int compareTo(Object arg0) {
			return 0;
		}
	}

	public Set<Map.Entry<K, V>> entries = null;
	public ArrayList<ArrayMapEntry<K, V>> list = null;

	public ArrayMap() {
		list = new ArrayList<>();
	}

	public int size() {
		return list.size();
	}

	public Set<Map.Entry<K, V>> entrySet() {
		if(entries == null) {
			entries = new AbstractSet() {
				public void clear() {
					list.clear();
				}

				public Iterator iterator() {
					return list.iterator();
				}

				public int size() {
					return list.size();
				}
			};
		}
		return entries;
	}

	public V put(K key, V value) {
		int size = list.size();
		ArrayMapEntry entry = null;
		int i;
		if(key == null) {
			for(i = 0; i < size; i++) {
				entry = (ArrayMapEntry)(list.get(i));
				if(entry.getKey() == null)
					break;
			}
		}
		else {
			for(i = 0; i < size; i++) {
				entry = (ArrayMapEntry)(list.get(i));
				if(key.equals(entry.getKey()))
					break;
			}
		}
		V oldValue = null;
		if(i < size) {
			oldValue = (V)entry.getValue();
			entry.setValue((V)value);
		}
		else {
			list.add(new ArrayMapEntry(key, value));
		}
		return (V)oldValue;
	}

	public String toString() {
		String result = "";
		for(int i = 0; i < list.size(); ++i) 
			result += list.get(i);

		return result;
	}
}