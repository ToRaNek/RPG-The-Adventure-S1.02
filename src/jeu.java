import extensions.CSVFile;
class jeu extends Program{
     // le NEW_LINE Permet de faire à la ligne
    final char NEW_LINE='\n';

    String celebration(){
        return  "                                                  BRAVO VOUS AVEZ COMPLETEZ RPG THE ADVANTURE !" + NEW_LINE +
                "                                                  NOUS ESPERONS QUE VOTRE EXPERIENCE DE JEU FU" + NEW_LINE +
                "                                                       AUSSI PLAISANTE QUE POSSIBLE";
    }

    // Cette fonction permet de rappeler les touches de deplacement dans le terminal
    String rappelle(){
        String res="";
        res=res+"RAPPELLE:    ╭ ———————————————————————————————————————— ╮";
        res=res+ NEW_LINE +"             |                                          |";
        res=res+ NEW_LINE +"             |   ⤆ H: en haut d'un block⤇              |";
        res=res+ NEW_LINE +"             |   ⤆ B: en bas d'un block⤇               |";
        res=res+ NEW_LINE +"             |   ⤆ G: à gauche d'un block⤇             |";
        res=res+ NEW_LINE +"             |   ⤆ D: à droite d'un block⤇             |";
        res=res+ NEW_LINE +"             |   ⤆ S: pour annuller le déplacement⤇    |";
        res=res+ NEW_LINE +"             |                                          |";
        res=res+ NEW_LINE +"             ╰ ———————————————————————————————————————— ╯";
        return res;
    }

    // Cette fonction permet de rappeler les actions possible au joueurs    
    void listAction(){
        println("RAPPELLE:    ╭ —————————————————————————————————————————— ╮" + NEW_LINE +
                "             |                                            |" + NEW_LINE +
                "             |   ⤆REGARDER AUTOUR DE SOI: Tapez \"R\"⤇   |" + NEW_LINE +
                "             |   ⤆SE DEPLACER: Tapez \"D\"⤇              |" + NEW_LINE +
                "             |   ⤆ACTION: Tapez \"A\"⤇                   |" + NEW_LINE +
                "             |   ⤆SAUVEGARDER: Tapez \"S\"⤇              |" + NEW_LINE +
                "             |   ⤆QUITTER: Tapez \"Q\"⤇                  |" + NEW_LINE +
                "             |                                            |" + NEW_LINE +
                "             ╰ —————————————————————————————————————————— ╯");
    }

    // Recherche une valeur dans un fichier suivant une syntaxe précise
    String rechercherValeur (String file, String valeur){
        String result="";
        boolean trouve=false;
        int cpt=0;
        int debut=0;
        int indice=0;
        while (!trouve && cpt<length(file)){
            if (equals(substring(file, cpt, cpt+length(valeur)), valeur)){
                while (charAt(file, cpt+indice)!='!' && charAt(file, cpt+indice)!='\n' && charAt(file, cpt+indice)!='.'){
                    debut=cpt+length(valeur);
                    indice++;
                    trouve=true;
                }
            } else {
                cpt++;
            }
            result=substring(file, debut, indice+cpt);
        }
        return result;
    }

    // test rechercherValeur dans les deux cas utiliser dans ce code; le nom du joeur pour listjoueur, et les informations dans les sauvegardes
    void testrechercherValeur(){
        assertEquals("Grande Plaine", rechercherValeur("F nom: Grande Plaine! intro: introduction", "nom: "));
        assertEquals("Milan", rechercherValeur("Player-Milan.csv", "Player-"));
    }

    //Permet d'initialiser la Map
    void creerMap(Block[][] TrueMap){
        CSVFile csv=loadCSV("Save/ressource-Block/RessourceMap.csv");
        int block=1;
        for (int l=0, cpt=0;l<length(TrueMap,1);l++){
            for (int c=0;c<length(TrueMap,2);c++){
                TrueMap[l][c] = new Block();
                if (l==0 && c==0){
                    TrueMap[l][c].decouvert=true;
                } else {
                    TrueMap[l][c].decouvert=false;
                }
                TrueMap[l][c].nom=getCell(csv, block, cpt+1); // va chercher dans le fichier de ressource les informations de chaques block
                TrueMap[l][c].intro=getCell(csv, block, cpt+2);
                TrueMap[l][c].quete=getCell(csv, block, cpt+3);
                TrueMap[l][c].QueFaire=getCell(csv, block, cpt+4);
                TrueMap[l][c].objectif=stringToInt(getCell(csv, block, cpt+5));
                block++;
            }
        }
    }

