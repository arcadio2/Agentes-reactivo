/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentes;

import java.awt.Color;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author macario
 */
public class Agente extends Thread{
    String nombre;
    //coordenadas
    int i;
    int j;
    
    //Icono de imagen
    ImageIcon icon;
    ImageIcon nave;
    
    //Labels y matriz
    int[][] matrix;
    JLabel tablero[][];
    JLabel casillaAnterior;
    Random aleatorio = new Random(System.identityHashCode(this)); 
    
    //el otro agente
    Agente agente2;
    
    JLabel txt; 
    
    //lleva recompensa, inicializamos en falso
    boolean llevaZapato = false; 
    
    
    int xNave;
    int yNave; 
    
    public Agente(String nombre, ImageIcon icon, int[][] matrix, JLabel tablero[][]){
        this.nombre = nombre;
        this.icon = icon;
        this.matrix = matrix;
        this.tablero = tablero;

        
        //this.i = aleatorio.nextInt(matrix.length); // posicion en i aleatoria
        //this.j = aleatorio.nextInt(matrix.length);  //posicion en j 
        //tablero[i][j].setIcon(icon); //pone el muñeco en la posicion

        
    }
    
    /*Comenzamos el HILo*/
    @Override
    public void run(){
        asignarObjetos(); 
        int dirRow=1;
        int dirCol=1;
        int valores[] = {-1,1,0};
        asignarMuñecos(); 
        
        while(true){
            /*Comenzamos borrando la posicion anterior*/
            borrando(); 
            
             //evitarObstaculos(); 
            //dirCol = valores[aleatorio.nextInt(3)];
            //dirRow = valores[aleatorio.nextInt(3)];
            
            /*En caso de que no lleve zapato, cambiamos el fondo de letra y el texto (No es importante)*/
            
            if(!llevaZapato){
                txt.setText(nombre);
                txt.setForeground(Color.black);
            }
            
            /*Cuando per*/
            if(i > matrix.length-2 && dirRow == 1){
                dirRow=-1;//si pega en la abajo
                dirCol = valores[aleatorio.nextInt(3)];
            }
            if(i < 1 && dirRow==-1){
                dirRow=1;//si pega en la arriba
                dirCol = valores[aleatorio.nextInt(3)];
            }

            if(j > matrix.length-2 && dirCol ==1){
                dirCol=-1;//si pega en la derecha
                dirRow = valores[aleatorio.nextInt(3)];
            }
            if(j < 1 && dirCol==-1){
                dirCol=1; // si pega en la izquierda
                dirRow = valores[aleatorio.nextInt(3)];
            }
                            
       
            /**Aquí hace el proceso, mientras que haya bombas a lado**/
            try{
                /*Coma asignamos a la matrix, que bombas sea 1, evalúa mientras bomba es 1. 
                (i+dirRow, j+dirCol es el siguiente paso que
                podría dar, por eso evaluamos antes de que lo de)*/
                while(matrix[i+dirRow][j+dirCol]==1  ){
                    /*Genera aleatorios mientras entra*/
                    aleatorio = new Random(System.currentTimeMillis());
                    dirRow=valores[aleatorio.nextInt(3)]; 
                    dirCol=valores[aleatorio.nextInt(3)];
  
                    
                    /*Existe el caso en que los aleatorios sean 0,0, por lo que
                    vamos a reasginar los valores, para que no se quede quieto.
                    Además, evaluamos los casos en los que pueda chocar contra la pared
                    y de igual manera reasignamos*/
                    while(dirRow==0 && dirCol==0 ||
                            (i+dirRow)>matrix.length-1 || (j+dirCol)>matrix.length-1
                        || (i+dirRow)<0 || (j+dirCol)<0){
                        Random otro = new Random(System.currentTimeMillis());
                        dirRow = valores[otro.nextInt(3)]; 
                        dirCol = valores[otro.nextInt(3)];
                        System.out.println("SE QUEDA "+dirRow+": "+dirCol);
                    }
                   /* while( ){
                        dirRow=valores[aleatorio.nextInt(3)]; 
                        dirCol=valores[aleatorio.nextInt(3)];
                        System.out.println("ENTRA");
                    }*/
                    
                }
            }catch(Exception e){
                //System.out.println("EN el error las cooredanasa son (i= "+(i+dirRow)+", j="+(j+dirCol));
               // System.out.println("EXCEPCION "+e.getMessage());
            }
           
            /*Actualizamos la posición*/
            i=i+dirRow;
            j=j+dirCol;

            //llama la función de actualizar
            actualizando(500);//500 son los milisegundos para pausar el hilo
            
            /*Cuando agarra un zapato*/
            try{
                if(matrix[i][j]==2){//evaluamos si es un zapato (ya que asignamos en matrix 2 cuando es zapato)
                    llevaZapato = true;
                    //Reasignamos en 0, ya que no debe ser un zapato una vez agarrado
                    matrix[i][j]=0;
                    System.out.println("Lleva zapatito");
                    System.out.println("X: "+xNave+" ,Y: "+yNave);
                }
            }catch(Exception e){
                
            }
            if(llevaZapato){ 
                /*Mientras la posiciones sean distintas de la posicion de la nave, entra al bucle*/
                while(i+dirRow!=xNave && j+dirCol!=yNave && llevaZapato){
                    /**/
                    System.out.println("LLevando zapato...");
                   
                    /*De nuevo, borramos la posición anterior del agente*/
                    borrando(); 
                    
                    /**Esto es para que se vaya moviendo en la nave**/
                    /*Si la posicion en i + lo que vaya avanzar, es menor que la nave
                    entonces hacemos que vaya hacia la nave en i, sumando de 1 en 1*/
                    if(i+dirRow<xNave){//pos en y si y + 1->
                        dirRow=1; 
                    }else if(i+dirRow>xNave){ //sino, es porque es menor y en lugar de sumar, resta
                        dirRow=-1; 
                    }
                    /*Si la posicion en j + lo que vaya avanzar, es menor que la nave
                    entonces hacemos que vaya hacia la nave en j, sumando de 1 en 1*/
                    if(j+dirCol<yNave){ 
                       dirCol=1; 
                    }else if(j+dirCol>yNave){ //sino es porque es menor y hacemos que vaya en -1
                        dirCol=-1; 
                    }
                    /*Si la posicion actual es igual a la de la nave, hacemos que siga en esa direccion*/
                    if(j==yNave){
                        dirCol=0; 
                    }
                    if(i==xNave){
                        dirRow=1; 
                    }
                    
                    /*De nuevo validamos los casos en que haya bombas, sin embargo
                    ahora haremos que cuando ya tenga un zapato, los demás se comporten
                    como bombas*/
                    try{ 
                        /*Mientras donde el agente vaya a ir no sea zapato o bomba, entonces reasignamos*/
                        while( (matrix[i+dirRow][j+dirCol]==1 || matrix[i+dirRow][j+dirCol]==2) ){

                            /*Asignamos valores aleatorios*/
                            dirRow=valores[aleatorio.nextInt(3)]; 
                            dirCol=valores[aleatorio.nextInt(3)];
                            /*De neuvo, si nos pasamos de las paredes, o si ambas direcciones
                            a donde vaya a ir sean 0, reasignamos los valores con aleatorios*/
                            while(dirRow==0 && dirCol==0 ||
                                (i+dirRow)>matrix.length-1 || (j+dirCol)>matrix.length-1
                                || (i+dirRow)<0 || (j+dirCol)<0){
                                
                                Random otro = new Random(System.identityHashCode("dssda"));
                                dirRow = valores[otro.nextInt(3)]; 
                                dirCol = valores[otro.nextInt(3)];
                                /*Como hay ocasiones en las que se bugea debido a algo raro (generando los
                                mosmos aleatorios), 
                                ponemos  esto*/
                                if(dirRow==0 && dirCol==0){
                                    dirCol = valores[otro.nextInt(2)];
                                    dirRow = valores[otro.nextInt(2)]; 
                                }
                                if(i+dirRow==-1){
                                    dirCol = valores[otro.nextInt(3)];
                                    dirRow = valores[otro.nextInt(2)+1]; 
                                }
                                if(j+dirCol==-1){
                                    dirCol = valores[otro.nextInt(2)+1];
                                    dirRow = valores[otro.nextInt(3)]; 
                                }
                                /*if(i+dirRow==matrix.length-1){
                                    dirCol = valores[otro.nextInt(3)];
                                    dirRow = valores[otro.nextInt(2)+1]; 
                                }
                                if(j+dirCol==-1){
                                    dirCol = valores[otro.nextInt(2)+1];
                                    dirRow = valores[otro.nextInt(3)]; 
                                }*/
                                
                                System.out.println("SE QUEDA 2 "+dirRow+": "+dirCol);
                            }
                        }
                    }catch(Exception e){
                        
                    }
                    /*Obtenemos las distancias de nuestra futura posicon, con respecto a la nave*/
                    int d1 = (xNave- (i+dirRow) ); 
                    int d2 = (yNave- (j+dirCol) );
                    
                    /*Obtenemos la distancia euclidiana*/
                    double distancia = Math.sqrt( Math.pow(d1, 2) + Math.pow(d2, 2) ); 
                    /*En caso de que la distancia sea 0, quiere decir que ya llego
                    si es 1, quiere decir que en 1 paso llegará*/
                    if( distancia==1.0 ||   distancia == 0.0){
                        /*Lo ponemos en texto en label*/
                        txt.setText("Llevando zapato a distancia "+(distancia));
                        txt.setForeground(Color.GREEN);
                        //txt.setBorder(Border);
                        System.out.println("LLEGAAAAAA "+distancia);
                        /*Reasignamos la posicion del aghente*/
                        i=i+dirRow;
                        j=j+dirCol;
                        /*Actualizamos la posición*/
                        actualizando(1000);
                        /*Borramos la posición anterior*/
                        borrando(); 
                        
                        /*COmo ya estaba prácticamente en la nave, solo asignamos a los agentes
                        la posición de la nave*/
                        i = xNave;
                        j = yNave; 
                        /*Actualizamos la posición*/
                        actualizando(3000); 
                        /*rompemos el ciclo*/
                        break; 
                    }else{
                        /*Si no está cerca de la nave, solo actualizamos la posición*/
                        i=i+dirRow;
                        j=j+dirCol;
                        actualizando(1000); 
                        int dis1 = (xNave- i);
                        int dis2 = (yNave- j); 
                        /*Aquí solo ponemos la distancia que falta, en el label*/
                        double txtD = Math.sqrt( Math.pow(dis1, 2) + Math.pow(dis2, 2) ); 
                        txt.setText("Llevando zapato a distancia "+(txtD));
                        txt.setForeground(Color.RED);
                    }
                        
                    
                }
                /*Esto es para cuando ya llego*/
                if(i==xNave && j==yNave){
                    /*Actualizamos el texto*/
                    System.out.println("LLEGA A LA NAVE");
                    txt.setText("Zapato llevado!");
                    
                    /*Aquí solo damos un tiempo de espera en lo que acomoda los zapatos
                    en la nave*/
                    try{
                           sleep(100+aleatorio.nextInt(1000));
                    }catch (InterruptedException ex){
                            ex.printStackTrace();
                    }
                    //actualizarPosicion();
                    llevaZapato= false; 
                }
            }
            
            
        }

                      
    }
    
