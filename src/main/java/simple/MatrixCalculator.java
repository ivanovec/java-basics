package simple;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * A dense matrix calculator implementing common linear-algebra operations from
 * scratch (no external libraries).
 *
 * <p>Supported operations:
 * <ul>
 *   <li>Addition, subtraction, scalar and matrix multiplication.</li>
 *   <li>Transpose, trace, Frobenius norm.</li>
 *   <li>Determinant via LU decomposition with partial pivoting.</li>
 *   <li>Inverse and linear-system solving ({@code Ax = b}) via LU.</li>
 *   <li>Gaussian elimination to reduced row-echelon form and rank.</li>
 *   <li>Identity / zero / diagonal factory helpers.</li>
 * </ul>
 *
 * <p>The matrix is immutable: every operation returns a new instance.
 */
public final class MatrixCalculator {

    private static final double EPSILON = 1e-10;

    private final double[][] data;
    private final int rows;
    private final int cols;

    // ------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------

    public MatrixCalculator(double[][] values) {
        Objects.requireNonNull(values, "values");
        if (values.length == 0 || values[0].length == 0) {
            throw new IllegalArgumentException("Matrix must have at least one element");
        }
        this.rows = values.length;
        this.cols = values[0].length;
        this.data = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            if (values[r].length != cols) {
                throw new IllegalArgumentException("Ragged matrix: row " + r + " has "
                        + values[r].length + " columns, expected " + cols);
            }
            System.arraycopy(values[r], 0, data[r], 0, cols);
        }
    }

    public static MatrixCalculator identity(int n) {
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++) {
            m[i][i] = 1.0;
        }
        return new MatrixCalculator(m);
    }

    public static MatrixCalculator zeros(int rows, int cols) {
        return new MatrixCalculator(new double[rows][cols]);
    }

    public static MatrixCalculator diagonal(double... values) {
        int n = values.length;
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++) {
            m[i][i] = values[i];
        }
        return new MatrixCalculator(m);
    }

    // ------------------------------------------------------------------
    // Accessors
    // ------------------------------------------------------------------

    public int rows() {
        return rows;
    }

    /**
     * Returns the total number of elements in the matrix.
     */
    public int size() {
        return rows * cols;
    }

    public void callmanual() {
        rows();
    }

    public int cols() {
        return cols;
    }

    public double get(int r, int c) {
        return data[r][c];
    }

    public boolean isSquare() {
        return rows == cols;
    }

    // ------------------------------------------------------------------
    // Element-wise operations
    // ------------------------------------------------------------------

    public MatrixCalculator add(MatrixCalculator other) {
        checkSameShape(other);
        double[][] result = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = data[r][c] + other.data[r][c];
            }
        }
        return new MatrixCalculator(result);
    }

    public MatrixCalculator subtract(MatrixCalculator other) {
        checkSameShape(other);
        double[][] result = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = data[r][c] - other.data[r][c];
            }
        }
        return new MatrixCalculator(result);
    }

    public MatrixCalculator scale(double factor) {
        double[][] result = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = data[r][c] * factor;
            }
        }
        return new MatrixCalculator(result);
    }

    // ------------------------------------------------------------------
    // Matrix multiplication
    // ------------------------------------------------------------------

    public MatrixCalculator multiply(MatrixCalculator other) {
        if (cols != other.rows) {
            throw new IllegalArgumentException("Incompatible dimensions for multiplication: "
                    + rows + "x" + cols + " * " + other.rows + "x" + other.cols);
        }
        double[][] result = new double[rows][other.cols];
        for (int r = 0; r < rows; r++) {
            for (int k = 0; k < cols; k++) {
                double a = data[r][k];
                if (a == 0.0) {
                    continue;
                }
                for (int c = 0; c < other.cols; c++) {
                    result[r][c] += a * other.data[k][c];
                }
            }
        }
        return new MatrixCalculator(result);
    }

    public MatrixCalculator power(int exponent) {
        if (!isSquare()) {
            throw new IllegalArgumentException("Power is only defined for square matrices");
        }
        if (exponent < 0) {
            return inverse().power(-exponent);
        }
        MatrixCalculator result = identity(rows);
        MatrixCalculator base = this;
        int e = exponent;
        while (e > 0) {
            if ((e & 1) == 1) {
                result = result.multiply(base);
            }
            base = base.multiply(base);
            e >>= 1;
        }
        return result;
    }

    // ------------------------------------------------------------------
    // Transpose / scalar summaries
    // ------------------------------------------------------------------

    public MatrixCalculator transpose() {
        double[][] result = new double[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[c][r] = data[r][c];
            }
        }
        return new MatrixCalculator(result);
    }

    public double trace() {
        requireSquare("trace");
        double sum = 0.0;
        for (int i = 0; i < rows; i++) {
            sum += data[i][i];
        }
        return sum;
    }

    public double frobeniusNorm() {
        double sum = 0.0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sum += data[r][c] * data[r][c];
            }
        }
        return Math.sqrt(sum);
    }

    // ------------------------------------------------------------------
    // LU decomposition (partial pivoting)
    // ------------------------------------------------------------------

    /** Result of an LU decomposition with a permutation vector. */
    public static final class LuResult {
        public final double[][] lu;
        public final int[] pivot;
        public final int permutationSign;

        LuResult(double[][] lu, int[] pivot, int permutationSign) {
            this.lu = lu;
            this.pivot = pivot;
            this.permutationSign = permutationSign;
        }
    }

    private LuResult decomposeLu() {
        requireSquare("LU decomposition");
        int n = rows;
        double[][] lu = new double[n][n];
        for (int r = 0; r < n; r++) {
            System.arraycopy(data[r], 0, lu[r], 0, n);
        }
        int[] pivot = new int[n];
        for (int i = 0; i < n; i++) {
            pivot[i] = i;
        }
        int sign = 1;

        for (int col = 0; col < n; col++) {
            int maxRow = col;
            double maxVal = Math.abs(lu[col][col]);
            for (int r = col + 1; r < n; r++) {
                double v = Math.abs(lu[r][col]);
                if (v > maxVal) {
                    maxVal = v;
                    maxRow = r;
                }
            }
            if (maxVal < EPSILON) {
                // Singular column; leave as-is (determinant will be ~0).
                continue;
            }
            if (maxRow != col) {
                double[] tmp = lu[col];
                lu[col] = lu[maxRow];
                lu[maxRow] = tmp;
                int p = pivot[col];
                pivot[col] = pivot[maxRow];
                pivot[maxRow] = p;
                sign = -sign;
            }
            for (int r = col + 1; r < n; r++) {
                double factor = lu[r][col] / lu[col][col];
                lu[r][col] = factor;
                for (int c = col + 1; c < n; c++) {
                    lu[r][c] -= factor * lu[col][c];
                }
            }
        }
        return new LuResult(lu, pivot, sign);
    }

    public double determinant() {
        requireSquare("determinant");
        LuResult lu = decomposeLu();
        double det = lu.permutationSign;
        for (int i = 0; i < rows; i++) {
            det *= lu.lu[i][i];
        }
        return det;
    }

    /** Solves {@code A x = b} for a column vector {@code b}. */
    public double[] solve(double[] b) {
        requireSquare("solve");
        if (b.length != rows) {
            throw new IllegalArgumentException("Right-hand side length " + b.length
                    + " does not match matrix size " + rows);
        }
        LuResult lu = decomposeLu();
        int n = rows;

        // Apply permutation to b.
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            y[i] = b[lu.pivot[i]];
        }

        // Forward substitution (L has unit diagonal).
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                y[i] -= lu.lu[i][j] * y[j];
            }
        }

        // Back substitution.
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = y[i];
            for (int j = i + 1; j < n; j++) {
                sum -= lu.lu[i][j] * x[j];
            }
            if (Math.abs(lu.lu[i][i]) < EPSILON) {
                throw new ArithmeticException("Matrix is singular; cannot solve");
            }
            x[i] = sum / lu.lu[i][i];
        }
        return x;
    }

    public MatrixCalculator inverse() {
        requireSquare("inverse");
        int n = rows;
        double[][] inv = new double[n][n];
        double[] e = new double[n];
        for (int col = 0; col < n; col++) {
            Arrays.fill(e, 0.0);
            e[col] = 1.0;
            double[] x = solve(e);
            for (int r = 0; r < n; r++) {
                inv[r][col] = x[r];
            }
        }
        return new MatrixCalculator(inv);
    }

    // ------------------------------------------------------------------
    // Gaussian elimination: RREF and rank
    // ------------------------------------------------------------------

    public MatrixCalculator reducedRowEchelon() {
        double[][] m = copyData();
        int lead = 0;
        for (int r = 0; r < rows; r++) {
            if (lead >= cols) {
                break;
            }
            int pivotRow = r;
            while (Math.abs(m[pivotRow][lead]) < EPSILON) {
                pivotRow++;
                if (pivotRow == rows) {
                    pivotRow = r;
                    lead++;
                    if (lead == cols) {
                        return new MatrixCalculator(m);
                    }
                }
            }
            double[] tmp = m[r];
            m[r] = m[pivotRow];
            m[pivotRow] = tmp;

            double pivotVal = m[r][lead];
            for (int c = 0; c < cols; c++) {
                m[r][c] /= pivotVal;
            }
            for (int other = 0; other < rows; other++) {
                if (other != r) {
                    double factor = m[other][lead];
                    for (int c = 0; c < cols; c++) {
                        m[other][c] -= factor * m[r][c];
                    }
                }
            }
            lead++;
        }
        return new MatrixCalculator(m);
    }

    public int rank() {
        MatrixCalculator rref = reducedRowEchelon();
        int rank = 0;
        for (int r = 0; r < rref.rows; r++) {
            boolean nonZero = false;
            for (int c = 0; c < rref.cols; c++) {
                if (Math.abs(rref.data[r][c]) > EPSILON) {
                    nonZero = true;
                    break;
                }
            }
            if (nonZero) {
                rank++;
            }
        }
        return rank;
    }

    // ------------------------------------------------------------------
    // Equality / display
    // ------------------------------------------------------------------

    public boolean approxEquals(MatrixCalculator other, double tolerance) {
        if (rows != other.rows || cols != other.cols) {
            return false;
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (Math.abs(data[r][c] - other.data[r][c]) > tolerance) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatrixCalculator)) {
            return false;
        }
        MatrixCalculator other = (MatrixCalculator) o;
        return rows == other.rows && cols == other.cols && Arrays.deepEquals(data, other.data);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            sb.append('[');
            for (int c = 0; c < cols; c++) {
                if (c > 0) {
                    sb.append(", ");
                }
                sb.append(String.format(Locale.ROOT, "%8.4f", data[r][c]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // Internal helpers
    // ------------------------------------------------------------------

    private double[][] copyData() {
        double[][] copy = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(data[r], 0, copy[r], 0, cols);
        }
        return copy;
    }

    private void checkSameShape(MatrixCalculator other) {
        if (rows != other.rows || cols != other.cols) {
            throw new IllegalArgumentException("Shape mismatch: " + rows + "x" + cols
                    + " vs " + other.rows + "x" + other.cols);
        }
    }

    private void requireSquare(String operation) {
        if (!isSquare()) {
            throw new IllegalArgumentException(operation + " requires a square matrix, got "
                    + rows + "x" + cols);
        }
    }

    // ------------------------------------------------------------------
    // Demo
    // ------------------------------------------------------------------

    public static void main(String[] args) {
        MatrixCalculator a = new MatrixCalculator(new double[][] {
                {2, 1, 1},
                {1, 3, 2},
                {1, 0, 0}
        });

        System.out.println("A =");
        System.out.print(a);

        System.out.printf(Locale.ROOT, "det(A)   = %.4f%n", a.determinant());
        System.out.printf(Locale.ROOT, "trace(A) = %.4f%n", a.trace());
        System.out.printf(Locale.ROOT, "rank(A)  = %d%n", a.rank());
        System.out.printf(Locale.ROOT, "||A||_F  = %.4f%n", a.frobeniusNorm());

        System.out.println("\nA^-1 =");
        MatrixCalculator inv = a.inverse();
        System.out.print(inv);

        System.out.println("A * A^-1 (should be identity) =");
        System.out.print(a.multiply(inv));

        double[] b = {4, 5, 6};
        double[] x = a.solve(b);
        System.out.println("Solving A x = " + Arrays.toString(b));
        System.out.println("x = " + Arrays.toString(x));

        System.out.println("\nA^3 =");
        System.out.print(a.power(3));
    }
}
