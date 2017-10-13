package edu.technopolis;

import java.math.BigInteger;

/**
 * Это <b>(ну вроде)правильный</b> с точки зрения задания алгоритм.
 */
public class DummyFibonacciAlgorithm implements FibonacciAlgorithm {
    @Override
    public String evaluate(int index) {
        String st="";
        if (index<0 && (-index)%2==0)
            st+="-";
        index=Math.abs(index);
        st+=fibIter(index, index / 64+ 1).toString();
        return st;
    }

    public static BigNumber fibIter(long n, int count){
        if (n==0) return new BigNumber(count, 0L); else
        if (n==1 || N==2) return new BigNumber(count, 1L); else{
            BigNumber N1 = new BigNumber(count, 1L);
            BigNumber N2 = new BigNumber(count, 1L);
            for (long i=3; i<=n; i+=2){
                N1 = BigNumber.Summ(N1,N2);
                N2 = BigNumber.Summ(N1,N2);
            }
            if (N % 2==0) return N2; else return N1;
        }
    }
}

class BigNumber{
    public int c;
    public long[] Longs;

    public BigNumber(int Count,long d){
        Longs = new long[Count];
        c = Count;
        for (int x=0; x<Count; x++)
            Longs[x] = 0;
        Longs[Count-1] = d;
    }

    public String toString(){
        java.math.BigDecimal D = new java.math.BigDecimal(0);
        D = D.add(new java.math.BigDecimal((Longs[0])>>>1));
        D = D.add(new java.math.BigDecimal((Longs[0])>>>1));
        D = D.add(new java.math.BigDecimal(Longs[0]&1));
        for (int x=1; x<c; x++){
            D = D.multiply(new java.math.BigDecimal(1L<<32));
            D = D.multiply(new java.math.BigDecimal(1L<<32));
            D = D.add(new java.math.BigDecimal((Longs[x])>>>1));
            D = D.add(new java.math.BigDecimal((Longs[x])>>>1));
            D = D.add(new java.math.BigDecimal(Longs[x]&1));
        }
        return D.toString();
    }

    public static boolean Bigger(long L1, long L2){
        return Long.compare(L1^0x8000000000000000L, L2^0x8000000000000000L)>0;
    }

    public static BigNumber Summ(BigNumber N1, BigNumber N2){
        BigNumber N = new BigNumber(N1.C, 0L);
        N.Longs[N.c-1]=(long)(N1.Longs[N.c-1]+N2.Longs[N.c-1]);
        for (char a=(char)(N.c-1); a >0; a--){
            N.Longs[a-1] = (long)(N1.Longs[a-1]+N2.Longs[a-1]);
            if (Bigger(N1.Longs[a],N.Longs[a])||Bigger(N2.Longs[a],N.Longs[a]))
                N.Longs[a-1]++ ;
        }
        return N;
    }
}
