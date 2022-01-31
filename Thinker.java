import java.util.*;
import java.io.*;
class Thinker {

  //Variable Declaration
  private int lengthUsers = 0;
    private int lengthBooks = 0;
    private ArrayList < User > users;
    private Book[] books;
    private String usersPath="files\\ratings55V3.txt";
    private String booksPath="files\\books55.txt";

    //This method loads the users from ratings55V3.txt
    public ArrayList < User > loadUsers() throws Exception {
        BufferedReader fr = new BufferedReader(new FileReader(usersPath));
        String ff = fr.readLine();
        ArrayList < String > userbaseList = new ArrayList < String > ();
        while (ff != null) {
            lengthUsers += 1;
            userbaseList.add(ff);
            ff = fr.readLine();
        }
        fr.close();
        ArrayList < User > users = new ArrayList < User > ();

        for (int i = 0; i < lengthUsers; i++) {
            String line = ((String) userbaseList.get(i));
            String[] tempArr = null;
            User a = new User(lengthBooks);
            if (line.charAt(0) == '') {
                //System.out.println("Password loaded");
                String[] parts = line.split("");
                tempArr = parts[2].split(" ");
                a.setPass(parts[1]);
            } else {
                tempArr = line.split(" ");
            }

            a.setName(tempArr[0]);
            //add ratings
            for (int j = 1; j <= lengthBooks; j++) {
                a.setRating(j - 1, Integer.parseInt(tempArr[j]));
            }
            users.add(a);
        }
        return users;
    }

    //Loads list of books from a file to an ArrayList of the Book class
    public Book[] loadBooks() throws Exception {
        BufferedReader fr2 = new BufferedReader(new FileReader(booksPath));
        String ff2 = fr2.readLine();
        ArrayList < String > bookbaseList = new ArrayList < String > ();
        //Finds length of file
        while (ff2 != null) {
            lengthBooks += 1;
            bookbaseList.add(ff2);
            ff2 = fr2.readLine();
        }
        fr2.close();
        Book[] books = new Book[lengthBooks];
        for (int i = 0; i < lengthBooks; i++) {
            String[] tempArr = (bookbaseList.get(i)).split(",");
            Book a = new Book();

            a.setAuthor(tempArr[0]);
            a.setTitle(tempArr[1]);
            a.setIndex(i);
            books[i] = a;
        }
        return books;
    }

    public void saveUsers() {
        try {
            BufferedWriter wf = new BufferedWriter(new FileWriter(usersPath));
            String uLine = "";
            for (int i = 0; i < users.size(); i++) {
                uLine = users.get(i).getName();
                if (users.get(i).getPass() != null) {
                    uLine = "" + users.get(i).getPass() + "" + uLine;
                }
                for (int j = 0; j < books.length; j++) {
                    uLine += " " + users.get(i).getRating(j);
                }
                wf.write(uLine);
                if (i < users.size() - 1) {
                    wf.newLine();
                }
            }
            wf.close();
        } catch (IOException e) {

        }
    }

