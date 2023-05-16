package com.example.demo.util;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 随机红包算法  伊辉_集团提供
 *
 * @author guoguangxiao
 * @date 2020/7/6 10:25:59
 */
public class RedEnvelope {

    private static final int TOTAL_MAX = 9999999;
    private static final int SINGLE_MAX = 5000 * 10;
    private static final int SINGLE_MIN = 1 * 10;

    /* This algorithm comes from MT19937: Real number version */
    private static class MT {
        /* Period parameters */
        private static final int N = 624;
        private static final int M = 397;
        /* constant vector a */
        private static final int MATRIX_A = 0x9908b0df;
        /* most significant w-r bits */
        private static final int UPPER_MASK = 0x80000000;
        /* least significant r bits */
        private static final int LOWER_MASK = 0x7fffffff;
        /* Tempering parameters */
        private static final int TEMPERING_MASK_B = 0x9d2c5680;
        private static final int TEMPERING_MASK_C = 0xefc60000;

        private static final int[] MAGIC = new int[]{0x0, MATRIX_A};
        /* magic[x] = x * MATRIX_A  for x=0,1 */

        private static final AtomicInteger SEED1 = new AtomicInteger(1170416187);

        /* L'Ecuyer, "Tables of Linear Congruential Generators of Different Sizes and Good Lattice Structure", 1999 */
        private static int seed1() {
            for (; ; ) {
                int current = SEED1.get();
                int next = current * 48413;
                if (SEED1.compareAndSet(current, next))
                    return next;
            }
        }

        private static int seed2() {
            return (int) System.nanoTime();
        }

        private static int seed() {
            return (seed1() ^ seed2()) ^ 0x7fffffff;
        }

        private final int[] mt = new int[N];
        private int mti;

        MT(int seed) {
            //int i;
            if (seed == 0)
                seed = 4357;
            /* setting initial seeds to mt[N] using         */
            /* the generator Line 25 of Table 1 in          */
            /* [KNUTH 1981, The Art of Computer Programming */
            /*    Vol. 2 (2nd Ed.), pp102]                  */
            for (mti = 0; mti < N; mti++) {
                mt[mti] = seed & 0xffff0000;
                seed = 69069 * seed + 1;
                mt[mti] |= (seed & 0xffff0000) >> 16;
                seed = 69069 * seed + 1;
            }
        }

        MT() {
            this(seed());
        }

        /* Returns an int [0..0x7FFFFFFF] */
        int next() {
            int y;
            int i = this.mti;

            if (i >= N) { /* generate N words at one time */

                int kk;
                for (kk = 0; kk < N - M; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + M] ^ (y >> 1) ^ MAGIC[y & 0x1];
                }
                for (; kk < N - 1; kk++) {
                    y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                    mt[kk] = mt[kk + (M - N)] ^ (y >> 1) ^ MAGIC[y & 0x1];
                }
                y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                mt[N - 1] = mt[M - 1] ^ (y >> 1) ^ MAGIC[y & 0x1];
                i = 0;
            }

            y = mt[i++];
            // Tempering
            y ^= (y >>> 11);
            y ^= (y << 7) & TEMPERING_MASK_B;
            y ^= (y << 15) & TEMPERING_MASK_C;
            y ^= (y >>> 18);
            mti = i;
            if (y < 0)
                y += 0x7FFFFFFF;
            return y;
        }

        /* Uniform distribution sample: [0,1) */
        double uniformDistribution() {
            return next() * (1.0 / 0x7fffffff);
        }

        /* Uniform distribution sample: [min,max] */
        int uniformDistribution(int min, int max) {
            int range = max - min + 1;
            return (int) (uniformDistribution() * range) + min;
        }

