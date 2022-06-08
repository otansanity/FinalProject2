package com.diy.finalproj2;

public class BookList {
    String bookImage;
    String bookTitle ;
    String bookAuthor;
    String cellNumber;
    String bookPages;
    String manufacturedBy;
    String bookId;



    public BookList() {

    }

    public BookList(String bookImage,String bookId, String bookTitle, String bookAuthor, String cellNumber, String bookPages, String manufacturedBy) {
        this.bookImage = bookImage;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.cellNumber = cellNumber;
        this.bookPages = bookPages;
        this.manufacturedBy = manufacturedBy;
        this.bookId = bookId;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getBookPages() {
        return bookPages;
    }

    public void setBookPages(String bookPages) {
        this.bookPages = bookPages;
    }

    public String getManufacturedBy() {
        return manufacturedBy;
    }

    public void setManufacturedBy(String manufacturedBy) {
        this.manufacturedBy = manufacturedBy;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
