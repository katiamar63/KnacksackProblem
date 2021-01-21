package knapsackproblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 0/1 Knapsack using Branch and Bound
 * Strategy Least Count
 *
 */
public class BranchAndBound {

    public static int size;
    public static float capacity;

    // Function to calculate upper bound (includes fractional part of the items) 
    static float upperBound(float tv, float tw,int idx, Item arr[]) {
        float value = tv;
        float weight = tw;
        for (int i = idx; i < size; i++) {
            if (weight + arr[i].getWeight()<= capacity) {
                weight += arr[i].getWeight();
                value -= arr[i].getCost();
            } else {
                value -= (float) (capacity - weight)
                        / arr[i].getWeight()
                        * arr[i].getCost();
                break;
            }
        }
        return value;
    }

    // Calculate lower bound (doesn't include fractional part of items) 
    static float lowerBound(float tv, float tw,
            int idx, Item arr[]) {
        float value = tv;
        float weight = tw;
        for (int i = idx; i < size; i++) {
            if (weight + arr[i].getWeight() <= capacity) {
                weight += arr[i].getWeight();
                value -= arr[i].getCost();
            } else {
                break;
            }
        }
        return value;
    }

    static void assign(BBNode a, float ub, float lb,
            int level, boolean isSelected,
            float tv, float tw) {
        a.upperBound = ub;
        a.lowerBound = lb;
        a.level = level;
        a.isSelected = isSelected;
        a.totalCost = tv;
        a.totalWeight = tw;
    }

    public static Item [] solve(Item arr[]) {
        // Sort the items based on the  profit/weight ratio 
        Arrays.sort(arr, new RatioComparator());

        BBNode current, left, right;
        current = new BBNode();
        left = new BBNode();
        right = new BBNode();

        // min_lb -> Minimum lower bound of all the nodes explored 
        // final_lb -> Minimum lower bound of all the paths that reached 
        // the final level 
        float minLB = 0, finalLB = Integer.MAX_VALUE;
        current.totalCost = current.totalWeight = current.upperBound
                = current.lowerBound = 0;
        current.level = 0;
        current.isSelected = false;

        // Priority queue to store elements based on lower bounds 
        PriorityQueue<BBNode> pq = new PriorityQueue<BBNode>(new LowerBoundComparator());

        // Insert a dummy node 
        pq.add(current);

        // curr_path -> Boolean array to store 
        // at every index if the element is 
        // included or not 
        // final_path -> Boolean array to store 
        // the result of selection array when 
        // it reached the last level 
        boolean currPath[] = new boolean[size];
        boolean finalPath[] = new boolean[size];

        while (!pq.isEmpty()) {
            current = pq.poll();
            if (current.upperBound > minLB
                    || current.upperBound >= finalLB) {
                // if the current node's best case 
                // value is not optimal than minLB, 
                // then there is no reason to 
                // explore that node. Including 
                // finalLB eliminates all those 
                // paths whose best values is equal 
                // to the finalLB 
                continue;
            }

            if (current.level != 0) {
                currPath[current.level - 1]
                        = current.isSelected;
            }

            if (current.level == size) {
                if (current.lowerBound < finalLB) {
                    // Reached last level 
                    for (int i = 0; i < size; i++) {
                        finalPath[arr[i].getIdx()]
                                = currPath[i];
                    }
                    finalLB = current.lowerBound;
                }
                continue;
            }

            int level = current.level;

            // right node -> Exludes current item 
            // Hence, cp, cw will obtain the value 
            // of that of parent 
            assign(right, upperBound(current.totalCost,
                    current.totalWeight,
                    level + 1, arr),
                    lowerBound(current.totalCost, current.totalWeight,
                            level + 1, arr),
                    level + 1, false,
                    current.totalCost, current.totalWeight);

            if (current.totalWeight + arr[current.level].getWeight() <= capacity) {

                // left node -> includes current item 
                // c and lb should be calculated 
                // including the current item. 
                left.upperBound = upperBound(
                        current.totalCost
                        - arr[level].getCost(),
                        current.totalWeight
                        + arr[level].getWeight(),
                        level + 1, arr);
                left.lowerBound = lowerBound(
                        current.totalCost
                        - arr[level].getCost(),
                        current.totalWeight
                        + arr[level].getWeight(),
                        level + 1,
                        arr);
                assign(left, left.upperBound, left.lowerBound,
                        level + 1, true,
                        current.totalCost - arr[level].getCost(),
                        current.totalWeight
                        + arr[level].getWeight());
            } // If the left node cannot 
            // be inserted 
            else {

                // Stop the left node from 
                // getting added to the 
                // priority queue 
                left.upperBound = left.lowerBound = 1;
            }

            // Update minLB 
            minLB = Math.min(minLB, left.lowerBound);
            minLB = Math.min(minLB, right.lowerBound);

            if (minLB >= left.upperBound) {
                pq.add(new BBNode(left));
            }
            if (minLB >= right.upperBound) {
                pq.add(new BBNode(right));
            }
        }
        ArrayList<Item> result = new ArrayList<>();     
        for (int i = 0; i < size; i++) {
            if (finalPath[i]) {
                Item el;
                final int z = i;
             //   System.out.print("1 ");
                el = Arrays.asList(arr)
                        .stream()
                        .filter(v -> v.getIdx()==z)
                        .findAny()
                        .orElse(null);
                if (el!=null)
                    result.add(el);
            } else {
               // System.out.print("0 ");
            }
        }
        //System.out.println("Maximum profit" + " is " + (-finalLB));
        return result.toArray(new Item[0]);
    }

}

/**
 * Comparator required by ProrityQueue
 * Standard Priority Queue is an extension of queue with following properties.
 * 1.Every item has a priority associated with it.
 * 2. An element with high priority is dequeued before an element with low priority.
 * 3. If two elements have the same priority, they are served according to their order in the queue.
 * This comparator takes into account accumulated weight to settle draws.
 * 
 * @author Andrzej Marciniak
 */
class LowerBoundComparator implements Comparator<BBNode> {

    @Override
    public int compare(BBNode a, BBNode b) {
        // if cost is the same, then choose less weighting
        if (a.lowerBound == b.lowerBound)
            return a.totalWeight<b.totalWeight?1:-1;
        return a.lowerBound > b.lowerBound ? 1 : -1;
    }
}

/**
 * Comparator used in sorting for better initial point. 
 */

class RatioComparator implements Comparator<Item> {

    @Override
    public int compare(Item a, Item b) {
        return (float) a.getCost() / a.getWeight()
                > (float) b.getCost() / b.getWeight() ? -1 : 1;
    }
}
