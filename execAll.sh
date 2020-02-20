#!/bin/bash
echo "A"
java -cp bin app.App < data/a_example.txt > outputs/a_example.out
echo "B"
java -cp bin app.App < data/b_read_on.txt > outputs/b_read_on.out
echo "C"
java -cp bin app.App < data/c_incunabula.txt > outputs/c_incunabula.out
echo "D"
java -cp bin app.App < data/d_tough_choices.txt > outputs/d_tough_choices.out
echo "E"
java -cp bin app.App < data/e_so_many_books.txt > outputs/e_so_many_books.out
echo "F"
java -cp bin app.App < data/f_libraries_of_the_world.txt > outputs/f_libraries_of_the_world.out


