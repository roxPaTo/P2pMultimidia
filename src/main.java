import java.io.IOException;

import protocolo.SocketModoEmissor;
import uinterface.P2pMultimidiaUI;

public class main {
	private static P2pMultimidiaUI ui;
	private static SocketModoEmissor emissor;
	
	public static void main(String[] args) throws IOException {
		ui = P2pMultimidiaUI.getInstancia();
		emissor = SocketModoEmissor.getInstancia();
		
		ui.montaTela();		
		emissor.runServer();		     
    }
}
