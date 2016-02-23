package lexer;

import java.util.Vector;

public class Lexer {
 
  private String text;
  private Vector<Token> tokens; 
  private static final String[] KEYWORD = {"if", "else", "while", "switch", 
    "case", "return", "int", "float", "void", "char", "string", "boolean", 
    "true", "false", "print"};
 int flag=0;
  //Constants; YOU WILL NEED TO DEFINE MORE CONSTANTS
  private static final int ZERO      =  0;
  private static final int ONE       =  2;
  private static final int B         =  1;
  private static final int OTHER     =  18;
  private static final int DELIMITER =  19;
  private static final int ERROR     =  19;
  private static final int STOP      = -2;
  private static final int oct       =  3;
  private static final int en        =  4;
 private static final int gz         =  9;
 private static final int AF         =  6;
  private static final int X         =  5;
  private static final int doll      =  7;
  private static final int under     =  8;
  private static final int sing      =  10;
  private static final int doub      =  11;
  private static final int back      =  12;
  private static final int rtn       =  13;
  private static final int dot       =  14;
  private static final int e       =  15;
  private static final int plus       =  16;
  private static final int min       =  17;
  // states table; THIS IS THE TABLE FOR BINARY NUMBERS; YOU SHOLD COMPLETE IT
  private static final int[][] stateTable = { 
      {1,8,5,5,5,8,8,8,8,8,9,13,ERROR,8,17,8,ERROR,ERROR,ERROR,STOP},
      {4,2,4,4,ERROR,6,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,17,18,ERROR,ERROR,ERROR,STOP},
      {3,ERROR,3,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {3,ERROR,3,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {4,ERROR,4,4,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {5,ERROR,5,5,5,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,17,18,ERROR,ERROR,ERROR,STOP},
      {7,7,7,7,7,ERROR,7,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,7,ERROR,ERROR,ERROR,STOP},
      {7,7,7,7,7,ERROR,7,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,7,ERROR,ERROR,ERROR,STOP},
      {8,8,8,8,8,8,8,8,8,8,ERROR,ERROR,ERROR,8,ERROR,8,ERROR,ERROR,ERROR,STOP},
      {11,11,11,11,11,11,11,11,11,11,ERROR,11,10,11,11,11,11,11,11,STOP},
      {ERROR,11,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,11,ERROR,11,11,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,12,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {15,15,15,15,15,15,15,15,15,15,15,22,14,15,15,15,15,15,15,STOP},
      {ERROR,15,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,15,15,15,15,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {15,15,15,15,15,15,15,15,15,15,15,22,14,15,15,15,15,15,15,STOP},
      {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {20,ERROR,20,20,20,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,18,ERROR,ERROR,ERROR,STOP},
      {23,ERROR,23,23,23,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,21,21,ERROR,STOP},
      {23,ERROR,23,23,23,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {20,ERROR,20,20,20,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,18,ERROR,ERROR,ERROR,STOP},
      {23,ERROR,23,23,23,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {23,ERROR,23,23,23,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP},
      {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,STOP}
      
  };
  
  
  //constructor
  public Lexer(String text) {
    this.text = text;
  }

  //run
  public void run () {
    tokens = new Vector<Token>();
    String line;
    int counterOfLines= 1;
    // split lines
    do {
      int eolAt = text.indexOf(System.lineSeparator());
      if (eolAt >= 0) {
        line = text.substring(0,eolAt); 
        if (text.length()>0) text = text.substring(eolAt+1);  
      } else {
        line = text; 
        text = "";
      }
      splitLine (counterOfLines, line);
      counterOfLines++;
    } while ( !text.equals("") );   
  }
  
  //slit line
  private void splitLine(int row, String line) {
    int state = 0;
    int index = 0;
    char currentChar;
    String string="";
    if (line.equals("")) return; 
    //DFA working
    int go; 
    do {   
      currentChar = line.charAt(index);
      go = calculateNextState(state, currentChar);  
      if( go != STOP ) {
        string = string + currentChar;   
        state = go;
      }
      index++;        
    } while (index < line.length() && go != STOP);
    //review final state
    if (state == 3) {
      tokens.add(new Token(string, "BINARY", row));
    } 
  else if (state == 4) {
      tokens.add(new Token(string, "OCTAL", row));
    }
  else if (state == 5) {
      tokens.add(new Token(string, "INTEGER", row));
    } 
 
         else if (state == 7)
  {
     tokens.add(new Token(string, "HEXADECIMAL", row)); 
  }
      else if (state == 8) {
            
          
              for(int i=0;i<KEYWORD.length;i++)
          {
            
              if(string.contentEquals(KEYWORD[i]))
                  
              flag=1;
          }
              
           
          if(flag==1)
              
             tokens.add(new Token(string, "KEYWORD", row));
          else
              tokens.add(new Token(string, "IDENTIFIER", row));
              
      }
     
      else if (state == 12) {
      tokens.add(new Token(string, "CHARACTER", row));
      
    }
    else if (state == 22) {
      tokens.add(new Token(string, "STRING", row));
      
    }  
     else if ((state == 20)||(state ==17) ||(state==23)) {
        
      tokens.add(new Token(string, "FLOAT", row));
      
    }  
    else {
      if (!string.equals(""))
        tokens.add(new Token(string, "ERROR", row));
    }
    // current char
    if( isDelimiter(currentChar) )
       
      tokens.add(new Token(currentChar+"", "DELIMITER", row));
    else if (isOperator(currentChar) || isOperator1(currentChar) )
      tokens.add(new Token(currentChar+"", "OPERATOR", row));
    // loop
    if (index < line.length()) 
      splitLine(row, line.substring(index));
  }
  
  // calculate state
  private int calculateNextState(int state, char currentChar) {
    if ((isSpace(currentChar) && (state!=15) &&(state!=9) && (state!=13) && (state!=15) &&(state!=14))  || isDelimiter(currentChar) && (state!=9)&& (state!=13) && (state!=15) &&(state!=14)  ||  isDelimiter1(currentChar) && (state!=9)&& (state!=13)  &&(state!=14) ||
      isOperator(currentChar) && (state!=9) && (state!=13) && (state!=15) &&(state!=14) ||(isOperator1(currentChar)&&(state!=18 ) && (state!=13) && (state!=15) &&(state!=14) && (state!=9)) || isQuotationMark(currentChar) )
      return stateTable[state][DELIMITER];
    else if (currentChar == 'b') 
      return stateTable [state][B];
    else if (currentChar == '0')
      return stateTable [state][ZERO];    
    else if (currentChar == '1')
      return stateTable [state][ONE];
    else if (currentChar>=50 && currentChar<=55)
       return stateTable [state][oct];
    else if (currentChar>=56 && currentChar<=57)
        return stateTable[state][en];
    else if (currentChar=='x')
        return stateTable[state][X];
     else if ((currentChar>=65 && currentChar<=68)||(currentChar==70)|| (currentChar>=99 && currentChar<=100)|| (currentChar==102)  || (currentChar==97))
       return stateTable [state][AF]; 
     else if (currentChar==36)
      return stateTable[state][doll];
     else if (currentChar==95)
       return stateTable[state][under];
     else if (((currentChar>=71 && currentChar<=90))||((currentChar>=103) && (currentChar<=109))||((currentChar>=111) && (currentChar<=113))|| (currentChar>=117 && currentChar>=119) || (currentChar==115)||(currentChar>=121 && currentChar<=122) )
       return stateTable[state][gz];
     else if (currentChar==39)
         return stateTable[state][sing];
    else if (currentChar==34)
         return stateTable[state][doub];
    else if (currentChar==92)
         return stateTable[state][back];
    else if (currentChar=='n'||currentChar=='r'||currentChar=='t' ) 
        return stateTable[state][rtn];
    else if (currentChar==46)
         return stateTable[state][dot];
    else if (currentChar=='e' || currentChar=='E')
        return stateTable[state][e];
    else if (currentChar==43)
         return stateTable[state][plus];
    else if (currentChar==45)
         return stateTable[state][min];
    
         return stateTable [state][OTHER];
  }
 
  // isDelimiter
  private boolean isDelimiter(char c) {
     char [] delimiters = {':', ';', '}','{', '[',']','(',')',','};
     for (int x=0; x<delimiters.length; x++) {
      if (c == delimiters[x]) 
          
      return true;
                  }
     return false;
  }
  private boolean isDelimiter1(char c) 
  {
char [] delimiters={'\n'};
for (int x=0; x<delimiters.length; x++) {
      if (c == delimiters[x]) 
          
      return true;
                  }
     return false;
  }

  // isOperator
  private boolean isOperator(char o) {
     // == and != should be handled in splitLine
     char [] operators = {'*','/','<','>','=','!','&','|'};
     for (int x=0; x<operators.length; x++) {
      if (o == operators[x]) return true;      
     }
     return false;
  }
  private boolean isOperator1(char o) {
     // == and != should be handled in splitLine
     char [] operators = {'+', '-' };
     for (int x=0; x<operators.length; x++) {
      if (o == operators[x]) return true;      
     }
     return false;
  }
//float x1=(float)23e9+6;
  // isQuotationMark
  private boolean isQuotationMark(char o) {
     char [] quote = {};
     for (int x=0; x<quote.length; x++) {
      if (o == quote[x]) return true;      
     }
     return false;
  }

  // isSpace
  private boolean isSpace(char o) {
     return o == ' ';
  }
  
  // getTokens
  public Vector<Token> getTokens() {
    return tokens;
  }
  
}
