package com.pyramid;

import java.util.*;
import org.json.simple.*;

public class EgyptianPyramidsApp {
  // I've used two arrays here for O(1) reading of the pharaohs and pyramids.
  // other structures or additional structures can be used
  protected HashMap<Integer, Pyramid> pyramidHashMap = new HashMap<Integer, Pyramid>();
  protected HashMap<Integer, Pharaoh> pharaohHashMap = new HashMap<Integer, Pharaoh>();
  protected HashMap<String, Integer> hieroglyphMap = new HashMap<String, Integer>();
  // Notice that there is a separate HashMap (hieroglyphMap) for matching hieroglyphs to IDs
  // Having this additional HashMap removes the need to iterate through a whole array to find the right hieroglyph

  protected TreeSet<Integer> requestedPyramids = new TreeSet<Integer>();
  // Observe that this program uses a TreeSet rather than an ordinary set
  // (This ensures that the elements are printed out sorted)

  protected Integer numPharaohs;
  protected Integer numPyramids;

  public static void main(String[] args) {
    // create and start the app
    EgyptianPyramidsApp app = new EgyptianPyramidsApp();
    app.start();
  }

  // main loop for app
  public void start() {
    Scanner scan = new Scanner(System.in);
    Character command = '_';

    // loop until user quits
    while (command != 'q') {
      printMenu();
      System.out.print("Enter a command: ");
      command = menuGetCommand(scan);
      executeCommand(scan, command);
    }
  }

  // constructor to initialize the app and read commands
  public EgyptianPyramidsApp() {
    // read egyptian pharaohs
    String pharaohFile =
      "/Users/redsm/Documents/GitHub/egypt-pyramid/src/pyramid/src/main/java/com/pyramid/pharaoh.json";
    JSONArray pharaohJSONArray = JSONFile.readArray(pharaohFile);

    // create and intialize the pharaoh array
    initializePharaoh(pharaohJSONArray);

    // read pyramids
    String pyramidFile =
      "/Users/redsm/Documents/GitHub/egypt-pyramid/src/pyramid/src/main/java/com/pyramid/pyramid.json";
    JSONArray pyramidJSONArray = JSONFile.readArray(pyramidFile);

    // create and initialize the pyramid array
    initializePyramid(pyramidJSONArray);

  }

  // initialize the pharaoh array
  private void initializePharaoh(JSONArray pharaohJSONArray) {
    // create array and hash map
    numPharaohs = pharaohJSONArray.size();
    // initalize the array
    for (int i = 0; i < numPharaohs; i++) {
      // get the object
      JSONObject o = (JSONObject) pharaohJSONArray.get(i);

      // parse the json object
      Integer id = toInteger(o, "id");
      String name = o.get("name").toString();
      Integer begin = toInteger(o, "begin");
      Integer end = toInteger(o, "end");
      Integer contribution = toInteger(o, "contribution");
      String hieroglyphic = o.get("hieroglyphic").toString();

      // add a new pharoah to array
      Pharaoh p = new Pharaoh(id, name, begin, end, contribution, hieroglyphic);

      pharaohHashMap.put(id, p);
      hieroglyphMap.put(hieroglyphic, id);
    }
    //System.out.println("PHAROAH MAP:");
    //pharaohHashMap.get(1).print();
  }

    // initialize the pyramid array
    private void initializePyramid(JSONArray pyramidJSONArray) {
      // create array and hash map
      numPyramids = pyramidJSONArray.size();

      // initalize the array
      for (int i = 0; i < numPyramids; i++) {
        // get the object
        JSONObject o = (JSONObject) pyramidJSONArray.get(i);
  
        // parse the json object
        Integer id = toInteger(o, "id");
        String name = o.get("name").toString();
        JSONArray contributorsJSONArray = (JSONArray) o.get("contributors");
        String[] contributors = new String[contributorsJSONArray.size()];
        for (int j = 0; j < contributorsJSONArray.size(); j++) {
          String c = contributorsJSONArray.get(j).toString();
          contributors[j] = c;
        }
  
        // add a new pyramid to array
        Pyramid p = new Pyramid(id, name, contributors);
        pyramidHashMap.put(id, p);
      }
    }

  // get a integer from a json object, and parse it
  private Integer toInteger(JSONObject o, String key) {
    String s = o.get(key).toString();
    Integer result = Integer.parseInt(s);
    return result;
  }

  // get first character from input
  private static Character menuGetCommand(Scanner scan) {
    Character command = '_';

    String rawInput = scan.nextLine();

    if (rawInput.length() > 0) {
      rawInput = rawInput.toLowerCase();
      command = rawInput.charAt(0);
    }

    return command;
  }

