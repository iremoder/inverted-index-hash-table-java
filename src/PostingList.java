import java.util.ArrayList;

public class PostingList {
    private ArrayList<DocumentEntry> documents;

    public PostingList() {
        this.documents = new ArrayList<>();
    }

    public void addOrIncrement(String fileName) {
        if (fileName == null) return;
        for (DocumentEntry doc : documents) {
            if (doc.getDocName().equals(fileName)) {
                doc.increaseFrequency();
                return;
            }
        }
        documents.add(new DocumentEntry(fileName));
    }

    public ArrayList<DocumentEntry> getDocuments() {
        return documents;
    }

    @Override
    public String toString() {
        return documents.toString();
    }
}
