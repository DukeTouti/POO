package entity;

public class ChambreSimple {
	public int numCh;
	public boolean balcon;
	public boolean tv;
	
	public ChambreSimple() {
		;
	}

	public ChambreSimple(int numCh, boolean balcon, boolean tv) {
		// super();
		this.numCh = numCh;
		this.balcon = balcon;
		this.tv = tv;
	}

	@Override
	public String toString() {
		return "toS chS ==> " + "ChambreSimple [numCh = " + numCh + ", balcon = " + balcon + ", tv = " + tv + "]";
	}

}
