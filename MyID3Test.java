package decisiontree;

import org.junit.Test;
import support.decisiontree.Attribute;
import support.decisiontree.DataReader;
import support.decisiontree.DecisionTreeData;
import support.decisiontree.DecisionTreeNode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * This class can be used to test the functionality of your MyID3 implementation.
 * Use the Heap stencil and your heap tests as a guide!
 * 
 */

public class MyID3Test {

	/**
	 * Tests if there are only 2 classifications.
	 */
	@Test
	public void simpleClassificationTest() {
	    
	    MyID3 id3 = new MyID3();

	    // This creates a DecisionTreeData object that you can use for testing.
	    DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		assertTrue(shortData.getClassifications().length ==2);
	}

	/**
	 * Tests whether the mostFreqClassificationNode helper method says that the most frequent classification is "true".
	 */
	@Test
	public void mostFreqClassificationNodeTest() {

		MyID3 id3 = new MyID3();

		// This creates a DecisionTreeData object that you can use for testing.
		DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		DecisionTreeNode node = new DecisionTreeNode();
		node.setElement(shortData.getClassifications()[0]);

		assertThat(id3.mostFreqClassificationNode(shortData.getExamples()).getElement(), is(node.getElement()));

	}

	/**
	 * Tests whether the findAttributeWithLargestGainTest helper method recognizes
	 * that Pat is the attribute with the largest gain, in accordance with the demo.
	 */
	@Test
	public void findAttributeWithLargestGainTest() {

		MyID3 id3 = new MyID3();

		// This creates a DecisionTreeData object that you can use for testing.
		DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		assertThat(id3.findAttributeWithLargestGain(shortData.getExamples(), shortData.getAttributeList()).getName(), is(" Pat"));

	}

	/**
	 * Tests whether the remainder method returns the correct remainder value for attribute Alt,
	 * as compared to manual calculations.
	 */
	@Test
	public void remainderTest() {
		MyID3 id3 = new MyID3();

		// This creates a DecisionTreeData object that you can use for testing.
		DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		assertThat(id3.remainder(shortData.getExamples(), shortData.getAttributeList().get(0)), is(0.9512050593046015));

	}

	/**
	 * Tests whether the findEntropy method returns the correct entropy value for the data,
	 * as compared to calculations.
	 */
	@Test
	public void findEntropyTest() {

		MyID3 id3 = new MyID3();

		// This creates a DecisionTreeData object that you can use for testing.
		DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		// the entropy of the examples should roughly be 1
		assertTrue(id3.findEntropy(shortData.getExamples()) >= 0.95);
	}

	/**
	 * Tests whether the findQ method returns the correct q value for the last column
	 * of the Examples 2D array.
	 */
	@Test
	public void findQTest() {

		MyID3 id3 = new MyID3();

		// This creates a DecisionTreeData object that you can use for testing.
		DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		double q = (5.0/(5.0+3.0)); // based on csv file classification column, q = p/(n+p)
		assertThat(id3.findQ(shortData.getExamples()), is(q));
	}

	/**
	 * Tests whether the findQ method returns the correct q value for a portion of the last column
	 * of the Examples 2D array.
	 */
	@Test
	public void moreSophisticatedFindQTest() {

		MyID3 id3 = new MyID3();

		// This creates a DecisionTreeData object that you can use for testing.
		DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		String[][] classifications = new String[5][1];

		for(int i = 0; i < 5; i++){
			classifications[i][0] = shortData.getExamples()[i][10];
		}

		double q = (3.0/(2.0+3.0)); // based on csv file classification column, q = p/(n+p)
		assertThat(id3.findQ(classifications), is(q));

	}

	/**
	 * Tests whether the findQ method returns the correct q value if the 2D Array
	 * it is given has no classifications. The 2D Array is chosen to be a random column
	 * with non-classification values from the examples 2D array.
	 *
	 * This is testing whether the method returns 0 if the sum p + n is equal to 0.
	 *
	 */
	@Test
	public void moreSophisticatedFindQTest2() {

		MyID3 id3 = new MyID3();

		// This creates a DecisionTreeData object that you can use for testing.
		DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		String[][] classifications = new String[5][1];

		for(int i = 0; i < 5; i++){
			classifications[i][0] = shortData.getExamples()[i][3];
		}

		assertThat(id3.findQ(classifications), is(0.0));

	}


	/**
	 * Tests the conclusions observed from the demo with regard to the relative
	 * importance of the attributes.
	 *
	 */
	@Test
	public void findInfoGainTest() {

		MyID3 id3 = new MyID3();

		// This creates a DecisionTreeData object that you can use for testing.
		DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		// Based on the demo, I know that Pat should be more important than Bar and that Rain should be more important
		// than Price.
		Attribute Pat = shortData.getAttributeList().get(4);
		Attribute Bar = shortData.getAttributeList().get(1);

		assertTrue(id3.findInfoGain(shortData.getExamples(), Pat) > id3.findInfoGain(shortData.getExamples(), Bar));

		Attribute Price = shortData.getAttributeList().get(5);
		Attribute Rain = shortData.getAttributeList().get(6);

		assertTrue(id3.findInfoGain(shortData.getExamples(), Rain) > id3.findInfoGain(shortData.getExamples(), Price));

	}

	/**
	 * Tests whether logBase2(0) returns 0.
	 *
	 */
	@Test
	public void logBase2Test() {

		MyID3 id3 = new MyID3();

		// This creates a DecisionTreeData object that you can use for testing.
		DecisionTreeData shortData = DataReader.readFile("src/decisiontree/decisiontree-data/short-data-training.csv");
		id3.id3Trigger(shortData);

		assertTrue(id3.logBase2(2) == 1);
		assertTrue(id3.logBase2(8) == 3);
		assertTrue(id3.logBase2(0) == 0);

	}
}