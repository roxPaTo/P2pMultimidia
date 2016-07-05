package protocolo;

import java.io.IOException;
import java.io.ObjectInputStream;

import uinterface.P2pMultimidiaUI;

public class Mensagem extends Thread{
	
	private ObjectInputStream input;
	private boolean isServer;
	
	public Mensagem(ObjectInputStream input, boolean isServer) {
		this.input = input;
		this.isServer = isServer;
	}
	
	@Override
	public void run() {
		String msg = "";
		do {
			System.out.println("qualquer");
			try { //lê e exibe a mensagem
				msg = (String) input.readObject();
				if (isServer){
					P2pMultimidiaUI.getInstancia().mostraMensagemServer("\n"+msg);
				}else{
					P2pMultimidiaUI.getInstancia().mostraMensagemCliente("\n"+msg);
				}				
			} catch (ClassNotFoundException | IOException classNotFoundException) {
				classNotFoundException.printStackTrace();
				if (isServer){
					P2pMultimidiaUI.getInstancia().mostraMensagemServer("\nTipo de objeto recebido desconhecido");
				}else{
					P2pMultimidiaUI.getInstancia().mostraMensagemCliente("\nTipo de objeto recebido desconhecido");
				}
				return;
			}
		} while (!msg.equals("CLIENT>>> TERMINATE"));
	}

}
