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


public class Vaguear extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;	
	JTextArea textArea = new JTextArea();
	
	VariaveisRobot var;
	VariaveisSemaforos vs;
	
	private int nVag = 0;
	private int[] varAc = new int[3];
	private boolean estadoRobot = false;
	private boolean desligado = true;
	
	byte state = 0;
	final byte parado = 0;
	final byte executar = 1;
	final byte esperar = 2;
	

	public Vaguear(VariaveisRobot var, VariaveisSemaforos vs) {
		this.var = var;	
		initialize();
		this.vs = vs;
	}
	
	public void ligar(boolean estadoRobot) {
		vs.getSemaphoreVaguear().release();
		this.estadoRobot = estadoRobot;		
		desligado = false;
	}
	
	public void desligar(){
		var.robot.Parar(true);
		desligado = true;
	}

	public void run(){	
		while(true) {
			int numEvitar = vs.getNumEvitar();
			int numFugir = vs.getNumFugir();
			
			switch(state) {
			case parado:
				try {
					if(desligado) {
						vs.getSemaphoreVaguear().acquire();
					}
					else {
						nVag++;
						vs.setNumVaguear(nVag);
						vs.getSemaphoreVaguear().acquire();
						nVag--;
						vs.setNumVaguear(nVag);
					}		
					state = executar;
					var.addLinha("Vou vaguear...");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case executar:
				if(desligado || numEvitar != 0 || numFugir != 0) {
					state = parado;
					break;
				}				
				if(estadoRobot) {
					gerarAcao();
					if (varAc[0] == 1) {
						var.robot.Reta(varAc[1]);
						textArea.append("Reta com " + varAc[1] + "cm \n");
						try {
							Thread.sleep(quantoTempo());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}					
					}
					else if(varAc[0] == 2){
						var.robot.CurvarDireita(varAc[1], varAc[2]);
						textArea.append("Curva Direita: Raio = " + varAc[1] + ", Angulo = " + varAc[2] + "\n");
						try {
							Thread.sleep(quantoTempo());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}						
					}
					else {
						var.robot.CurvarEsquerda(varAc[1], varAc[2]);
						textArea.append("Curva Esquerda: Raio = " + varAc[1] + ", Angulo = " + varAc[2] + "\n");
						try {
							Thread.sleep(quantoTempo());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}						
					}
				}
				else {
					state = parado;
				}
				break;
			}
		}
	}
	
	private long quantoTempo() {
		long tempo = 0;
		int tipoAc = varAc[0];
		int t = 250;
		
		switch(tipoAc) {
		case 1:
			// tempo para percorrer a reta em ms
			t += (varAc[1] * 1000) / 30;
			tempo = (long) (t);
			break;
		case 2:
			// tempo para percorrer a distancia da curva em ms
			t += (((varAc[2] / 360) * 2*3.1415 * varAc[1])*1000)/30;
			tempo = (long) (t);
			break;		
		case 3:
			// tempo para percorrer a distancia da curva em ms
			t += (((varAc[2] / 360) * 2*3.1415 * varAc[1])*1000)/30; 
			tempo = (long) (t);
			break;
		}
		return tempo;
	}
	
	private int[] gerarAcao() {
		Random rand = new Random();
		int type = rand.nextInt(3)+1;
		varAc[0] = type;
		if (type == 1) {
			varAc[1] = rand.nextInt(10)+10;
			varAc[2] = 0;
		}
		else if (type == 1) {
			varAc[1] = rand.nextInt(40)+10;
			varAc[2] = rand.nextInt(50)+10;
		}
		else {
			varAc[1] = rand.nextInt(40)+10;
			varAc[2] = rand.nextInt(50)+10;
		}
		return varAc;
	}


	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("GUI Vaguear");
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

		JLabel HistoricoLabel = new JLabel("Historico Mensagens:");
		HistoricoLabel.setBounds(10, 11, 156, 24);
		contentPane.add(HistoricoLabel);
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 45, 282, 221);
		contentPane.add(scrollPane);

		scrollPane.setViewportView(textArea);

		setVisible(true);

	}
}
