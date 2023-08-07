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
            System.out.println("3. Exit");
            System.out.println("Enter option: ");
            
            int option = sc.nextInt();

            switch (option) {
                case 1:
                    auth = c.login();
                    break;

                case 2:
                    auth = c.signup();
                    break;

                case 3:
                    c.disconnect();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        while (auth) {
            System.out.println("\n1. Show contacts");
            System.out.println("2. Add contact");
            System.out.println("3. Show contact info");
            System.out.println("4. Open private chat");
            System.out.println("5. Open group chat");
            System.out.println("6. Change status");
            System.out.println("7. Send file");
            System.out.println("8. Exit");

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

                case 8:
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