package A4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author javiergs
 */
public class Parser {

  private static DefaultMutableTreeNode root;
  private static Vector<Token> tokens;
  private static int currentToken;
  private static Gui gui;
  static int count1=0,a1=0,a2=0,a3=0,a4=0,a5=0,a6=0,a7=0,a8=0,a9=0,a10=0,a11=0,a12=0,counter,i,j,k,h,b,p,q,i1=0;
   static boolean flag=false,a=false;
  static String w="",o="";
  public static DefaultMutableTreeNode run(Vector<Token> t, Gui gui) 
  {
    A4.SemanticAnalyzer.symbolTable.clear();
      i=0;counter=1;
     j=0;
     k=0;
      h=0;
     b=0;
     p=0;
     q=0;
   o="h"+q;
      
    Parser.gui=gui;
      tokens = t;
    currentToken = 0;
    root = new DefaultMutableTreeNode("program");
    CodeGenerator.clear(gui);
    rule_program(root);
   gui.writeSymbolTable(SemanticAnalyzer.getSymbolTable());
    CodeGenerator.writeCode(gui);
    return root;
  }
  
  
   public static boolean rule_program(DefaultMutableTreeNode parent)
  {
     // System.out.println("inside program");
      boolean error=false;
      if(currentToken<tokens.size() && tokens.get(currentToken).getWord().equals("{"))
      {
        DefaultMutableTreeNode node=new DefaultMutableTreeNode("{");  
      currentToken++;
      
      parent.add(node);
      DefaultMutableTreeNode node1=new DefaultMutableTreeNode("body");
      parent.add(node1);
      
      error=RULE_BODY(node1);
      //System.out.println(currentToken+" < "+tokens.size());
      }
          
      else
      {
         // System.err.println("inside error1");
          error(1);
        
        while(currentToken < tokens.size()&&!tokens.get(currentToken).getWord().equals("print")
                &&!tokens.get(currentToken).getToken().equals("IDENTIFIER")
                &&!tokens.get(currentToken).getWord().equals("int")&&!tokens.get(currentToken).getWord().equals("float")&&!tokens.get(currentToken).getWord().equals("boolean")&&!tokens.get(currentToken).getWord().equals("char")&&!tokens.get(currentToken).getWord().equals("string")&&!tokens.get(currentToken).getWord().equals("void")
                &&!tokens.get(currentToken).getWord().equals("while")
                &&!tokens.get(currentToken).getWord().equals("if")
                &&!tokens.get(currentToken).getWord().equals("return")
                &&!tokens.get(currentToken).getWord().equals("}")
                )       
                {
                    currentToken++;
                }
         
       DefaultMutableTreeNode node;
       node=new DefaultMutableTreeNode("body");
       parent.add(node);
       error=RULE_BODY(node);
       
    } 
      if(currentToken<tokens.size() && tokens.get(currentToken).getWord().equals("}"))
          
      {
      DefaultMutableTreeNode node=new DefaultMutableTreeNode("}");
      parent.add(node);
      currentToken++;
      }
      else
      {
         //System.out.println("inside error2");
         
          error(2);
          
      }
      if(b==0)
      {
     CodeGenerator.addInstruction("OPR", "0", "0");
     
      }
      return error;
  }
  
