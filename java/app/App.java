package app;

import java.util.Scanner;
import java.util.Arrays;
import java.util.LinkedList;

class Library {
    public Integer[] books;
    public int id;
    public int signUpDays;
    public int booksPerDay;
    public int totalScore;
    public int totalScanningDays;
    public int scorePerDay;

    public LinkedList<Integer> selectedBooks = new LinkedList<>();

    public Library(int id, int signUpDays, int booksPerDay, Integer[] books) {
        this.books = books;
        this.id = id;
        this.signUpDays = signUpDays;
        this.booksPerDay = booksPerDay;

        // Calculate the total score
        Integer totalScore = 0;
        for (Integer b : books) {
            totalScore += App.bookScores[b];
        }
        this.totalScore = totalScore;

        // Sort books by score
        Arrays.sort(this.books, (Integer b1, Integer b2) -> {
            if (App.bookScores[b1] > App.bookScores[b2])
                return 1;
            if (App.bookScores[b1] < App.bookScores[b2])
                return -1;
            return 0;
        });

        this.totalScanningDays = (int) Math.ceil(this.books.length / this.booksPerDay);

        if (totalScanningDays == 0) {
            this.scorePerDay = 0;
        } else {
            this.scorePerDay = this.totalScore / this.totalScanningDays;
        }
    }

    // Basic: 8.730.481
    // Wilix Score (one single time): 9.153.817
    // Wilix Score (every step): 14.371.641
    // Wilix Score (every step with scanned books tracking): 14.623.844
    // After Wilix's illumination: 23.816.648
    // After fixing Peter's bug: 24.373.063
    // After fixing the other Peter's bug: 24.375.422
    // Our last hope : 24.383.316

    public int getWilixScore(int remainingDays, LinkedList<Integer> selectedBooks) {
        int score = 0;
        int skippedBooks = 0;
        for (int i = 0; i <= remainingDays * booksPerDay - signUpDays + skippedBooks; i++) {
            if (i >= this.books.length) {
                break;
            }
            int selectedBook = this.books[i];
            if (App.scannedBooks[selectedBook] == true) {
                skippedBooks += 1;
            } else {
                score += App.bookScores[selectedBook];
                selectedBooks.push(selectedBook);
            }
        }

        this.selectedBooks = selectedBooks;
        return score / this.signUpDays;
    }
}

public class App {
    private static Scanner scanner = new Scanner(System.in);
    public static int[] bookScores;
    public static boolean[] scannedBooks;

    static Library getMaxWilixScore(Library[] libraries, int remainingDays) {
        Library maxLib = null;
        int maxLibIndex = -1;
        int maxWilixScore = 0;
        LinkedList<Integer> maxSelectedBooks = new LinkedList<>();

        for (int i = 0; i < libraries.length; i++) {
            Library currentLib = libraries[i];
            if (currentLib != null) {
                LinkedList<Integer> selectedBooks = new LinkedList<>();
                int currentWilixScore = currentLib.getWilixScore(remainingDays, selectedBooks);
                if (currentWilixScore > maxWilixScore) {
                    maxLib = currentLib;
                    maxWilixScore = currentWilixScore;
                    maxSelectedBooks = selectedBooks;
                    maxLibIndex = i;
                }
            }
        }

        if (maxLibIndex >= 0) {
            libraries[maxLibIndex] = null;

            for (int b : maxSelectedBooks) {
                App.scannedBooks[b] = true;
            }
        }

        return maxLib;
    }

    public static void main(String[] args) throws Exception {
        int nBooks = scanner.nextInt();
        int nLibraries = scanner.nextInt();
        int days = scanner.nextInt();
        bookScores = new int[nBooks];
        scannedBooks = new boolean[nBooks];
        Library[] libraries = new Library[nLibraries];

        // Read data from input
        for (int i = 0; i < nBooks; i++) {
            bookScores[i] = scanner.nextInt();
        }

        for (int i = 0; i < nLibraries; i++) {
            int lNumBooks = scanner.nextInt();
            int lSignDays = scanner.nextInt();
            int lShipDays = scanner.nextInt();
            Integer[] lBooks = new Integer[lNumBooks];
            for (int j = 0; j < lNumBooks; j++) {
                lBooks[j] = scanner.nextInt();
            }
            libraries[i] = new Library(i, lSignDays, lShipDays, lBooks);
        }

        // Book scan process
        int remainingDays = days;
        int libScanned = 0;
        LinkedList<Library> scanned = new LinkedList<Library>();

        // Print solution
        for (int i = 0; remainingDays >= 0 && i < nLibraries; i++) {
            Library l = getMaxWilixScore(libraries, remainingDays);
            if (l == null)
                break;

            if (l.signUpDays > remainingDays) {
                continue;
            }

            libScanned++;

            remainingDays -= l.signUpDays;
            scanned.add(l);
            libraries[l.id] = null;
        }

        System.out.println(libScanned);
        for (Library l : scanned) {
            System.out.println(l.id + " " + l.selectedBooks.size());
            for (int j : l.selectedBooks) {
                System.out.print(j + " ");
            }
            System.out.println();
        }

        scanner.close();
    }
}
