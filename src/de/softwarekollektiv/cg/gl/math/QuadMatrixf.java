package de.softwarekollektiv.cg.gl.math;

public class QuadMatrixf {
	protected final double[][] m;
	protected final int n;

	static public QuadMatrixf createIdentity(int n) {
		QuadMatrixf ret = new QuadMatrixf(n);
		for (int i = 0; i < n; i++)
			ret.m[i][i] = 1.0;
		return ret;
	}

	public QuadMatrixf(int n) {
		this.m = new double[n][n];
		this.n = n;
	}

	public QuadMatrixf(double[][] val) {
		this.n = val.length;
		this.m = val;
	}

	public double get(int row, int col) {
		return m[row][col];
	}

	public void set(int row, int col, double val) {
		m[row][col] = val;
	}

	public void set(int row, int col, QuadMatrixf val) {
		assert (val.n <= n - row && val.n <= n - col);

		for (int i = 0; i < val.n; i++)
			for (int j = 0; j < val.n; j++)
				m[i + row][j + col] = val.m[i][j];
	}

	public void set(int row, int col, Vector3f val) {
		m[row][col] = val.getX();
		m[row + 1][col] = val.getY();
		m[row + 2][col] = val.getZ();
	}

	public QuadMatrixf transpose() {
		double[][] newm = new double[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				newm[i][j] = m[j][i];

		return new QuadMatrixf(newm);
	}
	

	public QuadMatrixf scale(double v) {
		double[][] newm = new double[n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				newm[i][j] = m[i][j] * v;
		
		return new QuadMatrixf(newm);
	}

	public QuadMatrixf mult(QuadMatrixf m2) {
		assert (m2.n == n);

		double[][] newm = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double sum = 0;
				for (int k = 0; k < n; k++)
					sum += m[i][k] * m2.m[k][j];
				newm[i][j] = sum;
			}
		}

		return new QuadMatrixf(newm);
	}

	public Coordinate2f mult(Coordinate2f c) {
		assert (n == 2);

		return new Coordinate2f(c.getX() * m[0][0] + c.getY() * m[0][1],
				c.getX() * m[1][0] + c.getY() * m[1][1]);
	}

	public Vector3f mult(Vector3f v) {
		assert (n == 4);

		return new Vector3f(m[0][0] * v.getX() + m[0][1] * v.getY() + m[0][2]
				* v.getZ(), m[1][0] * v.getX() + m[1][1] * v.getY() + m[1][2]
				* v.getZ(), m[2][0] * v.getX() + m[2][1] * v.getY() + m[2][2]
				* v.getZ());
	}

	public Vector4f mult(Vector4f v) {
		assert (n == 4);

		return new Vector4f(m[0][0] * v.getX() + m[0][1] * v.getY() + m[0][2]
				* v.getZ() + m[0][3] * v.getW(), m[1][0] * v.getX() + m[1][1]
				* v.getY() + m[1][2] * v.getZ() + m[1][3] * v.getW(), m[2][0]
				* v.getX() + m[2][1] * v.getY() + m[2][2] * v.getZ() + m[2][3]
				* v.getW(), m[3][0] * v.getX() + m[3][1] * v.getY() + m[3][2]
				* v.getZ() + m[3][3] * v.getW());
	}

	public QuadMatrixf negative() {
		double[][] newm = new double[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				newm[i][j] = -m[i][j];
		return new QuadMatrixf(newm);
	}
}
