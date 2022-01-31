class Book{

  //Variable declaration

  private String title;
  private String author;
  private int index;
  private java.awt.Image previewImage;
  private java.awt.Image fullImage;


  //Getters

  public String getTitle(){
    return this.title;
  }
  public String getAuthor(){
    return this.author;
  }
  public String getDesc(){
    return this.title;
  }
  public String getGenre(){
    return this.title;
  }
  public int getIndex(){
    return this.index;
  }
  public java.awt.Image getPreviewImage(){
    return previewImage;
    }
  public java.awt.Image getFullImage(){
    return fullImage;
  } 

  //Setters

  public void setTitle(String titl){
    this.title=titl;
  }
  public void setAuthor(String auth){
    this.author=auth;
  }

  public void setIndex(int indx){
    this.index=indx;
  }
  public void setPreviewImage(java.awt.Image small){
    previewImage=small;
  }
  public void setFullImage(java.awt.Image big){
    fullImage=big;
  }
}