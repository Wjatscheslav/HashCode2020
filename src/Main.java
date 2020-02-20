import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main
{

  public static String FILE_NAME = "a_example.txt";
  public static int booksCount;
  public static int librariesCount;
  public static int days;
  public List<Book> booksToScan = new ArrayList<>();
  //  public static String FILE_NAME = "b_read_on.txt";
//  public static String FILE_NAME = "c_incunabula.txt";
//  public static String FILE_NAME = "d_tough_choices.txt";
//  public static String FILE_NAME = "e_so_many_books.txt";
//  public static String FILE_NAME = "f_libraries_of_the_world.txt";

  public static void main(String[] args) throws IOException
  {
    Main main = new Main();
    List<Library> libraries = main.readLibraries(FILE_NAME);
    List<Library> processedLibraries = main.getProcessedLibraries(libraries);

  }

  public List<Library> getProcessedLibraries(List<Library> libraries)
  {
    int signupDays = 0;
    List<Library> processedLibraries = new ArrayList<>();
    for (int i = 0; i < libraries.size(); i++)
    {
      signupDays += libraries.get(i).signup;
      if (signupDays > days)
      {
        break;
      }
      libraries.get(i).books = getProcessedBooks(libraries.get(i), days - signupDays);
      processedLibraries.add(libraries.get(i));
    }

    return processedLibraries;
  }

  private void printProcessedLibrariesToFile(List<Library> libraries) throws IOException
  {
    Path out = Paths.get("result.out");
    if (!Files.exists(out))
      Files.createFile(out);
    try (BufferedWriter writer = Files
        .newBufferedWriter(out, StandardCharsets.US_ASCII, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING))
    {
      writer.write(String.format("%d", libraries.size()));
      for (Library library : libraries)
      {
        writer.write(String.format("\n%d %d", library.id, library.books.size()));
        StringBuilder sb = new StringBuilder();
        writer.write(String.format("\n%d %d", library.id, library.books.size()));
      }
    }
  }


  private List<Book> getProcessedBooks(Library library, int remainingDays)
  {
    int isAbleToScan = remainingDays * library.booksPerDay;
    return library.books.subList(0, isAbleToScan);
  }

  public List<Library> readLibraries(String fileName) throws IOException
  {
    List<Library> libraries = new ArrayList<>();
    try (Scanner scanner = new Scanner(Paths.get(fileName)))
    {
      Map<Integer, Book> booksById = new HashMap<>();

      booksCount = scanner.nextInt();
      librariesCount = scanner.nextInt();
      days = scanner.nextInt();
      for (int i = 0; i < booksCount; i++)
      {
        booksById.put(i, new Book(scanner.nextInt()));
      }

      for (int i = 0; i < librariesCount; i++)
      {
        Library library = new Library(i, scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        for (int j = 0; j < library.booksCount; j++)
        {
          int bookId = scanner.nextInt();
          library.books.add(booksById.get(bookId));
        }
        library.score = calculateScore(library);
        library.books.sort(Comparator.comparingInt(b -> b.score));
        libraries.add(library);
      }
      libraries.sort(Comparator.comparingDouble(l -> l.score));
      booksToScan.addAll(booksById.values());
      booksToScan.sort(Comparator.comparingInt(b -> b.score));
    }

    return libraries;
  }

  public int calculateScore(Library library)
  {
//    return (days - library.signup) / (library.booksPerDay * calculateTotalScore(library));
    return calculateTotalScore(library) / (library.signup + (library.booksCount / library.booksPerDay));
  }

  public int calculateTotalScore(Library library)
  {
    return library.books.stream()
        .mapToInt(book -> book.score)
        .sum();
  }

  public class Library
  {
    public Library(int id, int booksCount, int signup, int booksPerDay)
    {
      this.booksCount = booksCount;
      this.signup = signup;
      this.booksPerDay = booksPerDay;
    }

    int id;
    int booksCount;
    int signup;
    int booksPerDay;
    double score;
//    Map<Integer, Book> bookById = new HashMap<>();
    List<Book> books = new ArrayList<>();

    @Override
    public String toString()
    {
      return "Library{" +
          "booksCount=" + booksCount +
          ", signup=" + signup +
          ", booksPerDay=" + booksPerDay +
          ", score=" + score +
          ", books=" + books +
          '}';
    }
  }

  public class Book
  {

    public Book(int score)
    {
      this.score = score;
    }

    int score;

    @Override
    public String toString()
    {
      return "Book{" +
          "score=" + score +
          '}';
    }
  }

}
