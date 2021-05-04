package webcrawler;

/*****************************************************************
 //
 //  NAME:          Kana'i Gooding
 //
 //  HOMEWORK:      10
 //
 //  CLASS:         ICS 212
 //
 //  INSTRUCTOR:    Ravi Narayan
 //
 //  DATE:          May 4, 2021
 //
 //  FILE:          homework10.java
 //
 //  DESCRIPTION:   Generates a table of numbers from 0 to the maximum number given by the user.  The table has a column telling if each number is even or odd. The user interface will ask the user for the maximum number to print.
 //
 ****************************************************************/
public class homework10 {

  /*****************************************************************
   //
   //  Function name:   user_interface
   //
   //  DESCRIPTION:     Takes user input, checks if it is a positive int, calls print_table
   //
   //  Parameters:      n/a
   //
   //  Return values:   0 : when done
   //
   ****************************************************************/
  public static int user_interface() {

    int maxNum;
    int retVal;
    retVal = 0;

    System.out.println("The following program generates a table of numbers from 0 to the number you provide");
    System.out.print("Enter maximum number to show:  ");

    while ( retVal != -1 )
    {
      try
      {
        maxNum = Integer.parseInt(System.console().readLine());
        if ( maxNum <= 0 )
        {
          System.out.print("Please enter a POSITIVE INTEGER:  ");
        }
        else
        {
          print_table(maxNum);
          retVal = -1;
        }

      }
      catch (NumberFormatException e )
      {
        System.out.print("Please enter a Valid Integer:  ");
      }
    }
    return retVal;
  }

  /*****************************************************************
   //
   //  Function name:   print_table
   //
   //  DESCRIPTION:     Generates a table of numbers from 0 to num.
   //                   The table has a column telling if each number is even or odd.
   //
   //  Parameters:      num (int) : the maximum number given by the user.
   //
   //  Return values:   n/a void
   //
   ****************************************************************/
  public static void print_table( int num ) {

    int counter;
    int isEven;

    System.out.println("Number\tOdd/Even");

    for ( counter = 0; counter <= num; counter++ )
    {

      System.out.print(counter + "\t");

      isEven = is_even( counter );

      if( isEven == 1)
      {
        System.out.println("Even");
      }
      else
      {
        System.out.println("Odd");
      }
    }
  }

  /*****************************************************************
   //
   //  Function name:   is_even
   //
   //  DESCRIPTION:     Determines if a number is even or odd
   //
   //  Parameters:      num (int) : to be checked if even or odd
   //
   //  Return values:   0 : odd
   //                   1 : even
   //
   ****************************************************************/
  public static int is_even( int num ) {

    int isEven = 0;

    if ( num % 2 == 0 )
    {
      isEven = 1;
    }
    return isEven;
  }
}