        /* Normal (Gaussian) distribution sample: [0,1) */
        double normalDistribution() {
            double u1 = uniformDistribution();
            double u2 = uniformDistribution();
            double r = Math.sqrt(-2.0 * Math.log(u1));
            double theta = 2.0 * Math.PI * u2;
            return r * Math.sin(theta);
        }

    }

    private static final MT mt = new MT();

    private static void next(TreeSet<Integer> buffer, int total) {
        for (; ; ) {
            Integer value;
            synchronized (mt) {
                value = mt.uniformDistribution(0, total - 1);
            }
            if (!buffer.contains(value)) {
                buffer.add(value);
                break;
            }
        }
    }

    private static Integer[] splitIndexes(int total, int n) {
        TreeSet<Integer> indexes = new TreeSet<Integer>();
        for (int i = 1; i < n; i++) {
            next(indexes, total);
        }
        return indexes.toArray(new Integer[n - 1]);
    }

    private static boolean checkResult(int[] result) {
        for (int i = 0; i < result.length; i++) {
            if (result[i] > SINGLE_MAX || result[i] <= 0)
                return false;
        }
        return true;
    }

    private static void shuffle(int[] values) {
        for (int i = values.length - 1; i > 0; i--) {
            int j = mt.uniformDistribution(0, i);
            int v = values[i];
            values[i] = values[j];
            values[j] = v;
        }
    }

    public static int[] SplitMoney(int total, int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must > 0");
        if (total < n)
            throw new IllegalArgumentException("total must >= n * " + SINGLE_MIN);
        if (total > TOTAL_MAX)
            throw new IllegalArgumentException("total must <= " + TOTAL_MAX);
        if (total > n * SINGLE_MAX)
            throw new IllegalArgumentException("total must <= n * " + SINGLE_MAX);

        int[] result = new int[n];

        if (n == 1) {
            result[0] = total;
            return result;
        }

        if (total == n * SINGLE_MAX) {
            for (int i = 0; i < n; i++)
                result[i] = SINGLE_MAX;
            return result;
        }

        int base;
        if (total > (n - 1) * SINGLE_MAX)
            base = total - (n - 1) * SINGLE_MAX;
        else
            base = total / n / 10;

        for (; ; ) {
            for (int i = 0; i < n; i++)
                result[i] = base;
            int remain = total - base * n;
            Integer[] indexes = splitIndexes(remain, n);
            result[0] += indexes[0];
            for (int i = 1; i < n - 1; i++) {
                result[i] += indexes[i] - indexes[i - 1];
            }
            result[n - 1] += remain - indexes[n - 2];
            if (checkResult(result)) {
                shuffle(result);
                return result;
            }
        }
    }

    public static int[] SplitMoney(int total, int n, int singleMax) {
        if (n <= 0)
            throw new IllegalArgumentException("n must > 0");
        if (total < n)
            throw new IllegalArgumentException("total must >= n * " + SINGLE_MIN);
        if (total > TOTAL_MAX)
            throw new IllegalArgumentException("total must <= " + TOTAL_MAX);
        if (total > n * singleMax)
            throw new IllegalArgumentException("total must <= n * " + singleMax);

        int[] result = new int[n];

        if (n == 1) {
            result[0] = total;
            return result;
        }

        if (total == n * singleMax) {
            for (int i = 0; i < n; i++)
                result[i] = singleMax;
            return result;
        }

        int base;
        if (total > (n - 1) * singleMax)
            base = total - (n - 1) * singleMax;
        else
            base = total / n / 10;

        for (; ; ) {
            for (int i = 0; i < n; i++)
                result[i] = base;
            int remain = total - base * n;
            Integer[] indexes = splitIndexes(remain, n);
            result[0] += indexes[0];
            for (int i = 1; i < n - 1; i++) {
                result[i] += indexes[i] - indexes[i - 1];
            }
            result[n - 1] += remain - indexes[n - 2];
            if (checkResult(result, singleMax)) {
                shuffle(result);
                return result;
            }
        }
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        System.out.println(Arrays.toString(SplitMoney(10, 10, 40)));
        System.out.println(System.currentTimeMillis() - l);
    }

    private static boolean checkResult(int[] result, int singleMax) {
        for (int i = 0; i < result.length; i++) {
            if (result[i] > singleMax || result[i] <= 0)
                return false;
        }
        return true;
    }

//    public static double getRandomMoney(RedPackage _redPackage) {
//        // remainSize 剩余的红包数量
//        // remainMoney 剩余的钱
//        if (_redPackage.remainSize == 1) {
//            _redPackage.remainSize--;
//            return (double) Math.round(_redPackage.remainMoney * 100) / 100;
//        }
//        Random r = new Random();
//        double min = 0.01;
//        double max = _redPackage.remainMoney / _redPackage.remainSize * 2;
//        double money = r.nextDouble() * max;
//        money = money <= min ? 0.01 : money;
//        money = Math.floor(money * 100) / 100;
//        _redPackage.remainSize--;
//        _redPackage.remainMoney -= money;
//        return money;
//    }
}
