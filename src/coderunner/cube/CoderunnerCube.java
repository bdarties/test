/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coderunner.cube;

import java.util.Random;

/**
 *
 * @author benoit
 */
class Piece {

    int nombre;
    boolean viable;
    boolean arrivee;
    static Random random = new Random();  // generateur aléatoire  

    public Piece() {
        viable = false;
        arrivee = false;
        // nombre aléatoire qui n'est pas premier
        nombre = (10+ random.nextInt(200)) * (1+random.nextInt(200));

    }
}

class Labyrinthe {
// conventions : 
    // x : ouest  / est
    // y : plafond, sol
    // z : nord / sud

    final static int dimX = 10;
    final static int dimY = 10;
    final static int dimZ = 10;
    public static int C_x; // coord de la current room
    public static int C_y; //
    public static int C_z; //

    int tailleChemin=0;
    Piece currentRoom;
    final static int[] primesNumber = {1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499, 1511, 1523, 1531, 1543, 1549, 1553, 1559, 1567, 1571, 1579, 1583, 1597, 1601, 1607, 1609, 1613, 1619, 1621, 1627, 1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699, 1709, 1721, 1723, 1733, 1741, 1747, 1753, 1759, 1777, 1783, 1787, 1789, 1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873, 1877, 1879, 1889, 1901, 1907, 1913, 1931, 1933, 1949, 1951, 1973, 1979, 1987, 1993, 1997, 1999, 2003, 2011, 2017, 2027, 2029, 2039, 2053, 2063, 2069, 2081, 2083, 2087, 2089, 2099, 2111, 2113, 2129, 2131, 2137, 2141, 2143, 2153, 2161, 2179, 2203, 2207, 2213, 2221, 2237, 2239, 2243, 2251, 2267, 2269, 2273, 2281, 2287, 2293, 2297, 2309, 2311, 2333, 2339, 2341, 2347, 2351, 2357, 2371, 2377, 2381, 2383, 2389, 2393, 2399, 2411, 2417, 2423, 2437, 2441, 2447, 2459, 2467, 2473, 2477, 2503, 2521, 2531, 2539, 2543, 2549, 2551, 2557, 2579, 2591, 2593, 2609, 2617, 2621, 2633, 2647, 2657, 2659, 2663, 2671, 2677, 2683, 2687, 2689, 2693, 2699, 2707, 2711, 2713, 2719, 2729, 2731, 19319, 19333, 19373, 19379, 19381, 19387, 19391, 19403, 19417, 19421, 19423, 19427, 19429, 19433, 19441, 19447, 19457, 19463, 19469, 19471, 19477, 19483, 19489, 19501, 19507, 19531, 19541, 19543, 19553, 19559, 19571, 19577, 19583, 19597, 19603, 19609, 19661, 19681, 19687, 19697, 19699, 19709, 19717, 19727, 19739, 19751, 19753, 19759, 19763, 19777, 19793, 19801, 19813, 19819, 19841, 19843, 19853, 19861, 19867, 19889, 19891, 19913, 19919, 19927, 19937, 19949, 19961, 19963, 19973, 19979, 19991, 19993, 19997, 20011, 20021, 20023, 20029, 20047, 20051, 20063, 20071, 20089, 20101, 20107, 20113, 20117, 20123, 20129, 20143, 20147, 20149, 20161, 20173, 20177, 20183, 20201, 20219, 20231, 20233, 20249, 20261, 20269, 20287, 20297, 20323, 20327, 20333, 20341, 20347, 20353, 20357, 20359, 20369, 20389, 20393, 20399, 20407, 20411, 20431, 20441, 20443, 20477, 20479, 20483, 20507, 20509, 20521, 20533, 20543, 20549, 20551, 20563, 20593, 20599, 20611, 20627, 20639, 20641, 20663, 20681, 20693, 20707, 20717, 20719, 20731, 20743, 20747, 20749, 20753, 20759, 20771, 20773, 20789, 20807, 20809, 20849, 20857, 20873, 20879, 20887, 20897, 20899, 20903, 20921, 20929, 20939, 20947, 20959, 20963, 20981, 20983, 21001, 21011, 21013, 21017, 21019, 21023, 21031, 21059, 21061, 21067, 21089, 21101, 21107, 21121, 21139, 21143, 21149, 21157, 21163, 21169, 21179, 21187, 21191, 21193, 21211, 21221, 21227, 21247, 21269};
    Piece[][][] pieces = new Piece[dimX][dimY][dimZ];

