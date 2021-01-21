package knapsackproblem;

/**
 *
 * @author Andrzej Marciniak
 */
public class Item {

    private float weight;
    private float cost;
    private int idx;
    private String label;

    public Item() {
    }

    public Item(float cost, float weight, int idx,String label) {
        this.cost = cost;
        this.weight = weight;
        this.idx = idx;
        this.label = label;
    }

    public float getWeight() {
        return weight;
    }

    public float getCost() {
        return cost;
    }

    public int getIdx() {
        return idx;
    }
    
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "Item "+label+" :{" + "weight=" + weight + ", cost=" + cost + "}" ;
    }

}