    /*Este métdo actualiza la posición del agente*/
    public synchronized void actualizarPosicion(){
        
        try{
            casillaAnterior.setIcon(null); // Elimina su figura de la casilla anterior
            tablero[i][j].setIcon(icon); // Pone su figura en la nueva casilla
        }catch(Exception e){
            
        }
        
        //System.out.println(nombre + " in -> Row: " + i + " Col:"+ j);              
    }
    
    /*Este método asigna en nuestra matriz "matrix" de enteros, el tipo de objeto que hay
    en las casillas, de manera que podemos saberlo con las posiciones ahora*/
    public void asignarObjetos(){
        for (int k = 0; k < matrix.length; k++) {//recorre la matriz del tamaño del tablero
            for (int l = 0; l < matrix.length; l++) {
                if(tablero[k][l].getName()!=null){ // valida si tiene un nombre el jLabel[k][l]
                    if(tablero[k][l].getName().equalsIgnoreCase("bomba")){ // si el nombre es bomba
                        matrix[k][l]=1; //asignamos un 1 a la bomba
                    }else if(tablero[k][l].getName().equalsIgnoreCase("zapato")){//si el nombre es zapato
                        matrix[k][l] = 2; //asginamos un 2 a los zapatos
                    }else if(tablero[k][l].getName().equalsIgnoreCase("nave")){//si el nombre es nave
                        matrix[k][l] = 3; //asignamos un 3 a la nave
                        xNave = k;  //asignamos las posiciones de la nave
                        yNave = l; 
                    }
                }
                
            }
        }
    }
    /*Asigna los muñecos cerca de la nave*/
    public void asignarMuñecos(){
         boolean rompe = false; //bandera para saber cuando ponerlos 
        for (int k = 0; k < matrix.length; k++) {//iteramos la matriz
            for (int l = 0; l < matrix.length; l++) {
                if(matrix[k][l]==3){ // si la matriz con k,l es 3, significa que encontro una nave
                    System.out.println("Encuentra nave");
                    tablero[k][l].setIcon(icon);//pone el agente en esa posicion
                    
                    this.i=k; //asigna y a k
                    this.j=l; // asigna j a l
                    
                    rompe = true; //significa que ya los asigno
                    break;  // rompe este bucle
                    
                }
                if (rompe) break; // si ya los asigno, rompe todo el bucle
            }
        }
        agente2.i=this.i+1; //para que no aparezcan juntos
    }
    
