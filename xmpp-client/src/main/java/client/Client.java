package client;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Scanner;
import java.util.Collection;

public class Client {
    private Scanner sc = new Scanner(System.in);

    AbstractXMPPConnection connection;
    XMPPTCPConnectionConfiguration config;
    public String username;
    public String password;

    public void connect() {
        try {
            config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain("alumchat.xyz")
                .setHost("alumchat.xyz")
                .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
                .build();   
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        connection = new XMPPTCPConnection(config);

        try {
            connection.connect(); 
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }

    public void disconnect() {
        connection.disconnect();
    }

    public boolean login() {
        System.out.println("\nEnter username: ");
        username = sc.nextLine();

        System.out.println("Enter password: ");
        password = sc.nextLine();

        try {
            connection.login(username, password);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        System.out.println("Logged in successfully");
        return true;
    }

    public boolean signup() {
        System.out.println("\nEnter username: ");
        username = sc.nextLine();

        System.out.println("Enter password: ");
        password = sc.nextLine();

        Localpart usernameLocalpart;
        try {
            usernameLocalpart = Localpart.from(username);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        try {
            AccountManager.sensitiveOperationOverInsecureConnectionDefault(true);
            AccountManager.getInstance(connection).createAccount(usernameLocalpart, password);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        System.out.println("Account created successfully");
        return true;
    }   

    public void showContacts() {
        System.out.println("\nContacts:");

        Roster roster = Roster.getInstanceFor(connection);

        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            System.out.println(entry);
        }
    }

    public void addContact() {
        System.out.println("\nEnter username: ");
        String username = sc.nextLine();

        Roster roster = Roster.getInstanceFor(connection);
        BareJid userID;

        try {
            userID = JidCreate.entityBareFrom(username + "@" + config.getXMPPServiceDomain());
            roster.createItemAndRequestSubscription(userID, username, null);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Contact added successfully");
    }

    public void showContactInfo() {
        System.out.println("\nEnter username: ");
        String username = sc.nextLine();

        Roster roster = Roster.getInstanceFor(connection);
        BareJid userID;

        try {
            userID = JidCreate.entityBareFrom(username + "@" + config.getXMPPServiceDomain());
            RosterEntry entry = roster.getEntry(userID);

            System.out.println("\n" + "Contact info:");
            System.out.println(entry);
            System.out.println(roster.getPresence(userID).getType());
            System.out.println(roster.getPresence(userID).getStatus());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }

    public void sendPrivateMessage() {
        System.out.println("TODO");
    }

    public void sendGroupMessage() {
        System.out.println("TODO");
    }

    public void changeStatus() {
        System.out.println("TODO");
    }

    public void sendFile() {
        System.out.println("TODO");
    }
}
