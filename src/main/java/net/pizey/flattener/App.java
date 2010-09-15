package net.pizey.flattener;

import java.io.File;
import java.util.Iterator;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.XSXPath;
import com.sun.xml.xsom.impl.util.DraconianErrorHandler;
import com.sun.xml.xsom.impl.util.SchemaTreeTraverser;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.visitor.XSVisitor;

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
      parser.setErrorHandler(new DraconianErrorHandler());
      parser.parse(file);
      this.schemaSet = parser.getResult();
      this.xsSchema = this.schemaSet.getSchema(1);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void printElements() {
    System.out.println(xsSchema.getElementDecl("study-info"));
    for (String key : xsSchema.getElementDecls().keySet())
      System.out.println("E:" + key);

    for (String key : xsSchema.getComplexTypes().keySet())
      System.out.println("CT:" + key);
    while (xsSchema.iterateComplexTypes().hasNext())
      System.out.println("T:" + xsSchema.iterateComplexTypes().next());

    XSComplexType xsComplexType = xsSchema.getComplexType("CT");
    if (xsComplexType != null) {
      XSContentType xsContentType = xsComplexType.getContentType();
      XSParticle particle = xsContentType.asParticle();
      if (particle != null) {
        XSTerm term = particle.getTerm();
        if (term.isModelGroup()) {
          XSModelGroup xsModelGroup = term.asModelGroup();
          XSParticle[] particles = xsModelGroup.getChildren();
          for (XSParticle p : particles) {
            XSTerm pterm = p.getTerm();
            if (pterm.isElementDecl()) { // xs:element inside complex type
              System.out.println(pterm);
            }
          }
        }
      }
    } else
      System.out.println("Not found");
    
    System.out.println("-----------");
    System.out.println("Target namespace: "+xsSchema.getTargetNamespace());
    
    Iterator<XSElementDecl> jtr = xsSchema.iterateElementDecls();
    while( jtr.hasNext() ) {
      XSElementDecl e = (XSElementDecl)jtr.next();
      
      System.out.print( e.getName() );
      if( e.isAbstract() )
        System.out.print(" (abstract)");
      System.out.println();
    }
    System.out.println("-----------");
  }

  private void visit() { 
    xsSchema.visit(new SimpleVisitor());
    
    MyTraverser stt = new MyTraverser();
    stt.visit(schemaSet);
  }
  
  
  public static void main(String[] args) {
    App it = new App();
    File f = new File(args[0]);
    if (!f.exists())
      throw new RuntimeException("Cannot find file " + args[0]);
    it.parse(f);
    it.printElements();
    
    it.visit();
    
  }
}
class SimpleVisitor implements XSVisitor{

  @Override
  public void annotation(XSAnnotation ann) {
    System.out.println("XSAnnotation");
    
  }

  @Override
  public void attGroupDecl(XSAttGroupDecl decl) {
    System.out.println("XSAttGroupDecl");
    
  }

  @Override
  public void attributeDecl(XSAttributeDecl decl) {
    System.out.println("XSAttributeDecl");
    
  }

  @Override
  public void attributeUse(XSAttributeUse use) {
    System.out.println("XSAttributeUse");
    
  }

  @Override
  public void complexType(XSComplexType type) {
    System.out.println("XSComplexType");
    
  }

  @Override
  public void facet(XSFacet facet) {
    System.out.println("XSFacet");
    
  }

  @Override
  public void identityConstraint(XSIdentityConstraint decl) {
    System.out.println("XSIdentityConstraint");
    
  }

  @Override
  public void notation(XSNotation notation) {
    System.out.println("XSNotation");
    
  }

  @Override
  public void schema(XSSchema schema) {
    System.out.println("XSSchema");
    
  }

  @Override
  public void xpath(XSXPath xp) {
    System.out.println("XSXPath");
    
  }

  @Override
  public void elementDecl(XSElementDecl decl) {
    System.out.println("XSElementDecl");
    
  }

  @Override
  public void modelGroup(XSModelGroup group) {
    System.out.println("XSModelGroup");
    
  }

  @Override
  public void modelGroupDecl(XSModelGroupDecl decl) {
    System.out.println("XSModelGroupDecl");
    
  }

  @Override
  public void wildcard(XSWildcard wc) {
    System.out.println("XSWildcard");
    
  }

  @Override
  public void empty(XSContentType empty) {
    System.out.println("XSContentType");
    
  }

  @Override
  public void particle(XSParticle particle) {
    System.out.println("XSParticle");
    
  }

  @Override
  public void simpleType(XSSimpleType simpleType) {
    System.out.println("XSSimpleType");
  } 

}

class MyTraverser extends SchemaTreeTraverser {

  @Override
  public void particle(XSParticle part) {
    // TODO Auto-generated method stub
    super.particle(part);
    int repeat = 1;
    if (part.isRepeated())
      if (part.getMaxOccurs() == XSParticle.UNBOUNDED)
        repeat = 5;
      else 
        repeat = part.getMaxOccurs();
    //System.err.println(part.toString());  
    //for (int i = 1; i < repeat; i++)
    //  System.err.println(part.getTerm().toString() + repeat);  
  }

  @Override
  public void elementDecl(XSElementDecl decl) {
    // TODO Auto-generated method stub
    super.elementDecl(decl);
    System.err.println(decl.getName());
  }

  
}