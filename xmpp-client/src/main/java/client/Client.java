package client;

// Smack imports
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.MessageListener;
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

// Java imports
import java.util.Scanner;
import java.io.File;
import java.util.Collection;

public class Client {
    // Scanner object to read user input
    private Scanner sc = new Scanner(System.in);

    // Private connection and configuration objects
    private AbstractXMPPConnection connection;
    private XMPPTCPConnectionConfiguration config;
    private String username;

    public void connect() {
        try {
            // Create configuration object
            config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain("alumchat.xyz")
                .setHost("alumchat.xyz")
                .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
                .build();   
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // Create connection object
        connection = new XMPPTCPConnection(config);

        try {
            // Attempt to connect
            connection.connect(); 
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }

    public void disconnect() {
        // Disconnect user from server
        connection.disconnect();
    }

    public boolean login() {
        /* 
         * Returns true if login was successful
         * Returns false if login was unsuccessful
         */

        System.out.println("\nEnter username: ");
        username = sc.nextLine();

        System.out.println("Enter password: ");
        String password = sc.nextLine();

        try {
            // Attempt to login
            connection.login(username, password);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        System.out.println("Logged in successfully");
        return true;
    }

    public boolean signup() {
        /* 
         * Returns true if signup was successful
         * Returns false if signup was unsuccessful
         */

        System.out.println("\nEnter username: ");
        username = sc.nextLine();

        System.out.println("Enter password: ");
        String password = sc.nextLine();

        Localpart usernameLocalpart;
        try {
            // Create username localpart
            usernameLocalpart = Localpart.from(username);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        try {
            // Attempt to create account over insecure connection
            AccountManager.sensitiveOperationOverInsecureConnectionDefault(true);
            AccountManager.getInstance(connection).createAccount(usernameLocalpart, password);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

        System.out.println("Account created successfully");
        return true;
    }  
    
    public void deleteAccount() {
        System.out.println("\nAre you sure you want to delete your account? (y/n)");

        String answer = sc.nextLine();

        if (!answer.equals("y")) {
            return;
        }

        try {
            // Attempt to delete account over insecure connection
            AccountManager.sensitiveOperationOverInsecureConnectionDefault(true);
            AccountManager.getInstance(connection).deleteAccount();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Account deleted successfully");
    }

    public void addMessageListener() {
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        Roster roster = Roster.getInstanceFor(connection);

        // Add incoming message listener
        chatManager.addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                // Check if message start with "file://"
                if (message.getBody().startsWith("file://")) {
                    String base64File, fileType;
                    byte[] file;

                    // In case decoding fails
                    try {
                        // Get file type and base64 file
                        base64File = message.getBody().substring(7);
                        fileType = message.getBody().substring(7, 7 + base64File.indexOf("://"));
                        base64File = base64File.substring(base64File.indexOf("://") + 3);

                        // Decode file
                        file = java.util.Base64.getDecoder().decode(base64File);
                    } catch (Exception e) {
                        System.out.println("Error decoding file from " + from.toString());
                        return;
                    }

                    System.out.println("\nReceived file from " + from.toString());

                    // Create new file
                    File fileToSave = new File("fileFrom" + from.toString() + "." + fileType);
                    
                    // In case writing fails
                    try {
                        // Write file
                        java.io.FileWriter fileWriter = new java.io.FileWriter(fileToSave);
                        fileWriter.write(new String(file));
                        fileWriter.close();
                    } catch (Exception e) {
                        System.out.println("Error writing file from " + from.toString());
                        return;
                    }
                } else {
                    System.out.println("\nReceived message: " + message.getBody() + " from " + from.toString());
                }
            }
        });

        // Add roster listener
        roster.addRosterListener(new RosterListener() {
            @Override
            // Called when a contact changes presence
            public void presenceChanged(Presence presence) {
                System.out.println("\nReceived presence: " + presence.getMode() + " from " + presence.getFrom().toString());
            }

            @Override
            // Called when a contact is added
            public void entriesAdded(Collection<Jid> addresses) {
                System.out.println("\nReceived entries added: " + addresses.toString());
            }

            @Override
            // Called when a contact is updated
            public void entriesUpdated(Collection<Jid> addresses) {
                System.out.println("\nReceived entries updated: " + addresses.toString());
            }

            @Override
            // Called when a contact is deleted
            public void entriesDeleted(Collection<Jid> addresses) {
                System.out.println("\nReceived entries deleted: " + addresses.toString());
            }
        });

        // Set subscription mode to accept all
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
    }

    public void addGroupListener(String group) {
        MultiUserChat muc;
        try {
            // Get group
            EntityBareJid roomID = JidCreate.entityBareFrom(group + "@conference.alumchat.xyz");
            muc = MultiUserChatManager.getInstanceFor(connection)
                .getMultiUserChat(roomID);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // Add listener to group chat
        muc.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Message message) {
                // Output message
                System.out.println("\nReceived group message: " + message.getBody() + " from " + message.getFrom().toString());
            }
        }); 
    }

    public void showContacts() {
        System.out.println("\nContacts:");

        // Get roster instance
        Roster roster = Roster.getInstanceFor(connection);

        // Get roster entries
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            System.out.println(entry);
        }
    }

