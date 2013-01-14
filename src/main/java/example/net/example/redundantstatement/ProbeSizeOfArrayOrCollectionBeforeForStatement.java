package net.example.redundantstatement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/*
 * Wrong occurrences: 2
 */
public class ProbeSizeOfArrayOrCollectionBeforeForStatement {

	String[] globalArray = { "A", "B", "C", "D", "E" };
	Collection<String> globalCollection = Arrays.asList(globalArray);

    public void correctTestifyLength() {
        String[] parts = "en_US".split("_");
        List<String> partsList = null;
        if (parts.length > 0) {
            partsList = Arrays.asList(parts);
        }
        assert (partsList != null);

    }
    
    public void correctUsage(){
    	List<String> services=new LinkedList<String>();
    	if (services.size() > 0) {
    	       List<String> temp = new ArrayList<String>();
    	       for (String elem : services) {
    	                  temp.add(elem);
    	       }
    	}
    }
    
    public void correctIterateArray() {
		String[] sampleArr = { "A", "B", "C", "D", "E" };
        if (sampleArr != null) {
            for (String element : sampleArr) {
                assert (element != null);
            }
        }
		
		Collection<String> sampleCollection = Arrays.asList(sampleArr);
        if (sampleCollection != null) {
            for (String element : sampleCollection) {
                assert (element != null);
            }
        }
        if (sampleCollection.size() > 0) {
        	List<String> temp = new ArrayList<String>();
            for (String element : sampleCollection) {
            	temp.add(element);
                assert (element != null);
            }
        }

    }

    public void wrongIterateArray() {
		String[] sampleArr = { "A", "B", "C", "D", "E" };
        if (sampleArr.length > 0) {
            for (String element : sampleArr) {
                assert (element != null);
            }
        }
		
		Collection<String> sampleCollection = Arrays.asList(sampleArr);
        if (sampleCollection.size() > 0) {
            for (String element : sampleCollection) {
                assert (element != null);
            }
        }
    }
	
	public void correctIterateArray2() {
		if (globalArray.length > 0) {
            for (String element : globalArray) {
                assert (element != null);
            }
        }
		
		if (globalCollection.size() > 0) {
            for (String element : globalCollection) {
                assert (element != null);
            }
        }
    }
	
}
