import java.io.IOException;
import java.net.*;

public class UdpClient {
    private DatagramSocket client;
    private int porta = 51700;
    private String serverIP = "crive.xyz";  // IP del server predefinito
    private String lastReceivedMessage = "";

    public UdpClient() throws SocketException {
        this.client = new DatagramSocket();
        System.out.println("Client UDP attivo sulla porta locale: " + client.getLocalPort());
    }

    public void sendMessage(String message) {
        try {
            byte[] dati_out = message.getBytes();
            InetAddress IPDestinatario = InetAddress.getByName(serverIP);
            DatagramPacket pkt_out = new DatagramPacket(dati_out, dati_out.length, IPDestinatario, porta);
            client.send(pkt_out);
            System.out.println("Pacchetto spedito a " + serverIP + ":" + porta);
        } catch (IOException e) {
            System.out.println("Errore invio messaggio: " + e.getMessage());
        }
    }

    public String receiveMessage() {
        try {
            byte[] dati_in = new byte[1024];
            DatagramPacket pkt_in = new DatagramPacket(dati_in, dati_in.length);
            client.receive(pkt_in);
            String receivedMessage = new String(pkt_in.getData(), 0, pkt_in.getLength());
            System.out.println("Messaggio ricevuto da " + pkt_in.getAddress() + ": " + receivedMessage);
            lastReceivedMessage = receivedMessage;
            return receivedMessage;
        } catch (IOException e) {
            if (!client.isClosed()) {
                System.out.println("Errore ricezione messaggio: " + e.getMessage());
            }
            return "";
        }
    }

    public void startReceivingMessages(Runnable callback) {
        Thread receiveThread = new Thread(() -> {
            System.out.println("Thread di ricezione avviato");
            while (!client.isClosed()) {
                String message = receiveMessage();  // Ricevi il messaggio UNA VOLTA
                if (!message.isEmpty() && callback != null) {
                    callback.run();  // Chiama il callback SENZA ricevere di nuovo
                }
            }
        });
        receiveThread.setDaemon(true);
        receiveThread.start();
    }

    public String getLastReceivedMessage() {
        return lastReceivedMessage;
    }

    public void setServerIP(String ip) {
        this.serverIP = ip;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getServerIP() {
        return this.serverIP;
    }

    public int getPorta() {
        return this.porta;
    }

    public int getLocalPort() {
        return client.getLocalPort();
    }

    public void close() {
        if (client != null && !client.isClosed()) {
            client.close();
            System.out.println("Client UDP chiuso");
        }
    }
}