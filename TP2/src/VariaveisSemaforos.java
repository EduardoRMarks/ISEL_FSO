import java.util.concurrent.Semaphore;

public class VariaveisSemaforos {
	
	private Semaphore sMutexEvitar = new Semaphore(0);
	private Semaphore sMutexFugir = new Semaphore(0);
	private Semaphore sMutexVaguear = new Semaphore(0);
	
	private int numEvitar;
	private int numFugir;
	private int numVaguear;
	
	public VariaveisSemaforos() {
		numEvitar = 0;
		numFugir = 0;
		numVaguear = 0;
	}
	
	public Semaphore getSemaphoreEvitar() {
		return sMutexEvitar;
	}
	
	public Semaphore getSemaphoreFugir() {
		return sMutexFugir;
	}
	
	public Semaphore getSemaphoreVaguear() {
		return sMutexVaguear;
	}
	
	
	public void setNumEvitar(int numEvitar) {
		this.numEvitar = numEvitar;
	}
	
	public int getNumEvitar() {
		return this.numEvitar;
	}
	
	public void setNumFugir(int numFugir) {
		this.numFugir = numFugir;
	}
	
	public int getNumFugir() {
		return this.numFugir;
	}
	
	public void setNumVaguear(int numVaguear) {
		this.numVaguear = numVaguear;
	}
	
	public int getNumVaguear() {
		return this.numVaguear;
	}
}
