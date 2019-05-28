import java.util.*;

public class AprioriTrie {

    private final TrieNode root;
    private ArrayList<String> results;
    private boolean didGenerateCandidates;


    AprioriTrie() {
        this.root = new TrieNode();
        this.results = new ArrayList<>(0);
        this.didGenerateCandidates = false;
    }


    void generateCandidates(TrieNode idNode, List<TrieNode> newItemset, int k, int depth, int minsup, List<SortedSet<Integer>> transactionDB) {
        List<TrieNode> children = idNode.getChildren();
        TrieNode child = idNode;
        //int count = 0;

        for (TrieNode aChild : children) {
            child = aChild;
            newItemset.add(child);

            if (depth == k - 1) {
                this.buildNewItemsets(child, children, newItemset, k);
            } else {
                this.generateCandidates(child, newItemset, k, depth + 1, minsup, transactionDB);
            }
            //int n;
//            if (minsup == 2 && count == (children.size()/4)) {
//                this.countSupportChild(this.root, transactionDB, k);
//                //System.out.println("about to prune node" + " " + child.getItemLabel());
//                n = this.pruneCandidatesChild(this.root, minsup);
//                //System.out.println("done pruning node children of" + " " + child.getItemLabel() + ". Pruned: " + n);
//                count = -1;
//            }
            //count++;
            //System.out.println(child.getItemLabel());
            //System.out.println("count: " + count);
            newItemset.remove(child);
        }

    }

    private void generateFirstCandidates(int[] ids) {
        List<TrieNode> children = root.getChildren();
        Iterator<TrieNode> it = children.iterator();
        TrieNode id = new TrieNode();
        TrieNode oldID;

        for (int id1 : ids) {

            while (it.hasNext() && id.getItemLabel() < id1) {
                id = it.next();
            }

            if (id.getItemLabel() == id1) {
                id.incrementNodeCounter();
                if (it.hasNext())
                    id = it.next();
            } else if (id.getItemLabel() > id1) {
                int index = children.indexOf(id);
                TrieNode child = new TrieNode(id1);
                child.incrementNodeCounter();
                this.root.addChildAtIndex(child, index);
                it = children.iterator();
                oldID = new TrieNode(id);
                while (it.hasNext() && id.getItemLabel() < oldID.getItemLabel()) {
                    id = it.next();
                }
                this.didGenerateCandidates = true;
            } else {
                TrieNode child = new TrieNode(id1);
                child.incrementNodeCounter();
                this.root.addChildNode(child);
                it = children.iterator();
                oldID = new TrieNode(id);
                while (it.hasNext() && id.getItemLabel() != oldID.getItemLabel()) {
                    id = it.next();
                }
                this.didGenerateCandidates = true;
            }
        }
    }

    private void buildNewItemsets(TrieNode idNode, List<TrieNode> siblings, List<TrieNode> current, int k) {
        Iterator<TrieNode> i = siblings.iterator();
        TrieNode parent = idNode;
        TrieNode sibling = new TrieNode();

        while (sibling.getItemLabel() < parent.getItemLabel() && i.hasNext()) {
            sibling = i.next();
        }

        while (i.hasNext()) {
            sibling = i.next();
            current.add(sibling);
            if (k <= 2 || this.isSubsetFrequent(current, this.root.getChildren(), k, 0, 1)) {
                parent.addChildNode(new TrieNode(sibling.getItemLabel()));
                this.didGenerateCandidates = true;
            }
            current.remove(sibling);
        }
    }

    void countSupport(List<SortedSet<Integer>> transactionDB, int k) {
        int[] ids;

        for (SortedSet<Integer> transaction: transactionDB) {
            ids = toPrimitiveArray(transaction);
            if (k == 1) {
                this.root.incrementNodeCounter();
                generateFirstCandidates(ids);
            } else {
                countSupport(root, ids, k, 0, 1);
            }
        }
    }

    void countSupportChild(TrieNode child ,List<SortedSet<Integer>> transactionDB, int k) {
        int[] ids;

        for (SortedSet<Integer> transaction: transactionDB) {
            ids = toPrimitiveArray(transaction);
            countSupport(child, ids, k, 0, 1);
        }
    }

