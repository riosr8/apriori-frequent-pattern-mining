import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class DataFileProcessor {

    private StringTokenizer lineTokens;
    private String line;
    private Scanner inputStream;
    private PrintWriter outputStream;
    private String idString;
    private Integer idNumber;

    DataFileProcessor() {
        lineTokens = null;
        line = null;
        idString = null;
        idNumber = 1;
    }

    DataFileProcessor(String filePath) {
        lineTokens = null;
        line = null;
        idString = null;
        idNumber = 1;
    }

    Apriori firstScan(String fileIn) {

        Apriori a = new Apriori();
        Map<Integer, String> reviewerIDMap = a.getReviewerIDMap();
        Map<String, Integer> stringIDs = new HashMap<>();
        List<SortedSet<Integer>> transactionDB = a.getTransactionDB();
        openInputFile(fileIn);

        while (inputStream.hasNextLine()) {
            line = inputStream.nextLine();
            lineTokens = new StringTokenizer(line);
            SortedSet<Integer> transaction = new TreeSet<>();
            while (lineTokens.hasMoreTokens()) {
                idString = lineTokens.nextToken();
                if (!stringIDs.containsKey(idString)) {
                    reviewerIDMap.put(idNumber, idString);
                    transaction.add(idNumber);
                    stringIDs.put(idString, idNumber);
                    idNumber++;
                } else {
                    transaction.add(stringIDs.get(idString));
                }
            }
            transactionDB.add(transaction);
        }
        closeInputFile();
        return a;
    }

    void outputResults(String fileOut, AprioriTrie trie) {
        ArrayList<String> results = trie.getResults();
        openOutputFile(fileOut);

        for (String result : results) {
            outputStream.println(result);
        }

        closeOutputFile();
    }

    void openInputFile(String fileIn) {
        try {
            inputStream = new Scanner(new FileInputStream(fileIn));
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    void openOutputFile(String fileOut) {
        try {
            outputStream = new PrintWriter(new FileOutputStream(fileOut));
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    void closeInputFile() {
        inputStream.close();
    }

    void closeOutputFile() {
        outputStream.close();
    }

    Scanner getInputStream() {
        return inputStream;
    }

    PrintWriter getOutputStream() {
        return outputStream;
    }

}
