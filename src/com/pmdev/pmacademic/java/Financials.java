package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.pmdev.pmacademic.java.Hospital.Takestrinp;

public class Financials {
    HashMap<String, ArrayList<String>> Expenses;
    HashMap<String, ArrayList<String>> Ins;

    //add expense method
    public double[] quickshotdata() {
        double expenses = 0.0;
        double income = 0.0;
        for (String x : Expenses.keySet()) {
            ArrayList<String> ex = Expenses.get(x);
            expenses += Double.valueOf(ex.get(0));
        }
        for (String k : Ins.keySet()) {
            ArrayList<String> ex = Expenses.get(k);
            income += Double.valueOf(ex.get(0));
        }
        return new double[]{expenses, income};
    }

    public void addIncome(String serviceName, double cost, double hospcost, boolean hosexp) {
        Random rand = new Random();
        int n = rand.nextInt(9999);
        ArrayList<String> data = new ArrayList<>();
        if (hosexp) {
            data.add(String.valueOf(hospcost));
            Expenses.put(serviceName, data);
        }
        data.add(0, serviceName);
        data.add(1, String.valueOf(cost));
        data.add(0, String.valueOf(hospcost));
        Ins.put(serviceName+"_"+n, data);
    }

    public void addExpense(String serviceName, double cost) {
        Random rand = new Random();
        int n = rand.nextInt(9999);
        ArrayList<String> data = new ArrayList<>();
        data.add(0,serviceName);
        data.add(1, String.valueOf(cost));

        Expenses.put(serviceName+"_"+n, data);
    }

        public void showFinancials() {
            System.out.println("------------------------------------------------------------");
            System.out.printf("| %-20s | %-15s | %-15s | %-15s |\n", "ID", "Service Name", "Cost Incurred", "Amount Gained");
            System.out.println("------------------------------------------------------------");

            // Display Income
            for (String incomeName : Ins.keySet()) {
                ArrayList<String> incomeData = Ins.get(incomeName);
                System.out.printf("| %-20s | %-15s | %-15s | %-15s |\n", incomeName, incomeData.get(0), incomeData.get(1), incomeData.get(2));
            }
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.printf("| %-20s | %-15s | %-15s |\n","Expense ID" ,"Expense name", "Cost Incurred");
            System.out.println("------------------------------------------------------------");

            // Display Expenses
            for (String expenseName : Expenses.keySet()) {
                ArrayList<String> expenseData = Expenses.get(expenseName);
                System.out.printf("| %-20s | %-15s | %-15s |\n",expenseName,expenseData.get(0) ,expenseData.get(1), "");
            }

            System.out.println("------------------------------------------------------------");
        }

    public void modfiyIncome() {
        String serviceId = Takestrinp("Enter the service ID: ");
        System.out.print("\u000C");
        ArrayList<String> incomeData = Ins.get(serviceId);
        int n =1;
        if (incomeData == null) {
            System.out.println("Service ID not found");
            return;
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("| %-20s | %-15s | %-15s | %-15s |\n", "ID", "Service Name", "Cost Incurred", "Amount Gained");
        System.out.println("------------------------------------------------------------");
        System.out.printf("| %-20s | %-15s | %-15s | %-15s |\n", serviceId, incomeData.get(0), incomeData.get(1), incomeData.get(2));
        System.out.println("------------------------------------------------------------");
        do{
            System.out.println("Enter the field you want to modify");
            System.out.println("1. Service Name");
            System.out.println("2. Cost Incurred");
            System.out.println("3. Amount Gained");
            System.out.println("0. Exit");
            n = Integer.parseInt(Takestrinp("Enter your choice: "));
            switch (n){
                case 1:
                    String serviceName = Takestrinp("Enter the new service name: ");
                    incomeData.set(0, serviceName);
                    break;
                case 2:
                    double costIncurred = Double.parseDouble(Takestrinp("Enter the new cost incurred: "));
                    incomeData.set(1, String.valueOf(costIncurred));
                    break;
                case 3:
                    double amountGained = Double.parseDouble(Takestrinp("Enter the new amount gained: "));
                    incomeData.set(2, String.valueOf(amountGained));
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice");

            }

        }while(n!=0);

    }

    public void modfiyExpense() {
    }
}


