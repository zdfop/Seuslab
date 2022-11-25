public class Pi {
    int ntp = 25;
    double[] tp = new double[ntp];
    int tp1 = 0;
    double eps = 1e-17;

    public char getNumber(int index) {
        double pid, s1, s2, s3, s4;
        s1 = series(1, index);
        s2 = series(4, index);
        s3 = series(5, index);
        s4 = series(6, index);
        pid = 4. * s1 - 2. * s2 - s3 - s4;
        pid = pid - (int) pid + 1.;
        return getIhex(pid);
    }

    /**
     * This returns, in chx, the first nhx hex digits of the fraction of x.
     *
     * @param x - dec value
     * @return hex value
     */
    private char getIhex(double x) {
        char[] hx = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        double y = Math.abs(x);
        y = 16. * (y - Math.floor(y));
        return hx[(int) y];
    }

    /**
     * This routine evaluates the series  sum_k 16^(id-k)/(8*k+m)
     * using the modular exponentiation technique.
     *
     * @param m
     * @param id
     * @return value
     */
    private double series(int m, int id) {
        int k;
        double ak, p, s, t;

        s = 0.;

        /*  Sum the series up to id. */

        for (k = 0; k < id; k++) {
            ak = 8 * k + m;
            p = id - k;
            t = expm(p, ak);
            s = s + t / ak;
            s = s - (int) s;
        }

        /*  Compute a few terms where k >= id. */

        for (k = id; k <= id + 100; k++) {
            ak = 8 * k + m;
            t = Math.pow(16., (id - k)) / ak;
            if (t < eps) {
                break;
            }
            s = s + t;
            s = s - (int) s;
        }
        return s;
    }

    /**
     * expm = 16^p mod ak.  This routine uses the left-to-right binary exponentiation scheme.
     *
     * @param p
     * @param ak
     * @return value
     */
    private double expm(double p, double ak) {
        int i, j;
        double p1, pt, r;

        /*  If this is the first call to expm, fill the power of two table tp. */

        if (tp1 == 0) {
            tp1 = 1;
            tp[0] = 1.;

            for (i = 1; i < ntp; i++) tp[i] = 2. * tp[i - 1];
        }

        if (ak == 1.) return 0.;

        /*  Find the greatest power of two less than or equal to p. */

        for (i = 0; i < ntp; i++) if (tp[i] > p) break;

        pt = tp[i - 1];
        p1 = p;
        r = 1.;

        /*  Perform binary exponentiation algorithm modulo ak. */

        for (j = 1; j <= i; j++) {
            if (p1 >= pt) {
                r = 16. * r;
                r = r - (int) (r / ak) * ak;
                p1 = p1 - pt;
            }
            pt = 0.5 * pt;
            if (pt >= 1.) {
                r = r * r;
                r = r - (int) (r / ak) * ak;
            }
        }

        return r;
    }
}
