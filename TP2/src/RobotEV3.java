public abstract class RobotEV3 {
	public abstract void CloseEV3();
	public abstract Boolean OpenEV3(String s);
	public abstract void Parar(boolean s);
	public abstract void Reta(int s);
	public abstract void SetVelocidade(int s);
	public abstract void CurvarEsquerda(int r, int a);
	public abstract void CurvarDireita(int r, int a);
	public abstract int SensorToque();
	public abstract int SensorProximidade();
}
