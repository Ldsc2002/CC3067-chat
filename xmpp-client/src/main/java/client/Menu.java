package client;

import java.util.Scanner;

public class Menu {
    // Private scanner to read user input
    private Scanner sc = new Scanner(System.in);

    public void mainMenu() {
        // Create client object and connect to server
        Client c = new Client();
        c.connect();

        // Is user authenticated?
        boolean auth = false;
    
        // While not authenticated, show login menu
        while (!auth) {
            System.out.println("\n1. Log In");
            System.out.println("2. Create account");
            System.out.println("0. Exit");
            System.out.println("Enter option: ");
            
            int option = -1;
            try {
                String input = sc.nextLine();
                option = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Invalid option");
            }

            switch (option) {
                case -1:
                    // -1 Input error
                    break;

                case 1:
                    // If user logs in successfully, auth = true
                    auth = c.login();
                    break;

                case 2:
                    c.signup();
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

        // Add message listener to receive messages and notifications after authentication
        c.addMessageListener();

        // Show main menu
        while (auth) {
            System.out.println("\n1. Contacts");
            System.out.println("2. Send message");
            System.out.println("3. Group management");
            System.out.println("4. Change status message");
            System.out.println("5. Change status");
            System.out.println("6. Delete account");
            System.out.println("0. Exit");

            int option = -1;
            try {
                String input = sc.nextLine();
                option = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Invalid option");
            }

            switch (option) {
                case -1:
                    // -1 Input error
                    break;

                case 1:
                    // Show contacts menu
                    while (true) {
                        System.out.println("\n1. Show contacts");
                        System.out.println("2. Add contact");
                        System.out.println("3. Show contact info");
                        System.out.println("0. Back");

                        int option2 = -1;
                        try {
                            String input = sc.nextLine();
                            option2 = Integer.parseInt(input);
                        } catch (Exception e) {
                            System.out.println("Invalid option");
                        }

                        switch (option2) {
                            case -1:
                                // -1 Input error
                                break;

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

                    break;

                case 2:
                    // Show send message menu
                    while (true) {
                        System.out.println("\n1. Send private message");
                        System.out.println("2. Send group message");
                        System.out.println("3. Send file");
                        System.out.println("0. Back");

                        int option2 = -1;
                        try {
                            String input = sc.nextLine();
                            option2 = Integer.parseInt(input);
                        } catch (Exception e) {
                            System.out.println("Invalid option");
                        }

                        switch (option2) {
                            case -1:
                                // -1 Input error
                                break;

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

                    break;

                case 3:
                    // Show group management menu
                    while (true) {
                        System.out.println("\n1. Create group");
                        System.out.println("2. Show groups");
                        System.out.println("3. Join group");
                        System.out.println("0. Back");

                        int option2 = -1;
                        try {
                            String input = sc.nextLine();
                            option2 = Integer.parseInt(input);
                        } catch (Exception e) {
                            System.out.println("Invalid option");
                        }

                        switch (option2) {
                            case -1:
                                // -1 Input error
                                break;

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

                    break;

                case 4:
                    c.changeStatusMessage();
                    break;

                case 5:
                    c.changeStatus();
                    break;

                case 6:
                    // Delete account and exit
                    c.deleteAccount();
                    System.exit(0);
                    break;

                case 0:
                    // Disconnect and exit
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