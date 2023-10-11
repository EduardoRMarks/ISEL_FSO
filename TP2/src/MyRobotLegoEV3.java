import robot.RobotLegoEV3;

public class MyRobotLegoEV3 extends RobotEV3 {
	RobotLegoEV3 robot;

	public MyRobotLegoEV3() {
		robot = new RobotLegoEV3();
	}

	@Override
	public void CloseEV3() {
		robot.Parar(true);
		robot.CloseEV3();
	}

	@Override
	public Boolean OpenEV3(String s) {
		return robot.OpenEV3(s);
	}

	@Override
	public void Parar(boolean s) {
		robot.Parar(s);
		
	}

	@Override
	public void Reta(int s) {
		robot.Reta(s);
		robot.Parar(false);
	}

	@Override
	public void SetVelocidade(int s) {
		robot.SetVelocidade(s);
	}

	@Override
	public void CurvarEsquerda(int r, int a) {
		robot.CurvarEsquerda(r, a);
		robot.Parar(false);	
	}

	@Override
	public void CurvarDireita(int r, int a) {
		robot.CurvarDireita(r, a);
		robot.Parar(false);
	}

	@Override
	public int SensorToque() {
		return robot.SensorToque( RobotLegoEV3.S_1);
	}
	
	@Override
	public int SensorProximidade() {
		return (int) robot.SensorUS(RobotLegoEV3.S_2);
	}
}