    /*Esta funciuon recibe un entero, entre mas grande mas tiempo de espera
    Lo que hace es actualizar la posicion del agente y además la de la nave, para
    que quede fija, luego duerme el programa con base en el entero*/
    public void actualizando(int unAleatorio){
        actualizarPosicion();
        tablero[xNave][yNave].setIcon(nave);    
         try{
            sleep(100+aleatorio.nextInt(unAleatorio));
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
    
    /*Borra el agente de la casilla anterior, para que no se queden ahí*/
    public void borrando(){
        try{
            casillaAnterior = tablero[i][j];
        }catch(Exception e){
                
        }
    }
    
    /*Esta función aun no la ocupamos*/
    public void operaciones(int dirRow, int dirCol, int[] valores, boolean... banderas){
        boolean pasa = true;
 
        for (int k = 0; k < banderas.length; k++) {
            pasa = pasa & banderas[i];
        }
        System.out.println("el valor esss : "+pasa);
        /*try{
            while( (matrix[i+dirRow][j+dirCol]==1 || matrix[i+dirRow][j+dirCol]==2) 
                    && i+dirRow<matrix.length && j+dirCol<matrix.length
                    && i+dirRow>=0 && j+dirCol>=0){
            dirRow=valores[aleatorio.nextInt(3)]; 
            dirCol=valores[aleatorio.nextInt(3)];
            while(dirRow==0 && dirCol==0){
                Random otro = new Random(System.identityHashCode("dssda"));
                dirRow = valores[otro.nextInt(3)]; 
                dirCol = valores[otro.nextInt(2)];
                 System.out.println("SE QUEDA 2"+dirRow+": "+dirCol);
                 }
             }
        }catch(Exception e){
            System.out.println("Entra en la excep");
        }*/
    }
    
}




//por si acaso
   /*if(dirRow==0 && dirCol==0){
                        if(matrix[i+1][j]==1){
                            dirRow=-1; 
                        }else if(matrix[i-1][j]==1){
                            dirRow=1;
                        }
                        if(matrix[i][j+1]==1){
                            dirCol=-1; 
                        }else{
                            dirCol=1;
                        }
                    }*/


 /* public boolean evitarObstaculos(){
        boolean pasa = false; 
        try{
           
            for(int k=0;k<2;k++){
                for(int l=0;l<2;l++){
                    //para valores positivos 
                    if(tablero[i+k][j+l].getName()!=null){
                        if(tablero[i+k][j+l].getName().equalsIgnoreCase("bomba")){
                            System.out.println("Aquí chocaría derecha");
                            matrix[i+k][k+l] = 1; 
                        }
                        //System.out.println("("+k+","+l+")");
                    }
                    //para valores negativos
                    if(tablero[i-k][j-l].getName()!=null){
                        if(tablero[i-k][j-l].getName().equalsIgnoreCase("bomba")){
                            System.out.println("Aquí chocaría izquierda");
                            matrix[i+k][k+l] = 1; 
                        }
                        //System.out.println("(-"+k+",-"+l+")");
                    }
                  
                }
            }
            if(tablero[i-1][j+1].getName()!=null){
                if(tablero[i-1][j+1].getName().equalsIgnoreCase("bomba")){
                    System.out.println("Aquí chocaría derecha ");
                     matrix[i-1][j+1] = 1; 
                }
                        
            }
             if(tablero[i+1][j-1].getName()!=null){
                if(tablero[i+1][j-1].getName().equalsIgnoreCase("bomba")){
                    System.out.println("Aquí chocaría izquierda a");
                     matrix[i+1][j-1] = 1; 
                }
                        
            }
          
            
     
        }catch(Exception e){
            System.out.println("Excepcion "+e.getMessage()+ " Entonces no choca, está al borde");
            
        }
        
        return pasa; 
    }*/
    