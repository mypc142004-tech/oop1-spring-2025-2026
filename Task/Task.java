package Task;
 
interface BookOperations {
    void addQuantity(int amount);
    void sellQuantity(int amount);
}
abstract class Book {
    private String isbn;
    private String bookTitle;
    private String authorName;
    private double price;
    private int availableQuantity;
 
    public Book() {}
 
    public Book(String isbn, String bookTitle, String authorName, double price, int availableQuantity) {
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }
 
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
 
    public String getIsbn() {
        return isbn;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public String getAuthorName() {
        return authorName;
    }
    public double getPrice() {
        return price;
    }
    public int getAvailableQuantity() {
        return availableQuantity;
    }
 
    public abstract void showDetails();
}
 
 
class StoryBook extends Book implements BookOperations {
    private String category;
 
    public StoryBook() {}
 
    public StoryBook(String isbn, String bookTitle, String authorName, double price, int availableQuantity, String category) {
        super(isbn, bookTitle, authorName, price, availableQuantity);
        this.category = category;
    }
 
    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }
 
    public void addQuantity(int amount) {
        setAvailableQuantity(getAvailableQuantity() + amount);
    }
 
    public void sellQuantity(int amount) {
        if(getAvailableQuantity() >= amount) {
            setAvailableQuantity(getAvailableQuantity() - amount);
        }
    }
 
    public void showDetails() {
        System.out.println(getIsbn() + " " + getBookTitle() + " " + getAuthorName() + " " + getPrice() + " " + getAvailableQuantity() + " " + category);
    }
}
 
class TextBook extends Book implements BookOperations {
    private int standard;
 
    public TextBook() {}
 
    public TextBook(String isbn, String bookTitle, String authorName, double price, int availableQuantity, int standard) {
        super(isbn, bookTitle, authorName, price, availableQuantity);
        this.standard = standard;
    }
 
    public void setStandard(int standard) {
        this.standard = standard;
    }
    public int getStandard() {
        return standard;
    }
 
    public void addQuantity(int amount) {
        setAvailableQuantity(getAvailableQuantity() + amount);
    }
 
    public void sellQuantity(int amount) {
        if(getAvailableQuantity() >= amount) {
            setAvailableQuantity(getAvailableQuantity() - amount);
        }
    }
 
    public void showDetails() {
        System.out.println(getIsbn() + " " + getBookTitle() + " " + getAuthorName() + " " + getPrice() + " " + getAvailableQuantity() + " " + standard);
    }
}
 
interface BookShopOperations {
    boolean insertBook(Book b);
    boolean removeBook(Book b);
    void showAllBooks();
    Book searchBook(String isbn);
}
 
class BookShop implements BookShopOperations {
    private String name;
    private Book[] listOfBooks = new Book[100];
 
    public BookShop() {}
 
    public BookShop(String name) {
        this.name = name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
 
    public boolean insertBook(Book b) {
        for(int i = 0; i < listOfBooks.length; i++) {
            if(listOfBooks[i] == null) {
                listOfBooks[i] = b;
                return true;
            }
        }
        return false;
    }
 
    public boolean removeBook(Book b) {
        for(int i = 0; i < listOfBooks.length; i++) {
            if(listOfBooks[i] == b) {
                listOfBooks[i] = null;
                return true;
            }
        }
        return false;
    }
 
    public void showAllBooks() {
        for(Book b : listOfBooks) {
            if(b != null) {
                b.showDetails();
            }
        }
    }
 
    public Book searchBook(String isbn) {
        for(Book b : listOfBooks) {
            if(b != null && b.getIsbn().equals(isbn)) {
                return b;
            }
        }
        return null;
    }
}
 
public class Task {
    public static void main(String[] args) {
 
        BookShop shop = new BookShop("AIUB Book Shop");
 
        StoryBook s1 = new StoryBook("1", "Story1", "Author1", 100, 10, "Fiction");
        StoryBook s2 = new StoryBook("2", "Story2", "Author2", 120, 15, "Drama");
        StoryBook s3 = new StoryBook("3", "Story3", "Author3", 130, 20, "Mystery");
        StoryBook s4 = new StoryBook("4", "Story4", "Author4", 140, 25, "Horror");
        StoryBook s5 = new StoryBook("5", "Story5", "Author5", 150, 30, "Fantasy");
 
        TextBook t1 = new TextBook("6", "Text1", "Author6", 200, 10, 5);
        TextBook t2 = new TextBook("7", "Text2", "Author7", 220, 12, 6);
        TextBook t3 = new TextBook("8", "Text3", "Author8", 240, 14, 7);
        TextBook t4 = new TextBook("9", "Text4", "Author9", 260, 16, 8);
        TextBook t5 = new TextBook("10", "Text5", "Author10", 280, 18, 9);
 
        shop.insertBook(s1);
        shop.insertBook(s2);
        shop.insertBook(s3);
        shop.insertBook(s4);
        shop.insertBook(s5);
 
        shop.insertBook(t1);
        shop.insertBook(t2);
        shop.insertBook(t3);
        shop.insertBook(t4);
        shop.insertBook(t5);
 
        shop.showAllBooks();
 
        s1.addQuantity(5);
        s1.sellQuantity(3);
 
        t1.addQuantity(10);
        t1.sellQuantity(4);
 
        shop.removeBook(s5);
 
        Book found = shop.searchBook("3");
        if(found != null) {
            found.showDetails();
        }
    }
}