  public static boolean RULE_BODY(DefaultMutableTreeNode parent) 
{
	DefaultMutableTreeNode node;
        boolean error=false;
       
       // System.out.println("inside body");
     while (currentToken < tokens.size() && !(tokens.get(currentToken).getWord().equals("}"))) 
	{ 
	 //System.out.println(count1);	
            if (currentToken < tokens.size() &&tokens.get(currentToken).getToken().equals("IDENTIFIER")) 
			{
			   
                            node = new DefaultMutableTreeNode("assingment");
                         parent.add(node);
                         node=new DefaultMutableTreeNode(tokens.get(currentToken).getWord());
                         parent.add(node);
                         String a=tokens.get(currentToken).getWord();
      if(A4.SemanticAnalyzer.symbolTable.containsKey(a))
      {
      Vector<SymbolTableItem> v= SemanticAnalyzer.symbolTable.get(a);
      
      SymbolTableItem st=v.firstElement();
      String str=st.getType();
      SemanticAnalyzer.pushStack(str);
      }
      else
      {
       A4.SemanticAnalyzer.error(gui,4,tokens.get(currentToken).getLine(),a);
       SemanticAnalyzer.pushStack("error");
      }                   
      currentToken++;
                         
       error=RULE_ASSIGNMENT(node);
                          
                            
	if (currentToken<tokens.size() && tokens.get(currentToken).getWord().equals(";")) 
        {   
            node = new DefaultMutableTreeNode(";");
          parent.add(node);
        currentToken++;
       
        }
                        
      else 
        {
          //  System.out.println("inside else-");
            error(3);
            
         }
                    
                    
       }
	else if (tokens.get(currentToken).getWord().equals("int") || tokens.get(currentToken).getWord().equals("float")||tokens.get(currentToken).getWord().equals("boolean")||tokens.get(currentToken).getWord().equals("char")||tokens.get(currentToken).getWord().equals("string")||tokens.get(currentToken).getWord().equals("void") ) 
	{
	     
            String s=tokens.get(currentToken).getWord();
            node = new DefaultMutableTreeNode("variable " + s);
                         parent.add(node);
                         currentToken++;
                       //  node=new DefaultMutableTreeNode(s);
            error=RULE_VARIABLE(node);
	    if (currentToken<tokens.size() && tokens.get(currentToken).getWord().equals(";")) 
            {
                node = new DefaultMutableTreeNode(";");
               parent.add(node);
	       currentToken++;
            }
		else 
           {
		            
               error(3);
                   
           }
        }
        else if (currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("{"))
        {
            currentToken++;
        }
	else if (currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("while")) 
	{
	
        node = new DefaultMutableTreeNode("while");
        parent.add(node);
        currentToken++;
        error=true;
        error=RULE_WHILE(node);
	} 
        else if (currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("if")) 
	{
	 node = new DefaultMutableTreeNode("if");
         parent.add(node);
         currentToken++;
         error=RULE_IF(node);
	} 
	else if (currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("return")) 
	{
	  node = new DefaultMutableTreeNode("return");
          parent.add(node);
          currentToken++;
          error=RULE_RETURN();
          if (currentToken<tokens.size() &&tokens.get(currentToken).getWord().equals(";")) 
          {
	     node = new DefaultMutableTreeNode(";");
          parent.add(node);
              currentToken++;
          }
	else
		error(3);
			
	}
        else if (currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("print")) 
	{
	   node = new DefaultMutableTreeNode("print");
           parent.add(node);
           currentToken++;
           //error=true;
           error=RULE_PRINT(node);
	if (currentToken<tokens.size() &&tokens.get(currentToken).getWord().equals(";")) 
        {		
            node = new DefaultMutableTreeNode(";");
          parent.add(node);
            currentToken++;
        }
        else
        {
		
            error(3);
            
        }
	}
        
        else 
        {
            
           // System.out.println("inside error4");
            
            if(currentToken<tokens.size() &&tokens.get(currentToken).getWord().equals(";") )
            {
                node = new DefaultMutableTreeNode(";");
          parent.add(node);
                currentToken++;
            }
            else if(currentToken<tokens.size())
             {
             error(4);
                 currentToken++;
             }
            // if(!(count1>1)&& (count2==1))           
             

        }
	}
     
     return error;
}
 public static boolean RULE_ASSIGNMENT(DefaultMutableTreeNode parent)
      {
         
           
          String x,y,c1,q,result;
          DefaultMutableTreeNode node;
         boolean error=false;
          try
            {
          if (currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("="))
          {
              q=tokens.get(currentToken-1).getWord();
              currentToken++;
              node = new DefaultMutableTreeNode("=");
              parent.add(node);
              error=true;
              error = rule_expression(node);
              CodeGenerator.addInstruction("STO",q , "0");
              counter++;
              x=SemanticAnalyzer.popStack();
            //System.out.println("type of x " +x);
              y=SemanticAnalyzer.popStack();
            // System.out.println("type of y " +y);
              
              result=SemanticAnalyzer.calculateCube(x, y, "=");
           //  System.out.println("result " +result);
              if(result.equals("error"))
              {
                  
                  A4.SemanticAnalyzer.error(gui,2,tokens.get(currentToken).getLine(),x);
              }
             
              
             
          }
          else
          {              
              
              while(currentToken<tokens.size()
                      &&!tokens.get(currentToken).getToken().equals("INTEGER")
                      &&!tokens.get(currentToken).getWord().equals("!")
                      &&!tokens.get(currentToken).getWord().equals("-")
                      &&!tokens.get(currentToken).getToken().equals("OCTAL")
                      &&!tokens.get(currentToken).getToken().equals("HEXADECIMAL")
                      &&!tokens.get(currentToken).getToken().equals("FLOAT")
                      &&!tokens.get(currentToken).getToken().equals("CHAR")
                      &&!tokens.get(currentToken).getToken().equals("STRING")
                      &&!tokens.get(currentToken).getWord().equals("TRUE")
                      &&!tokens.get(currentToken).getWord().equals("FALSE")
                      &&!tokens.get(currentToken).getToken().equals("IDENTIFIER")
                      &&!tokens.get(currentToken).getWord().equals("BINARY")
                      &&!tokens.get(currentToken).getWord().equals("(")
                      &&!tokens.get(currentToken).getWord().equals(")")
                      &&!tokens.get(currentToken).getWord().equals(";")
                    )
              {
                  currentToken++;
              }
               error(5);
               //System.out.println(tokens.get(currentToken).getWord());
               node=new DefaultMutableTreeNode("");
               parent.add(node);
               error=rule_expression(node);
          }
            }
            catch(Exception e)
            {
            }
          return error;
      }
      