    public Labyrinthe() {
        for (int i = 0; i < dimX * dimY * dimZ; i++) {
            C_x = i % dimX;
            C_y = (i / dimX) % dimY;
            C_z = (i / (dimY * dimX));
            pieces[C_x][C_y][C_z] = new Piece();

        }
    }

    int nbPiecesVoisinesViables(int px, int py, int pz) {
        // retourne le nombre de pieces voisines viables 
        if (px >= 10 || px < 0 || py >= 10 || py < 0 || pz >= 10 || pz < 0) {
            return -1;
        }
        // System.out.print("pieces voisines viables de : " + px + " " + py + " " + pz + ": ");

        int cpt = 0;
        if (px < dimX - 1 && pieces[px + 1][py][pz].viable) {
            cpt++;
        }
        if (0 < px && pieces[px - 1][py][pz].viable) {
            cpt++;
        }
        if (py < dimY - 1 && pieces[px][py + 1][pz].viable) {
            cpt++;
        }
        if (0 < py && pieces[px][py - 1][pz].viable) {
            cpt++;
        }
        if (pz < dimZ - 1 && pieces[px][pz][pz + 1].viable) {
            cpt++;
        }
        if (0 < pz && pieces[px][py][pz - 1].viable) {
            cpt++;
        }
        // System.out.println(" " + cpt);
        return cpt;
    }

    int nbVoisinsEligibles(int px, int py, int pz) {
        // retourne le nombre de pieces voisines viables 
        int cpt = 0;
        if (px < dimX - 2 && nbPiecesVoisinesViables(px + 1, py, pz) == 1) {
            cpt++;
        }
        if (1 < px && nbPiecesVoisinesViables(px - 1, py, pz) == 1) {
            cpt++;
        }
        if (py < dimY - 2 && nbPiecesVoisinesViables(px, py + 1, pz) == 1) {
            cpt++;
        }
        if (1 < py && nbPiecesVoisinesViables(px, py - 1, pz) == 1) {
            cpt++;
        }
        if (pz < dimZ - 2 && nbPiecesVoisinesViables(px, pz, pz + 1) == 1) {
            cpt++;
        }
        if (1 < pz && nbPiecesVoisinesViables(px, py, pz - 1) == 1) {
            cpt++;
        }
        return cpt;
    }

    public int lireNombreAssocieADirection(int x, int y, int z, String acces) {
        // conventions : 
        // x : ouest  / est
        // y : plafond, sol
        // z : nord / sud
        switch (acces) {
            case "nord":
            case "Nord":
                return pieces[x][y][z + 1].nombre;
            case "sud":
            case "Sud":
                return pieces[x][y][z - 1].nombre;
            case "est":
            case "Est":
                return pieces[x + 1][y][z].nombre;
            case "ouest":
            case "Ouest":
                return pieces[x - 1][y][z].nombre;
            case "sol":
            case "Sol":
                return pieces[x][y + 1][z].nombre;
            case "plafond":
            case "Plafond":
                return pieces[x][y - 1][z].nombre;
            default:
                return -1;

        }

    }

