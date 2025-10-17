package ex1;

public class ResultatModule {
	private static int cmpt = 1;
	
	private int idModule;
	private double cc;
	private double tp;
	private double exam;
	
	private static final double COEF_CC = 0.3;
	private static final double COEF_TP = 0.2;
	private static final double COEF_EXAM = 0.5;
	
	public ResultatModule(double cc, double tp, double exam) {
		// TODO Auto-generated constructor stub
		this.idModule = cmpt++;
		this.cc = cc;
		this.tp = tp;
		this.exam = exam;
	}

	public int getIdModule() {
		return idModule;
	}

	public void setIdModule(int idModule) {
		this.idModule = idModule;
	}

	public double getCc() {
		return cc;
	}

	public void setCc(double cc) {
		this.cc = cc;
	}

	public double getTp() {
		return tp;
	}

	public void setTp(double tp) {
		this.tp = tp;
	}

	public double getExam() {
		return exam;
	}

	public void setExam(double exam) {
		this.exam = exam;
	}
	
	public double calculeMoyenne() {
		return (cc * COEF_CC) + (tp * COEF_TP) + (exam * COEF_EXAM);
	}

	public boolean valideModule() {
		return calculeMoyenne() >= 10;
	}
}