       public static boolean RULE_VARIABLE(DefaultMutableTreeNode parent)
      {
          boolean error=false;
          if(currentToken < tokens.size() &&tokens.get(currentToken).getToken().equals("IDENTIFIER"))
          {  
              String s=tokens.get(currentToken).getWord();
               CodeGenerator.addVariable(tokens.get(currentToken-1).getWord(),tokens.get(currentToken).getWord()); 
               //counter++;
              A4.SemanticAnalyzer.checkVariable(tokens.get(currentToken-1).getWord(),tokens.get(currentToken).getWord());
             if(A4.SemanticAnalyzer.flag==1)
               A4.SemanticAnalyzer.error(gui,1,tokens.get(currentToken).getLine(),tokens.get(currentToken).getWord());
              A4.SemanticAnalyzer.flag=0;
             
             
              currentToken++;
              DefaultMutableTreeNode node;
              node = new DefaultMutableTreeNode("identifier"+ "("+ s +")" );
              parent.add(node);
            
              
            
          }
          else
              error(6);
          return error;
                      
      }
       
        public static boolean RULE_WHILE(DefaultMutableTreeNode parent)
      {
        
         DefaultMutableTreeNode node;
         String x1,m="",n="";
         
      b++;
        
          boolean error=false;
          if(currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("("))
          {
                    
               currentToken++;
               node = new DefaultMutableTreeNode("(");
               parent.add(node);
               m="d"+i;
              // System.out.println(m);
               CodeGenerator.addLabel(m, counter);
               
               rule_expression(node);
               n="e"+j;
               j++;
               CodeGenerator.addInstruction("JMC", "#"+n, "false");
               
               counter++;
               
               
               x1=SemanticAnalyzer.popStack();
               if(!x1.equals("boolean"))
                  SemanticAnalyzer.error(gui, 3, tokens.get(currentToken).getLine(),x1);
          }
          else
          {
              
               error(8);
               while(currentToken < tokens.size()
                      &&!tokens.get(currentToken).getToken().equals("INTEGER")
                      &&!tokens.get(currentToken).getWord().equals("!")
                      &&!tokens.get(currentToken).getWord().equals("-")
                      &&!tokens.get(currentToken).getToken().equals("OCTAL")
                      &&!tokens.get(currentToken).getToken().equals("HEXADECIMAL")
                      &&!tokens.get(currentToken).getToken().equals("FLOAT")
                      &&!tokens.get(currentToken).getToken().equals("CHAR")
                      &&!tokens.get(currentToken).getToken().equals("STRING")
                      &&!tokens.get(currentToken).getWord().equals("TRUE")
                      &&!tokens.get(currentToken).getWord().equals("FALSE")
                      &&!tokens.get(currentToken).getToken().equals("IDENTIFIER")
                      &&!tokens.get(currentToken).getWord().equals("BINARY")
                      &&!tokens.get(currentToken).getWord().equals("(")
                      &&!tokens.get(currentToken).getWord().equals(")"))      
                       {
                           currentToken++;
                       }
              
               node=new DefaultMutableTreeNode(" ");
               parent.add(node);
               error=rule_expression(node);
          }
          if(currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals(")"))
          {
             
               currentToken++;
               node = new DefaultMutableTreeNode(")");
               parent.add(node);
              
                      i++; 
                     error=rule_program(node);
                      
                      
                      CodeGenerator.addInstruction("JMP", "#"+m, "0");
                                
                        counter++;
              
          }
          else
          {
              error(7);
              while(currentToken < tokens.size()&&!tokens.get(currentToken).getWord().equals("{"))
                     
              {
                  currentToken++;
              }
              node=new DefaultMutableTreeNode("");
              parent.add(node);
              node=new DefaultMutableTreeNode("program");
              parent.add(node);
              error=rule_program(node);
              
          }
         
           CodeGenerator.addLabel(n, counter);
           b--;
           return error;
      }
        
   
   public static boolean RULE_IF(DefaultMutableTreeNode parent)
      {
           DefaultMutableTreeNode node;
           String z,n="",m="";
           boolean error=false,flag1=false,flag2=false;
           
          if(currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("("))
          {
              b++;
              currentToken++;
              node = new DefaultMutableTreeNode("(");
              parent.add(node);
             
       
              rule_expression(node);
              
              n="a"+k;
              m="b"+h;
              h++;
              CodeGenerator.addInstruction("JMC", "#"+n, "false");
              
               counter++;
               z=SemanticAnalyzer.popStack();
              
              if(!z.equals("boolean"))
                  SemanticAnalyzer.error(gui, 3, tokens.get(currentToken).getLine(),z);
          }
          else
          {
              error(8);
               while(currentToken < tokens.size()
                      &&!tokens.get(currentToken).getToken().equals("INTEGER")
                      &&!tokens.get(currentToken).getWord().equals("!")
                      &&!tokens.get(currentToken).getWord().equals("-")
                      &&!tokens.get(currentToken).getToken().equals("OCTAL")
                      &&!tokens.get(currentToken).getToken().equals("HEXADECIMAL")
                      &&!tokens.get(currentToken).getToken().equals("FLOAT")
                      &&!tokens.get(currentToken).getToken().equals("CHAR")
                      &&!tokens.get(currentToken).getToken().equals("STRING")
                      &&!tokens.get(currentToken).getWord().equals("TRUE")
                      &&!tokens.get(currentToken).getWord().equals("FALSE")
                      &&!tokens.get(currentToken).getToken().equals("IDENTIFIER")
                      &&!tokens.get(currentToken).getWord().equals("BINARY")
                      &&!tokens.get(currentToken).getWord().equals("(")
                      &&!tokens.get(currentToken).getWord().equals(")"))      
                       {
                           currentToken++;
                       }
               node=new DefaultMutableTreeNode(" ");
               parent.add(node);
               error=rule_expression(node);
               
          }
           if(currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals(")"))
          {
              currentToken++;
               node = new DefaultMutableTreeNode(")");
              parent.add(node);
             
               k++;
               
              error=rule_program(node);
              flag2=true;
             
              CodeGenerator.addInstruction("JMP", "#"+m, "0");
              counter++;
              
           }
          else
          {
              error(7);
              while(currentToken < tokens.size()&&!tokens.get(currentToken).getWord().equals("{"))
                     
              {
                  currentToken++;
              }
              node=new DefaultMutableTreeNode("");
              parent.add(node);
              node=new DefaultMutableTreeNode("program");
              parent.add(node);
              rule_program(node);
              
          }
            
            
           
            if(currentToken==tokens.size())
                currentToken--;
                   
           if(currentToken<tokens.size() &&tokens.get(currentToken).getWord().equals("else"))
           {
              flag1=true;
              currentToken++;
              node = new DefaultMutableTreeNode("else");
              parent.add(node);
               
              CodeGenerator.addLabel(n, counter);
              k++;
              
              rule_program(node);
              if(!flag2==true)
              {
                  
              CodeGenerator.addInstruction("JMP", "#"+m, "0");
              
              
              counter++;
              }
           }
      if(flag1==false)
      {
           CodeGenerator.addLabel(n, counter);
           
      }
      CodeGenerator.addLabel(m, counter);
     b--;
       return error;
      }
               
      
                        
