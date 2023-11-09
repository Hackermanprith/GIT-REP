package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class Hospital {
    static HashMap<String, ArrayList<Rooms>> Floorplan;
    HashMap<String, Operation> Operation_Registry;
    HashMap<String,Boolean> hasThings;
    HashMap<String ,Patient> Patient_Registry;
    HashMap<String,Doctor>Doctor_Registry;
    HashMap<String,ArrayList<String>> Doctor_Schedule;
    HashMap<String,Staff>Staff_Registry;
    String clinic_managementSystem;
    Financials financials;
    public static int Takeintinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        int number = 0;
        try {
            number = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid Input, Try again");
            number = Takeintinp(msg);
        }
        System.out.println();
        return number;
    }

    public static String Takestrinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();

    }

    Hospital() {
        Patient_Registry = new HashMap<>();
        Doctor_Registry = new HashMap<>();
        Floorplan = new HashMap<>();
        hasThings = new HashMap<>();
        System.out.println("Welcome to the Clinic Management System");
        System.out.println("                                by MSD");
        clinic_managementSystem = Takestrinp("Enter the name of the clinic: ");
        String hasoperation = Takestrinp("Do you have a operation room Y/N: ");
        hasThings.put("OT",hasoperation.toLowerCase().equals("y"));
        String haswaiting = Takestrinp("Do you have bed's Y/N: ");
        hasThings.put("Bed",haswaiting.toLowerCase().equals("y"));
        haswaiting = Takestrinp("Does the clinic have chamber Y/N: ");
        hasThings.put("Chambers",haswaiting.toLowerCase().equals("y"));
        int nooffloors = Takeintinp("Enter the number of floors: ");
        floorPlannerMenu(nooffloors);
        System.out.print("\033[H\033[2J");
        financials = new Financials();

    }

    public void RegisterPatient(){
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = Takestrinp("Enter the phone no of the patient: ");
        String addmitno = name.substring(0).trim()+"_"+phoneno.substring(8,11).trim();
        String RegisterAseatofDoc = Takestrinp("Enter the doctor id to register the patient: ");
        String enterthedate = Takestrinp("Enter the date of Registration : ");
        if(Patient_Registry.containsKey(addmitno.toLowerCase())){
            ScheduleAppointment(enterthedate,Patient_Registry.get(addmitno),RegisterAseatofDoc);
        }
        else{
            System.out.println("Not Registered");
            ArrayList<String> patientdata = new ArrayList<String>();
            String adress = Takestrinp("Enter the adress of the patient: ");
            String email = Takestrinp("Enter the email of the patient: ");
            String Gurdian = Takestrinp("Enter the Gurdian of the patient: ");
            String GurdianPhoneno = Takestrinp("Enter the Gurdian Phone no of the patient: ");
            Patient pat = new Patient(addmitno,enterthedate,name,phoneno,null,adress,Gurdian,GurdianPhoneno,email);
            Patient_Registry.put(addmitno,pat);
            ScheduleAppointment(enterthedate,pat,RegisterAseatofDoc);
        }

    }

    public static void beds_printing() {
        for (String floorKey : Floorplan.keySet()) {
            ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
            String[] floorInfo = floorKey.split("_");
            int floor = Integer.parseInt(floorInfo[0]);

            System.out.println("Empty beds on Floor " + floor + ":");

            for (Rooms room : roomsOnFloor) {
                for (String bedType : room.occupant.keySet()) {
                    Beds bed = room.occupant.get(bedType);
                    if (bed.occupant == null) {
                        System.out.println("Room " + floorKey + " - Bed Type: " + bedType);
                    }
                }
            }
        }
    }

    public void floorPlannerMenu(int floorNo) {
        int n;
        for(int i = 1;i<= floorNo;i++){
            System.out.println("Floor Planner Menu for Floor " +i);
            do {
                System.out.println("1. Add Rooms");
                if (hasThings.get("Chambers")) {
                    System.out.println("2. Add Chamber");
                }
                if (hasThings.get("OT")) {
                    System.out.println("3. Add Operation Room");
                }
                System.out.println("4. Exit");
                n = Takeintinp("Enter your choice: ");
                switch (n) {
                    case 1:
                        String roomType = Takestrinp("Enter the type of room: ");
                        roomPlanner(i, roomType);
                        if (hasThings.get("Chambers")) {
                            addChamber(i);
                        } else {
                            System.out.println("Chambers are not available in this hospital.");
                        }
                        break;
                    case 3:
                        if (hasThings.get("OT")) {
                            addOperationRoom(i);
                        } else {
                            System.out.println("Operation rooms are not available in this hospital.");
                        }
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (n != 4);
        }
    }


    public void roomPlanner(int floorNo, String roomType) {
        ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorNo + "_" + roomType);
        if (roomsOnFloor == null) {
            roomsOnFloor = new ArrayList<>();
            Floorplan.put(floorNo + "_" + roomType, roomsOnFloor);
        }

        int roomID = roomsOnFloor.size() + 1;
        String addBeds = Takestrinp("Do you want to add beds Y/N: ").toLowerCase();

        int roomCapacity = Takeintinp("Enter the capacity of the room: ");
        Rooms room = new Rooms(roomID + "_" + roomType, roomType, roomCapacity);
        roomsOnFloor.add(room);

        if (addBeds.equals("y")) {
            addBeds(room, roomID, floorNo);
        }
    }
    public void addOperationRoom(int floorNo) {
        ArrayList<Rooms> theatresOnFloor = Floorplan.get(floorNo + "_OperationRoom");
        if (theatresOnFloor == null) {
            theatresOnFloor = new ArrayList<>();
            Floorplan.put(floorNo + "_OperationRoom", theatresOnFloor);
        }

        int theatreID = theatresOnFloor.size() + 1;

        int theatreCapacity = Takeintinp("Enter the capacity of the operation theatre: ");
        OperationTheatre theatre = new OperationTheatre(theatreID + "_OperationRoom", theatreCapacity);
        theatresOnFloor.add(theatre);

        System.out.println("Operation theatre added: " + theatre.getTheatreID());
    }


    public void addBeds(Rooms room, int roomID, int floorNo) {
        int numTypesOfBeds = Takeintinp("Enter the number of types of beds you want to have.To exit enter 0: ");
        if(numTypesOfBeds == 0){
            return;
        }
        for (int i = 1; i <= numTypesOfBeds; i++) {
            int numBeds = Takeintinp("Enter the number of beds for this type: ");
            String bedType = Takestrinp("Enter the type of beds: ");
            for (int j = 1; j <= numBeds; j++) {
                Beds bed = new Beds(roomID + "_" + bedType + "_" + j, null, bedType);
                room.occupant.put(bedType, bed);
            }
        }
    }
    public void addChamber(int floorNo) {
        ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorNo + "_Chamber");
        if (roomsOnFloor == null) {
            roomsOnFloor = new ArrayList<>();
            Floorplan.put(floorNo + "_Chamber", roomsOnFloor);
        }

        int roomID = roomsOnFloor.size() + 1;

        int roomCapacity = Takeintinp("Enter the capacity of the chamber: ");
        Chamber chamber = new Chamber(roomID + "_Chamber", roomCapacity);
        roomsOnFloor.add(chamber);

        System.out.println("Chamber added: " + chamber.getChamberID());
    }