    //permet d'afficher la map au joueur
    void toStringMapDecouverte(Block[][] TrueMap, Player player){
        for (int l=0;l<length(TrueMap,1);l++){
            for (int c=0;c<length(TrueMap,2);c++){
                if (l==player.idcLignes && c==player.idcColonnes){ // vérifie où est le joueur 
                    print(player.nom + ", ");
                } else if (TrueMap[l][c].decouvert==true){
                    print(TrueMap[l][c].nom + ", ");
                } else if (TrueMap[l][c].decouvert==false){
                    print("?????, ");
                }
            }
            println(); 
        }
    }
    
    //Permet de creer un nouveau joueur
    Player newPlayer(String nom){
        Player p = new Player();
        p.nom = nom;
        p.argent = 0;
        p.pv = 10;
        p.idcColonnes=0;
        p.idcLignes=0;
        return p;
    }

    //Permet de transmettre les info du joueurs dans le terminal
    String toStringPlayer(Player p){
        return
        "Nom: " + p.nom + NEW_LINE +
        "PV: " + p.pv + ANSI_RED + "❤️"+ ANSI_WHITE + NEW_LINE +
        "Argent: " +p.argent + ANSI_GREEN +"$"+ ANSI_WHITE + NEW_LINE;
    }

    //Permet de recuperer les info d'un joueurs stocké dans son fichier.csv
    Player ChargerJoueur(String NomJoueur){
        CSVFile fichier=loadCSV("Save/Player/Player-"+NomJoueur+".csv");
        Player player=new Player();
        player.nom=getCell(fichier, 1,0);
        player.pv=stringToInt(getCell(fichier, 1, 1));
        player.argent=stringToInt(getCell(fichier, 1, 2));
        player.idcLignes=stringToInt(getCell(fichier, 1, 3));
        player.idcColonnes=stringToInt(getCell(fichier, 1, 4));
        return player;
    }

    //Permet de recuperer les detail des zones/quetes via ressource.csv
    String[][] ChargerQuete(){
        CSVFile csv=loadCSV("Save/ressource-Block/Ressource.csv");
        String[][] tab=new String[rowCount(csv)][columnCount(csv)];
        tab=ress(csv);
        return tab;
    }

    //Permet de convertir un csv en tableau de String, différent de toStringMap car pas la même utilité dans le code
    String[][] ress(CSVFile csv){
        String[][] tab=new String[rowCount(csv)][columnCount(csv)];
        for (int l=1;l<rowCount(csv);l++){
            for (int c=0; c<columnCount(csv);c++){
                tab[l][c]=new String();
                tab[l][c]=getCell(csv, l, c);
                tab[l][c]=getCell(csv, l, c);
                tab[l][c]=getCell(csv, l, c);
                tab[l][c]=getCell(csv, l, c);
                tab[l][c]=getCell(csv, l, c);
            }
        }
        return tab;
    }
  
    //Permet de charger un la save d'une map d'un joueur. quand il fait une nouvelle partie
    Block[][] ChargerMap(String NomJoueur){
        CSVFile csv=loadCSV("Save/Block/Block-"+NomJoueur+".csv");
        Block[][] tab=new Block[rowCount(csv)][columnCount(csv)];
        tab=TraduireMap(csv);
        return tab;
    }