    public static boolean RULE_RETURN()
      {
          boolean error=false;
          
            CodeGenerator.addInstruction("OPR","1" , "0");
            counter++;
              return error;
      }
          
     public static boolean RULE_PRINT(DefaultMutableTreeNode parent)
      {
          DefaultMutableTreeNode node;
         boolean error=false;
         
          if(currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals("("))
          {
              currentToken++;
              node = new DefaultMutableTreeNode("(");
              parent.add(node);
              error= rule_expression(node);
              
          }
          else
          {
              error(8);
              while(currentToken < tokens.size()
                      &&!tokens.get(currentToken).getToken().equals("INTEGER")
                      &&!tokens.get(currentToken).getWord().equals("!")
                      &&!tokens.get(currentToken).getWord().equals("-")
                      &&!tokens.get(currentToken).getToken().equals("OCTAL")
                      &&!tokens.get(currentToken).getToken().equals("HEXADECIMAL")
                      &&!tokens.get(currentToken).getToken().equals("FLOAT")
                      &&!tokens.get(currentToken).getToken().equals("CHAR")
                      &&!tokens.get(currentToken).getToken().equals("STRING")
                      &&!tokens.get(currentToken).getWord().equals("TRUE")
                      &&!tokens.get(currentToken).getWord().equals("FALSE")
                      &&!tokens.get(currentToken).getToken().equals("IDENTIFIER")
                      &&!tokens.get(currentToken).getWord().equals("BINARY")
                      &&!tokens.get(currentToken).getWord().equals("(")
                      &&!tokens.get(currentToken).getWord().equals(")"))      
                       {
                           currentToken++;
                       }
              node=new DefaultMutableTreeNode(" ");
               parent.add(node);
               error=rule_expression(node);
              
          }
          if(currentToken < tokens.size() &&tokens.get(currentToken).getWord().equals(")"))
          {
              currentToken++;
              node = new DefaultMutableTreeNode(")");
              parent.add(node);
          }
          else
              error(7);
          CodeGenerator.addInstruction("OPR","21" , "0");
          counter++;
          return error;
      }
           
