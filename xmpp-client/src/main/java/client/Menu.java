package client;

import java.util.Scanner;

public class Menu {
    private Scanner sc = new Scanner(System.in);

    public void mainMenu() {
        Client c = new Client();
        c.connect();

        boolean auth = false;
    
        while (!auth) {
            System.out.println("\n1. Log In");
            System.out.println("2. Create account");
            System.out.println("0. Exit");
            System.out.println("Enter option: ");
            
            int option = sc.nextInt();

            switch (option) {
                case 1:
                    auth = c.login();
                    break;

                case 2:
                    auth = c.signup();
                    break;

                case 0:
                    c.disconnect();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        c.addMessageListener();

        while (auth) {
            System.out.println("\n1. Show contacts");
            System.out.println("2. Add contact");
            System.out.println("3. Show contact info");
            System.out.println("4. Send private message");
            System.out.println("5. Send group message");
            System.out.println("6. Create group");
            System.out.println("7. Show groups");
            System.out.println("8. Join group");
            System.out.println("9. Change status message");
            System.out.println("10. Change status");
            System.out.println("11. Send file");
            System.out.println("0. Exit");

            int option = sc.nextInt();

            switch (option) {
                case 1:
                    c.showContacts();
                    break;

                case 2:
                    c.addContact();
                    break;

                case 3:
                    c.showContactInfo();
                    break;

                case 4:
                    c.sendPrivateMessage();
                    break;

                case 5:
                    c.sendGroupMessage();
                    break;

                case 6:
                    c.createGroup();
                    break;

                case 7:
                    c.showGroups();
                    break;

                case 8:
                    c.joinGroup();
                    break;

                case 9:
                    c.changeStatusMessage();
                    break;

                case 10:
                    c.changeStatus();
                    break;

                case 11:
                    c.sendFile();
                    break;

                case 0:
                    c.disconnect();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option");
                    break;
            }   
        }
    }
}