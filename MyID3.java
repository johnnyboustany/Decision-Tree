package decisiontree;

import support.decisiontree.Attribute;
import support.decisiontree.DecisionTreeData;
import support.decisiontree.DecisionTreeNode;
import support.decisiontree.ID3;
import java.util.ArrayList;

/**
  * This class is where your ID3 algorithm should be implemented.
  */
public class MyID3 implements ID3 {
    private String _negative;
    private String _positive;

    /**
     * Constructor. You don't need to edit this.
     */
    public MyID3() {
        
    }

    /**
     * This is the trigger method that actually runs the algorithm.
     * This will be called by the visualizer when you click 'train'.
     */
    @Override
    public DecisionTreeNode id3Trigger(DecisionTreeData data) {
        _negative = data.getClassifications()[1];
        _positive = data.getClassifications()[0];

        return myID3Algorithm(data, data);
    }

    /**
     * This method implements the algorithm.
     */
    private DecisionTreeNode myID3Algorithm(DecisionTreeData data, DecisionTreeData parentData) {
        if(data.getExamples().length == 0){
            // when there are more rows of examples
            return mostFreqClassificationNode(parentData.getExamples());

        } else if(findEntropy(data.getExamples()) == 0){
            // when entropy is 0, then all examples have the same classification
            return mostFreqClassificationNode(data.getExamples()); // since all classifications are the same

        } else if(data.getAttributeList().isEmpty()){
            // when there are no more attributes in list
            return mostFreqClassificationNode(data.getExamples());

        } else {
            Attribute A = findAttributeWithLargestGain(data.getExamples(), data.getAttributeList());
            DecisionTreeNode treeRoot = new DecisionTreeNode();
            treeRoot.setElement(A.getName());
            ArrayList<Attribute> newAttributeList = data.getAttributeList();
            newAttributeList.remove(A);

            for(String val: A.getValues()){
                ArrayList<Integer> rowIndices = new ArrayList<>();

                // going through rows of examples
                for(int row = 0; row <= data.getExamples().length - 1; row++){

                    // adding the row indices to the arraylist, that correspond
                    // to the current value of the attribute
                    if(data.getExamples()[row][A.getColumn()].equals(val)){
                        rowIndices.add(row);
                    }
                }
                // new 2D Array new_data that will hold examples from data in accordance with the row indices in
                // the 2D Array rowIndices
                String[][] new_data = new String[rowIndices.size()][data.getExamples()[0].length];

                for(int j = 0; j <= rowIndices.size() -1; j++){
                    String[][] examples = data.getExamples();
                    new_data[j] = examples[rowIndices.get(j)];
                }

                // recursion occurs here, in which a new DecisionTreeData is created and a new 2D Array of examples,
                // ArrayList of the remaining attributes and a 1D array of the classifications.

                // data is passed in as the parentData that is ued in one of the edge cases above.
                treeRoot.addChild(val, myID3Algorithm(new DecisionTreeData(new_data, newAttributeList, data.getClassifications()), data));
            }
            return treeRoot;
        }
    }

    /**
     * This method find the classification that appears most frequently and returns
     * a new node with this classification.
     */
    public DecisionTreeNode mostFreqClassificationNode(String[][] examples){
        int negativeCount = 0;
        int positiveCount = 0;

        // iterating through every row of examples
        for (int row = 0; row <  examples.length; row++){

            // checking last column (classification column) for each example
            if(examples[row][examples[0].length - 1].equals(_positive)){
                positiveCount++;
            } else {
                negativeCount++;
            }
        }

        DecisionTreeNode node = new DecisionTreeNode();

        if(positiveCount > negativeCount){
            node.setElement(_positive);
        } else {
            node.setElement(_negative);
        }

        return node;
    }

    /**
     * This method find the most important attribute, with the largest gain.
     */
    public Attribute findAttributeWithLargestGain(String[][] examples, ArrayList<Attribute> attributeList){

        // initialize maxAttribute with the first attribute and maxInfoGain with its info gain
        Attribute maxAttribute = attributeList.get(0);
        double maxInfoGain = findInfoGain(examples, attributeList.get(0));


        for (Attribute a : attributeList) {

            double gain = findInfoGain(examples, a);

            if(gain > maxInfoGain){
                maxAttribute = a;
                maxInfoGain = findInfoGain(examples, a);
            }
        }

        return maxAttribute;
    }

    /**
     * This method is used to find the remainder, which is used to calculate Information Gain.
     */
    public double remainder(String[][] examples, Attribute attribute) {
        double remainder = 0;

        for (String val: attribute.getValues()) {

            //resetting the counters so that they start at 0 in the case of each value
            double negativeCount = 0;
            double positiveCount = 0;

            // this arraylist will hold the classifications of the indices that correspond to this value
            ArrayList<String> classificationsForVal = new ArrayList<>();

            // checking through all examples
            for(int row = 0; row <= examples.length-1; row++){

                // checking for the indices that have the current value
                if(examples[row][attribute.getColumn()].equals(val)){

                    // updating positive and negative counts accordingly
                    if(examples[row][examples[row].length - 1].equals(_positive)){
                        positiveCount++;
                    } else {
                        negativeCount++;
                    }

                    // adding the classifications of these indices to the array
                    classificationsForVal.add(examples[row][examples[row].length - 1]);
                }
            }

            // to ensure there is no 0 in the denominator
            if(positiveCount + negativeCount == 0){
                continue;
            }

            // moving all the classifications into a 2D array that will be used to find entropy
            String[][] classificationsToFindE = new String[classificationsForVal.size()][1];

            for (int i = 0; i <= classificationsForVal.size() -1; i++) {
                classificationsToFindE[i][0] = classificationsForVal.get(i);
            }

            double proportion = (positiveCount + negativeCount) / examples.length;
            remainder = remainder + (proportion*findEntropy(classificationsToFindE));
        }
        return remainder;
    }

    /**
     * This method is used to find the entropy, which is used to calculate the remainder.
     */
    public double findEntropy(String[][] examples){
        double q = findQ(examples);

        // no edge cases required as the logBase2 function already implements log(0)=0
        return -(q*logBase2(q)+ (1-q)*logBase2(1 - q));
    }

    /**
     * This method is used to find the variable q, which is used to calculate the entropy.
     */
    public double findQ(String[][] examples){
        double negativeCount = 0;
        double positiveCount = 0;

        for(int row = 0; row <= examples.length-1; row++) {

            // checking the classification of each example (row)
            if (examples[row][examples[0].length -1].equals(_positive)) {
                positiveCount++;
            } else {
                negativeCount++;
            }
        }

        // accounting for possible 0 in the denominator
        if((positiveCount + negativeCount) == 0){
            return 0;
        }

        return positiveCount / (positiveCount + negativeCount);
    }

    /**
     * This method is used to find the infoGain, which is used to choose the most important attribute.
     */
    public double findInfoGain(String[][] examples, Attribute A){

        return findEntropy(examples) - remainder(examples, A);
    }

    /**
     * This method is used to implement a log function with a base of 2.
     * It also makes sure that log(0) = 0.
     */
    public double logBase2(double x){

        // making sure that log(0) = 0
        if(x==0){
            return 0;
        }

        return Math.log(x)/Math.log(2);
    }
}