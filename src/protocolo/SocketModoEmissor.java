package protocolo;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import uinterface.P2pMultimidiaUI;

public class SocketModoEmissor {
	private ServerSocket server;
	private Socket conexao;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private int contador = 1;
	
	//Singleton
	private static SocketModoEmissor instancia;
	public static SocketModoEmissor getInstancia(){
		if (instancia == null) {
			instancia = new SocketModoEmissor();
	    } 
		return instancia;
	}
	
	public void runServer() throws IOException{
		try {
			server = new ServerSocket(12345, 100);
			
			while(true){
				try {
					esperandoConexao();
					obtemFluxoEntradaSaida();
					processaConexao();
				} catch (EOFException eofException) {
					enviaMensagem("\n Conex�o com o servidor finalizada");
				} finally {
					fechaConexao();
					++contador;
				}
			}
			
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		
	}
	
	private void esperandoConexao() throws IOException{
		P2pMultimidiaUI.getInstancia().mostraMensagemServer("Esperando por conex�es\n");
		conexao = server.accept();
		P2pMultimidiaUI.getInstancia().mostraMensagemServer("Conex�o: "+contador+" recebido de: "+conexao.getInetAddress().getHostName());		
	}
	
	private void obtemFluxoEntradaSaida() throws IOException{
		output = new ObjectOutputStream(conexao.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(conexao.getInputStream());
		
		P2pMultimidiaUI.getInstancia().mostraMensagemServer("\nObteve I/O\n");
	}
	
	private void processaConexao() throws IOException{
		String msg = "Conex�o bem sucedida!";
		enviaMensagem(msg);
		
		//ativa serverField para servidor mandar mensagem
		P2pMultimidiaUI.getInstancia().habilitaEnvioMsgServidor(true);
		
		Mensagem mensagem = new Mensagem(input, true);
		mensagem.start();
		
		/*do {
			System.out.println("qualquer");
			try { //l� e exibe a mensagem
				msg = (String) input.readObject();
				P2pMultimidiaUI.getInstancia().mostraMensagemServer("\n"+msg);
			} catch (ClassNotFoundException classNotFoundException) {
				P2pMultimidiaUI.getInstancia().mostraMensagemServer("\nTipo de objeto recebido desconhecido");
			}
		} while (!msg.equals("CLIENT>>> TERMINATE"));*/
	}
	
	//senddata
	public void enviaMensagem(String msg) throws IOException{
		try {
			output.writeObject("SERVIDOR>>> "+msg);
			output.flush();
			P2pMultimidiaUI.getInstancia().mostraMensagemServer("\nSERVIDOR>>> "+msg);
		} catch (Exception e) {
			P2pMultimidiaUI.getInstancia().adicionaMensagemConsoleServidor("\nErro ao escrever objeto\n");
		}		
	}
	
	private void fechaConexao(){
		P2pMultimidiaUI.getInstancia().mostraMensagemServer("\nFinalizando conex�o\n");
		P2pMultimidiaUI.getInstancia().habilitaEnvioMsgServidor(false);
		
		try {
			output.close();
			input.close();
			conexao.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
