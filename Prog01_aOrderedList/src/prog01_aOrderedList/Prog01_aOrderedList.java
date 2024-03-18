package prog01_aOrderedList;
/**
 * CSC1351 programming project no. 1
 * section 2
 * @author Colton Comiskey
 * @since 03/17/2024
 */
import java.util.Scanner;
import java.util.Arrays;
import java.io.*;
import java.io.FileNotFoundException;

public class Prog01_aOrderedList {
    /**
     * steps included:
     *   step 2: Input Car Data
     *   step 4: Populating aOrderedList
     *   step 7: Operational Script
     *   step 8: Extend our aOrderedList class
     *
     * CSC1351 programming project no. 1
     * section 2
     * @author Colton Comiskey
     * @since 03/17/2024
     */

    public static void main(String[] args) {
        Scanner in;
        // step 2: Input Car Data
        while(true){
            try{
                in = GetInputFile("Enter input filename: ");
                break;
            }
            catch(FileNotFoundException e){
                String message = e.getMessage();
                for(int i = 0; i < message.length(); i++){
                    if(message.charAt(i) == '('){
                        message = message.substring(0,i-1);       //reads name of file name that is not found
                        break;
                    }
                }
                System.out.print("File specified <"+message+"> does not exist. Would you like to continue? <Y/N>  ");
                //prompts the user to reenter a corrected value
                Scanner confirm = new Scanner(System.in);
                String input = confirm.next();
                if(input.equalsIgnoreCase("N")){                        //terminates program and notifies user
                    System.out.println("\nProgram ended by user.\n");
                    System.exit(0);
                }
                else if(!input.equalsIgnoreCase("Y")){                  //insurance for typos
                    System.out.println("\nCommand unclear. Program terminating.\n");
                }
            }
        }

        //step 4: populating aOrderedList
        aOrderedList list = new aOrderedList();

        //step 7: Operational Script and step 8: Extend our aOrderedList class

        while(in.hasNextLine()){                                        //runs if input file has more data
            String cDesc = in.nextLine();       //store data
            String[] fields = cDesc.split(",");     //format data with commas
            if(fields[0].equals("A")){      //selectively adding input based on presence of 'A'
                Car c = new Car(fields[1], Integer.parseInt(fields[2]), Integer.parseInt(fields[3]));       //stores all data in a Car object
                list.add(c);        //add the object c with 3 separate parameters into the list
            }
            else{
                if(fields.length == 2){
                    int index = Integer.parseInt(fields[1]);
                    list.remove(index);
                }
                else {
                    String make = fields[1];
                    int year = Integer.parseInt(fields[2]);     //stores make and year data
                    for(int i = 0; i < list.size(); i++){
                        Car c = (Car)list.get(i);
                        if(make.equals(c.getMake()) && year == c.getYear()){        //Car object is deleted if values match
                            list.remove(i);
                            break;
                        }
                    }
                }

            }
        }
        in.close();
        //step 4: Populating aOrderedList
        PrintWriter p;
        while(true){
            try{
                p = GetOutputFile("Enter output filename: ");              //calls the method GetOutputFile below
                break;
            }
            catch(FileNotFoundException e){
                String message = e.getMessage();
                for(int i = 0; i < message.length(); i++){
                    if(message.charAt(i) == '('){
                        message = message.substring(0,i-1);         //reads the name of the incorrect file to be displayed below
                        break;
                    }
                }
                System.out.print("Filename entered <"+message+"> is not valid. Please press Y to type a different name, or press N to exit the program. <Y/N>  ");
                //notify the user that the value entered is invalid
                //prompts the user to reenter a valid value

                Scanner confirm = new Scanner(System.in);
                String input = confirm.next();
                if(input.equalsIgnoreCase("N")){                        //terminates the program
                    System.out.println("\nProgram terminated by user.\n");
                    System.exit(0);
                }
                else if(!input.equalsIgnoreCase("Y")){                  //accommodating for cases of typos and return the user to the previous input stage
                    System.out.println("\nUnclear command.\n");
                }
            }
        }
        p.println(list.toString());
        p.close();

        //step 7: Operational Script
        System.out.println("Number of cars:\t"+list.size()+"\n");   //format number of cars
        for(int i = 0; i < list.size(); i++){
            Car c = (Car)list.get(i);
            int price = c.getPrice();
            String $price = "";
            int count = 0;

            //inverse division to make the money separated with a comma
            while(price != 0){
                count++;
                String lastDigit = String.valueOf(price % 10);
                price /= 10;
                $price = lastDigit + $price;        //calculations for correct price
                if(count % 3 == 0){
                    $price = "," + $price;
                }
            }
            $price = "$" + $price;

            System.out.printf("Make:\t%12s\nYear:\t%12d\nPrice:\t%12s\n\n", c.getMake(), c.getYear(), $price);
        }
    }
    //step 2: Input Car Data
    public static Scanner GetInputFile(String UserPrompt) throws FileNotFoundException{
        System.out.print(UserPrompt);
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        File inputFile = new File(input);
        return new Scanner(inputFile);
    }

