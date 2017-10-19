package com.donutellko;

import java.util.ArrayList;

class FatBigUInt { // Хранит число как набор цифр
	ArrayList<Byte> digits;

	public FatBigUInt(int a) {
		digits = new ArrayList<>();
		if (a == 0)
			digits.add((byte) 0);
		while (a > 0) {
			digits.add((byte) (a % 10));
			a /= 10;
		}
	}
	public FatBigUInt(ArrayList<Byte> digits) {
		this.digits = (ArrayList<Byte>) digits.clone();
	}

	public void add(FatBigUInt a) {
		ArrayList<Byte> digits2 = a.getDigitsList();
		if (digits.size() < digits2.size()) {
			ArrayList<Byte> tmp = digits2;
			digits2 = digits;
			digits = digits2;
		}

		byte to_next = 0;
		int i;
		for (i = 0; i < digits2.size(); i++) {
			byte tmp = (byte) (digits2.get(i) + digits.get(i) + to_next);
			digits.set(i, (byte) (tmp % 10));
			to_next = (byte) (tmp / 10);
		}

		while (to_next > 0) {
			if (digits.size() <= i)
				digits.add((byte) 0);
			byte tmp = (byte) (digits.get(i) + to_next);
			digits.set(i, (byte) (tmp % 10));
			to_next = (byte) (tmp / 10);
			i++;
		}
	}

	public void add(int a) {
		add(new FatBigUInt(a));
	}

	public ArrayList<Byte> getDigitsList() {
		return (ArrayList<Byte>) digits.clone();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Byte b : digits)
			s.insert(0, b);
		return s.toString();
	}
}