    //Permet de trouver les info d'une zone, et le mettre dans une map BLOCK
    Block[][] TraduireMap(CSVFile csv){
        Block[][] TrueMap=new Block[rowCount(csv)][columnCount(csv)];
        for (int cpt=0;cpt<rowCount(csv); cpt++){
            for (int idx=0;idx<columnCount(csv); idx++){
                TrueMap[cpt][idx] = new Block();
                println(getCell(csv, cpt, idx));
                if (charAt(getCell(csv, cpt, idx),0)=='T'){
                    TrueMap[cpt][idx].decouvert=true;
                } else {
                    TrueMap[cpt][idx].decouvert=false;
                }
                TrueMap[cpt][idx].nom=rechercherValeur(getCell(csv, cpt, idx), "nom: "); // recherche les valeurs celon une syntaxe précise et les mets dans la carte du joueur 
                TrueMap[cpt][idx].intro=rechercherValeur(getCell(csv, cpt, idx), "intro: ");
                TrueMap[cpt][idx].quete=rechercherValeur(getCell(csv, cpt, idx), "Quete: ");
                TrueMap[cpt][idx].QueFaire=rechercherValeur(getCell(csv, cpt, idx), "QueFaire: ");
                TrueMap[cpt][idx].objectif=stringToInt(rechercherValeur(getCell(csv, cpt, idx), "objectif: "));
            }
        }
        return TrueMap;
    }

    //Permet de Sauvegarder la map d'un Joueur dans un csv
    void sauvegarderMap(Block[][] map, String NomJoueur){
        String[][] csv=new String[length(map,1)][length(map,2)];
        for (int cpt=0;cpt<length(csv,1); cpt++){
            for (int idx=0;idx<length(csv,2); idx++){
                if (map[cpt][idx].decouvert==true){// regarde si la map est découverte ou pas avec le premier charactère
                    csv[cpt][idx]="T "; 
                } else {
                    csv[cpt][idx]="F ";
                }
                String res="" + map[cpt][idx].objectif;
                csv[cpt][idx]=csv[cpt][idx] + "nom: " + map[cpt][idx].nom + "! intro: " + map[cpt][idx].intro + "! Quete: " + map[cpt][idx].quete + "! QueFaire: " + map[cpt][idx].QueFaire + "! objectif: " +res + "!";
            }
        }
        saveCSV(csv, "Save/Block/Block-"+NomJoueur+".csv");
    }

    //Permet de Sauvegarder un Joueur dans un fichier csv
    void sauvegarderJoueur(Player player){
        String[][] content=new String [2][5];
        String argent=""+player.argent; // sauvegarde tout les int en String
        String pv=""+player.pv;
        String idcLignes=""+player.idcLignes;
        String idcColonnes=""+player.idcColonnes;
        content[0][0] = "nom";
        content[0][1] = "argent";
        content[0][2] = "pv";
        content[0][3] = "idcLignes";
        content[0][4] = "idcColonnes";
        content[1][0] = player.nom;
        content[1][1] = argent;
        content[1][2] = pv;
        content[1][3] = idcLignes;
        content[1][4] = idcColonnes;

        saveCSV(content, "Save/Player/Player-"+player.nom+".csv");
    }

    //Permet de se deplacer dans la map grace à des caractères
    void deplacement (Block [][] TrueMap, String[][] map, String NomJoueur, Player p){
        clearScreen();
        toStringMapDecouverte(TrueMap, p);
        println();
        println();
        println(rappelle());
        println();
        boolean valide=false;
        String impossible="Déplacement impossible";
        do {
            println("Ou voulez vous allez,"); // verifie simplement quel déplacement veux faire et le bouge en fonction
            String deplacement=readString();
            if (equals(deplacement, "H")){
                if (p.idcLignes-1>-1 || TrueMap[p.idcLignes-1][p.idcColonnes].decouvert==false){
                    println(impossible);
                } else {
                p.idcLignes=p.idcLignes+1;
                valide=true;
                }
            } else if (equals(deplacement, "B")){
                if (p.idcLignes+1>length(TrueMap,1)+1 || TrueMap[p.idcLignes+1][p.idcColonnes].decouvert==false){
                    println(impossible);
                } else {
                p.idcLignes=p.idcLignes+1;
                valide=true;
                }
            } else if (equals(deplacement, "G")){
                if (p.idcColonnes-1>-1 || TrueMap[p.idcLignes][p.idcColonnes-1].decouvert==false){
                    println(impossible);
                } else {
                p.idcColonnes=p.idcColonnes-1;
                valide=true;
                }
            } else if (equals(deplacement, "D")){
                if (p.idcColonnes+1>length(TrueMap,1)+1 || TrueMap[p.idcLignes][p.idcColonnes+1].decouvert==false){
                    println(impossible);
                } else {
                p.idcColonnes=p.idcColonnes+1;
                valide=true;
                }
            } else if (equals(deplacement, "S")){
                valide=true;
            }
            
        } while (!valide);
    }

