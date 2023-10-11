import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Evitar extends JFrame implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea = new JTextArea();

	VariaveisRobot var;
	VariaveisSemaforos vs;
	
	private boolean estadoRobot = false;
	private boolean desligado = true;
	private int numEv = 0;
	
	byte state = 0;
	final byte parado = 0;
	final byte valSensor = 1;
	final byte evita = 2;
	final byte dormir = 3;
	
	
	public Evitar(VariaveisRobot var, VariaveisSemaforos vs) {
		this.var = var;	
		initialize();
		this.vs = vs;
	}
	
	public void ligar(boolean estadoRobot) {
		vs.getSemaphoreEvitar().release();
		this.estadoRobot = estadoRobot;
		desligado = false;
	}
	
	public void desligar(){
		desligado = true;
	}

	public void run() {
		while (true) {
			switch (state) {
			case parado:
				try {
					vs.getSemaphoreEvitar().acquire();
					state = valSensor;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case valSensor:
				if(desligado) {
					state = parado;
					break;
				}
				if(estadoRobot) {
					int sensor = var.getSensorToque();
					textArea.append("Valor do sensor: " + sensor + "\n");
					if(sensor == 1) {
						numEv++;
						vs.setNumEvitar(numEv);
						textArea.append("Estou a bater num obstaculo, vou evitar!\n");
						state = evita;
						break;
					}
					else {
						state = dormir;
						break;
					}
				}
				break;
			case evita:
				var.addLinha("Obstaculo detectado, vou evitar!");
				var.robot.Parar(true);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				textArea.append("Parei\n");
				
				var.robot.Reta(-15);
				try {
					Thread.sleep(quantoTempo(0, 15, 0));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				textArea.append("Andei para trás\n");
				
				var.robot.CurvarEsquerda(0, 90);
				try {
					Thread.sleep(quantoTempo(1, 0, 90));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				textArea.append("Curvei à esquerda\n");
				
				var.robot.Parar(false);
				textArea.append("Parei de novo\n");
				numEv--;
				vs.setNumEvitar(numEv);
				
				if (vs.getNumFugir() > 0) {
					vs.getSemaphoreFugir().release();
				}
				else if (vs.getNumVaguear() > 0) {
					vs.getSemaphoreVaguear().release();
				}
				
				
				
				state = valSensor;
				break;
			case dormir:
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				state = valSensor;
				break;
			}

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
		setTitle("GUI Evitar");
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