package uinterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import protocolo.SocketModoEmissor;
import protocolo.SocketModoReceptor;

public class P2pMultimidiaUI {
	private JFrame janela;
	private JPanel painelPrincipal;
	private JPanel painelModoEmissor;
	private JPanel painelModoReceptor;
	private JPanel painelRelatorios;
	private JComboBox<Object> comboModoEnvio = null;
	private JTabbedPane abas = new JTabbedPane();
	private final JTextField fValor = new JTextField();
	private final JTextField rttValor = new JTextField();
	private final JTextField exValor = new JTextField();
	private JTextField serverField;
	private JTextArea serverTexto;
	private JTextField clienteField;
	private JTextArea clienteTexto;
	
	//Singleton
	private static P2pMultimidiaUI instancia;
	public static P2pMultimidiaUI getInstancia(){
		if (instancia == null) {
            instancia = new P2pMultimidiaUI();
        } 
        return instancia;
	}
	
	private static void adicionaFormulario(Component componente, int nColuna, int nLinha, int nLargura, int nAltura, JPanel painel){
        painel.add(componente) ;                      
        componente.setBounds(nColuna, nLinha, nLargura, nAltura);
    }  
	
	public void montaTela() {
		preparaJanela();
		preparaPainel();
		preparaPainelModoReceptor();
		preparaPainelModoEmissor();
		preparaPainelRelatorios();
		preparaAbas();
		mostraJanela();
	}
	
	private void preparaJanela() {
		janela = new JFrame("Trabalho de redes - P2pMultimida");
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLocationRelativeTo(null);
	}
	
	private void mostraJanela() {
		janela.pack();
		janela.setSize(350, 360);
		janela.setVisible(true);
	}
	
	private void preparaPainel() {
		painelPrincipal = new JPanel();
		painelPrincipal.setLayout(null);
		janela.add(painelPrincipal);	
	}
	
