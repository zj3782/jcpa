package net.example.redundantstatement;

import java.util.Arrays;
import java.util.Collection;

/*
 * Wrong occurrences: 2
 */
public class ProbeSizeOfArrayOrCollectionBeforeForStatement {
    String[] sampleArr = { "A", "B", "C", "D", "E" };
    Collection<String> sampleCollection = Arrays.asList(sampleArr);

    public void correctIterateArray() {
        if (sampleArr != null) {
            for (String element : sampleArr) {
                assert (element != null);
            }
        }
        if (sampleCollection != null) {
            for (String element : sampleCollection) {
                assert (element != null);
            }
        }

    }

    public void wrongIterateArray() {
        if (sampleArr.length > 0) {
            for (String element : sampleArr) {
                assert (element != null);
            }
        }
        if (sampleCollection.size() > 0) {
            for (String element : sampleCollection) {
                assert (element != null);
            }
        }
    }
}
