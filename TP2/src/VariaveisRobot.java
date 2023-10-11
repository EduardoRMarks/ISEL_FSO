import java.util.concurrent.Semaphore;

public class VariaveisRobot {
	
	private String nomeRobot;
	private String consola[];
	private final int nLinhas = 10;
	private int linhaAtual;
	private int raio;
	private int distancia;
	private int angulo;
	private boolean robotAtivo;
	protected RobotEV3 robot;
	private Semaphore msgConsola;
	
	public VariaveisRobot() {
		setRobotSim();
		robotAtivo = false;
		nomeRobot = "EV";
		raio = 30;
		angulo = 90;
		distancia = 50;
		consola = new String[nLinhas];
		linhaAtual = 0;
		msgConsola = new Semaphore(0);
		for (int i = 0; i < nLinhas; i++) {
			consola[i] = new String("");
		}
    }
	
	public String getNomeRobot() {
		return nomeRobot;
	}

	public void setNomeRobot(String nomeRobot) {
		this.nomeRobot = nomeRobot;
	}

	public int getRaio() {
		return raio;
	}

	public void setRaio(int raio) {
		this.raio = raio;
	}

	public int getDistancia() {
		return distancia;
	}

	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}

	public int getAngulo() {
		return angulo;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public boolean isRobotAtivo() {
		return robotAtivo;
	}

	public void setRobotAtivo(boolean ativo) {
		this.robotAtivo = ativo;
	}

	public void setRobotSim() {
		this.robot = new MyRobotLegoEV3Sim();
	}
	
	public void setRobot() {
		this.robot = new MyRobotLegoEV3();
	}

	public String getConsola() {
		String c = "";
		try {
			msgConsola.acquire();
			for (int i = 0; i < nLinhas; i++) {
				c += consola[i] + "\n";
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public int getSensorToque() {
		return robot.SensorToque();
	}
	
	public int getSensorProximidade() {
		return robot.SensorProximidade();
	}

	public void addLinha(String s) {
		if (linhaAtual == nLinhas) {
			for (int i = 0; i < nLinhas - 1; i++) {
				consola[i] = consola [i+1];
			}
			consola[linhaAtual-1] = s;
		} else {
			consola[linhaAtual] = s;
			linhaAtual++;
		}
		msgConsola.release();
	}
}
