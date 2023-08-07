package client;

import java.util.Scanner;

public class Menu {
    private Scanner sc = new Scanner(System.in);

    public void mainMenu() {
        Client c = new Client();
        c.connect();

        boolean auth = false;
    
        while (!auth) {
            System.out.println("\n1. Login");
            System.out.println("2. Signup");
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
            System.out.println("2. Exit");

            int option = sc.nextInt();

            switch (option) {
                case 1:
                    // c.showContacts();
                    break;

                case 2:
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