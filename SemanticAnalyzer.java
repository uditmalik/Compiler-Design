package A2;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

public class SemanticAnalyzer {
  
  public static final Hashtable<String, Vector<SymbolTableItem>> symbolTable = new Hashtable<String, Vector<SymbolTableItem>>();
  public static final Stack stack = new Stack();
  private static Gui gui;
  static int flag=0;
  //SemanticAnalyzer.gui=gui;
  // create here a data structure for the cube of types
  
  private static final int INT=0;
  private static final int FLOAT=1;
  private static final int CHAR=2;
  private static final int STRING=3;
  private static final int BOOLEAN=4;
  private static final int VOID=5;
  private static final int PLUS=3;
  private static final int BMINUS=0;
  private static final int MULT=1;
  private static final int DIV=2;
  private static final int LESS=4;
  private static final int GREAT=5;
  private static final int NOTEQUAL=6;
  private static final int EQUALEQUAL=7;
  private static final int AND=8;
  private static final int OR=9;
  private static final int EQUAL=10;
  private static final int UMINUS=0;
  private static final int UNOT=1;
  private static final int ERROR=6;

   private static final int[][][] $cube = {
       {
       //BINARY OPERATOR - 
       {INT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},
       {FLOAT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
       {
           //BINARY OPERATOR *
       {INT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},
       {FLOAT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
       {
           //BINARY OPERATOR /
       {INT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},
       {FLOAT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
       {
           //BINARY OPERATOR +
       {INT,FLOAT,ERROR,STRING,ERROR,ERROR,ERROR},
       {FLOAT,FLOAT,ERROR,STRING,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,STRING,ERROR,ERROR,ERROR},
       {STRING,STRING,STRING,STRING,STRING,ERROR,ERROR},
       {ERROR,ERROR,ERROR,STRING,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
       {
         //OPERATOR <  
       {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR},
       {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
       
       {
         //OPERATOR >  
       {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR},
       {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
       
       {
         //OPERATOR !=  
       {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR},
       {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,BOOLEAN,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
       
       {
         //OPERATOR ==  
       {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR},
       {BOOLEAN,BOOLEAN,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,BOOLEAN,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
       
       {
         //OPERATOR &  
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
          
       {
           //BINARY OPERATOR |
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       },
         
       {
           //OPERATOR =
       {INT,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR},
       {FLOAT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,CHAR,ERROR,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,STRING,ERROR,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,VOID,ERROR},
       {ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR}
       }
       
       
   };
   private static final int[][] $cube1 = {
       // UNARY - OPERATOR
       {INT,FLOAT,ERROR,ERROR,ERROR,ERROR,ERROR},
       // ! OPERATOR
       {ERROR,ERROR,ERROR,ERROR,BOOLEAN,ERROR,ERROR}
   };
  public static Hashtable<String, Vector<SymbolTableItem>> getSymbolTable() {
    return symbolTable;
  }
  
  public static void checkVariable(String type, String id) {
   
      
    // A. search the id in the symbol table
     // B. if !exist then insert: type, scope=global, value={0, false, "", '')
       // C. else error: “variable id is already defined”
   if(!symbolTable.containsKey(id))
   {
       
   
    Vector v = new Vector();
    v.add(new SymbolTableItem(type,"global", ""));
    symbolTable.put(id, v);
    
   }
   
   else
   {
       //System.out.println("variable arleady defined");
       flag=1;
   }
   
  }

  public static void pushStack(String type) {
  
    // push type in the stack
      
      
      stack.push(type);
  }
  
  public static String popStack() {
    String result;
    // pop a value from the stack
    result=(String) stack.pop();
    return result;
  }
  
  
  public static String calculateCube(String type, String operator) {
    String result;
    int result1=0;
    int val=0;
    // unary operator ( - and !)
    if(type.equals("int")) 
        val=0;
    if(type.equals("float")) 
        val=1;
    if(type.equals("char")) 
        val=2;
    if(type.equals("string"))
        val=3;
    if(type.equals("boolean"))
        val=4;
    if(type.equals("void")) 
        val=5;
   if (type.equals("error")) 
        val=6;
     if(operator.equals("-"))
    result1=$cube1[UMINUS][val];
     if(operator.equals("!"))
        result1=$cube1[UNOT][val];
     if(result1==0)
        result="int";
    else if(result1==1)
        result="float";
    else if(result1==2)
        result="char";
    else if(result1==3)
        result="string";
    else if(result1==4)
        result="boolean";
    else if(result1==5)
        result="void";
    else
        result="error";
    return result;
  }

  public static String calculateCube(String type1, String type2, String operator) {
    String result;
    int result1=0,val=0,val1=0;
    if(type1.equals("int")) 
        val=0;
    if(type1.equals("float")) 
        val=1;
    if(type1.equals("char")) 
        val=2;
    if(type1.equals("string"))
        val=3;
    if(type1.equals("boolean"))
        val=4;
    if(type1.equals("void")) 
        val=5;
     if(type1.equals("error")) 
        val=6;
    if(type2.equals("int")) 
        val1=0;
    if(type2.equals("float")) 
        val1=1;
    if(type2.equals("char")) 
        val1=2;
    if(type2.equals("string"))
        val1=3;
    if(type2.equals("boolean"))
        val1=4;
    if(type2.equals("void")) 
        val1=5; 
     if(type2.equals("error")) 
        val=6;
    // binary operator ( - and !)
    if(operator.equals("-"))
    result1=$cube[BMINUS][val][val1];
     else if(operator.equals("*"))
    result1= $cube[MULT][val][val1]; 
      else if(operator.equals("/"))
    result1= $cube[DIV][val][val1]; 
    else if(operator.equals("+"))
    result1= $cube[PLUS][val][val1];   
    else if(operator.equals(">")||operator.equals("<"))
    result1=$cube[LESS][val][val1];
    else if(operator.equals("!=")||operator.equals("=="))
    result1=$cube[NOTEQUAL][val][val1];
    else if(operator.equals("&")||operator.equals("|"))
    result1=$cube[AND][val][val1];
     else if(operator.equals("="))
    result1= $cube[EQUAL][val][val1];
    
    if(result1==0)
        result="int";
    else if(result1==1)
        result="float";
    else if(result1==2)
        result="char";
    else if(result1==3)
        result="string";
    else if(result1==4)
        result="boolean";
    else if(result1==5)
        result="void";
    else
        result="error";
    return result;
    }
  
  public static void error(Gui gui, int err, int n,String s) {
    switch (err) {
      case 1: 
        gui.writeConsole("Line" + n + ": variable <"+ s +"> is already defined"); 
        break;
      case 2: 
        gui.writeConsole("Line" + n + ": incompatible types: type mismatch"); 
        break;
      case 3: 
        gui.writeConsole("Line" + n + ": incompatible types: expected boolean"); 
        break;
      case 4: 
        gui.writeConsole("Line" + n + ": variable <"+ s +"> not found"); 
        break;
    }
  }
  
}
