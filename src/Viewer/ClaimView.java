package Viewer;

import Controller.ClaimController;
import Model.BankingInfo;
import Model.Claim;
import Model.Customer;

import java.text.SimpleDateFormat;
import java.util.*;

public class ClaimView {
    private Scanner in = new Scanner(System.in);
    private ClaimController controller;
    public void AdminScreen()
    {
        System.out.println("╔═════════════════════════════════╗");
        System.out.println("║  ADMIN CLAIM MANAGER MENU       ║");
        System.out.println("╠═════════════════════════════════╣");
        System.out.println("║ Enter 1: Add Claim              ║");
        System.out.println("║ Enter 2: Update Claim via ID    ║");
        System.out.println("║ Enter 3: Delete Claim via ID    ║");
        System.out.println("║ Enter 4: Get Claim via ID       ║");
        System.out.println("║ Enter 5: Get All Claims         ║");
        System.out.println("║ Enter 6: Exit                   ║");
        System.out.println("╚═════════════════════════════════╝");
    }
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void displayClaim(Claim claim) {
        System.out.println("Claim ID: " + claim.getId());
        System.out.println("Claim Date: " + sdf.format(claim.getClaimDate()));
        System.out.println("Insured Person: " + claim.getInsuredPerson().getFullName());
        System.out.println("Card Number: " + claim.getCardNumber());
        System.out.println("Exam Date: " + sdf.format(claim.getExamDate()));
        System.out.println("Claim Amount: " + claim.getClaimAmount());
        System.out.println("Status: " + claim.getStatus());
        System.out.println("Receiver Banking Info: " + claim.getReceiverBankingInfo().getBank() + " - " + claim.getReceiverBankingInfo().getName() + " - " + claim.getReceiverBankingInfo().getNumber());
        System.out.println("Documents: ");
        for (String document : claim.getDocuments()) {
            System.out.println("- " + document);
        }
    }

    public void displayNewClaimForm() {
        System.out.println("╔═════════════════════════════════╗");
        System.out.println("║         NEW CLAIM FORM          ║");
        System.out.println("╚═════════════════════════════════╝");


        System.out.print("Enter Claim ID in format of f-xxxxxxxxxx(10 xs): ");
        String id = in.nextLine();
        while (!id.matches("f-\\d{10}")) {
            System.out.println("Invalid Claim ID format.  Please re-enter, example: f-1234567890.");
            id = in.nextLine();
        }
        System.out.print("Enter Claim Date in format of yyyy-MM-dd, for example: 2024-04-07 :  ");
        String claimDateStr = in.nextLine();
        Date claimDate = null;
        try {
            claimDate = new SimpleDateFormat("yyyy-MM-dd").parse(claimDateStr);
        } catch (Exception e) {
            System.out.println("Invalid date format!");
            return;
        }

        Customer insuredPerson;
        do {
            System.out.print("Enter Insured Person's ID: ");
            System.out.print("Please enter the format of c-xxxxxxx(7 xs):");
            String insuredPersonId = in.nextLine();
            controller.getCustomerById(insuredPersonId);
             insuredPerson =  null;
            if (insuredPerson == null) System.out.println("Insured Person does not exist!");
        } while (insuredPerson == null);

        String cardNumber = insuredPerson.getInsuranceId();

        System.out.print("Enter Examination Date in format of yyyy-MM-dd, for example: 2024-04-07 : ");
        String examDateStr = in.nextLine();
        Date examDate = null;
        try {
            examDate = new SimpleDateFormat("yyyy-MM-dd").parse(examDateStr);
        } catch (Exception e) {
            System.out.println("Invalid date format!");
            return;
        }

        List<String> documents = new ArrayList<>();
        String document;
        do {
            System.out.print("Enter document (or 'done' to finish): ");
            document = in.nextLine();
            if (!document.equals("done")) {
                documents.add(document);
            }
        } while (!document.equals("done"));

        System.out.print("Enter Claim Amount: ");
        double claimAmount = in.nextDouble();

        in.nextLine();

        String status = "new";

        System.out.println("Banking Info:");
        System.out.print("Enter bank Name: ");
        String bankName = in.nextLine();
        System.out.print("Enter owner Name: ");
        String ownerName = in.nextLine();
        System.out.print(" Enter account Number: ");
        String accountNumber = in.nextLine();

        BankingInfo receiverBankingInfo = new BankingInfo(bankName, ownerName, accountNumber);

        Claim newClaim = new Claim(
                id,
                claimDate,
                insuredPerson,
                cardNumber,
                examDate,
                documents,
                claimAmount,
                status,
                receiverBankingInfo
        );


        insuredPerson.addnewclaim(newClaim);
    }
    public void deleteClaimForm() {
        System.out.println("╔═════════════════════════════════╗");
        System.out.println("║         DELETE CLAIM FORM       ║");
        System.out.println("╚═════════════════════════════════╝");

        String claimId;
        Claim FindclaimbyID = null;
        while (true) {
            System.out.print("Enter the ID of the claim you want to delete:");
            System.out.print("Please enter the format of c-xxxxxxx(7 xs):");

            claimId = in.nextLine();

            FindclaimbyID = controller.getClaim(claimId);

            if (FindclaimbyID == null) {
                System.out.println("Cannot Find Claim with ID: " + claimId);
                System.out.println("Enter 1: Continue searching");
                System.out.println("Enter 2: Exit");
                System.out.print("Your choice: ");
                int choice = in.nextInt();
                in.nextLine();

                if (choice == 1) {
                    continue;
                } else if (choice == 2) {
                    return;
                }
            } else {
                System.out.println("Claim found:");
                displayClaim(FindclaimbyID);
                System.out.println("Is this the claim you're looking for?");
                System.out.println("Enter 1: Yes");
                System.out.println("Enter 2: No, i want to continue searching");
                System.out.println("Enter 3: I want to use other functions");
                System.out.print("Your choice: ");
                int choice = in.nextInt();
                in.nextLine();

                if (choice == 1) {
                    controller.getCustomerById(FindclaimbyID.getInsuredPerson().getId()).removeClaim(FindclaimbyID);
                    controller.deleteClaim(claimId);
                    System.out.println("Claim deleted");
                } else if (choice == 2) {
                    continue;
                } else if (choice == 3) {
                    return;
                }
            }
        }
    }


}

