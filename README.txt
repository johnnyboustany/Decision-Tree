FINAL HAND-IN

1. Design Choices:

    First of all, I handled edge cases in myID3Algorithm method with regard to no more examples,
    examples with the same classification and no more attributes. I used the mostFreqClassificationNode
    helper method for this, which effectively iterated through the 2D examples array of the data or parent data
    in order to find the classification that appears the most.

    Then, in the else statement, I found the attribute with the largest importance by calculating its
    Info Gain with the findInfoGain method. The findInfoGain method relies on finding the difference of the entropy of the
    examples and the remainder. Several helper methods are used to find the remainder, including findQ, findEntropy
    and findRemainder. A logBase2 helper method is used to implement a log function with a base of 2 and also hardcode in
    that log(0)=0. findQ and findEntropy both account for the case when the positive and negative counts sum up to 0.
    The findEntropy helper method adds the classifications from examples that correspond to each value
    of an attribute to an ArrayList. It then moves these classifications to a 2D array.
    The remainder is equal to the proportion value times the entropy of these classifications
    (this product is calculated for each value of the Attribute and then summed at the end).
    The positive and negative count values, which were incremented within the for loop, are used to find
    the proportion of (#p + #n)/(total #examples). Thee positive and negative counters are reset to 0 inside
    the for loop to make sure that they are reset for each value of the attribute.

    Once the attribute with the largest infoGain is chosen, it is made the tree root and removed from the
    new Attribute list that will be used the next time the algorithm runs. Then, a for loop iterates through
    the attribute values and uses an arraylist called rowIndices. It goes through 2D examples array and adds the row indices
    to the arraylist, that correspond to the current value of the attribute. Then, it creates a new 2D Array new_data
    that will hold examples from data in accordance with the row indices in the 2D Array rowIndices. Finally, a child
    is added to the tree root and the value and myID3Algorithm are passed in. The new_data 2D array, new Attribute list,
    and 1D array of classifications are all passed into myID3Algorithm. This implements the recursive aspect
    of the algorithm and builds the whole tree.

2. No known bugs.


3. Explanation of Test Cases:

    The JUnit tests test important edge cases such as the sum of positive and negative count being 0
    and log(0) being 0, among other important cases. It also compares the results of the helper methods
    to what is apparent from the demo.

    My trees all look the same as the ones in the demo and my accuracy percentages are similar for all of the data.
    When trained on full and tested on either subset, my trees are 100 percent accurate.