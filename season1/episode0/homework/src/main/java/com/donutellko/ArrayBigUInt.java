package com.donutellko;

import java.util.Arrays;

public class ArrayBigUInt {

	// -9 * 10^18 < long < 9 * 10 ^ 18
	/// следовательно, храним по 18 знаков: от 0 до 999_999_999_999_999_999L
	long CELL_MAX = 1_000_000_000_000_000_000L;
	long[] arr;
	int size;

	// capacity >= количество цифр / 64 + 1
	public ArrayBigUInt (long initial, int capacity) {
		arr = new long[capacity];
		arr[0] = initial;
		size = 1;
	}

	public ArrayBigUInt add(ArrayBigUInt a){
		add(a.getArray(), a.getSize());
		return this;
	}

	private void add(long[] a, int count) {
		if (count > size) size = count;
		long to_next = 0;

		int i = 0;
		for (; i < count; i++) {
			long tmp = arr[i] + a[i] + to_next;
			to_next = 0;
			if (tmp > CELL_MAX) {
				to_next = tmp / CELL_MAX;
				tmp %= CELL_MAX;
			}
			arr[i] = tmp;
		}

		if (to_next > 0) add(to_next, i);
	}

	private void add(long a, int index) {
		if (index >= size) size = index + 1;

		long tmp = arr[index] + a;
		if (tmp > CELL_MAX) {
			long extra = tmp / CELL_MAX;
			add(extra, index + 1);
			tmp %= CELL_MAX;
		}
		arr[index] = tmp;
	}

	long[] getArray() {
		return arr;
	}

	int getSize() {
		return size;
	}

	@Override
	public String toString() {
//		System.out.println(size);
		StringBuilder sb = new StringBuilder();
		for (int i = size - 1; i >=0; i--)
			sb.append(arr[i]);
		return sb.toString();
	}

	public ArrayBigUInt minus(ArrayBigUInt a) {
		minus(a.getArray(), a.getSize());
		return this;
	}

	private void minus(long[] a, int count) {
		if (count > size) size = count;
		long to_next = 0;

		int i = 0;
		for (; i < count; i++) {
			long tmp = arr[i] - a[i] + to_next;
			to_next = 0;
			if (tmp < 0) {
				to_next = tmp / CELL_MAX;
				tmp %= CELL_MAX;
			}
			arr[i] = tmp;
		}

		if (to_next > 0) add(to_next, i);
	}

	public ArrayBigUInt sqr() {
		long[] tmp = Arrays.copyOf(arr, size);
		this.mult(tmp);
		return this;
	}

	public ArrayBigUInt mult(ArrayBigUInt a) {
		long[] tmp = Arrays.copyOf(a.getArray(), a.getSize());
		mult(tmp);
		return this;
	}

	private void mult(long[] tmp) {
		// TODO
	}

}
