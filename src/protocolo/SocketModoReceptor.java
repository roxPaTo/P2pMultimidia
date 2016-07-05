package protocolo;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import uinterface.P2pMultimidiaUI;

public class SocketModoReceptor {
	private String ipEmissor;
	private int portaEmissor;
	private Socket receptor;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String mensagem;
	
	//Singleton
	private static SocketModoReceptor instancia;
	public static SocketModoReceptor getInstancia(){
		if (instancia == null) {
			instancia = new SocketModoReceptor();
	    } 
		return instancia;
	}
		
	public void runCliente(String ip, int porta){
		ipEmissor = ip;
		portaEmissor = porta;		
		
		System.out.println("to aki!");
		try {
			conectaAoEmissor();
			obtemFluxoEntradaSaida();
			processaConexao();
		} catch (EOFException eofException) {
			P2pMultimidiaUI.getInstancia().mostraMensagemCliente("\nReceptor finalizou conexão");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally{
			fechaConexao();
		}
	}
	
	private void conectaAoEmissor() throws UnknownHostException, IOException{
		System.out.println("pegar conexao, ip e porta: "+ipEmissor+" - "+portaEmissor);
		P2pMultimidiaUI.getInstancia().mostraMensagemCliente("Obtendo Conexão\n");
		System.out.println("cria sockets conexao");
		receptor = new Socket(InetAddress.getByName(ipEmissor), portaEmissor);
		System.out.println("exibe mensagem - sockets: "+receptor.toString());
		P2pMultimidiaUI.getInstancia().mostraMensagemCliente("Conectado a: "+receptor.getInetAddress().getHostName());
	}
	
	private void obtemFluxoEntradaSaida() throws IOException{
		output = new ObjectOutputStream(receptor.getOutputStream());
		output.flush();
		input = new ObjectInputStream(receptor.getInputStream());
		P2pMultimidiaUI.getInstancia().mostraMensagemCliente("\nObteve I/O\n");
		System.out.println("obtendo fluxo!");
	}
	
	private void processaConexao() throws IOException{	
		System.out.println(" processa conexao ");
		//ativa clienteField para cliente mandar mensagem
		P2pMultimidiaUI.getInstancia().habilitaEnvioMsgCliente(true);
		
		Mensagem mensagem = new Mensagem(input, false);
		mensagem.start();
		
		/*do {
			try { //lê e exibe a mensagem
				mensagem = (String) input.readObject();
				P2pMultimidiaUI.getInstancia().mostraMensagemCliente("\n"+mensagem);
			} catch (ClassNotFoundException classNotFoundException) {
				P2pMultimidiaUI.getInstancia().mostraMensagemCliente("\nTipo de objeto recebido desconhecido");
			}
		} while (!mensagem.equals("SERVER>>> TERMINATE"));*/
		
		System.out.println("fim processa conexao ");
	}
	
	private void fechaConexao(){
		P2pMultimidiaUI.getInstancia().mostraMensagemCliente("\nFinalizando conexão\n");
		P2pMultimidiaUI.getInstancia().habilitaEnvioMsgCliente(false);
		
		try {
			output.close();
			input.close();
			receptor.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	public void enviaMensagem(String msg){
		try {
			output.writeObject("CLIENT>>> "+msg);
			output.flush();
			P2pMultimidiaUI.getInstancia().mostraMensagemCliente("\nCLIENT>>> "+msg);
		} catch (Exception e) {
			P2pMultimidiaUI.getInstancia().adicionaMensagemConsoleCliente("\nErro ao escrever objeto\n");
		}
	}
	
}
