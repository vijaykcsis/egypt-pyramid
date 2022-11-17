package com.pyramid;

import java.util.*;
import org.json.simple.*;

public class EgyptianPyramidsApp {


  // I've used two arrays here for O(1) reading of the pharaohs and pyramids.
  // other structures or additional structures can be used
  protected Pharaoh[] pharaohArray;
  protected Pharaoh[] pharaohArray2;
  protected Pyramid[] pyramidArray;
  static HashMap<Integer, Pharaoh> pharaohHashMap = new HashMap<Integer, Pharaoh>();
  static HashMap<String, Integer> hieroglyphMap = new HashMap<String, Integer>();
  // Notice that there is a separate HashMap for matching hieroglyphs to IDs
  // Having this second HashMap removes the need to iterate through a whole array to find the right hieroglyph

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
    pharaohArray = new Pharaoh[pharaohJSONArray.size()];
    
    // initalize the array
    for (int i = 0; i < pharaohJSONArray.size(); i++) {
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
      pharaohArray[i] = p;

      pharaohHashMap.put(id, p);
      hieroglyphMap.put(hieroglyphic, id);
    }
    //System.out.println("PHAROAH MAP:");
    //pharaohHashMap.get(1).print();
  }

    // initialize the pyramid array
    private void initializePyramid(JSONArray pyramidJSONArray) {
      // create array and hash map
      pyramidArray = new Pyramid[pyramidJSONArray.size()];
  
      // initalize the array
      for (int i = 0; i < pyramidJSONArray.size(); i++) {
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
        pyramidArray[i] = p;
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
    for (int i = 0; i < pharaohArray.length; i++) {
      printMenuLine();
      pharaohArray[i].print();
      printMenuLine();
    }    
  }

  private void printAllPyramid() {
    for (int i = 0; i < pyramidArray.length; i++) {
      printMenuLine();
      System.out.printf("Pyramid %s\n", pyramidArray[i].name);
      System.out.printf("\tid: %d\n", pyramidArray[i].id);
      for (int j = 0; j < pyramidArray[i].contributors.length; j++) {
        // pharaohHashMap.get(hieroglyphMap.get(pyramidArray[i].contributors[j]))
        //System.out.printf("\tContributor %i: %s\n", j, pyramidArray[i].contributors);
        System.out.println(hieroglyphMap.get(pyramidArray[i].contributors[j]));
      }
      printMenuLine();
    }    
  }

  // print a particular pharaoh
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
