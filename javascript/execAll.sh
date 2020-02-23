#!/bin/bash
echo "A"
node index.js < ../data/a_example.txt > ./outputs/a_example.out
echo "B"
node index.js < ../data/b_read_on.txt > ./outputs/b_read_on.out
echo "C"
node index.js < ../data/c_incunabula.txt > ./outputs/c_incunabula.out
echo "D"
node index.js < ../data/d_tough_choices.txt > ./outputs/d_tough_choices.out
echo "E"
node index.js < ../data/e_so_many_books.txt > ./outputs/e_so_many_books.out
echo "F"
node index.js < ../data/f_libraries_of_the_world.txt > ./outputs/f_libraries_of_the_world.out


