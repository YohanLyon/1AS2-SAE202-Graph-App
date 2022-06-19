package Groupes.Groupe11;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.Kruskal;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.coloring.WelshPowell;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.ui.swingViewer.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.graphstream.ui.swingViewer.*;
import static packMesClassesEtInterfaces.SAE202_Algos.charger_graphe;
import static packMesClassesEtInterfaces.SAE202_Algos.*;
import packMesClassesEtInterfaces.SAE202_Interface;

//import org.graphstream.ui.view.Camera;
//import org.graphstream.ui.view.View;
//import org.graphstream.ui.view.Viewer;

/**
 *
 * Cette classe permet une visualisation indiciduelle de chaque graphe en fonction du fichier
 * visé. Elle permet d'effectuer la colloration désiré et etudier les probabilité de conflit.
 * Elle donne egallement un aspect visuel au graphe.
 */
public class FenetrePrincipale extends JFrame {

    JPanel panneauDessin;
    PanneauChoixGraphe panneauChoixG;
    PanneauAlgo panneauAlgo;
    Graph grapheCourant = null;

    private Viewer graphViewer;
    private View graphView;

    /**
     * Cette fonction permet d'initialiser la fenetre principale.
     */
    
    public FenetrePrincipale() {
        panneauDessin = new JPanel();
        panneauChoixG = new PanneauChoixGraphe();
        panneauAlgo = new PanneauAlgo();
        panneauDessin.setLayout(new BorderLayout());
        panneauDessin.setPreferredSize(new Dimension(800, 600));
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = gc.gridy = 0;
        gc.gridheight = 3;
        gc.fill = GridBagConstraints.HORIZONTAL;
        this.getContentPane().add(panneauDessin, gc);
        gc.gridx = 1;
        gc.gridheight = 1;
        this.getContentPane().add(panneauChoixG, gc);
        gc.gridy = 1;
        this.getContentPane().add(panneauAlgo, gc);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();
    }

    /**
     * Cette fonction permet d'actualiser la zone de dessin du graphe.
     */
    public void updateDessin() {

        if (grapheCourant != null) {
            try {
                panneauDessin.remove((Component) graphViewer.getView(Viewer.DEFAULT_VIEW_ID));
            } catch (Exception ex) {
            }
            graphViewer = new Viewer(grapheCourant, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
            graphViewer.enableAutoLayout();
            graphView = graphViewer.addDefaultView(false);
            panneauDessin.add((Component) graphView);
            graphView.revalidate();
            for(Node n:grapheCourant) {
                //n.setAttribute("ui.style", "fill-color: rgb(0,100,255); shape:box; size:20px;");
                n.setAttribute("ui.label", n);
            }
            
        }
        this.pack();
        
    }
    
    /**
     * Cette fonction permet d'afficher les informations sur le graphes.
     * Elle permet egallement de cibler un graphe dans le dossier Données
     */
    class PanneauChoixGraphe extends JPanel {

        
        JButton generer;
        JTextField jtf1 = new JTextField(15);
        JLabel nbSommet;
        JLabel nbSommetText = new JLabel("Sommets : ");
        JLabel nbCouleurText = new JLabel("Couleurs : ");
        JLabel nbCouleur;
        
        
        JLabel dim1 = new JLabel("Nom Fichier");
        

        public PanneauChoixGraphe() {
            this.setLayout(new GridLayout(3, 2));
            nbSommet = new JLabel("0");
            nbCouleur = new JLabel("0");
            
            generer = new JButton("Generer");  

            this.setBorder(BorderFactory.createTitledBorder("Choix du graphe"));
            
            this.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            
            gc.gridx=0;
            gc.gridy=2;
            gc.insets = new Insets(10,0,0,0);
            this.add(generer,gc);
            gc.gridx=0;
            gc.gridy=0;
            gc.insets = new Insets(0,0,0,0);
            this.add(dim1, gc);
            gc.gridx=0;
            gc.gridy=1;
            this.add(jtf1,gc);
            gc.gridx=1;
            gc.gridy=3;
            gc.insets = new Insets(10,0,0,10);
            this.add(nbSommet, gc);
            gc.gridx=0;
            gc.gridy=3;
            this.add(nbSommetText, gc);
            
            gc.gridx=1;
            gc.gridy=4;
            gc.insets = new Insets(0,0,0,10);
            this.add(nbCouleur, gc);
            gc.gridx=0;
            gc.gridy=4;
            this.add(nbCouleurText, gc);

            generer.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (jtf1.getText() != null) {
                        grapheCourant =  charger_graphe(jtf1.getText()+".txt");
                        nbCouleur.setText(grapheCourant.getAttribute("nb_couleurs_max").toString());
                        Integer nbSom = new Integer(grapheCourant.getNodeCount());
                        nbSommet.setText(nbSom.toString());
                   
                        updateDessin();  
                    }
                }

            });

        }

    }

    /**
     * Cette foncion permet de selectionner une colloration quand un
     * graphe a été selectionné
     */
    class PanneauAlgo extends JPanel {
        JComboBox<TypeGraphe> combo;
        JButton colorier;

        public PanneauAlgo() {
            this.setBorder(BorderFactory.createTitledBorder("Type Coloration"));
            this.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            
            colorier = new JButton("Colorier");
            
            JLabel nbConflitText = new JLabel("Conflits : ");
            JLabel nbConflit = new JLabel("0");
            
            combo = new JComboBox();
            combo.addItem(TypeGraphe.RANDOM);
            combo.addItem(TypeGraphe.WelshPowell);
            combo.addItem(TypeGraphe.DSAT);
            /*
           
            combo.addItem(TypeGraphe.CYCLE);*/
            gc.gridx = 0;
            gc.gridy = 0;
            this.add(combo,gc);
            gc.gridx = 1;
            gc.gridy = 0;
            this.add(colorier,gc);
            
            gc.gridx=1;
            gc.gridy=1;
            gc.insets = new Insets(0,0,0,10);
            this.add(nbConflit, gc);
            gc.gridx=0;
            gc.gridy=1;
            this.add(nbConflitText, gc);
            gc.insets = new Insets(0,0,0,0);
            
            colorier.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int conf = 0;
                    switch (combo.getSelectedIndex()) {
                            case 0:
                                Groupe11.colorationRandom(grapheCourant, 200000);
                                conf = compte_nb_conflits(grapheCourant, "couleur_random");
                                nbConflit.setText(conf+"");
                                updateDessin();
                                break;
                            case 1:
                                Groupe11.colorationWelshPowell(grapheCourant, 200000);
                                conf = compte_nb_conflits(grapheCourant, "couleur_wp");
                                nbConflit.setText(conf+"");
                                updateDessin();
                                break;
                            case 2:
                                Groupe11.colorationDSAT(grapheCourant,  200000);
                                conf = compte_nb_conflits(grapheCourant, "couleur_dsat");
                                nbConflit.setText(conf+"");
                                updateDessin();
                                break;
                          
                        }
                        updateDessin();
                }

            });
           
        }
    }
}
