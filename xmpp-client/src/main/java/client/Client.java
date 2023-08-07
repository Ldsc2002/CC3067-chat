package client;

import rocks.xmpp.core.session.XmppClient;
import java.util.Scanner;

public class Client 
{
    public void logIn() {
        XmppClient xmppClient = XmppClient.create("alumchat.xyz");
        // xmppClient.connect();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

    }
}
