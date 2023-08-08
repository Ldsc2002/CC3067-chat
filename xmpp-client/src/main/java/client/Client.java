package client;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.PresenceBuilder;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.Chat;

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

    public void addMessageListener() {
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        Roster roster = Roster.getInstanceFor(connection);

        chatManager.addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                System.out.println("\nReceived message: " + message.getBody() + " from " + from.toString());
            }
        });

        roster.addRosterListener(new RosterListener() {
            @Override
            public void presenceChanged(Presence presence) {
                System.out.println("\nReceived presence: " + presence.getMode() + " from " + presence.getFrom().toString());
            }

            @Override
            public void entriesAdded(Collection<Jid> addresses) {
                System.out.println("\nReceived entries added: " + addresses.toString());
            }

            @Override
            public void entriesUpdated(Collection<Jid> addresses) {
                System.out.println("\nReceived entries updated: " + addresses.toString());
            }

            @Override
            public void entriesDeleted(Collection<Jid> addresses) {
                System.out.println("\nReceived entries deleted: " + addresses.toString());
            }
        });

        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
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
        System.out.println("\nEnter username: ");
        String username = sc.nextLine();

        System.out.println("Enter message: ");
        String message = sc.nextLine();

        ChatManager chatManager = ChatManager.getInstanceFor(connection);

        try {
            EntityBareJid userID = JidCreate.entityBareFrom(username + "@" + config.getXMPPServiceDomain());
            Chat chat = chatManager.chatWith(userID);
            chat.send(message);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Message sent successfully");
    }

    public void sendGroupMessage() {
        System.out.println("\nEnter group name: ");
        String groupName = sc.nextLine();

        System.out.println("Enter message: ");
        String message = sc.nextLine();

        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);

        try {
            EntityBareJid groupJid = JidCreate.entityBareFrom(groupName + "@conference.alumchat.xyz");
            MultiUserChat muc = manager.getMultiUserChat(groupJid);
            muc.sendMessage(message);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Message sent successfully");
    }

    public void createGroup() {
        System.out.println("\nEnter group name: ");
        String groupName = sc.nextLine();

        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);

        try {
            EntityBareJid groupJid = JidCreate.entityBareFrom(groupName + "@conference.alumchat.xyz");
            MultiUserChat muc = manager.getMultiUserChat(groupJid);
            muc.create(Resourcepart.from(username));
            muc.sendConfigurationForm(muc.getConfigurationForm().getFillableForm());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Group created successfully");
    }

    public void showGroups() {
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);

        try {
            DiscoverItems discoItems = discoManager.discoverItems(JidCreate.domainBareFrom("conference.alumchat.xyz"));

            System.out.println("\nGroups:");
            for (DiscoverItems.Item item : discoItems.getItems()) {
                System.out.println(item.getEntityID());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }

    public void joinGroup() {
        System.out.println("\nEnter group name: ");
        String groupName = sc.nextLine();

        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);

        try {
            EntityBareJid groupJid = JidCreate.entityBareFrom(groupName + "@conference.alumchat.xyz");
            MultiUserChat muc = manager.getMultiUserChat(groupJid);
            muc.join(Resourcepart.from(username));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Joined group successfully");
    }

    public void changeStatusMessage() {
        System.out.println("\nEnter status: ");
        String status = sc.nextLine();

        try {
            PresenceBuilder presenceBuilder = PresenceBuilder.buildPresence()
                .setStatus(status);

            connection.sendStanza(presenceBuilder.build());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Status changed successfully");
    }

    public void changeStatus() {
        Presence.Mode[] presenceModes = Presence.Mode.values();

        System.out.println("\nSelect presence mode:");
        for (int i = 1; i < presenceModes.length + 1; i++) {
            System.out.println(i + ". " + presenceModes[i - 1]);
        }

        int option = sc.nextInt() - 1;

        try {
            PresenceBuilder presenceBuilder = PresenceBuilder.buildPresence()
                .setMode(presenceModes[option]);

            connection.sendStanza(presenceBuilder.build());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Status changed successfully");
    }

    public void sendFile() {
        // TODO
        System.out.println("TODO");
    }
}