  private static boolean rule_expression(DefaultMutableTreeNode parent)
        {
      //  System.out.println("in expression");
            boolean error;
            
            String x,y,result,op = null;
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("X");
        parent.add(node);
        error = RULE_X(node);
       // count1++;
        while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("|"))
             {
                 if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("|")) 
                 {
                     
                     op=tokens.get(currentToken).getWord();
                    currentToken++;
                     node = new DefaultMutableTreeNode("|");
                    parent.add(node);
                    
                    node = new DefaultMutableTreeNode("X");
                    parent.add(node);
                   a1++;
                    error = RULE_X(node);
                    CodeGenerator.addInstruction("OPR", "8", "0");
                     counter++;
                       if(a1==1)
                 {
                     
               x=SemanticAnalyzer.popStack(); 
                     
               y=SemanticAnalyzer.popStack();
               result=SemanticAnalyzer.calculateCube(x,y,"|");
              
                 SemanticAnalyzer.pushStack(result);
              a1=0;
                 }
                 
                    
             }
                 
  }
       return error;
 }
  
  private static boolean RULE_X(DefaultMutableTreeNode parent)
  {
     // System.out.println("in x");
      String x,y,op1 = null,result;
      boolean error;
      DefaultMutableTreeNode node = new DefaultMutableTreeNode("Y");
    parent.add(node);
    error = RULE_Y(node);
     while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("&"))
             {
                 if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("&")) 
                 {
                      op1=tokens.get(currentToken).getWord();
                    node = new DefaultMutableTreeNode("&");
                    parent.add(node);
                    currentToken++;
                    node = new DefaultMutableTreeNode("Y");
                    parent.add(node);
                       a2++;
                    error = RULE_Y(node);
                     CodeGenerator.addInstruction("OPR", "9", "0");
                     counter++;
                    if(a2==1)
                 {
                    
               x=SemanticAnalyzer.popStack(); 
                     
               y=SemanticAnalyzer.popStack();
               result=SemanticAnalyzer.calculateCube(x,y,"&");
             
                 SemanticAnalyzer.pushStack(result);
                a2=0;
                 }
                
             }
               
  }
       return error;
 }
  
   private static boolean RULE_Y(DefaultMutableTreeNode parent) 
  {
   // System.out.println("in y");
      boolean error=false;
      String x,c= null,result;
    DefaultMutableTreeNode node;
    if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("!")) 
    {
       c=tokens.get(currentToken).getWord();
        node = new DefaultMutableTreeNode("!");
      parent.add(node);
      a3++;
      
      currentToken++;
    }
    
    node = new DefaultMutableTreeNode("R");
    parent.add(node);
    error = RULE_R(node);
    
    if(a3==1)
           {
     CodeGenerator.addInstruction("OPR", "10", "0");
              counter++;
                x=A4.SemanticAnalyzer.popStack(); 
            
                 result=A4.SemanticAnalyzer.calculateCube(x,"!");
               
                
                 SemanticAnalyzer.pushStack(result);
              a3=0;    
           }
     
    return error;
  }
   
   private static boolean RULE_R(DefaultMutableTreeNode parent)
   {
    //  System.out.println("in r");
       String x,y,c = null,result;
       boolean error;
      DefaultMutableTreeNode node = new DefaultMutableTreeNode("E");
    parent.add(node);
    error = RULE_E(node); 
    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals(">") || currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("<")||currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("==")||currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("!=")) {
      if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals(">")) 
      {
           c=tokens.get(currentToken).getWord();
        node = new DefaultMutableTreeNode(">");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("E");
        parent.add(node);
          a4++;
        error = RULE_E(node);
        CodeGenerator.addInstruction("OPR", "11", "0");
                     counter++;
        if(a4==1)
                 {
                     
               x=SemanticAnalyzer.popStack(); 
                    
               y=SemanticAnalyzer.popStack();
               result=SemanticAnalyzer.calculateCube(x,y,">");
            
                 SemanticAnalyzer.pushStack(result);
               a4=0;
                 }
                 

      } 
        else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("<")) 
        {
             c=tokens.get(currentToken).getWord();
        node = new DefaultMutableTreeNode("<");
        parent.add(node);
        node = new DefaultMutableTreeNode("E");
        parent.add(node);
        currentToken++;
        a5++;
        error = RULE_E(node);
        CodeGenerator.addInstruction("OPR", "12", "0");
                     counter++;
        if(a5==1)
                 {
                     
               x=SemanticAnalyzer.popStack(); 
                    
               y=SemanticAnalyzer.popStack();
               result=SemanticAnalyzer.calculateCube(x,y,"<");
               
                 SemanticAnalyzer.pushStack(result);
                a5=0;
                 }
               
        }
      else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("==")) 
        {
             c=tokens.get(currentToken).getWord();
        node = new DefaultMutableTreeNode("==");
        parent.add(node);
        node = new DefaultMutableTreeNode("E");
        parent.add(node);
        currentToken++;
        a6++;
        error = RULE_E(node);
        CodeGenerator.addInstruction("OPR", "15", "0");
                     counter++;
        if(a6==1)
                 {
                     
               x=SemanticAnalyzer.popStack(); 
                     
               y=SemanticAnalyzer.popStack();
               result=SemanticAnalyzer.calculateCube(x,y,"==");
             
                 SemanticAnalyzer.pushStack(result);
               a6=0;
                 }
                 
        }
      
      else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("!=")) 
        {
             c=tokens.get(currentToken).getWord();
        node = new DefaultMutableTreeNode("!=");
        parent.add(node);
        node = new DefaultMutableTreeNode("E");
        parent.add(node);
        currentToken++;
        a7++;
        error = RULE_E(node);
         CodeGenerator.addInstruction("OPR", "16", "0");
                     counter++;
        if(a7==1)
                 {
                    
               x=SemanticAnalyzer.popStack(); 
                     
               y=SemanticAnalyzer.popStack();
               result=SemanticAnalyzer.calculateCube(x,y,"!=");
              
                 SemanticAnalyzer.pushStack(result);
              a7=0;
                 }
                 
        }
      
    }
    return error;
    
   }

  private static boolean RULE_E(DefaultMutableTreeNode parent) 
  {
    //System.out.println("in e");
      boolean error;
     
      String x,y,c = null,c2,result;
    DefaultMutableTreeNode node;
    node = new DefaultMutableTreeNode("A");
    parent.add(node);
    error = rule_A(node);
    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("+") || currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) 
    {
      if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("+")) {
         
       c=tokens.get(currentToken).getWord();
          node = new DefaultMutableTreeNode("+");
        parent.add(node);
        
        currentToken++;
        
        node = new DefaultMutableTreeNode("A");
        parent.add(node);
         a8++;
        error = rule_A(node);
       CodeGenerator.addInstruction("OPR","2" , "0");
                      counter++;
       if(a8>=1)
                 {
                      
               x=SemanticAnalyzer.popStack(); 
                    // System.out.println(x);   
               y=SemanticAnalyzer.popStack();
                    // System.out.println(y);
               result=SemanticAnalyzer.calculateCube(x,y,"+");
                    // System.out.println(result);
                 SemanticAnalyzer.pushStack(result);
              a8=0;
                 }
                 
         
      } 
      else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) 
      {
         //lag=lag+5;
        //  System.out.println(lag);
          c2=tokens.get(currentToken).getWord();
          node = new DefaultMutableTreeNode("-");
          parent.add(node);
          currentToken++;
          node = new DefaultMutableTreeNode("A");
          parent.add(node);
          a9++;
          error = rule_A(node);
        CodeGenerator.addInstruction("OPR","3" , "0");
                    counter++;
          if(a9==1)
                {
                    
                    x=SemanticAnalyzer.popStack(); 
                   // System.out.println("x in binary " +x);
                    y=SemanticAnalyzer.popStack();
                    //System.out.println("y in binary " +y);
                    result=SemanticAnalyzer.calculateCube(x,y,"-");
                  //  System.out.println("result of binary "+result);
                    SemanticAnalyzer.pushStack(result);
                   // System.out.println("stack in binary "+A2.SemanticAnalyzer.stack);
                    a9=0;
                }
                 
      }
      
    }
    return error;
  }

  private static boolean rule_A(DefaultMutableTreeNode parent) 
  {
   // System.out.println("in a");
      
      String x,y,c = null,result;
      boolean error;
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("B");
    parent.add(node);
    error = rule_B(node);
    while (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("*") || currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("/")) {
      if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("*")) 
      {
        c=tokens.get(currentToken).getWord(); 
          node = new DefaultMutableTreeNode("*");
        parent.add(node);
        currentToken++;
        node = new DefaultMutableTreeNode("B");
        parent.add(node);
         a10++;
        error = rule_B(node);
         CodeGenerator.addInstruction("OPR","4" , "0");
                     counter++;
                    if(a10==1)
                 {
                    
                    x=SemanticAnalyzer.popStack(); 
                    
                y=SemanticAnalyzer.popStack();
               result=SemanticAnalyzer.calculateCube(x,y,"*");
               
                 SemanticAnalyzer.pushStack(result);
               a10=0;
                 }
                 
      } 
      
      else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("/")) 
      {
         
        c=tokens.get(currentToken).getWord();
          node = new DefaultMutableTreeNode("/");
        parent.add(node);
        node = new DefaultMutableTreeNode("B");
        parent.add(node);
        currentToken++;
        count1++;
        error = rule_B(node);
        CodeGenerator.addInstruction("OPR","5" , "0");
                      counter++;
        if(count1==1)
                 {
                     
                    x=SemanticAnalyzer.popStack(); 
                    
                y=SemanticAnalyzer.popStack();
               result=SemanticAnalyzer.calculateCube(x,y,"/");
               
                 SemanticAnalyzer.pushStack(result);
              
                                    count1=0; 

                 }
      
      }
      
    }
    return error;
  }

  private static boolean rule_B(DefaultMutableTreeNode parent) 
  {
   // System.out.println("in b");
      boolean error = false;
      
      
      int flag=0;
      String x,c = null,result;
    DefaultMutableTreeNode node;
    if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("-")) 
    {
      c=tokens.get(currentToken).getWord();
      node = new DefaultMutableTreeNode("-");
      parent.add(node);
       CodeGenerator.addInstruction("LIT","0","0" );
                     counter++;
     a12++;
      currentToken++;
      
      }
    
    
        
         // System.out.println(lag);
    node = new DefaultMutableTreeNode("C");
    parent.add(node);
    error = rule_C(node);
      
  if(a12==1)
                 {
                    
                     CodeGenerator.addInstruction("OPR","3" , "0");
                     counter++;
                    x=SemanticAnalyzer.popStack(); 
                    // System.out.println("in unary "+ x);
             // System.out.println("stack in unary "+A2.SemanticAnalyzer.stack);
                 result=SemanticAnalyzer.calculateCube(x,"-");
                     //System.out.println(result);
                 SemanticAnalyzer.pushStack(result);
              
                 //System.out.println("stack in unary "+A2.SemanticAnalyzer.stack);
                  a12=0; 
                 }
      
    return error;
  }

  private static boolean rule_C(DefaultMutableTreeNode parent) 
  {
   // System.out.println("in c");
      boolean error = false;
    DefaultMutableTreeNode node;
    // System.out.println(currentToken+ "<" +tokens.size()+"in c");
     
    if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("INTEGER")) 
        {
      node = new DefaultMutableTreeNode("integer" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
        String st="int";
        SemanticAnalyzer.pushStack(st);
        CodeGenerator.addInstruction("LIT",tokens.get(currentToken).getWord() , "0");
        counter++;
      currentToken++;
      
        } 
        else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("OCTAL")) 
        {
      node = new DefaultMutableTreeNode("Octal" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      String st="int";
        SemanticAnalyzer.pushStack(st);
        
        CodeGenerator.addInstruction("LIT",tokens.get(currentToken).getWord() , "0");
        counter++;
      currentToken++;
        } 
        else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("HEXADECIMAL")) 
        {
      node = new DefaultMutableTreeNode("Hexadecimal" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
       String st="int";
        SemanticAnalyzer.pushStack(st);
        
        CodeGenerator.addInstruction("LIT",tokens.get(currentToken).getWord() , "0");
        counter++;
      currentToken++;
        } 
        
        else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("BINARY")) 
        {
        node = new DefaultMutableTreeNode("Binary" + "(" + tokens.get(currentToken).getWord() + ")");
        parent.add(node);
        String st="int";
        SemanticAnalyzer.pushStack(st);
        
        CodeGenerator.addInstruction("LIT",tokens.get(currentToken).getWord() , "0");
        counter++;
        currentToken++;
        } 
        
        else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("true")) 
        {
          node = new DefaultMutableTreeNode("True" + "(" + tokens.get(currentToken).getWord() + ")");
          parent.add(node);
          String st1="boolean";
          SemanticAnalyzer.pushStack(st1);
          CodeGenerator.addInstruction("LIT",tokens.get(currentToken).getWord() , "0");
          counter++;
         currentToken++;
        } 
        
        else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("false")) 
        {
      node = new DefaultMutableTreeNode("False" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
         String st2="boolean";
        SemanticAnalyzer.pushStack(st2);
        CodeGenerator.addInstruction("LIT",tokens.get(currentToken).getWord() , "0");
        counter++;
      currentToken++;
        } 
        
        else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("STRING")) 
        {
      node = new DefaultMutableTreeNode("String" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      String st3="string";
        SemanticAnalyzer.pushStack(st3);
        CodeGenerator.addInstruction("LIT",tokens.get(currentToken).getWord() , "0");
        counter++;
      currentToken++;
        } 
        
        else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("CHARACTER")) 
        {
      node = new DefaultMutableTreeNode("Char" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
       String st4="char";
        SemanticAnalyzer.pushStack(st4);
        CodeGenerator.addInstruction("LIT",tokens.get(currentToken).getWord() , "0");
        counter++;
      currentToken++;
        } 
        
        else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("FLOAT")) 
        {
      node = new DefaultMutableTreeNode("Float" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
       String st5="float";
        SemanticAnalyzer.pushStack(st5);
        CodeGenerator.addInstruction("LIT",tokens.get(currentToken).getWord() , "0");
        counter++;
      currentToken++;
        } 
        
              
        else if (currentToken < tokens.size() && tokens.get(currentToken).getToken().equals("IDENTIFIER")) 
        {
        //    System.out.println(tokens.get(currentToken).getWord());
      node = new DefaultMutableTreeNode("identifier" + "(" + tokens.get(currentToken).getWord() + ")");
      parent.add(node);
      String a=tokens.get(currentToken).getWord();
      CodeGenerator.addInstruction("LOD",a , "0");
      counter++;
      if(SemanticAnalyzer.symbolTable.containsKey(a))
      {
        Vector<SymbolTableItem> v= SemanticAnalyzer.symbolTable.get(a);
      
        SymbolTableItem st=v.firstElement();
        String str=st.getType();
        SemanticAnalyzer.pushStack(str);
       // System.out.println("stack "+A2.SemanticAnalyzer.stack);
      }
        else
        {
            A4.SemanticAnalyzer.error(gui,4,tokens.get(currentToken).getLine(),a); 
             SemanticAnalyzer.pushStack("error");
        }
      currentToken++;
        } 
        
        else if (currentToken < tokens.size() && tokens.get(currentToken).getWord().equals("(")) 
        {
      node = new DefaultMutableTreeNode("(");
      parent.add(node);
      currentToken++;
      //
      node = new DefaultMutableTreeNode("expression");
      parent.add(node);
      error = rule_expression(node);
      //
      node = new DefaultMutableTreeNode(")");
      parent.add(node);
      currentToken++;      
        }
        
        
    
        else 
        {
      error(9);
      return true;
        }
    return false;
  }
  
  public static void error(int err) 
    {
        int n;
        if(currentToken<tokens.size())
        {
        n = tokens.get(currentToken).getLine();
        }
        else 
   
        {
            n=tokens.get(currentToken-1).getLine();
        }
        
        switch (err) 
        {
        case 1: gui.writeConsole("Line" + n + ": expected {"); 
                break;
        case 2: gui.writeConsole("Line" + n + ": expected }"); 
                break;
        case 3: gui.writeConsole("Line" + n + ": expected ;"); 
                break;
        case 4:
        gui.writeConsole("Line" +n+": expected identifier or keyword");
                break;
        case 5:
        gui.writeConsole("Line" +n+": expected ="); 
                break;
        case 6:
        gui.writeConsole("Line" +n+": expected identifier"); 
                break;
        case 7:
        gui.writeConsole("Line" +n+": expected )"); 
                break;
        case 8:
        gui.writeConsole("Line" +n+": expected ("); 
                break;
        case 9:
        gui.writeConsole("Line" +n+": expected value, identifier, (");
                break;
         case 10:
        gui.writeConsole("Line" +n+": expected :"); 
                break;    
        }
  }
  


}
