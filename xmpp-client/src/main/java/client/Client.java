package client;

import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.XmppException;
import rocks.xmpp.core.net.client.SocketConnectionConfiguration;

public class Client {
    public XmppClient xmppClient;
    public String username;
    public String password;

    public void connect() {
        SocketConnectionConfiguration tcpConfiguration = SocketConnectionConfiguration.builder()
            .hostname("alumchat.xyz")
            .port(5222)
            .build();

        xmppClient = XmppClient.create("alumchat.xyz", tcpConfiguration);

        try {
            xmppClient.connect();
         } catch (XmppException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
         }

         try {
            xmppClient.close(); 
        } catch (XmppException e) {
            System.out.println("Error closing connection: " + e.getMessage());
         }
    }
}