public void ScheduleAppointment(String Date,Patient patient,String DoctorID){
        if(Doctor_Registry.containsKey(DoctorID)){
            Doctor doc = Doctor_Registry.get(DoctorID);
            doc.AddPatientToSchedule(Date,patient);
            financials.addIncome(String.valueOf("Doctors visit by"+ patient.addmitno), doc.perpatientcharge, (doc.perpatientcharge-doc.clinicshare), true);
            doc.earned+=doc.perpatientcharge-doc.clinicshare;
        }
        else{
            System.out.println("Doctor not found");
        }
    }
    public void RegisterDoctor(){
        String name = Takestrinp("Enter the name of the doctor: ");
        String phoneno = Takestrinp("Enter the phone no of the doctor: ");
        String doctorid = name.substring(0).trim()+"_"+phoneno.substring(8,11).trim();
        String speaclity = Takestrinp("Enter the speaclity of the doctor: ");
        String emphno = Takestrinp("Enter the emergency no of the doctor: ");
        ArrayList<String> data = new ArrayList<String>();
        data.add(name);
        data.add(phoneno);
        data.add(doctorid);
        data.add(speaclity);
        data.add(emphno);
        int dlimit = Takeintinp("Enter the daily limit of the doctor: ");
        double perpatientcharge = Takeintinp("Enter the per patient charge of the doctor: ");
        Doctor doc = new Doctor(doctorid,name,speaclity,emphno,data,perpatientcharge,dlimit);
        Doctor_Registry.put(doctorid,doc);
        System.out.println("Doctor Registered");
    }
    public void NewBooking(){
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = Takestrinp("Enter the phone no of the patient: ");
        String addmitno = name.substring(0).trim()+"_"+phoneno.substring(8,11).trim();
        String[] docdata = printDocChart();
        printDoctorSchedule(docdata[0],docdata[1]);
        String RegisterAseatofDoc = Takestrinp("Enter the doctor id to register the patient: ");
        String enterthedate = Takestrinp("Enter the date of Registration : ");
        if(Patient_Registry.containsKey(addmitno.toLowerCase())){
            ScheduleAppointment(enterthedate,Patient_Registry.get(addmitno),RegisterAseatofDoc);
        }
        else{
            System.out.println("Not Registered");
            ArrayList<String> patientdata = new ArrayList<String>();
            String adress = Takestrinp("Enter the adress of the patient: ");
            String email = Takestrinp("Enter the email of the patient: ");
            String Gurdian = Takestrinp("Enter the Gurdian of the patient: ");
            String GurdianPhoneno = Takestrinp("Enter the Gurdian Phone no of the patient: ");
            Patient pat = new Patient(addmitno,enterthedate,name,phoneno,null,adress,Gurdian,GurdianPhoneno,email);
            Patient_Registry.put(addmitno,pat);
            ScheduleAppointment(enterthedate,pat,RegisterAseatofDoc);

        }
    }
    public void AddOTSchedule(){
        String date = Takestrinp("Enter the date of the operation: ");
        String patientID = Takestrinp("Enter the patient name: ");
        String patientphone = Takestrinp("Enter the patient phone no: ");
        String doctorID = Takestrinp("Enter the doctor ID: ");
        String operationID = Takestrinp("Enter the operation ID: ");
        String time = Takestrinp("Enter the time of the operation: ");
        FreeOT(date,time);
        String theatreID = Takestrinp("Enter theatre id: ");
        Operation operation = new Operation(patientID, date, operationID, doctorID,time,theatreID);
        Operation_Registry.put(operationID, operation);
        patientID = patientID.substring(0).trim()+"_"+patientphone.substring(8,11).trim();
        Patient pat = Patient_Registry.get(patientID);
        Doctor doc = Doctor_Registry.get(doctorID);
        pat.ScheduleOT(doc,date,operationID);
        System.out.println("Operation scheduled successfully.");
    }


    public void removeOTSchedule(){
        for(String floorKey:Floorplan.keySet()){
            ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
            for(Rooms room:roomsOnFloor){
                if(room.roomtype.equals("OperationRoom")){
                    OperationTheatre theatre = (OperationTheatre) room;
                    theatre.printOperations();
                }
            }
        }
        String operationID = Takestrinp("Enter the operation ID: ");
        if (Operation_Registry.containsKey(operationID)) {
            Operation operation = Operation_Registry.get(operationID);
            String date = operation.getDate();
            String theatreID = operation.getTheatreid();
            ArrayList<Rooms> theatreFLOOR = Floorplan.get(1);
            System.out.println("Operation removed successfully.");
        } else {
            System.out.println("Operation not found.");
        }
    }
    public String[] printDocChart(){
        System.out.println("Doctors");
        System.out.println("--------------------------------------------------");
        System.out.printf("| %-12s | %-12s | %-12s |\n", "Day", "Timing", "Patient ID");
        System.out.println("--------------------------------------------------");
        for(String l : Doctor_Registry.keySet()){
            Doctor  doc = Doctor_Registry.get(l);
            System.out.printf(doc.doctorid + "||"+doc.name + " ||"+ doc.speaclity);

        }
        String docname = Takestrinp("Enter the name of the doctor: ");
        String docSpeciality = Takestrinp("Enter the speciality of tbe doctor: ");
        return new String[]{docname,docSpeciality};


    }
    public void printDoctorSchedule(String doctorName, String specialty) {
        String doctorKey = doctorName + "_" + specialty.substring(0,4);

        if (Doctor_Schedule.containsKey(doctorKey)) {
            ArrayList<String> schedule = Doctor_Schedule.get(doctorKey);

            System.out.println("Doctor Schedule for " + doctorName + " (" + specialty + "):");
            System.out.println("--------------------------------------------------");
            System.out.printf("| %-12s | %-12s | %-12s |\n", "Day", "Timing", "Patient ID");
            System.out.println("--------------------------------------------------");

            for (String day : schedule) {
                System.out.println(day);
            }

            System.out.println("--------------------------------------------------");
            System.out.println();
        } else {
            System.out.println("Doctor not found or schedule not available.");
        }
    }
    public void FreeOT(String date,String time){
        for(String floorKey:Floorplan.keySet()){
            ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
            for(Rooms room:roomsOnFloor){
                if(room.roomtype.equals("OperationRoom")){
                    OperationTheatre theatre = (OperationTheatre) room;
                    if(theatre.availability.containsKey(date+"_"+time)){
                        if(theatre.availability.get(date+"_"+time)){
                            System.out.println("Theatre "+theatre.theatreID+" is available");
                        }
                        else{
                            System.out.println("Theatre "+theatre.theatreID+" is not available");
                        }
                    }
                    else{
                        System.out.println("Theatre "+theatre.theatreID+" is available");
                    }
                }
            }
        }
    }
    public void RemovePatientBooking(){
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = Takestrinp("Enter the phone no of the patient: ");
        String admitno = name.substring(0).trim() +"_"+phoneno.substring(8,11).trim();
        String date = Takestrinp("Enter the date of the appointment: ");
        Patient pat = Patient_Registry.get(admitno);
        String docid = pat.getAppointment(date);
        pat.removeAppointment(docid);
        Doctor doc = Doctor_Registry.get(docid);
        doc.RemovePatientReg(date,pat);

    }

    public void mainMeu(){
        int n = 0;
        do{
            System.out.print("\u000c");
            System.out.flush();
            System.out.println("Welcome to the admin menu of "+clinic_managementSystem );
            System.out.println("1. Patient Functions");
            System.out.println("2. Staff Functions");
            System.out.println("3. Management and Infrastructure");
            System.out.println("4. Financials");
            System.out.println("5. Export Data");
            System.out.println("6. Exit");
             n = Takeintinp("-> ");
             switch (n){
                 case 1:
                     PatientMenu();
                     break;
                 case 2:
                     //StaffFunctions();
                     break;
                 case 3:
                     Management();
                     break;
                 case 4:
                     FinancialsMenu();
                     break;
                 case 5:
                     ExportData();
                     break;
                 case 6:
                     return;
                 default:
                     n = Takeintinp("Enter a number in range 1 and 6:");

             }

        }
        while(n!= 6);
    }
    private void FinancialsMenu() {
        int n= 0;
        do{
            System.out.println("Welcome to the Financial Menu of "+clinic_managementSystem+": " );
            double [] arr = financials.quickshotdata();
            System.out.println("Expenses: "+arr[0]);
            System.out.println("Income: "+arr[1]);
            System.out.println("Available balance"+(arr[1]-arr[0]));
            System.out.println("1.Add a new income");
            System.out.println("2.Add a new expense");
            System.out.println("3.Modify Income");
            System.out.println("4.Modify Expense");
            System.out.println("5.Show Financials");
            System.out.println("6.Pay roll");
            System.out.println("6.Exit");
            n = Takeintinp("->");
            switch (n){
                case 1:
                    String incomename= Takestrinp("Source of income: ");
                    double cost = Takeintinp("Cost of the income: ");
                    boolean hosexp = Takestrinp("Does it have a hospital expense Y/N: ").toLowerCase().equals("y");
                    double hosexpense = 0.0;
                    if(hosexp){
                        hosexpense = Takeintinp("Enter the hospital expense: ");
                    }
                    financials.addIncome(incomename,cost,hosexpense,hosexp);
                    break;
                case 2:
                    String expensename= Takestrinp("Source of expense: ");
                    double expensecost = Takeintinp("Cost of the expense: ");
                    financials.addExpense(expensename,expensecost);
                    break;
                case 3:
                    //modify income
                    financials.showFinancials();
                    financials.modfiyIncome();
                    break;
                case 4:
                    //modify expense
                    financials.showFinancials();
                    financials.modfiyExpense();
                    break;
                case 5:
                     financials.showFinancials();
                     break;
                case 6:
                    releasepayroll();
                    break;
                case 7:
                    return;
            }
        }while (n!= 6);
    }

    private void releasepayroll() {
        for(Doctor doc:Doctor_Registry.values()){
            System.out.println("Doctor "+doc.name+" has earned "+doc.earned);
            System.out.println("Paying Doctor "+doc.name+" "+doc.earned);
            financials.addExpense(String.valueOf("Paying"+doc.name),doc.earned);
        }
        for(Staff staff:Staff_Registry.values()){
            System.out.println("Staff "+staff.staffname+" has earned "+staff.staffsalary);
            System.out.println("Paying Staff "+staff.staffname+"_"+staff.staffsalary);
            financials.addExpense(String.valueOf("Paying"+staff.staffname),staff.staffsalary);
        }
    }

    private void ExportData() {
    }
    private void Management() {
        System.out.print("\u000c");
        int n = 0;
        do{
            System.out.println("Welcome to the Management and Infrastructure menu of "+clinic_managementSystem );
            System.out.println("1. Add Doctor");
            System.out.println("2. Add Staff");
            System.out.println("4. Add Operation Room");
            System.out.println("4. Add Chamber");
            System.out.println("5. Add Bed");
            System.out.println("7. Change Doctor things");
            System.out.println("8. Change Staff things");
            System.out.println("9.Remove Doctor");
            System.out.println("10.Remove staff");
            System.out.println("6. Exit");
            n = Takeintinp("-> ");
            switch (n){
                case 1:
                    RegisterDoctor();
                    break;
                case 2:
                    RegisterStaff();
                    break;
                case 3:
                    addOperationRoom(1);
                    break;
                case 4:
                    addChamber(1);
                    break;
                case 5:
                    roomPlanner(1,"Ward");
                    break;
                case 6:
                    return;
                case 7:
                    doctorMenu();
                    break;
                case 8:
                    staffMenu();
                    break;
                case 10:
                    displayStaffMembers();
                    String staffId = Takestrinp("Enter Staff ID to remove: ");
                    Staff staff = findStaffById(staffId);
                    if (staff != null) {
                        Staff_Registry.remove(staffId);
                        System.out.println("Staff member removed successfully.");
                    } else {
                        System.out.println("Staff member not found with ID: " + staffId);
                    }
                    break;
                case 9:
                    displayDoctors();
                    String doctorId = Takestrinp("Enter Doctor ID to remove: ");
                    Doctor doctor = findDoctorById(doctorId);
                    if (doctor != null) {
                        Doctor_Registry.remove(doctorId);
                        System.out.println("Doctor removed successfully.");
                    } else {
                        System.out.println("Doctor not found with ID: " + doctorId);
                    }


                default:
                    n = Takeintinp("Enter a number between (1-7): ");
            }

        }
        while (n != 7);
    }

    private void RegisterStaff() {
        String name = Takestrinp("Enter the name of the staff: ");
        String phoneno = Takestrinp("Enter the phone no of the staff: ");
        String staffid = name.substring(0).trim()+"_"+phoneno.substring(8,11).trim();
        String speaclity = Takestrinp("Enter the speaclity of the staff: ");
        String emphno = Takestrinp("Enter the emergency no of the staff: ");
        double x = Takeintinp("Enter the salary of the staff: ");
        ArrayList<String> data = new ArrayList<String>();
        data.add(name);
        data.add(phoneno);
        data.add(staffid);
        data.add(speaclity);
        data.add(emphno);
        Staff staff = new Staff(name,speaclity,emphno,data,x);
        Staff_Registry.put(staffid,staff);
        System.out.println("Staff Registered");

    }

    public void staffMenu() {
        Scanner scanner = new Scanner(System.in);

        // Assuming you have a method to display the list of staff members
        displayStaffMembers();

        System.out.print("Enter Staff ID to modify: ");
        String staffId = scanner.nextLine();

        Staff staff = findStaffById(staffId);

        if (staff != null) {
            int choice;
            do {
                System.out.println("Staff Menu:");
                System.out.println("1. Change Staff Name");
                System.out.println("2. Change Staff Position");
                System.out.println("3. Change Staff Phone Number");
                System.out.println("4.Change Staff Salary");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()

                switch (choice) {
                    case 1:
                        System.out.print("Enter New Staff Name: ");
                        String newName = scanner.nextLine();
                        staff.staffname = newName;
                        break;
                    case 2:
                        System.out.print("Enter New Staff Position: ");
                        String newPosition = scanner.nextLine();
                        staff.staffdesignation = newPosition;
                        break;
                    case 3:
                        System.out.print("Enter New Staff Phone Number: ");
                        String newPhoneNumber = scanner.nextLine();
                        staff.staffphno = newPhoneNumber;
                        break;
                    case 0:
                        System.out.println("Exiting Staff Menu");
                        break;
                    case 5:
                        System.out.print("Enter New Staff Salary: ");
                        double newSalary = scanner.nextDouble();
                        scanner.nextLine();
                        staff.staffsalary = newSalary;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);
        } else {
            System.out.println("Staff member not found with ID: " + staffId);
        }
    }

    private void displayStaffMembers() {
        System.out.println("List of Staff Members:");
        for (Staff staff : Staff_Registry.values()) {
            System.out.println("Staff ID: " + staff.staffid + ", Staff Name: " + staff.staffname);
        }
    }

    private Staff findStaffById(String staffId) {
        for (Staff staff : Staff_Registry.values()) {
            if (staff.staffid.equals(staffId)) {
                return staff;
            }
        }
        return null;
    }

    public void doctorMenu() {
        Scanner scanner = new Scanner(System.in);

        // Assuming you have a method to display the list of doctors
        displayDoctors();

        System.out.print("Enter Doctor ID to modify: ");
        String doctorId = scanner.nextLine();

        Doctor doctor = findDoctorById(doctorId);

        if (doctor != null) {
            int choice;
            do {
                System.out.println("Doctor Menu:");
                System.out.println("1. Change Doctor Name");
                System.out.println("2. Change Speciality");
                System.out.println("3. Change Phone Number");
                System.out.println("4. Change Per Patient Charge");
                System.out.println("5. Change Daily Appointment Limit");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()

                switch (choice) {
                    case 1:
                        System.out.print("Enter New Doctor Name: ");
                        String newName = scanner.nextLine();
                        doctor.name = newName;
                        break;
                    case 2:
                        System.out.print("Enter New Speciality: ");
                        String newSpeciality = scanner.nextLine();
                        doctor.speaclity = newSpeciality;
                        break;
                    case 3:
                        System.out.print("Enter New Phone Number: ");
                        String newPhoneNumber = scanner.nextLine();
                        doctor.emphno = newPhoneNumber;
                        break;
                    case 4:
                        System.out.print("Enter New Per Patient Charge: ");
                        double newPerPatientCharge = scanner.nextDouble();
                        scanner.nextLine(); // Consume the newline character left by nextDouble()
                        doctor.perpatientcharge = newPerPatientCharge;
                        break;
                    case 5:
                        System.out.print("Enter New Daily Appointment Limit: ");
                        int newDailyLimit = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character left by nextInt()
                        doctor.doctordailyLimit = newDailyLimit;
                        break;
                    case 0:
                        System.out.println("Exiting Doctor Menu");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);
        } else {
            System.out.println("Doctor not found with ID: " + doctorId);
        }
    }

    private void displayDoctors() {
        System.out.println("List of Doctors:");
        for (Doctor doctor : Doctor_Registry.values()) {
            System.out.println("Doctor ID: " + doctor.doctorid + ", Doctor Name: " + doctor.name);
        }
    }

    private Doctor findDoctorById(String doctorId) {
        for (Doctor doctor : Doctor_Registry.values()) {
            if (doctor.doctorid.equals(doctorId)) {
                return doctor;
            }
        }
        return null;
    }

    // Add other hospital-related functions here.



    public void PatientMenu(){
        int n= 0;
        System.out.print("\u000c");
        do{
            System.out.println("Welcome to the Patient menu of "+clinic_managementSystem );
            System.out.println("1.Add Patient");
            System.out.println("2.Schedule Appointment");
            System.out.println("3.Remove Appointment");
            System.out.println("4.Schedule OT");
            System.out.println("5.Remove OT");
            System.out.println("6. Exit");
            int x = Takeintinp("->");
            switch (x){
                case 1:
                    RegisterPatient();
                    break;
                case 2:
                    NewBooking();
                case 3:
                    RemovePatientBooking();
                case 4:
                    AddOTSchedule();
                case 5:
                    removeOTSchedule();
                case 6:
                    return;
                default:
                    x = Takeintinp("Enter a number between (1-6): ");
            }

        }
        while (n != 6);

    }
    

}
