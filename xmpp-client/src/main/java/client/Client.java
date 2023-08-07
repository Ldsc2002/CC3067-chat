package client;

import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.XmppException;
import java.util.Scanner;

public class Client 
{
    public void logIn() {
        XmppClient xmppClient = XmppClient.create("alumchat.xyz");

        try {
            xmppClient.connect();
        } catch (XmppException e) {
            System.err.println("Error connecting to XMPP server: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.println(username + " " + password);

        scanner.close();
    }

}
