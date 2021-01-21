package knapsackproblem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Main class for Discrete Knapsack Problem.
 *
 * @author Andrzej Marciniak
 */
public class KnapsackProblem {

    /**
     * Extract items from string.
     * @param text - text to parse
     * @return array of items 
     * @throws java.lang.Exception 
     */
    public static Item[] parseItems(String text) throws Exception {
        ArrayList<Item> array = new ArrayList();
        int itemCounter = 0;
        String patternString = "\\((.*?)\\)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ++itemCounter;
            String[] sItems = matcher.group(1).split(",");
            if (sItems.length != 3) {
                throw new Exception("Data Format Error: Improper number of properties in item no: "+itemCounter);
            }
            if (sItems[2].charAt(0) != '\u20AC') 
                throw new Exception("Data Format Error: Missing euro currency in item no:" + itemCounter );              
            float c = Float.parseFloat(sItems[2].substring(1));
            float w = Float.parseFloat(sItems[1]);
            int idx = Integer.parseInt(sItems[0]);
            if (idx != itemCounter) {
                throw new Exception("Data Format Error: Missing item no: "+itemCounter);
            }
            array.add(new Item(c, w, array.size(),sItems[0]));
        }
        return array.toArray(new Item[0]);
    }

    public static float parseMaxWeight(String text){
        int divide = text.indexOf(" : ");
        return Float.parseFloat(text.substring(0, divide));
    }

    /**
     * @param args the command line arguments containing filepath to incurred
     * data
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
                int lineCounter = 0;
                String s;
                System.out.println("```");
                while ((s = in.readLine()) != null) {
                    try {
                        lineCounter++;
                        if(lineCounter%2==0){
                         if (!s.trim().isEmpty()) throw new Exception("Data Format Error: Missing empty line in line: "+lineCounter);
                         continue;
                        }
                        Item [] items = Arrays.stream(parseItems(s))
                                .filter(x -> x.getCost() <=100 && x.getWeight()<=100)
                                .toArray(Item[]::new);
                        BranchAndBound.size=items.length<15?items.length:15;
                        BranchAndBound.capacity=parseMaxWeight(s)<100?parseMaxWeight(s):100;
                        Item [] res = BranchAndBound.solve(items);                        
                        
                        if (res.length==0)
                            System.out.println("-"); 
                        System.out.println(Arrays.stream(res)
                                .map(el->el.getLabel())
                                .collect(Collectors.joining(",")));
                       
                    }
                    catch (NumberFormatException ex) {
                        System.err.println("Data Format Error: Wrong number format in line: "+lineCounter);
                    }
                    catch (Exception ex) {
                        System.err.println(ex.getMessage());
                    } 
                }
                System.out.println("```");
            } catch (IOException e) {
                System.err.println("Cannot open the file at a given location: " + args[0]);
            } 

        } else {
            System.err.println("Improper number of arguments:" + args.length);
        }
    }

}