	private void preparaPainelModoReceptor(){
		painelModoReceptor = new JPanel();
		painelModoReceptor.setLayout(null);

		JLabel audio = new JLabel("Arquivo: ");
		JTextField arquivoAudio = new JTextField("ThreeLitteBirds.mp3");
		arquivoAudio.setEditable(false);
		
		JLabel ip = new JLabel("IP origem: ");
		final JTextField ipOrigem = new JTextField();
		ipOrigem.setEditable(true);
		ipOrigem.setText("127.0.0.1");
		
		JLabel porta = new JLabel("Porta origem: ");
		final JTextField portaOrigem = new JTextField();
		portaOrigem.setEditable(true);
		portaOrigem.setText("12345");
		
		JButton botaoEnvio = new JButton("Pegar Arquivo");
		botaoEnvio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				painelModoReceptor.setBackground(Color.ORANGE);
				clienteTexto.append("adiciona");
				clienteField.setEditable(true);
				SocketModoReceptor.getInstancia().runCliente(ipOrigem.getText(), Integer.valueOf(portaOrigem.getText()));
			}
		});
		
		clienteField = new JTextField();
		clienteField.setEditable(false);
		clienteField.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						mensagemConsoleCliente(e.getActionCommand());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					clienteField.setText("");					
				}
			}
		);
		clienteTexto = new JTextArea();
		
		adicionaFormulario(audio,10,10,80,25,painelModoReceptor);
		adicionaFormulario(arquivoAudio,90,10,230,25,painelModoReceptor);
		adicionaFormulario(ip,10,40,80,25,painelModoReceptor);
		adicionaFormulario(ipOrigem,90,40,230,25,painelModoReceptor);
		adicionaFormulario(porta,10,70,80,25,painelModoReceptor);
		adicionaFormulario(portaOrigem,90,70,230,25,painelModoReceptor);
		adicionaFormulario(botaoEnvio,90,110,230,25,painelModoReceptor);
		adicionaFormulario(new JScrollPane(clienteTexto),10,140,310,120,painelModoReceptor);
		adicionaFormulario(clienteField,10,260,310,25,painelModoReceptor);
	}
	
	private void preparaPainelModoEmissor() {	
		painelModoEmissor = new JPanel();
		painelModoEmissor.setLayout(null);
		
		String[] modosEnvio = {"Trasmissão Sequencial", "Transmissão Aleatória"};
		JLabel modoEnvio = new JLabel("Modo Envio: ");
		
		comboModoEnvio = new JComboBox<Object>(modosEnvio);
		comboModoEnvio.setEditable(true);
		
		JLabel f = new JLabel("F: ");
		fValor.setEditable(true);
		fValor.setText("1");
		
		JLabel rtt = new JLabel("RTT: ");
		rttValor.setEditable(true);
		rttValor.setText("2");
		
		JLabel ex = new JLabel("E[X]: ");
		exValor.setEditable(true);
		exValor.setText("3");
		
		serverField = new JTextField();
		serverField.setEditable(false);
		serverField.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						mensagemConsoleServer(e.getActionCommand());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					serverField.setText("");					
				}
			}
		);
		
		serverTexto = new JTextArea();
		
		adicionaFormulario(modoEnvio,10,10,80,25,painelModoEmissor);
		adicionaFormulario(comboModoEnvio,90,10,230,25,painelModoEmissor);
		adicionaFormulario(f,10,40,80,25,painelModoEmissor);
		adicionaFormulario(fValor,90,40,230,25,painelModoEmissor);
		adicionaFormulario(rtt,10,70,80,25,painelModoEmissor);
		adicionaFormulario(rttValor,90,70,230,25,painelModoEmissor);
		adicionaFormulario(ex,10,100,80,25,painelModoEmissor);
		adicionaFormulario(exValor,90,100,230,25,painelModoEmissor);
		adicionaFormulario(new JScrollPane(serverTexto),10,130,310,120,painelModoEmissor);
		adicionaFormulario(serverField,10,250,310,25,painelModoEmissor);
	}
	
	private void preparaPainelRelatorios(){
		painelRelatorios = new JPanel();
		painelRelatorios.setLayout(null);
		
		JButton relatorioFinal = new JButton("Relatório Final");
		relatorioFinal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton relatorioSaida = new JButton("Relatório Saídas");
		relatorioSaida.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		adicionaFormulario(relatorioFinal,50,10,230,25,painelRelatorios);
		adicionaFormulario(relatorioSaida,50,50,230,25,painelRelatorios);
	}
	
	private void preparaAbas() {
		abas = new JTabbedPane();
		abas.addTab("Modo Emissor", painelModoEmissor);
		abas.addTab("Modo Receptor", painelModoReceptor);
		abas.addTab("Relatórios", painelRelatorios);
		janela.add(abas);
	}
	
	public void mostraMensagemServer(final String msg){
		System.out.println("msg server: "+msg);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				serverTexto.append(msg);
			}
	    });
	}
	
	public void mostraMensagemCliente(final String msg){
		System.out.println("exibir mensagem método! - "+msg);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("append");
				clienteTexto.append(msg);
			}
	    });
	}
	
	public void habilitaEnvioMsgServidor(final boolean b){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				serverField.setEditable(b);
			}
	    });
	}
	
	public void habilitaEnvioMsgCliente(final boolean b){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("altera");
				clienteField.setEditable(b);
			}
	    });
	}
	
	public void adicionaMensagemConsoleServidor(String msg){
		serverTexto.append(msg); 
	}
	
	public void adicionaMensagemConsoleCliente(String msg){
		clienteTexto.append(msg); 
	}
	
	public void mensagemConsoleServer(String msg) throws IOException{
		SocketModoEmissor.getInstancia().enviaMensagem(msg);		
	}
	
	public void mensagemConsoleCliente(String msg) throws IOException{
		SocketModoReceptor.getInstancia().enviaMensagem(msg);		
	}
	
}
