import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main
{

  public static String FILE_NAME = "a_example.txt";
//  public static String FILE_NAME = "b_read_on.txt";
//  public static String FILE_NAME = "c_incunabula.txt";
//  public static String FILE_NAME = "d_tough_choices.txt";
//  public static String FILE_NAME = "e_so_many_books.txt";
//  public static String FILE_NAME = "f_libraries_of_the_world.txt";

  public static void main(String[] args) throws IOException
  {
    Main main = new Main();
    List<Library> libraries = main.readLibraries(FILE_NAME);
    System.out.println(libraries);
  }

  public List<Library> readLibraries(String fileName) throws IOException
  {
    List<Library> libraries = new ArrayList<>();
    try (Scanner scanner = new Scanner(Paths.get(fileName)))
    {
      Map<Integer, Book> booksById = new HashMap<>();
      int booksCount = scanner.nextInt();
      int librariesCount = scanner.nextInt();
      int days = scanner.nextInt();

      for (int i = 0; i < booksCount; i++)
      {
        booksById.put(scanner.nextInt(), new Book(scanner.nextInt()));
      }

      for (int i = 0; i < librariesCount; i++)
      {
        Library library = new Library(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        for (int j = 0; j < library.booksCount; j++)
        {
          int bookId = scanner.nextInt();
          library.bookById.put(bookId, booksById.get(bookId));
        }
      }
    }

    return libraries;
  }


  public class Library
  {
    public Library(int booksCount, int signup, int booksPerDay)
    {
      this.booksCount = booksCount;
      this.signup = signup;
      this.booksPerDay = booksPerDay;
    }

    int booksCount;
    int signup;
    int booksPerDay;
    Map<Integer, Book> bookById = new HashMap<>();
  }

  public class Book
  {

    public Book(int score)
    {
      this.score = score;
    }

    int score;
  }

}