  // print all pharaohs
  private void printAllPharaoh() {
    for (int i = 0; i < numPharaohs; i++) {
      printMenuLine();
      pharaohHashMap.get(i).print();
      printMenuLine();
    }    
  }

  // given some pyramid "p", this function will print information about the pyramid
  // It prints out the pyramid name, its ID, all its contributors, and the total contribution 
  private void printPyramidInfo(Pyramid p) {
    int totalContribution = 0;
    System.out.printf("Pyramid %s\n", p.name);
    System.out.printf("\tid: %d\n", p.id);
    for (int j = 0; j < p.contributors.length; j++) {
      // Note that "contrib" is short for "contributer"
      String contribHieroglyph = p.contributors[j];
      Integer contribId = hieroglyphMap.get(contribHieroglyph);
      String contribName = pharaohHashMap.get(contribId).name;
      Integer contribAmount = pharaohHashMap.get(contribId).contribution;
      totalContribution += contribAmount;
      System.out.println("\tContributor " + (j+1) + ": " + (contribName) + " (" + (contribAmount) + " gold coins)");
      // Notice that we have to add +1 to j, because of zero-indexing
    }
    System.out.println("\tTotal Contribution: " + (totalContribution) + " gold coins");
  }
  private void printAllPyramid() {
    for (int i = 0; i < numPyramids; i++) {
      printMenuLine();
      printPyramidInfo(pyramidHashMap.get(i));
      printMenuLine();
    }    
  }

  // find and print a particular pharaoh
  private void findPharaoh(Scanner scan) {
    System.out.print("Please enter the ID of the pharaoh you'd like to search for: ");
    try {
      int input = Integer.parseInt(scan.nextLine());
      printMenuLine();
      pharaohHashMap.get(input).print();
    }
    catch (Exception e) {
      System.out.println("ERROR: ID does not exist or invalid input");
    }
  }

   // find and print a particular pyramid
   private void findPyramid(Scanner scan) {
    System.out.print("Please enter the ID of the pyramid you'd like to search for: ");
    boolean success = true;
    int input = 0;
    try {
      input = Integer.parseInt(scan.nextLine());
      printMenuLine();
      printPyramidInfo(pyramidHashMap.get(input));
    }
    catch (Exception e) {
      success = false;
      System.out.println("ERROR: ID does not exist or invalid input");
    }
    if (success) {
      requestedPyramids.add(input);
    }
  }

  void printRequestedPyramids() {
    System.out.println("Printing a sorted list of all requested pyramids...");
    // (We sort the list of pyramids by using the TreeSet data structure)
    
    if (requestedPyramids.isEmpty()) {
      System.out.println("ERROR: There is nothing to print out because no pyramids have been requested yet");
    }
    else {
      System.out.println("ID\t\tPyramid Name");
      System.out.println("--\t\t------------");

      for (Integer i: requestedPyramids) {
        System.out.println((i) + "\t\t" + pyramidHashMap.get(i).name);
        //printPyramidInfo(pyramidHashMap.get(i));
      } 
    }
  }

  private Boolean executeCommand(Scanner scan, Character command) {
    Boolean success = true;

    switch (command) {
      case '1':
        printAllPharaoh();
        break;
      case '2':
        findPharaoh(scan);
        break;
      case '3':
        printAllPyramid();
        break;
      case '4':
        findPyramid(scan);
        break;
      case '5':
        printRequestedPyramids();
        break;
      case 'q':
        System.out.println("Thank you for using Nassef's Egyptian Pyramid App!");
        break;
      default:
        System.out.println("ERROR: Unknown commmand");
        success = false;
    }

    return success;
  }

  private static void printMenuCommand(Character command, String desc) {
    System.out.printf("%s\t\t%s\n", command, desc);
  }

  private static void printMenuLine() {
    System.out.println(
      "--------------------------------------------------------------------------"
    );
  }

  // prints the menu
  public static void printMenu() {
    printMenuLine();
    System.out.println("Nassef's Egyptian Pyramids App");
    printMenuLine();
    System.out.printf("Command\t\tDescription\n");
    System.out.printf("-------\t\t---------------------------------------\n");
    printMenuCommand('1', "List all the pharoahs");
    printMenuCommand('2', "Displays a specific Egyptian Pharaoh");
    printMenuCommand('3', "List all the pyramids");
    printMenuCommand('4', "Displays a specific pyramid");
    printMenuCommand('5', "Displays a list of specific pyramids");
    printMenuCommand('q', "Quit");
    printMenuLine();
  }
}