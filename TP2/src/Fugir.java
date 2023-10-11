import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Fugir extends JFrame implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea = new JTextArea();
	
	VariaveisRobot var;
	VariaveisSemaforos vs;
	
	private int nFug = 0;
	private boolean estadoRobot = false;
	private boolean desligado = true;
	
	byte state = 0;
	final byte parado = 0;
	final byte valSensor = 1;
	final byte fugir = 2;
	final byte dormir = 3;

	public Fugir(VariaveisRobot var, VariaveisSemaforos vs) {
		this.var = var;	
		initialize();
		this.vs = vs;
	}
	
	public void ligar(boolean estadoRobot) {
		vs.getSemaphoreFugir().release();
		this.estadoRobot = estadoRobot;
		desligado = false;
	}
	
	public void desligar(){
		desligado = true;
	}

	public void run() {
		while (true) {
			int numEvitar = vs.getNumEvitar();
			
			switch (state) {
			case parado:
				try {
					if(desligado) {						
						vs.getSemaphoreFugir().acquire();
					}
					else {
						nFug++;
						vs.setNumFugir(nFug);
						vs.getSemaphoreFugir().acquire();
						nFug--;
						vs.setNumFugir(nFug);
					}
					state = valSensor;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case valSensor:
				if(desligado || numEvitar != 0) {
					state = parado;
					break;
				}
				if(estadoRobot) {
					int sensor = var.getSensorProximidade();
					textArea.append("Valor do sensor: " + sensor + "cm\n");
					if (sensor < 50) {
						nFug++;
						vs.setNumFugir(nFug);
						textArea.append("Vou fugir!\n");
						state = fugir;
						break;
					}else {
						state = dormir;
						break;
					}					
				}
				break;
			case fugir:
				var.addLinha("Objeto detectado! Vou fugir!");
				var.robot.SetVelocidade(80);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				var.robot.Reta(50);
				var.robot.SetVelocidade(50);
				var.robot.Parar(false);
				try {
					//escolhe uma direção para virar random
					qualDirec();
					Thread.sleep(quantoTempo(0, 50, 0));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				textArea.append("Acabei de fugir\n");
				nFug--;
				vs.setNumFugir(nFug);
				
				vs.getSemaphoreVaguear().release();
				state = dormir;
				break;
			case dormir:
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				state = valSensor;
				break;
			}

		}
	}
	
	private void qualDirec() throws InterruptedException {
		Random rand = new Random();
		int d = rand.nextInt(2);
		if(d == 1) {
			var.robot.CurvarDireita(0, 90);
			Thread.sleep(50);
		}
		else {
			var.robot.CurvarEsquerda(0, 90);
			Thread.sleep(50);
		}
	}

	private long quantoTempo(int type, int val1, int val2) {
		long tempo = 0;
		int tipoAc = type;
		int t = 0;
		
		switch(tipoAc) {
		case 0:
			t += (val1 * 1000) / 30; // tempo para percorrer a reta em ms
			tempo = (long) (t);
			break;
		case 1:
			t += (((val2 / 360) * 2*3.1415 * val1)*1000)/30; // tempo para percorrer a distancia da curva em ms
			tempo = (long) (t);
			break;
		}
		return tempo;
	}

	/**
	 * Create the frame.
	 * @return 
	 */
	public void initialize() {
		setTitle("GUI Fugir");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {

			}
		});
		setBounds(100, 100, 314, 308);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JLabel HistoricoLabel = new JLabel("Historico Mensagens:");
		HistoricoLabel.setBounds(10, 11, 156, 24);
		contentPane.add(HistoricoLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 45, 282, 221);
		contentPane.add(scrollPane);

		scrollPane.setViewportView(textArea);

		setVisible(true);

	}
}
