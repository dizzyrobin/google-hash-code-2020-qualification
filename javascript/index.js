const fs = require('fs');

let data = fs.readFileSync(0, { encoding: 'ascii' });
data = data.split('\n').map(e => e.split(' ').map(e2 => Number(e2)));
data.pop(); // Remove last empty line

const [nBooks, nLibs, nDays] = data.shift();
const bookScores = data.shift();
const scannedBooks = [];

let libs = [];
for (let id = 0; id < nLibs; id += 1) {
  const index = id * 2;
  const [_, signUpDays, booksPerDay] = data[index];
  const books = data[index + 1].sort((a, b) => bookScores[b] - bookScores[a]);
  const totalScore = books.reduce((prev, b) => prev + bookScores[b], 0);
  const totalScanningDays = Math.ceil(books.length / booksPerDay);

  let scorePerDay = 0;
  if (totalScanningDays !== 0) {
    scorePerDay = totalScore / totalScanningDays;
  }

  libs.push({
    id,
    signUpDays,
    booksPerDay,
    books,
    totalScore,
    totalScanningDays,
    scorePerDay,
  });
}

const getWilixScore = (lib, remainingDays) => {
  let score = 0;
  let skippedBooks = 0;
  const selectedBooks = [];
  for (let i = 0; i < remainingDays * lib.booksPerDay - lib.signUpDays + skippedBooks; i++) {
    if (i >= lib.books.length) {
      break;
    }

    const selectedBook = lib.books[i];
    if (scannedBooks[selectedBook] === true) {
      skippedBooks += 1;
    } else {
      score += bookScores[selectedBook];
      selectedBooks.push(selectedBook);
    }
  }

  lib.selectedBooks = selectedBooks;

  return [score / lib.signUpDays, selectedBooks];
};

const getMaxWilixScore = (libs, remainingDays) => {
  let maxLib = null;
  let maxLibIndex = -1;
  let maxWilixScore = 0;
  let maxSelectedBooks = [];

  for (let i = 0; i < libs.length; i += 1) {
    const currentLib = libs[i];
    if (currentLib != null) {
      const [currentWilixScore, selectedBooks] = getWilixScore(currentLib, remainingDays);
      if (currentWilixScore > maxWilixScore) {
        maxLib = currentLib;
        maxWilixScore = currentWilixScore;
        maxSelectedBooks = selectedBooks;
        maxLibIndex = i;
      }
    }
  }

  if (maxLibIndex >= 0) {
    libs[maxLibIndex] = null;
    maxSelectedBooks.forEach((b) => {
      scannedBooks[b] = true;
    });
  }

  return maxLib;
};


// Book scan process
let remainingDays = nDays;
let libScanned = 0;
const scanned = [];

// Print solution
for (let i = 0; remainingDays >= 0 && i < nLibs; i += 1) {
  const l = getMaxWilixScore(libs, remainingDays);
  if (l == null) {
    break;
  }

  if (Math.random() < 0.01) {
    libs = libs.filter((e) => e);
  }

  if (l.signUpDays > remainingDays) {
    libs = libs.filter((e) => e && e.signUpDays < remainingDays);
    continue;
  }

  libScanned++;

  remainingDays -= l.signUpDays;
  scanned.push(l);
  libs[l.id] = null;
}

console.log(libScanned);
scanned.forEach((l) => {
  console.log(`${l.id} ${l.selectedBooks.length}`);
  console.log(l.selectedBooks.join(' '));
});