    void creerLabyrinthe() {

        // je mets toutes les faces en tant qu'impossible.
        // je choisis un point de départ sur le coté
        // tant que je n'ai pas atteint la dimension du chemin max ou que le nb de pieces voisines transformable est > 1
        // 
        Random random = new Random();  // generateur aléatoire  

        int x = random.nextInt(dimX - 3) + 1;
        int y = random.nextInt(dimY - 3) + 1;
        int z = random.nextInt(dimZ - 3) + 1;
        currentRoom = pieces[x][y][z];

        tailleChemin = 0;
        boolean impossible = false;
        // init 
        currentRoom.viable = true;
        currentRoom.arrivee = true;

        currentRoom.nombre = primesNumber[random.nextInt(primesNumber.length)];

        System.out.println("case : " + x + " " + y + " " + z);
        while (tailleChemin < 10 && nbVoisinsEligibles(x, y, z) > 0) {

            int direction;
            int x2 = x, y2 = y, z2 = z;
            do {
                x2 = x;
                y2 = y;
                z2 = z;
                direction = random.nextInt(6);
                switch (direction) {
                    case 0:
                        x2++;
                        break;
                    case 1:
                        x2--;
                        break;
                    case 2:
                        y2++;
                        break;
                    case 3:
                        y2--;
                        break;
                    case 4:
                        z2++;
                        break;
                    case 5:
                        z2--;
                        break;
                    default:
                        System.out.println("erreur trop grand");
                }
            } while (nbPiecesVoisinesViables(x2, y2, z2) != 1 || pieces[x2][y2][z2].viable == true || x2 == 0 || x2 == 9 || y2 == 0 || y2 == 9 || z2 == 0 || z2 == 9);
            pieces[x2][y2][z2].viable = true;
            pieces[x2][y2][z2].nombre = primesNumber[random.nextInt(primesNumber.length)];
            System.out.println("pieces ajoutée :  " + x2 + " " + y2 + " " + z2 + ": " + pieces[x2][y2][z2].nombre);
            x = x2;
            y = y2;
            z = z2;

            tailleChemin++;

        }
        currentRoom = pieces[x][y][z];
        System.out.println("taille chemin : " + tailleChemin);
        C_x = x;
        C_y = y;
        C_z = z;
    }

    boolean traverserAcces(String direction) {
        System.out.println("ancienne piece : " + C_x + " " + C_y + " " + C_z);

        switch (direction) {
            case "nord":
            case "Nord":
            case "NORD" :
                C_z++;
                break;
            case "sud":
            case "Sud":
            case "SUD" :
                C_z--;
                break;
            case "est":
            case "Est":
            case "EST" : 
                C_x++;
                break;
            case "ouest":
            case "Ouest":
            case "OUEST" : 
                C_x--;
                break;
            case "sol":
            case "Sol":
            case "SOL":
                C_y++;
                 break;
            case "plafond":
            case "Plafond":
            case "PLAFOND" :
                C_y--;
        }
      currentRoom.nombre = currentRoom.nombre*2; 
       // currentRoom.nombre = -1; 
        System.out.println("nouvelel piece : " + C_x + " " + C_y + " " + C_z);
        currentRoom = pieces[C_x][C_y][C_z];
        return currentRoom.viable;
    }
}

class currentPiece {

    public Labyrinthe lab;

    public boolean vivant = true;

    boolean etreArrivee() {
        return lab.currentRoom.arrivee;
    }

    //  Piece pieceCourante;
    void currentPiece() {
        // pieceCourante = lab.currentRoom;
    }

    public int lireNombreAssocieAAcces(String access) {

        return lab.lireNombreAssocieADirection(lab.C_x, lab.C_y, lab.C_z, access);
    }

    public boolean traverserAcces(String access) {

        if (vivant == false) {
            System.out.println("oublie t'es  mort");
            return false;
        }
        
        
        
        boolean result = lab.traverserAcces(access);
        if (result == false) {
            System.out.println("vous etes mort");
            vivant = false;
        } else {
            CoderunnerCube.nbAtteints++;
//pieceCourante = lab.currentRoom;
        }
        return result;
    }

}

public class CoderunnerCube {

    final static int tailleChemin = 10;
    static int nbAtteints = 0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Labyrinthe l = new Labyrinthe();
        do {
            l.creerLabyrinthe();
        } while (l.tailleChemin != 10);

        
        currentPiece pieceCourante = new currentPiece();
        pieceCourante.lab = l;

        ///
        String[] directions = {"Nord", "Est","Sol", "Ouest", "plafond", "Sud"};

        while (pieceCourante.etreArrivee() == false) {
            for (int a = 0; a < 6; a++) {

                int n = pieceCourante.lireNombreAssocieAAcces(directions[a]);
                System.out.println("direction : " + directions[a] + " : " + n);

                int i = 2;
                while (i < Math.sqrt(n) && n % i != 0) {
                    i++;
                }

                if (i >= Math.sqrt(n)) {
                    System.out.println("on traverse la direction : " + directions[a]);
                    pieceCourante.traverserAcces(directions[a]);
                   // break;
                }
            }
        }
        System.out.println("vous avez atteint  :" + nbAtteints);
    }

}
