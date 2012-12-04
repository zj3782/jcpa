package net.example.redundantstatement;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/*
 * Wrong occurrences: 2
 */
public class ProbeSizeOfArrayOrCollectionBeforeForStatement {
    String[] sampleArr = { "A", "B", "C", "D", "E" };
    Collection<String> sampleCollection = Arrays.asList(sampleArr);

    public void correctTestifyLength() {
        String[] parts = "en_US".split("_");
        List<String> partsList = null;
        if (parts.length > 0) {
            partsList = Arrays.asList(parts);
        }
        assert (partsList != null);

    }

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
