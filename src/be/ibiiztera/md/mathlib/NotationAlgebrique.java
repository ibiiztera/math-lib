/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ibiiztera.md.mathlib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author Manuel DAHMEN
 */
public class NotationAlgebrique {

    private Bloc search(Bloc[] bs, int idx) {
        if(idx==-1)
            return searchf(bs, idx);
        if(bs[idx].nbrOperandes==2)
            bs[idx].contenu.add(searchb(bs, idx));
        else if(bs[idx].nbrOperandes==1)
            bs[idx].contenu.add(searchf(bs, idx));
        else if(bs[idx].nbrOperandes==0)
            bs[idx].contenu=null;
        return bs[idx];
    }

    private Bloc searchb(Bloc[] bs, int idx) {
        int i=0;
        int pmin = 10000;
        int idx_retenu = idx;
        while(i>=0)
        {
            if(bs[i]!=null && bs[i].priorite<pmin)
            {
                idx_retenu = i;
                pmin = bs[i].priorite;
            }
            i--;
        }
        return bs[idx_retenu];
    }

    private Bloc searchf(Bloc[] bs, int idx) {
        int i=0;
        int pmin = 10000;
        int idx_retenu = idx;
        while(i<bs.length)
        {
            if(bs[i]!=null && bs[i].priorite<pmin)
            {
                idx_retenu = i;
                pmin = bs[i].priorite;
            }
            i++;
        }
        return bs[idx_retenu];
    }

    public class Bloc {

        public TypeBloc type;
        public String chaine;
        public int start;
        public int end;
        public ArrayList<Bloc> contenu = new ArrayList<Bloc>();
        public String fonction;
        public int priorite = -1;
        public int numero = 0;
        public Double valeur;
        private int nbrOperandes;
    }

    public String polonaise(String algebrique) {
        int l = algebrique.length();
        ArrayList<Bloc> blocs = new ArrayList<Bloc>();
        ArrayList<Bloc> pile = new ArrayList<Bloc>();
        TreeMap<String, String> arbre = new TreeMap<String, String>();
        String polonaise = "";
        /// rechercher les parentheses et fonctions

        int[] startParentheses = new int[algebrique.length() / 2];
        int[] endParentheses = new int[algebrique.length() / 2];
        int startParenthesesCpt = 0;
        int endParenthesesCpt = 0;
        int parentheses = 0;
        int numero = 0;
        for (int i = 0; i < l; i++) {
            // recherche de fonction et de parenthese
            if (algebrique.charAt(i) == '(') {
                startParentheses[startParenthesesCpt++] = i;
                parentheses += 100;
            } else if (algebrique.charAt(i) == ')') {
                parentheses -= 100;
                endParentheses[endParenthesesCpt++] = i;
                int start = i;
                int end = i - 1;
                int cpt = 1;
                while (end >= 0) {
                    if (algebrique.charAt(i) == ')') {
                        cpt++;
                    }
                    if (algebrique.charAt(i) == '(') {
                        cpt--;
                    }
                    if (cpt == 0) {
                        Bloc b = new Bloc();
                        b.chaine = algebrique.substring(end, start);
                        b.start = end;
                        b.end = start;
                        b.numero = ++numero;
                        if (end == 0) {
                            b.type = TypeBloc.PARENTHESE;
                        } else {
                            if (Character.isLetter(algebrique.charAt(end - 1))) {
                                b.type = TypeBloc.FONCTION;
                                int cptFct = 0;
                                while (end - 1 + cptFct >= 0 && Character.isLetter(algebrique.charAt(end - 1 + cptFct))) {
                                    cptFct--;
                                }
                                b.fonction = algebrique.substring(end + cptFct, end - 1);
                            }

                        }
                    }
                    i--;
                }
            }
            char c = algebrique.charAt(i);
            Bloc b = new Bloc();
            b.priorite = -1;
            switch (c) {

                case '+':
                    b.priorite = 4 + parentheses;
                    break;
                case '-':
                    b.priorite = 5 + parentheses;
                    break;
                case '/':
                    b.priorite = 6 + parentheses;
                    break;
                case '*':
                    b.priorite = 7 + parentheses;
                    if (algebrique.charAt(i + 1) == '*') {
                        c = '^';
                    } else {
                        break;
                    }
                case '^':
                    b.priorite = 9 + parentheses;
                    break;
            }
            if (b.priorite != -1) {
                b.chaine = "" + c;
                b.start = i;
                b.end = c == '^' ? i + 2 : i + 1;
                if (c == '^') {
                    i++;
                }
                b.type = TypeBloc.OPERATEUR;
                b.numero = ++numero;
            }
            // Rercherche de nombres
            if (Character.isDigit(algebrique.charAt(i)) || algebrique.charAt(i)=='.') {
                int cpt = i;
                while(i+1<l && Character.isDigit(algebrique.charAt(i+1)) || algebrique.charAt(i+1)=='.')
                    cpt++;
                Double v =Double.parseDouble(algebrique.substring(i, cpt));
                b = new Bloc();
                b.chaine = algebrique.substring(i, cpt);
                b.end = cpt;
                b.start = i;
                b.type = TypeBloc.NOMBRE;
                b.valeur = v;
                b.numero = ++numero;
                b.priorite = 1000;
                i = cpt+1;
                blocs.add(b);
            }
            // Recherche des variables
            if (Character.isLetter(algebrique.charAt(i))) {
                int cpt = i;
                boolean fct = false;
                while (cpt < l && Character.isLetter(cpt)) {
                    cpt++;
                }
                if (algebrique.charAt(cpt) == '(') {
                    fct = true;
                }

                b = new Bloc();
                b.chaine = algebrique.substring(i, cpt - 1);
                b.end = cpt - 1;
                b.start = i;
                b.type = fct ? TypeBloc.FONCTION : TypeBloc.VARIABLE;
                b.numero = ++numero;
                if (b.type == TypeBloc.FONCTION) {
                    parentheses += 100;
                }
                b.priorite = 2 + parentheses;
                b.numero = ++numero;
                i = b.type == TypeBloc.FONCTION ? cpt + 1 : cpt;
                parentheses += b.type == TypeBloc.FONCTION ? 100 : 0;
            }
        }
        // Construire l'arbre à partir de la notation algébrique
        // TODO: 
                // Chercher si opérateurs de priorité supérieure précède
                // Si oui, réservez dans la pile
               // Si non ajoutez les opérandes
        // Chercher itérativement par ordre de priorité : de plus basse à plus haute
        // 1 Premier
        // 2 Gauche
        // 3 Droite
        // 4 Gauche -> Gauche Droite 
        // 5 Droite -> ....
        Bloc [] bs = new Bloc[blocs.size()];
        for(int i= 0; i<blocs.size(); i++)
            bs[i] = blocs.get(i);
        
        Bloc idx = search(bs, -1);
        
        return polonaise;
    }

    public static void main(String[] args) {
        String[] str = new String[]{
            "a*b+(4+1)*5", "a b * 4 1 + 5 * +"
        };

    }
}
