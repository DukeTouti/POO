package entity;

public class SuiteRoyale extends ChambreSimple {

	public String nomSuite;

	
	public SuiteRoyale() {
		super();
	}

	public SuiteRoyale(int numCh, boolean balcon, boolean tv, String nomSuite) {
		super(numCh, balcon, tv);
		this.nomSuite = nomSuite;
	}

	@Override
	public String toString() {
		return "toS SR ==> SuiteRoyale [nomSuite = " + nomSuite + ", numCh = " + numCh + ", balcon = " + balcon + ", tv = " + tv
				+ "]";
	}
	/*
	 * public String toString() { return super.toString() + this.nomSuite; }
	 */

}