    //Permet de repertorier les differents joueur sauvegardé
    void listJoueur(){
        String[] tab=getAllFilesFromDirectory("Save/Player/");
        for (int idx=0;idx<length(tab);idx++){
            print(substring(rechercherValeur(tab[idx], "-"),0,length(tab[idx])-11) + ", ");
        }
    }

    //Permet de choisir une question parmis celle disponible
    String actionFaite(Block[][] TrueMap, Player player, boolean REGARDER){
        String action="";
        do {
            print(toStringPlayer(player));
            println();
            toStringMapDecouverte(TrueMap, player);
            println();
            listAction(); // liste les actions possible
            println();
            if (REGARDER){
                println("Vous êtes à: " + TrueMap[player.idcLignes][player.idcColonnes].nom);
                println();
                println(TrueMap[player.idcLignes][player.idcColonnes].intro);
            }
            println();
            println("Choississez votre action");
            action=readString(); // demande au joueur l'action à effectuer 
            if (!equals(action, "S") && !equals(action, "A") && !equals(action, "Q") && !equals(action, "D") && !equals(action, "R")){
                println("Veuillez choisir une action valide !");
                println();
                println();
            }
        } while(!equals(action, "S") && !equals(action, "A") && !equals(action, "Q") && !equals(action, "D") && !equals(action, "R"));
        return action;
    }

    //Fonction utiliser quand le joueur choisi de faire l'action dans une zone
    boolean faireAction(Player player, Block[][] TrueMap, String[][] ressource){
        println();
        boolean reussi=false;
        int cpt=TrueMap[player.idcLignes][player.idcColonnes].objectif;
        int Nbreussi=0;
        int Nbfait=0;
        println("Votre objectif est de "+TrueMap[player.idcLignes][player.idcColonnes].QueFaire+". Pour cela il faudra répondre à ces questions pour réussir !");
        println("Répondez juste à ces "+ TrueMap[player.idcLignes][player.idcColonnes].objectif+" question(s), et votre action sera réussite !");
        println();

        while (Nbfait<cpt || !reussi){ // démmarage de la quête
            reussi=poserQuestion(player, ressource, TrueMap);
            if (reussi){
                Nbreussi++;
                println("Bravo ! Vous avez eu bon à " + Nbreussi + " question(s).");
                Nbfait++;
            } else {
                player.pv=player.pv-1;
                println("OH NON! Vous avez perdu 1pv dû à votre erreur. Il vous reste " + player.pv + "HP.");
                Nbfait++;
                println();
            }
        }
            if (Nbreussi/cpt>=0.6){ // calcul si le joueur à réussi la quete ou pas 
                println("Vous avez réussi à " + TrueMap[player.idcLignes][player.idcColonnes].objectif);
                player.pv=player.pv+2;
                reussi=true;
            } else if (!(Nbreussi/cpt>=0.6)){
                reussi=false;
                println("Vous n'avez pas réussi à " + TrueMap[player.idcLignes][player.idcColonnes].objectif + ". Vous pouvez reassayer plus tard.");
            }
        clearScreen();
        return reussi;
    }

    //poser une question au joueur et renvoi si le joueur à eu ou non bon à la question
    boolean poserQuestion(Player player, String[][] ressource, Block[][] TrueMap){
        println();
        boolean reussi=true;
        double random=random()*35;
        int trueRandom=(int) random; // choisi question aléatoire

        double random2=random()*4;
        int trueRandom2=(int) random2+1; // mélange la Bonne réponse

        println(ressource[trueRandom][0]);
        println();
        if (trueRandom2==1){
            println(ressource[trueRandom][1] + ": 1");
            println(ressource[trueRandom][2] + ": 2");
            println(ressource[trueRandom][3] + ": 3");
            println(ressource[trueRandom][4] + ": 4");
        }
        if (trueRandom2==2){
            println(ressource[trueRandom][2] + ": 1");
            println(ressource[trueRandom][1] + ": 2");
            println(ressource[trueRandom][3] + ": 3");
            println(ressource[trueRandom][4] + ": 4");
        }
        if (trueRandom2==3){
            println(ressource[trueRandom][2] + ": 1");
            println(ressource[trueRandom][3] + ": 2");
            println(ressource[trueRandom][1] + ": 3");
            println(ressource[trueRandom][4] + ": 4");
        }
        if (trueRandom2==4){
            println(ressource[trueRandom][2] + ": 1");
            println(ressource[trueRandom][3] + ": 2");
            println(ressource[trueRandom][4] + ": 3");
            println(ressource[trueRandom][1] + ": 4");
        }

        println();
        int answer=readInt();
        println();

        if (answer==trueRandom2){
            reussi=true;
        } else {
            reussi=false;
        }
        return reussi;
    }

