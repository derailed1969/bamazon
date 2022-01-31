class User{
  private String nam, pass;
  // private int id;
  private int[] rating;
  public User(int booksAmount){
    rating=new int[booksAmount];
    pass="";//if the user doesn't have a password, their account is public by default
  }
  public int getRateSize(){
    return this.rating.length;
  }
  public int getRating(Integer indx){
    
    return this.rating[indx];
  }

  public void setRating (int index, int rate){
    this.rating[index]= rate;
  }
  public String getName(){
    return this.nam;
  }
  public void setName(String name){
    this.nam=name;
    return;
  }
  /*
  public int getID(){
    return this.id;
  }
  public void setID(int ID){
    this.id=ID;
    return;
  } 
  */

  public void setPass(String inp){
    this.pass = inp;
    return;
  }
  public String getPass(){
    return this.pass;
  }
}