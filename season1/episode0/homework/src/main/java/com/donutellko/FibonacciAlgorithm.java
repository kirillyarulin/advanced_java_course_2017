package com.donutellko;

import edu.technopolis.FibonacciAlgorithm;

class EasyFib implements FibonacciAlgorithm {

	@Override
	public String evaluate(int index) {
		ArrayBigUInt
				prev = new ArrayBigUInt(0, index / 64 + 2),
				cur = new ArrayBigUInt(1, index / 64 + 2);
		for (int i = 1; i < index; i++) {
			prev.add(cur);
			ArrayBigUInt tmp = cur;
			cur = prev;
			prev = tmp;
		}

		return cur.toString();
	}
}

class RecurFib implements FibonacciAlgorithm {

	@Override
	public String evaluate(int index) {
		return recurfib(index).toString();
	}

	ArrayBigUInt recurfib(int index) {
		if (index == 0) return new ArrayBigUInt(0, 1);
		if (index == 1) return new ArrayBigUInt(1, 1);
		return recurfib(index - 1).add(recurfib(index - 2));
	}
}

class SmartFib implements FibonacciAlgorithm {

	@Override
	public String evaluate(int index) {
		return fib(index).toString();
	}

	ArrayBigUInt fib(int n) {
		if (n == 0) return new ArrayBigUInt(0, 1);
		if (n == 1) return new ArrayBigUInt(1, 1);

		ArrayBigUInt
				f0 = fib(n / 2),
				f1 = fib(n / 2 + 1);

		if (n % 2 == 0) {
			return f0.mult(f1.add(f1).minus(f0)); // F_{2n} = F_n (2F_{n+1} — F_n)
		} else {
			return f0.sqr().add(f1.sqr()); //F_{2n+1} = F_n^2 + F_{n+1}^2
		}
	}
}
