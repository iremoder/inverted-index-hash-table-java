public class DocumentEntry{
    private String docName;
    private int frequency;

    public DocumentEntry(String docName){
        this.docName = docName;
        this.frequency = 1;
    }
    public void increaseFrequency(){
        this.frequency++;
    }
    public int getFrequency(){
        return frequency;
    }
    public String getDocName(){
        return docName;
    }
    @Override
    public String toString() {
        return frequency + "-" + docName;
    }
}