    //verifie si le joueur à compléter toute la map
    boolean verifierGagner(Block [][]TrueMap){
        boolean gagner=true;
        for (int i=0;i<length(TrueMap,1);i++){
            for (int x=0;x<length(TrueMap,2);x++){
                if (TrueMap[i][x].decouvert==false){
                    gagner=false;
                }
            }
        }
        return gagner;
    }

    void testverifierGagner(){//fonction pour tester le verifierGagner
        Block[][] TrueMap=new Block[5][5];
        creerMap(TrueMap);
        for (int i=0;i<length(TrueMap,1);i++){
            for (int x=0;x<length(TrueMap,2);x++){
                TrueMap[i][x].decouvert=true;
            }
        }
        assertTrue(verifierGagner(TrueMap));
    }

    //l'algorithm principale du jeu
    void algorithm(){
        clearScreen();
        Block[][] TrueMap=new Block[5][5]; // initialisation des ressources de base du jeu
        Player player;
        String NomPlayer="";
        String[][] ressource=ChargerQuete();

        println("Seléctionner une sauvegarde: 1"); // Choix de crée ou de reprendre une partie
        println("Nouvelle Sauvegarde: 2");
        println();
        int choix1=readInt();
        println();

        if (choix1==1){
            println("Qui êtes vous?");
            println();
            listJoueur();
            println();
            println();

            NomPlayer=readString();
            player=ChargerJoueur(NomPlayer);
            TrueMap=ChargerMap(NomPlayer);
            clearScreen();

        } else if (choix1==2){
            println("Quelle sera votre nom ?");
            NomPlayer=readString();
            player=newPlayer(NomPlayer);

            sauvegarderJoueur(player);
            creerMap(TrueMap);
            sauvegarderMap(TrueMap, NomPlayer);

            clearScreen();
            println("INTRO");
        }
        player=newPlayer(NomPlayer);
        clearScreen();

        boolean jouer=true;
        boolean REGARDER=false;

        while (jouer){
            println("Voici votre map et vos stats");
            println();
            println(length(TrueMap,2));
            String action=actionFaite(TrueMap, player, REGARDER); // demande au joueur ce qu'il veut faire
            REGARDER=false;
            
            if (equals(action, "A")){

                println();
                boolean reussi=faireAction(player, TrueMap, ressource); // quete de la zone où est le joueur 
                if (reussi){
                    if (player.idcColonnes==length(TrueMap,2)){
                        TrueMap[player.idcLignes+1][player.idcColonnes-5].decouvert=true;
                    } else {
                        TrueMap[player.idcLignes][player.idcColonnes+1].decouvert=true;
                    }
                }

            } else if (equals(action, "D")){ // deplacement du joueur dans la map
                deplacement(TrueMap, ressource, action, player);

            } else if (equals(action, "Q")){ // quite le jeu et sauvegarde
                sauvegarderJoueur(player);
                sauvegarderMap(TrueMap, NomPlayer);
                jouer=false;
                clearScreen();

            } else if (equals(action, "S")){ // sauvegarde le jeu
                println("Sauvegarde effectuer");
                sauvegarderJoueur(player);
                sauvegarderMap(TrueMap, NomPlayer);

            } else if (equals(action, "R")){ // donne l'information: nom de la zone, introduction de la zone
                REGARDER=true;

            }
            if (verifierGagner(TrueMap)){ // celebrate if player won
                celebration();
                jouer=false;
            }
        }
    }
}