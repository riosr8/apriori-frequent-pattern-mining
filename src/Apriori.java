import java.util.*;

public class Apriori {

    private Map<Integer, String> reviewerIDMap;
    private List<SortedSet<Integer>> transactionDB;

    public Apriori() {
        reviewerIDMap = new HashMap<>();
        transactionDB = new ArrayList<>();
    }

    public static void main(String[] args) {

        // long startTime = System.nanoTime();
        Apriori a;
        DataFileProcessor dp = new DataFileProcessor();
        int minsup;
        int inputK;
        String fileIn, fileOut;
        Scanner input = new Scanner(System.in);

        if (args.length != 0) {
            minsup = Integer.parseInt(args[0]);
            inputK = Integer.parseInt(args[1]);
            fileIn = args[2];
            fileOut = args[3];
        } else {
            System.out.print("Enter minsup value: ");
            minsup = input.nextInt();
            input.nextLine();
            System.out.print("Enter k value: ");
            inputK = input.nextInt();
            input.nextLine();
            System.out.print("Enter input file path: ");
            fileIn = input.nextLine();
            System.out.print("Enter output file path: ");
            fileOut = input.nextLine();
        }

        // place holder values
        // fileIn = "proj_1/transactionDB.txt";
        // fileOut = "outputTest.txt";
        AprioriTrie trie = new AprioriTrie();
        a = dp.firstScan(fileIn);
        a.init(minsup, inputK, trie);
        dp.outputResults(fileOut, trie);
        System.out.println("Done");
        // long estimatedTime = System.nanoTime() - startTime;
        // System.out.println(estimatedTime);
    }

    private void init(int minsup, int inputK, AprioriTrie trie) {
        System.out.println("here");

        boolean done = false;
        int k = 0;

        while (!done) {
            k++;
            System.out.println("k value: " + k);

            trie.generateCandidates(trie.getRoot(), new ArrayList<>(), k, 1, minsup, transactionDB);
            // if (minsup != 2 || k == 1){ //work on this later
            // trie.countSupport(transactionDB, k);
            // trie.pruneCandidates(trie.getRoot(), minsup);
            // }
            trie.countSupport(transactionDB, k);
            trie.pruneCandidates(trie.getRoot(), minsup);

            if (!trie.isThereAnyCandidates()) {
                done = true;
            }
            // System.out.println(k);
            // if (k >= 2 && minsup == 2) {
            // trie.removeUnwantedBranches(trie.getRoot(), inputK,1);
            // }

            trie.resetGenerationFlag();
            System.out.println("database size before: " + transactionDB.size());
            updateDatabase(k);
            System.out.println("database size after: " + transactionDB.size());
        }
        trie.getResults(trie.getRoot(), "", this.reviewerIDMap, 1, inputK);

    }

    private void updateDatabase(int k) {
        transactionDB.removeIf(transaction -> transaction.size() <= k);
    }

    public Map<Integer, String> getReviewerIDMap() {
        return reviewerIDMap;
    }

    public List<SortedSet<Integer>> getTransactionDB() {
        return transactionDB;
    }

}
