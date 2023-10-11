import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Administrador {

	private JFrame frame;
	JTextArea consola = new JTextArea();
	
	VariaveisRobot var;
	VariaveisSemaforos vs;
	
	public boolean ligado = false;
	public boolean debug = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Administrador gui = new Administrador();
		gui.run();
	}

	public void run() {
		while (true) {
			try {
				this.consola.setText(var.getConsola());
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void initVar() {
		var = new VariaveisRobot();
		vs = new VariaveisSemaforos();
	}

	/**
	 * Create the application.
	 */
	public Administrador() {
		
		initVar();
		
		frame = new JFrame();
		
		Vaguear vaguear = new Vaguear(var, vs);
		Thread t1 = new Thread(vaguear);
		t1.start();
		
		Evitar evitar = new Evitar(var, vs);
		Thread t2 = new Thread(evitar);
		t2.start();
		
		Fugir fugir = new Fugir(var, vs);
		Thread t3 = new Thread(fugir);
		t3.start();
		
		frame.setTitle("Servidor Robot");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				var.robot.Parar(true);
				var.robot.CloseEV3();
				System.exit(0);
			}
		});
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 600, 450);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextField tfNomeRobot = new JTextField();
		tfNomeRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tfNomeRobot.getText().length() > 0) {
					String nomeRobot = tfNomeRobot.getText();
					handleEnterS("Nome do Robot: ", nomeRobot);
					var.setNomeRobot(nomeRobot);
				}
			}
		});
		tfNomeRobot.setColumns(10);
		tfNomeRobot.setBounds(182, 26, 157, 20);
		contentPane.add(tfNomeRobot);
		
		consola = new JTextArea();
		consola.setEditable(false);
		consola.setBounds(10, 236, 568, 168);
		contentPane.add(consola);
		
		JLabel lblRobot = new JLabel("Robot");
		lblRobot.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRobot.setBounds(138, 28, 40, 14);
		contentPane.add(lblRobot);
		
		JLabel lblRaio = new JLabel("Raio");
		lblRaio.setBounds(138, 67, 40, 14);
		contentPane.add(lblRaio);
		
		JLabel lblDistancia = new JLabel("Distancia");
		lblDistancia.setBounds(330, 67, 46, 14);
		contentPane.add(lblDistancia);
		
		JLabel lblAngulo = new JLabel("Angulo");
		lblAngulo.setBounds(228, 67, 46, 14);
		contentPane.add(lblAngulo);
		
		JTextField tfRaio = new JTextField();
		tfRaio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tfRaio.getText().length() > 0) {
					int raio = Integer.parseInt(tfRaio.getText());
					handleEnterI("Raio: ", raio);
					var.setRaio(raio);
				}
			}
		});
		tfRaio.setText("30");
		tfRaio.setColumns(10);
		tfRaio.setBounds(167, 64, 35, 20);
		contentPane.add(tfRaio);
		
		JTextField tfAngulo = new JTextField();
		tfAngulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tfAngulo.getText().length() > 0) {
					int angulo = anguloCerto(Integer.parseInt(tfAngulo.getText()));
					tfAngulo.setText(String.valueOf(angulo));
					handleEnterI("Ângulo: ", angulo);
					var.setAngulo(angulo);
				}
			}
		});
		tfAngulo.setText("90");
		tfAngulo.setColumns(10);
		tfAngulo.setBounds(271, 64, 35, 20);
		contentPane.add(tfAngulo);
		
		JTextField tfDistancia = new JTextField();
		tfDistancia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tfDistancia.getText().length() > 0) {
					int dist = Integer.parseInt(tfDistancia.getText());
					handleEnterI("Distância: ", dist);
					var.setDistancia(dist);
				}
			}
		});
		tfDistancia.setText("50");
		tfDistancia.setColumns(10);
		tfDistancia.setBounds(386, 64, 35, 20);
		contentPane.add(tfDistancia);
		
				
		JButton btnFrente = new JButton("Frente");
		btnFrente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				var.robot.Reta(var.getDistancia());
				simulateRobotAction();
			}
		});
		btnFrente.setBackground(new Color(107, 255, 100));
		btnFrente.setBounds(250, 95, 89, 23);
		contentPane.add(btnFrente);
		
		JButton btnParar = new JButton("Parar");
		btnParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				var.robot.Parar(true);
				simulateRobotAction();
			}
		});
		btnParar.setBackground(new Color(255, 100, 100));
		btnParar.setBounds(250, 125, 89, 23);
		contentPane.add(btnParar);
		
		JButton btnRetaguarda = new JButton("Retaguarda");
		btnRetaguarda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				var.robot.Reta(- var.getDistancia());
				simulateRobotAction();
			}
		});
		btnRetaguarda.setBackground(new Color(251, 255, 100));
		btnRetaguarda.setBounds(250, 155, 89, 23);
		contentPane.add(btnRetaguarda);
		
		JButton btnDireita = new JButton("Direita");
		btnDireita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				var.robot.CurvarDireita(var.getRaio(), var.getAngulo());
				simulateRobotAction();
			}
		});
		btnDireita.setBackground(new Color(100, 128, 255));
		btnDireita.setBounds(345, 125, 89, 23);
		contentPane.add(btnDireita);
		
		JButton btnEsquerda = new JButton("Esquerda");
		btnEsquerda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				var.robot.CurvarEsquerda(var.getRaio(), var.getAngulo());
				simulateRobotAction();
			}
		});
		btnEsquerda.setBackground(new Color(100, 239, 255));
		btnEsquerda.setBounds(155, 125, 89, 23);
		contentPane.add(btnEsquerda);
		
		JCheckBox chckbxVaguear = new JCheckBox("Vaguear");		
		chckbxVaguear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxVaguear.isSelected()) {
					btnFrente.setEnabled(false);
					btnEsquerda.setEnabled(false);
					btnDireita.setEnabled(false);
					btnRetaguarda.setEnabled(false);
					vaguear.ligar(ligado);
				} else {
					btnFrente.setEnabled(true);
					btnEsquerda.setEnabled(true);
					btnDireita.setEnabled(true);
					btnRetaguarda.setEnabled(true);
					vaguear.desligar();
				}
			}
		});
		chckbxVaguear.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxVaguear.setBounds(358, 154, 76, 23);
		contentPane.add(chckbxVaguear);
		
		JCheckBox chckbxEvitar = new JCheckBox("Evitar");
		chckbxEvitar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxEvitar.isSelected()) {
					evitar.ligar(ligado);
				} else {
					evitar.desligar();
				}
			}
		});
		chckbxEvitar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxEvitar.setBounds(358, 176, 76, 23);
		contentPane.add(chckbxEvitar);
		
		JCheckBox chckbxFugir = new JCheckBox("Fugir");
		chckbxFugir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxFugir.isSelected()) {
					fugir.ligar(ligado);
				} else {
					fugir.desligar();
				}
			}
		});
		chckbxFugir.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxFugir.setBounds(358, 198, 76, 23);
		contentPane.add(chckbxFugir);
		
		JRadioButton rdbtnOnOff = new JRadioButton("Desconectado");
		rdbtnOnOff.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
				        if(var.robot.OpenEV3(var.getNomeRobot()) == false) {
				        	print("Não foi possível conectar o robot!");
					        ligado = false;
							rdbtnOnOff.setSelected(false);
						}
						else {
							print("Conexão estabelecida com sucesso!");
							ligado = true;
							rdbtnOnOff.setText("Conectado");
							simulateRobotAction();
						}
			   	}
			    else if (rdbtnOnOff.isEnabled() ){
			    	if(e.getStateChange() == ItemEvent.DESELECTED) {
			    		ligado = false;
			    		
			    		vaguear.desligar();
			    		chckbxVaguear.setSelected(false);
			    		evitar.desligar();
			    		chckbxEvitar.setSelected(false);
			    		fugir.desligar();
			    		chckbxFugir.setSelected(false);
			    		
			    		btnFrente.setEnabled(true);
						btnEsquerda.setEnabled(true);
						btnDireita.setEnabled(true);
						btnRetaguarda.setEnabled(true);
			    		
			    		var.robot.CloseEV3();
			    		
			    		rdbtnOnOff.setText("Desconectado");
			    		simulateRobotAction();
			    	}
				}
			}
		});
		rdbtnOnOff.setBounds(345, 25, 122, 23);
		contentPane.add(rdbtnOnOff);
		
		JRadioButton rdbtnDebug = new JRadioButton("Debug");
		rdbtnDebug.setSelected(true);
		rdbtnDebug.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0. getStateChange() == ItemEvent.SELECTED) {
					var.setRobotSim();
					debug = true;
					print("Robot de simulação selecionado;");
				} else {
					var.setRobot();
					debug = false;
					print("Robot real selecionado;");
				}
			}
		});
		rdbtnDebug.setBounds(160, 155, 63, 23);
		contentPane.add(rdbtnDebug);
		
		frame.setVisible(true);
	}
	
	private void simulateRobotAction() {
		if (var.robot instanceof MyRobotLegoEV3Sim) {
			String s = ((MyRobotLegoEV3Sim)(var.robot)).getOutstring();
			handleEnterS("Robot -> ", s);
		}
	}
	
	private void print(String text) {
		var.addLinha(text);
		this.consola.setText(var.getConsola());
	}
	
	private void handleEnterS(String wText, String content) {  //wText (whichText)
		var.addLinha("\r" + wText + content);
		this.consola.setText(var.getConsola());
	}
	
	private void handleEnterI(String wText, int content) {  //wText (whichText)
		this.consola.append("\r\n" + wText + content + "\n");
		var.addLinha("\r" + wText + content);
		this.consola.setText(var.getConsola());
	}
	
	/**
	 * Método que converte qualquer Angulo em 0 a 360
	 */
	private int anguloCerto(int angA) { //angA: angulos antes de ser modificad		
		int nVoltas = angA/360;		
		angA = angA - 360*nVoltas;
		return angA;
	}
}
