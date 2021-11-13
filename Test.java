/***
 * Class to model the Test class which contains the main method
 * @author Udita Agarwal
 * @version 0.1
 * Date of creation: October 23, 2021
 * Last Date Modified: October 25, 2021
 */


import java.util.Stack; // to use Stack class 
import java.util.EmptyStackException; // to throw EmptyStackException class 
import java.util.Scanner; // to use Scanner class 
import java.io.File; // to use File class 
import java.io.FileNotFoundException; // to throw FileNotFoundException class 
import java.util.LinkedList; // to use LinkedList class 

public class Test{
    public static void main (String[] args){

        // PART 1
        Scanner keyboard = new Scanner(System.in);
        String userAns = "";

        // do while loop prompts user for file path, accepts user inputs, and executes check method until user says no for checking another file
        do{
            
            System.out.println("Enter a file path:"); // prompts user for file path
            String filePath = keyboard.nextLine(); // accepts input 
            System.out.println("Enter the first character:"); // prompts user for first character
            String firstChar = keyboard.nextLine(); // accepts input
            char first = firstChar.charAt(0); // converts to char
            System.out.println("Enter the second character:"); // prompts user for second character
            String secondChar = keyboard.nextLine(); // accepts input
            char second = secondChar.charAt(0); // converts to char
            check(filePath, first, second); // calls check method and passes filePath, first, second variables
            System.out.println("Do you want to check another file? (yes/no)"); // asks user if they want to check another file
            userAns = keyboard.nextLine(); // accepts input 
        } while(userAns.equals("yes")); // executes do-while loop for as long as user says yes

 
        // PART 2
        System.out.printf("%-15s\t%-15s\t%-15s\t%-15s\n", "Cashiers", "Customers", "Average number of waiting customers", "Average waiting time per customer (mins)");
        simulation(480, 25/60.0, 5, 1);
        simulation(480, 25/60.0, 5, 2);
        simulation(480, 25/60.0, 5, 3);
        simulation(480, 25/60.0, 5, 4);
        simulation(480, 25/60.0, 5, 5);
    }

     /***
 	 * checks file for matching braces 
 	 * @param filePath which has the path of the file
     * @param first which contains the first character entered by user
     * @param second which contains the second character entered by user
 	 * no return value 
 	 */
    public static void check(String filePath, char first, char second){
        // creates an instance of the file with given file path
        File file = new File(filePath);
        // creates an instance of the class Stack for type String
        Stack<String> checkChar = new Stack<>();

            try{
                Scanner readFile = new Scanner(file);
                int lineCount = 0;
                // reads file line by line and checks for each character in the line
                while(readFile.hasNextLine()){
                    String line = readFile.nextLine();
                    lineCount++;
                    // checks if each character in the line matches either the first or second char 
                    for(int i = 0; i < line.length(); i++){
                        // if it matches first char, it pushes the first character and the line number
                        if(line.charAt(i) == first){
                            checkChar.push(first + ": line " + lineCount);
                        }
                        // if it matches second char, it pops an element from the stack
                        if(line.charAt(i) == second){
                            checkChar.pop();
                        }
                    }
                }
                // if the stack is empty, the pairs are correct and matching 
                if(checkChar.isEmpty()){
                    System.out.println("All '"+ first +"' and '"+ second + "' match.");
                // else file has mismatching pairs and the elements remaining in the stack are printed along with the line numbers 
                } else{
                    System.out.println("Mismatching: The following "+ first + "do not have a matching " + second);
                    while(!checkChar.isEmpty()){
                        System.out.println(checkChar.pop());
                    }
                }
            } 
            // catches file not found exception and prints error message 
            catch(FileNotFoundException e){
                System.out.println("File not found");
                System.exit(0);
            }
            // catches empty stack exception and prints error message 
            catch(EmptyStackException e){
                System.out.println("Mismatching pairs. There are more of" + second + " than " + first);
            }
           
            
    }


    /***
 * Method to simulate a queueing system where customers arrive at a given rate
 * Customers are served by one cashier
 * @param simulationTime the duration of the simulation in minutes
 * @param arrivalRate the number of customers who arrive in one minute
 * @param serviceTime the time, in minutes, it takes the cashier to serve one customer
 * @param numCashier the number of cashiers to serve the customers
 * no return value
 * The method prints the following simulation results:
 * Total number of cashiers available to serve customers
 * Total number of customers who arrived during the simulation time
 * Average number of customers who waited in the queue
 * Average waiting time per customer
*/
    public static void simulation(double simulationTime, double arrivalRate, double serviceTime, int numCashiers) {
        double[] servers = new double[numCashiers];
        // simulation variables
        double totalWaitingTime = 0;
        int customers = 0;
        int totalWaitingCustomers = 0;

        LinkedList<Double> queue = new LinkedList<>(); // where customer arrival times are enqueued

        for(double time=0; time<=simulationTime; time++) { // simulation loop
            double random = Math.random(); // generate a probability (between 0 and 1)
            if(random > arrivalRate) { // A customer arrives if the probability > arrivalRate
                queue.offer(time); // enqueue the arrival time of the customer (current time)
                customers++; // increment the number of customers
            }
        
            // if the cashier is not free, decrement the service time
            for(int i = 0; i < servers.length; i++){
                if(servers[i]!=0){
                    servers[i]--;
                }      
            }
            int i;
            // there are customers in the queue and the cashier is free
            while((i = checkCashierFree(servers)) != -1 && !queue.isEmpty()){
                servers[i] = serviceTime; // set cashier to service time
                totalWaitingTime += time - queue.poll(); // remove a customer from the queue
            }
            totalWaitingCustomers += queue.size(); // calculate her/his waiting time and add the waiting time to the total waiting time
        }
        System.out.printf("%-15d\t%-15d\t%-15d\t%-15.2f\n", numCashiers, customers, (int)(totalWaitingCustomers/simulationTime), (totalWaitingTime/customers)); 
}

 /***
 * Method to check if the cashier is free 
 * @param servers which contains an array of doubles containing the servers 
 * @return index of the array with free cashier or -1 if no cashier is free
*/
public static int checkCashierFree(double[] servers){
    for(int i = 0; i< servers.length; i++){
        if(servers[i] == 0){
            return i;
        }
    }
    return -1;
}

}
