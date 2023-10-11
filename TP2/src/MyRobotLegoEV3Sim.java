import robot.RobotLegoEV3;

public class MyRobotLegoEV3Sim extends RobotEV3 {
	private boolean ativo = false;
	private boolean fecho = false;
	private String robotname = "";
    private String outstring;

	@Override
	public void CloseEV3() {
		robotname = "";
		ativo = false;
		fecho = true;
		outstring = "Robot desconectado";
	}
	@Override
	public Boolean OpenEV3(String s) {
		robotname = s;
		ativo = true;
		outstring = ("Conexão ao robot " + robotname + " efetuada com sucesso");
		return true;
	}
	@Override
	public void Parar(boolean s) {
		outstring = ("Parar");
	}
	@Override
	public void Reta(int s) {
		String a = String.valueOf(s);
		outstring = ("Reta com " + a + "cm");
	}
	@Override
	public void SetVelocidade(int s) {
		String a = String.valueOf(s);
		outstring = ("Velocidade: " + a);
	}
	@Override
	public void CurvarEsquerda(int raio, int ang) {
		String r = String.valueOf(raio);
		String a = String.valueOf(ang);
		outstring = ("Curva Esquerda: Raio = " + r + ", Angulo = " + a);
	}
	@Override
	public void CurvarDireita(int raio, int ang) {
		String r = String.valueOf(raio);
		String a = String.valueOf(ang);
		outstring = ("Curva Direita: Raio = " + r + ", Angulo = " + a);
	}
	
	@Override
	public int SensorToque() {
		if (Math.random() < 0.8) {
			outstring = ("Sensor: 0");
			return 0;
		}
		outstring = ("Sensor: 1 - Robot em colisao!");
		return 1;
	}
	
	@Override
	public int SensorProximidade() {
		int val = (int) (Math.random()*255);
		if (val < 50) {
			outstring = ("Sensor: 0");
			return val;
		}
		else {
			return val;
		}
	}
	
	
	public String getOutstring() {
		if (ativo) {
			return outstring;
		} else if (fecho) {
			fecho = false;
			return outstring;
		} else {
			return "Conexão não establecida. Ligue ao robot!";
		}
	}

}