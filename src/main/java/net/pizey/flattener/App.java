package net.pizey.flattener;

import java.io.File;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.parser.XSOMParser;

/**
 * Hello world!
 * 
 */
public class App {

  private XSSchemaSet schemaSet;
  private XSSchema xsSchema;

  private void parse(File file) {
    try {
      XSOMParser parser = new XSOMParser();
      parser.parse(file);
      this.schemaSet = parser.getResult();
      this.xsSchema = this.schemaSet.getSchema(1);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private void printElements(){
    System.out.println(xsSchema.getElementDecl("study-info"));
    for (String key : xsSchema.getElementDecls().keySet()) 
      System.err.println(key);

    XSComplexType xsComplexType = xsSchema.getComplexType("study-info-CT");
    
    for (String key : xsSchema.getComplexTypes().keySet()) 
      System.err.println(key);
    
    
    XSContentType xsContentType = xsComplexType.getContentType();
    XSParticle particle = xsContentType.asParticle();
    if(particle != null){
        XSTerm term = particle.getTerm();
        if(term.isModelGroup()){
            XSModelGroup xsModelGroup = term.asModelGroup();
            XSParticle[] particles = xsModelGroup.getChildren();
            for(XSParticle p : particles ){
                XSTerm pterm = p.getTerm();
                if(pterm.isElementDecl()){ //xs:element inside complex type
                    System.out.println(pterm);
                }
            }
        }
    }
}


  public static void main(String[] args) {
    App it = new App();
    File f = new File(args[0]);
    if(!f.exists()) throw new RuntimeException("Cannot find file " + args[0]);
    it.parse(f);
    it.printElements();
  }
}