    public void addContact() {
        System.out.println("\nEnter username: ");
        String username = sc.nextLine();

        // Get roster instance
        Roster roster = Roster.getInstanceFor(connection);
        BareJid userID;

        try {
            // Add contact and request subscription
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

        // Get roster instance
        Roster roster = Roster.getInstanceFor(connection);
        BareJid userID;

        try {
            // Get contact info
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
            
            // Open chat with user and send message
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

        // Get group manager
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);

        try {
            // Get group and send message
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

        // Get group manager
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);

        try {
            // Create group
            EntityBareJid groupJid = JidCreate.entityBareFrom(groupName + "@conference.alumchat.xyz");
            MultiUserChat muc = manager.getMultiUserChat(groupJid);
            muc.create(Resourcepart.from(username));

            // Set group configuration
            muc.sendConfigurationForm(muc.getConfigurationForm().getFillableForm());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Group created successfully");
    }

    public void showGroups() {
        // Get service discovery manager
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);

        try {
            // Get groups using service discovery
            DiscoverItems discoItems = discoManager.discoverItems(JidCreate.domainBareFrom("conference.alumchat.xyz"));

            System.out.println("\nGroups:");
            
            // List groups
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

        // Get group manager
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);

        try {
            // Get group and join
            EntityBareJid groupJid = JidCreate.entityBareFrom(groupName + "@conference.alumchat.xyz");
            MultiUserChat muc = manager.getMultiUserChat(groupJid);
            muc.join(Resourcepart.from(username));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // Add group listener when user joins
        addGroupListener(groupName);

        System.out.println("Joined group successfully");
    }

    public void changeStatusMessage() {
        System.out.println("\nEnter status: ");
        String status = sc.nextLine();

        try {
            // Change status
            PresenceBuilder presenceBuilder = PresenceBuilder.buildPresence()
                .setStatus(status);

            // Send new presence
            connection.sendStanza(presenceBuilder.build());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Status changed successfully");
    }

    public void changeStatus() {
        // Get presence modes from enum
        Presence.Mode[] presenceModes = Presence.Mode.values();

        System.out.println("\nSelect presence mode:");
        for (int i = 1; i < presenceModes.length + 1; i++) {
            System.out.println(i + ". " + presenceModes[i - 1]);
        }

        int option = sc.nextInt() - 1;

        try {
            // Change status
            PresenceBuilder presenceBuilder = PresenceBuilder.buildPresence()
                .setMode(presenceModes[option]);

            // Send new presence
            connection.sendStanza(presenceBuilder.build());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Status changed successfully");
    }

    public void sendFile() {
        System.out.println("\nEnter username: ");
        String username = sc.nextLine();

        System.out.println("Enter file path: ");
        String filePath = sc.nextLine();

        // Open file
        File file = new File(filePath);

        // Create byte array to store file
        byte[] fileBytes = new byte[(int) file.length()];
        
        try {
            //Read file to byte array 
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
            fileInputStream.read(fileBytes);
            fileInputStream.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // Encode file to base64
        String base64File = java.util.Base64.getEncoder().encodeToString(fileBytes);
        // Get file type from file path
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);

        // Create message with format "file://fileType://base64File"
        String message = "file://" + fileType + "://" +  base64File;

        // Get chat manager
        ChatManager chatManager = ChatManager.getInstanceFor(connection);

        try {
            // Open chat with user and send message
            EntityBareJid userID = JidCreate.entityBareFrom(username + "@" + config.getXMPPServiceDomain());
            Chat chat = chatManager.chatWith(userID);
            chat.send(message);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("File sent successfully");
    }
}