    //Part of Step 4: Populating aOrderedList
    public static PrintWriter GetOutputFile(String UserPrompt) throws FileNotFoundException{
        System.out.print(UserPrompt);
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        File outputFile = new File(input);
        return new PrintWriter(outputFile);
    }
}
/**
 * steps included:
 *
 *  step 3: aOrderedList class
 *  step 5: Managing Array Size
 *  step 6: Completion of aOrderedList Class
 *  step 8: Extend aOrderedList class
 *
 * CSC1351 programming project no. 1
 * section 2
 * @author Colton Comiskey
 * @since 03/17/2024
 */
class aOrderedList {

    final int SIZEINCREMENTS = 20;  //size of increments for increasing ordered list
    private Comparable[] oList;     //the ordered list
    private int listSize;           //the size of the ordered list
    private int numObjects;         //number of objects in the ordered list
    private int current;        //index of current element

    public aOrderedList(){                      //Constructor that sets numObjects to 0, sets listSize to the value of SIZEINCREMENTS, and instantiates the array oList to an array of sizeSIZEINCREMENTS
        numObjects = 0;
        listSize = SIZEINCREMENTS;
        oList = new Comparable[listSize];
    }

    public String toString(){
        String result = "";
        for(int i = 0; i < numObjects; i++){
            if(i > 0){
                result += ",";      //format each result with commas in between
            }
            Comparable c = oList[i];
            result += "[" + c.toString() + "]";
        }
        return result;
    }

    public void add(Comparable newObject){      //generic constructor
        //step 5: Managing Array Size and step 8: Extend our aOrderedList class
        //returns copy of oList with 20 more spots
        if(numObjects == listSize){
            listSize += SIZEINCREMENTS;
            Comparable[] copyArr = Arrays.copyOf(oList, listSize);
            oList = copyArr;
        }
        //binary search!! to put all objects in order
        int beg = 0;
        int end = numObjects;
        int mid = 0;
        while(end > beg){
            mid = ((end - beg) / 2) + beg;
            //then add starting point value
            Comparable c = oList[mid];
            int result = newObject.compareTo(c);
            if(result < 0){
                end = mid;
            }
            else if(result > 0){
                beg = mid+1;

            }
            else{
                add(newObject, mid);
                return;
            }
        }
        add(newObject, beg);        //insurance for if no other cases are true
    }

    //step 6: Completion of aOrderedList Class
    public int size(){
        return numObjects;      //returns number of objects in list
    }

    public Comparable get(int index){
        return oList[index];        //returns object at given index
    }

    public void remove(int index){      //removes the object at the given index. no return
        for(int i = index; i < numObjects-1; i++){
            oList[i] = oList[i+1];
        }
        numObjects--;
    }
    //step 8: Extend our aOrderedList class
    public void reset(){                //resets iterator parameters so that the “next” element
        //is the first element in the array, if any
        current = 0;
    }

    public Comparable next(){           //returns the next element in the iteration and increments the iterator parameters
        current++;
        return oList[current];
    }

    public boolean hasNext(){           //returns true if the iteration has more elements to iterate through
        return current + 1 < numObjects;
    }

    public void remove(){               //removes the last element returned by the next() method
        this.remove(current);
        current = 0;
    }

    private void add(Comparable newObject, int index){      //adds given object in at given index
        for(int i = numObjects; i > index; i--){
            oList[i] = oList[i-1];
        }
        oList[index] = newObject;
        numObjects++;
    }
}

/**
 * steps included:
 *   step 1: Car Class
 *   step 8: Extend our aOrderedList class
 *
 * CSC1351 programming project no. 1
 * section 2
 * @author Colton Comiskey
 * @since 03/17/2024
 */

class Car implements Comparable<Car> {
    private String make;
    private int year;
    private int price;

    //Public methods
    public Car(String make, int year, int price){   //constructor that sets values for instance variables
        this.make = make;
        this.year = year;
        this.price = price;
    }

    public String getMake() { return make; }    //returns the value of make
    public int getYear() { return year; }       //returns the value of year
    public int getPrice() { return price; }     //returns the value of price

    public int compareTo(Car other){            //compares each Car based on make, followed by year
        int result = make.compareTo(other.getMake());
        if(result != 0){
            return result;
        }
        return year - other.getYear();
    }

    public String toString(){                   //returns each instance variable in a specific format
        return String.format("Make: %s, Year: %d, Price: %d;", make, year, price);
    }
}