

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main
{

  public static int booksCount;
  public static int librariesCount;
  public static int days;
  public List<Book> booksToScan = new ArrayList<>();

  public static String FILE_NAME_A = "a_example";
  public static String FILE_NAME_B = "b_read_on";
  public static String FILE_NAME_C = "c_incunabula";
  public static String FILE_NAME_D = "d_tough_choices";
  public static String FILE_NAME_E = "e_so_many_books";
  public static String FILE_NAME_F = "f_libraries_of_the_world";

//  public static String FILE_NAME_OUT_A = "out_a_example.txt";
//  public static String FILE_NAME_OUT_B = "out_b_read_on.txt";
//  public static String FILE_NAME_OUT_C = "out_c_incunabula.txt";
//  public static String FILE_NAME_OUT_D = "out_d_tough_choices.txt";
//  public static String FILE_NAME_OUT_E = "out_e_so_many_books.txt";
//  public static String FILE_NAME_OUT_F = "out_f_libraries_of_the_world.txt";

  public static void main(String[] args) throws IOException
  {
    Main main = new Main();
    Arrays.asList(FILE_NAME_A, FILE_NAME_B, FILE_NAME_C, FILE_NAME_D, FILE_NAME_E, FILE_NAME_F)
        .forEach(file -> {
          List<Library> libraries = main.readLibraries(file);
          List<Library> processedLibraries = main.getProcessedLibraries(libraries);
          main.printProcessedLibrariesToFile(processedLibraries, file);
        });
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

  private void printProcessedLibrariesToFile(List<Library> libraries, String fileName)
  {
    Path out = Paths.get(fileName + ".out");
    try (BufferedWriter writer = Files
        .newBufferedWriter(out, StandardCharsets.US_ASCII, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING))
    {
      writer.write(String.format("%d", libraries.size()));
      for (Library library : libraries)
      {
        writer.write(String.format("\n%d %d", library.id, library.books.size()));
        String res = library.books.stream()
            .map(book -> book.id)
            .map(String::valueOf)
            .collect(Collectors.joining(" "));
        writer.write("\n" + res);
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }


  private List<Book> getProcessedBooks(Library library, int remainingDays)
  {
    int isAbleToScan = remainingDays * library.booksPerDay;
    if (isAbleToScan < 0)
    {
      isAbleToScan = Integer.MAX_VALUE;
    }
    if (isAbleToScan > library.books.size())
    {
      isAbleToScan = library.books.size();
    }

    return library.books.subList(0, isAbleToScan);
  }

  public List<Library> readLibraries(String fileName)
  {
    List<Library> libraries = new ArrayList<>();
    try (Scanner scanner = new Scanner(Paths.get(fileName + ".txt")))
    {
      Map<Integer, Book> booksById = new HashMap<>();

      booksCount = scanner.nextInt();
      librariesCount = scanner.nextInt();
      days = scanner.nextInt();
      for (int i = 0; i < booksCount; i++)
      {
        booksById.put(i, new Book(i, scanner.nextInt()));
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
        library.books.sort(Comparator.comparingInt(b -> ((Book) b).score).reversed());
        libraries.add(library);
      }
      libraries.sort(Comparator.comparingDouble(l -> ((Library) l).score).reversed());
      booksToScan.addAll(booksById.values());
      booksToScan.sort(Comparator.comparingInt(b -> ((Book) b).score).reversed());
    }
    catch (Exception ex)
    {}

    return libraries;
  }

  public int calculateScore(Library library)
  {
//    return (days - library.signup) / (library.booksPerDay * calculateTotalScore(library));

//    return calculateTotalScore(library) / (library.signup + (library.booksCount / library.booksPerDay));

    return (days- library.signup)/((library.booksCount/ library.booksPerDay)*(library.booksCount/calculateTotalScore(library)));
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
      this.id = id;
      this.booksCount = booksCount;
      this.signup = signup;
      this.booksPerDay = booksPerDay;
    }

    int id;
    int booksCount;
    int signup;
    int booksPerDay;
    double score;
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

    public Book(int id, int score)
    {
      this.id = id;
      this.score = score;
    }

    int id;
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