    // simple sequential search
    public User searchUser(String nam) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equals(nam)) {
                return users.get(i);
            }
        }
        return null;
    }

    // gets dot product of all the books between 2 users
    public int getSimilarity(User u1, User u2) {
        int sim = 0;
        try{
            for (int i = 0; i < u1.getRateSize(); i++) {
                sim += (u1.getRating(i) * u2.getRating(i));
            }
        }
        catch (NullPointerException nPEOops){
            System.out.println("user(s) not found");
        }
        return sim;
    }


    // sequential search that returns an arraylist of book array indices. the indices depend on what books contain your search term
    public ArrayList < Integer > searchIndex(Book[] in , String req) {
        ArrayList < Integer > indxArr = new ArrayList < Integer > ();
        Book c;
        String b = " ";

        for (int i = 0; i < lengthBooks; i++) {
            c = in [i];
            try {
                b = c.getTitle();
            } catch (NullPointerException z) {}
            if (b.toLowerCase().contains(req.toLowerCase())) {
                try {
                    indxArr.add(c.getIndex());
                } catch (NullPointerException z) {}
            }
        }
        return indxArr;
    }

    // gets the most similar user from a given arraylist of users by comparing similarity scores and storing the user with the index of the highest one
    public User getMostSim(User u1, ArrayList < User > uArr) { 
        int tempSim = 0;
        for (int i = 0; i < uArr.size(); i++) {
            if (!u1.equals(uArr.get(i))) {
                if (getSimilarity(u1, uArr.get(i)) > getSimilarity(u1, uArr.get(tempSim))) {
                    tempSim = i;
                }
            }

        }

        return uArr.get(tempSim);
    }

    // returns the five most similar users by getting the most similar user, storing it in an array, removing it from the temporary array, and repeating 4 more times
    public User[] topFiveUsers(User u1, ArrayList < User > uArr) { 
        ArrayList < User > tempArr = uArr;
        User[] topFive = new User[5];
        for (int i = 0; i < 5; i++) {
            if (!tempArr.get(i).equals(u1)) {
                topFive[i] = getMostSim(u1, tempArr);
                tempArr.remove(tempArr.indexOf(topFive[i]));
                //System.out.println(topFive[i].getName() + " " + getSimilarity(u1, topFive[i])); 
            }

        }
        return topFive;
    }

    // returns an array of ten (unread) top rated books calculated from the weighted ratings of the top five most similar users
  public Book[] topTenBooks (User u1, ArrayList<User> uArr, Book[] books){ 
    ArrayList<Float> tempArr = new ArrayList<Float>();
    for (int i = 0; i < books.length; i++){
      tempArr.add((float) 0);
    }
    
    Book[] topTen = new Book[10];
    float temp = 0;
    float max = 0;  
    int tempPos = 0;

    for (int i = 0; i < tempArr.size(); i++){ // loops through all 5 users for all books
      for (int j = 0; j < 5; j++){
        try{
          temp += uArr.get(j).getRating(i) * adjustedWeight(uArr.get(j), uArr); // adds the weighted rating
        } catch (NullPointerException e){
        }
      }
      tempArr.set(i, temp); // stores weighted rating
      temp = 0;
    }
    
    // stores the top ten books in a similar way to topFiveUsers
    for (int k = 0; k < 10; k++){
      for (int i = 0; i < tempArr.size(); i++){
          try{
              if (tempArr.get(i) > max && u1.getRating(books[i].getIndex()) == 0){ // only counts unread books
                  max = tempArr.get(i);
                  tempPos = i;
              }
          }
          catch (NullPointerException nPEOops){
              System.out.println("books not found");
          }

      }
     
     
      topTen[k] = books[tempPos];
      // System.out.println(topTen[k].getTitle() + " " + max);
      tempArr.set(tempPos, (float) 0);
      max = 0;
      tempPos = 0;
    }

    return topTen;
  }

  // returns a decimal weight of the user in the top five user array; this will be used to adjust the weight of their book ratings
  public float adjustedWeight(User u1, ArrayList<User> uArr){
    for (int i = 0; i < uArr.size(); i++){
      if (u1==uArr.get(i)){
        return ((float) 1.0/(i+1));
      }
    }
    return (float) 0.0;
  }
  

  //Returns an array of books that have been sorted by their average score
  public Book[] sortBooks(Book[] books, ArrayList<User> users){
    //find the average scores
    float[] avgScore=new float[books.length];
    for(int i=0;i<books.length;i++){
      int ratingsAmount=0;
      for(int j=0;j<users.size();j++){
        if(users.get(j).getRating(i)!=0){
          ratingsAmount++;
          avgScore[i]+=users.get(j).getRating(i);
        }
      }
      avgScore[i]=avgScore[i]/ratingsAmount;
    }
    //sort the books
    Book[] sortedBooks=new Book[books.length];
    for(int i=0;i<books.length;i++){
      sortedBooks[i]=books[i];
    }
    for(int i=0;i<books.length-1;i++){
      for(int j=0;j<books.length-i-1;j++){
        if(avgScore[sortedBooks[j].getIndex()] <avgScore[sortedBooks[j+1].getIndex()]){
          Book temp=sortedBooks[j];
          sortedBooks[j]=sortedBooks[j+1];
          sortedBooks[j+1]=temp;
        }
      }
    }
    return sortedBooks;
  }

  public Book[] getBooks(){
      return books;
  }
  public ArrayList<User> getUsers(){
      return users;
  }
  public void setBooks(Book[] books){
    this.books=books;
  }public void setUsers(ArrayList<User> users){
    this.users=users;
  }
}