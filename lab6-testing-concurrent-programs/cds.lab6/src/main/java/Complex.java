public class Complex {

    private double re;
    private double im;

    public Complex() {
        this.re = 0.0;
        this.im = 0.0;
    }

    public Complex(double real, double imaginary) {
        this.re = real;
        this.im = imaginary;
    }

    public static Complex sum(Complex c1, Complex c2) {
        return new Complex(c1.getRe() + c2.getRe(), c1.getIm() + c2.getIm());

    }

    public static Complex difference(Complex c1, Complex c2) {
        double real = c1.getRe() - c2.getRe();
        double imag = c1.getIm() - c2.getIm();
        return new Complex(real, imag);
    }

    public static Complex product(Complex c1, Complex c2) {
        double real = c1.getRe() * c2.getRe() - c1.getIm() * c2.getIm();
        double imag = c1.getRe() * c2.getIm() + c1.getIm() * c2.getRe();
        return new Complex(real, imag);
    }

    // return abs/modulus/magnitude sqrt(re^2 +im^2)
    public double abs() {
        return Math.hypot(getRe(), getIm());
    }

    // return a new Complex object whose value is the conjugate of this
    public Complex conjugate() {
        return new Complex(getRe(), -getIm());
    }

   

    public String toString() {
		String s = "";
		if (getRe() != 0)
			s = s + getRe();
		if (getIm() > 0)
			s = s + "+" + getIm() + "i";
		else if (getIm() < 0)
			s = s + getIm() + "i";
		return s;
	}

    
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Complex) {
            Complex c = (Complex) other;
            return (getRe() == c.getRe() && getIm() == c.getIm());
        }
        else return false;
    }

    /**
     * @return the re
     */
    public double getRe() {
        return re;
    }

    /**
     * @return the im
     */
    public double getIm() {
        return im;
    }

    
}
