/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author macario
 */
public class Escenario extends JFrame
{
    
    private JLabel[][] tablero;     
    private int[][] matrix;
    private final int dim = 12;

    private ImageIcon robot1;
    private ImageIcon robot2;
    private ImageIcon obstacleIcon;
    private ImageIcon sampleIcon;
    private ImageIcon actualIcon;
    private ImageIcon motherIcon;
    
    private Agente wallE;
    private Agente eva;
   
    private final BackGroundPanel fondo = new BackGroundPanel(new ImageIcon("imagenes/surface.jpg"));
    
    private final JMenu settings = new JMenu("Settigs");
    private final JRadioButtonMenuItem obstacle = new JRadioButtonMenuItem("Obstacle");
    private final JRadioButtonMenuItem sample = new JRadioButtonMenuItem("Sample");
    private final JRadioButtonMenuItem motherShip = new JRadioButtonMenuItem("MotherShip");
    
    /*Labels que avisan las distancias*/
    private final JLabel labelA1=new JLabel("Agente 1: "); 
    private final JLabel labelA2=new JLabel("Agente 2: "); 
    
    
    public Escenario(){
        this.setContentPane(fondo);
        this.setTitle("Agentes");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(50,50,dim*50+35,dim*50+85+50);
        initComponents();
        
    }
        
    private void initComponents(){
        ButtonGroup settingsOptions = new ButtonGroup();
        settingsOptions.add(sample);
        settingsOptions.add(obstacle);       
        settingsOptions.add(motherShip);
        
        JMenuBar barraMenus = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem run  = new JMenuItem("Run");
        
        JMenuItem exit   = new JMenuItem("Exit");
              
        this.setJMenuBar(barraMenus);
        barraMenus.add(file);
        barraMenus.add(settings);
        file.add(run);
        file.add(exit);
        settings.add(motherShip);
        settings.add(obstacle);
        settings.add(sample);
        
        labelA1.setBounds(50,dim*50+10,300,50);
        
        labelA2.setBounds(350,dim*50+10,300,50);
        labelA1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        labelA2.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        labelA1.setOpaque(true);
        labelA2.setOpaque(true);
        labelA1.setBackground(Color.GRAY);
        labelA2.setBackground(Color.GRAY);
        this.add(labelA1); 
        this.add(labelA2); 
        
            
        robot1 = new ImageIcon("imagenes/wall-e.png");
        robot1 = new ImageIcon(robot1.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));//redimenciona
        
        robot2 = new ImageIcon("imagenes/eva.png");
        robot2 = new ImageIcon(robot2.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        
        obstacleIcon = new ImageIcon("imagenes/bomb.png");
        obstacleIcon = new ImageIcon(obstacleIcon.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        obstacleIcon.setDescription("bomba");//descripcion al icono bomba
        
        sampleIcon = new ImageIcon("imagenes/sample.png");
        sampleIcon = new ImageIcon(sampleIcon.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        sampleIcon.setDescription("zapato");//descripcion al icono zapato
        
        motherIcon = new ImageIcon("imagenes/mothership.png");
        motherIcon = new ImageIcon(motherIcon.getImage().getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH));
        motherIcon.setDescription("nave");//descripcioon al icono nave
        
        this.setLayout(null);
        formaPlano();  
        
        exit.addActionListener(evt -> gestionaSalir(evt));
        run.addActionListener(evt -> gestionaRun(evt));
        obstacle.addItemListener(evt -> gestionaObstacle(evt));
        sample.addItemListener(evt -> gestionaSample(evt));
        motherShip.addItemListener(evt -> gestionaMotherShip(evt));

              
            
        class MyWindowAdapter extends WindowAdapter
        {
            public void windowClosing(WindowEvent eventObject)
            {
		goodBye();
            }
        }
        addWindowListener(new MyWindowAdapter());
        
        // Crea 2 agentes
        wallE = new Agente("Wall-E",robot1, matrix, tablero); 
        
        eva = new Agente("Eva",robot2, matrix, tablero); 
        wallE.agente2= eva; // asignamos eva a wall-e
        eva.agente2 = wallE;//asignamos walle a eva
        wallE.nave=motherIcon;//asignamos  la nave
        eva.nave=motherIcon; // asignamos la nave
        //textos
        wallE.txt = labelA1; //asignamos label de la izquierda
        eva.txt = labelA2;  //asignamos label de la derecha
        
        
    }
        
    private void gestionaSalir(ActionEvent eventObject){
        goodBye();
    }
        
    private void goodBye(){
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Desea salir?","Aviso",JOptionPane.YES_NO_OPTION);
        if(respuesta==JOptionPane.YES_OPTION) System.exit(0);
    }
  
    private void formaPlano(){
        tablero = new JLabel[dim][dim];
        matrix = new int[dim][dim];
        
        int i, j;
        //crea los cuadros
        for(i=0;i<dim;i++)
            for(j=0;j<dim;j++){
                matrix[i][j]=0;
                tablero[i][j]=new JLabel();
                
                /*Llena el tablero*/
                tablero[i][j].setBounds(j*50+10,i*50+10,50,50);//cuadros de 50px * 50px
                tablero[i][j].setBorder(BorderFactory.createDashedBorder(Color.white));
                tablero[i][j].setOpaque(false); //sin relleno 
                this.add(tablero[i][j]);
                
                tablero[i][j].addMouseListener(new MouseAdapter() // Este listener nos ayuda a agregar poner objetos en la rejilla
                    {
                        @Override
                        public void mousePressed(MouseEvent e)  {
                               insertaObjeto(e);
                        }   
                
                        @Override
                        public void mouseReleased(MouseEvent e) 
                        {
                                insertaObjeto(e);
                        }   
                
                    });
                                
            }
    }
        
    private void gestionaObstacle(ItemEvent eventObject){
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if(opt.isSelected())
           actualIcon = obstacleIcon;
        else actualIcon = null;        
    }
    
    private void gestionaSample(ItemEvent eventObject)
    {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if(opt.isSelected())
           actualIcon = sampleIcon;
        else actualIcon = null;   
    }
    
    private void gestionaMotherShip(ItemEvent eventObject){
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if(opt.isSelected())
           actualIcon = motherIcon;
        else actualIcon = null;   
    }
    //Inicializa los hilos
    private void gestionaRun(ActionEvent eventObject){
        if(!wallE.isAlive()) wallE.start(); // si no están corriendo
        if(!eva.isAlive()) eva.start();
        settings.setEnabled(false);
        
        
    }
       
    public void insertaObjeto(MouseEvent e){
        //e es nuestro JLAbel al que damos click
        JLabel casilla = (JLabel) e.getSource();
        if(actualIcon!=null){//manda el icono que hay seleccionado si no es nulo 
            casilla.setIcon(actualIcon);
            /*asignamos el nombre de la casilla al nombre de la descripción de imagen
            es decir, nave, zapato o bomba*/
            casilla.setName(actualIcon.getDescription());
            
            
        }
    }
    
}
