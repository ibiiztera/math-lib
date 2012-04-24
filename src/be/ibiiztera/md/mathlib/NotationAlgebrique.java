package be.ibiiztera.md.mathlib;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manuel DAHMEN
 */
public class NotationAlgebrique {
    private Element base;
    private Element elemBase() {
        return base;
    }

    public class Operande implements Element{

        public Operande(String txt) {
        }

        @Override
       public String txt() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        public double calculer() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    public interface Element
    {
        public String txt();
        public double calculer();
    }
    public class Operateur extends Operande
    {
        public Operateur(String t)
        {
            super(t);
        }
        private String txt;

        @Override
        public String txt() {
            return txt;
        }
        public void setOperandes(List<Operande> op)
        {
            
        }
        @Override
        public double calculer()
        {
            return 0;
        }
        
    }
    public class Variable implements Element
    {

        @Override
        public String txt() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double calculer() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    public class Nombre implements Element
    {

        @Override
        public String txt() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double calculer() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    private int highest(List<Element> liste)
    {
        return -1;
    }
    public Fonction in(String infixee)
    {
        Fonction f = new Fonction();
        List<Element> liste = liste(infixee);
        while(liste.size()>0)
        {
            
        }
        
        return f;
    }
    private List<Element> liste(String x)
    {
        ArrayList<Element> liste = new ArrayList<Element>();
        return liste;
    }
    public double calculer()
    {
        return elemBase().calculer();
    }
}