    private boolean isSubsetFrequent(List<TrieNode> current, List<TrieNode> children, int k, int currentItemsetIndex, int depth) {
        boolean isSubset = true;
        TrieNode child;
        int index;
        int i = depth;

        if (children == null) return false;

        while (isSubset && (currentItemsetIndex <= i)) {
            index = children.indexOf(current.get(i));
            if (index >= 0) {
                if (depth < k-1) {
                    child = children.get(index);
                    isSubset = isSubsetFrequent(current, child.getChildren(), k, i+1, depth+1);
                }
            } else {
                isSubset = false;
            }
            i--;
        }

        return isSubset;
    }

    boolean willBeFrequent(List<TrieNode> current, List<SortedSet<Integer>> transactionDB) {
        int currentSupport = 0;
        for (SortedSet<Integer> transaction:transactionDB) {
            if (transaction.contains(current.get(0).getItemLabel()) && transaction.contains(current.get(1).getItemLabel())) {
                currentSupport++;
            }
            if (currentSupport == 2){
                //System.out.println(true);
                return true;
            }

        }
        return false;
    }

    private int[] toPrimitiveArray(SortedSet<Integer> t) {
        Integer[] toArray = t.toArray(new Integer[t.size()]);
        int[] primitives = new int[toArray.length];
        for(int i = 0; i < toArray.length; i++) {
            primitives[i] = toArray[i];
        }
        return primitives;
    }

    private void countSupport(TrieNode id, int[] ids, int k, int i, int depth) {
        List<TrieNode> children = id.getChildren();
        int temp;

        for (TrieNode child : children) {

            if (child.getItemLabel() > ids[ids.length-1]) {
                break;
            }

            if (i == ids.length) {
                break;
            }

            temp = i;
            while (temp < ids.length && ids[temp] < child.getItemLabel()){
                temp++;
            }

            if (temp < ids.length && child.getItemLabel() == ids[temp]) {
                if (depth == k) {
                    child.incrementNodeCounter();
                } else {
                    countSupport(child, ids, k, temp + 1, depth + 1);
                }
                i = temp + 1;
            }

        }
    }

    void pruneCandidates(TrieNode id, int minsup) {
        List<TrieNode> children = id.getChildren();
        TrieNode child = id;
        int count = 0;

        for (TrieNode trieNode : new ArrayList<>(children)) {
            child = trieNode;
            if (child.getSupport() < minsup) {
                children.remove(child);
                count++;
            } else {
                pruneCandidates(child, minsup);
            }
        }
    }

    int pruneCandidatesChild(TrieNode id, int minsup) {
        List<TrieNode> children = id.getChildren();
        TrieNode child = id;
        int count = 0;

        for (TrieNode trieNode : new ArrayList<>(children)) {
            child = trieNode;
            if (child.getSupport() < minsup) {
                children.remove(child);
                count++;
            } else {
                pruneCandidatesChild(child, minsup);
            }
        }
        return count;
    }

    TrieNode getRoot() {
        return root;
    }

    ArrayList<String> getResults() {
        return results;
    }

    void resetGenerationFlag() {
        this.didGenerateCandidates = false;
    }

    boolean isThereAnyCandidates() {
        return didGenerateCandidates;
    }


    void getResults(TrieNode id, String str, Map<Integer, String> reviewerIDMap, int depth, int inputK) {
        List<TrieNode> children = id.getChildren();

        for (TrieNode aChild : children) {
            id = aChild;

            if (depth >= inputK) {
                this.results.add(str + reviewerIDMap.get(id.getItemLabel()) + " (" + id.getSupport() + ")");
                //System.out.println(str + reviewerIDMap.get(id.getItemLabel()) + " (" + id.getSupport() + ")");
            }

            if (id.hasChildren()) {
                getResults(id, str + reviewerIDMap.get(id.getItemLabel()) + " ", reviewerIDMap, depth + 1, inputK);
            }
        }
    }

//    this will be for support = 2
//    void removeUnwantedBranches(TrieNode id, int inputK, int depth) {
//
//    }
}
