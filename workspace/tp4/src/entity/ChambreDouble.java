package entity;

public class ChambreDouble extends ChambreSimple {
	public int nbLits;
	
	public ChambreDouble() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChambreDouble(int numCh, boolean balcon, boolean tv, int nbLits) {
		super(numCh, balcon, tv);
		// TODO Auto-generated constructor stub
		this.nbLits = nbLits;
	}

	@Override
	public String toString() {
		return "ChambreDouble [nbLits = " + nbLits + ", numCh = " + numCh + ", balcon = " + balcon + ", tv = " + tv + "]";
	}

	// attributs

	// constructeur empty

	// constructeur full-params

	// toString

}
