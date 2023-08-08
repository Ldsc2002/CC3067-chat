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
            System.out.println("\n1. Contacts");
            System.out.println("2. Send message");
            System.out.println("3. Group management");
            System.out.println("4. Change status message");
            System.out.println("5. Change status");
            System.out.println("0. Exit");

            int option = sc.nextInt();

            switch (option) {
                case 1:
                    while (true) {
                        System.out.println("\n1. Show contacts");
                        System.out.println("2. Add contact");
                        System.out.println("3. Show contact info");
                        System.out.println("0. Back");

                        int option2 = sc.nextInt();

                        switch (option2) {
                            case 1:
                                c.showContacts();
                                break;

                            case 2:
                                c.addContact();
                                break;

                            case 3:
                                c.showContactInfo();
                                break;

                            case 0:
                                break;

                            default:
                                System.out.println("Invalid option");
                                break;
                        }

                        if (option2 == 0) {
                            break;
                        }
                    }

                case 2:
                    while (true) {
                        System.out.println("\n1. Send private message");
                        System.out.println("2. Send group message");
                        System.out.println("3. Send file");
                        System.out.println("0. Back");

                        int option2 = sc.nextInt();

                        switch (option2) {
                            case 1:
                                c.sendPrivateMessage();
                                break;

                            case 2:
                                c.sendGroupMessage();
                                break;

                            case 3:
                                c.sendFile();
                                break;

                            case 0:
                                break;

                            default:
                                System.out.println("Invalid option");
                                break;
                        }

                        if (option2 == 0) {
                            break;
                        }
                    }

                case 3:
                    while (true) {
                        System.out.println("\n1. Create group");
                        System.out.println("2. Show groups");
                        System.out.println("3. Join group");
                        System.out.println("0. Back");

                        int option2 = sc.nextInt();

                        switch (option2) {
                            case 1:
                                c.createGroup();
                                break;

                            case 2:
                                c.showGroups();
                                break;

                            case 3:
                                c.joinGroup();
                                break;

                            case 0:
                                break;

                            default:
                                System.out.println("Invalid option");
                                break;
                        }

                        if (option2 == 0) {
                            break;
                        }
                    }

                case 4:
                    c.changeStatusMessage();
                    break;

                case 5:
                    c.changeStatus();
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