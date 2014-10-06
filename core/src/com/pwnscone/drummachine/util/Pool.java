package com.pwnscone.drummachine.util;

import java.util.ArrayList;

public class Pool<E> extends ArrayList<E> {
	public final Class<?> classType;
	public int fill = 0;

	public Pool(Class<?> clazz) {
		this.classType = clazz;
	}

	public E add() {
		if (fill == size()) {
			try {
				add((E) classType.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		((Poolable) get(fill)).index = fill++;
		return (E) get(fill - 1);
	}

	public void remove(Poolable removed) {
		E oldLast = this.get(fill - 1);
		set(removed.index, oldLast);
		set(fill - 1, (E) removed);
		((Poolable) oldLast).index = (removed).index;
		// removed.index = fill - 1;
		fill--;
	}

	public void clear() {
		fill = 0;
	}

	public E getRandom() {
		if (fill == 0)
			return null;
		return this.get((int) Math.floor(Misc.random() * (fill - 1)));
	}
}
