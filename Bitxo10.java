package agents;

import static java.lang.Math.sqrt;
import java.util.Random;

//  Nuevo bitxo: cambiar nombre clase, constructor, atributo BITXO y foto
public class Bitxo10 extends Agent {

    static final int PARET = 0;
    static final int BITXO = 10;
    static final int RES = -1;

    static final int ESQUERRA = 0;
    static final int CENTRAL = 1;
    static final int DRETA = 2;

    Estat estat;

    public Bitxo10(Agents pare) {
        super(pare, "Marley", "imatges/marley.gif");
    }

    @Override
    public void avaluaComportament() {
        estat = estatCombat();
        tiempo_partida++;
        cd_disparo--;
        comida = 100 + estat.id;
        regularT();

        try {
            evalua();
        } catch (Exception e) {
        }
    }

    @Override
    public void inicia() {
        // atributsAgents(velocidad lineal, velocidad giro, distancia visores, angulo visores, nDisparos, nEscudos, nHiperespacios)
        int cost = atributsAgent(7, 4, 600, 30, 83, 5, 0);
        System.out.println("Cost total:" + cost);

        // Inicialització de variables que utilitzaré al meu comportament
        tiempo_partida = 0;
        t = 0;
        bloqueo_radar = false;
        cd_choque = -1;
        cd_disparo = 0;
        cd_escudo = 0;
        impactosAntes = 0;
        heridasRivalAntes = 0;
        spamEscudos = false;
        esquivando = false;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    //  Mis atributos
    int t;
    final int MAX_T = 99;
    boolean bloqueo_radar;
    int cd_choque;
    final int DIST_DISPARO = 390;
    final int DIST_CERCA = 130;

    final int TIEMPO_MAX = 825;

    int tiempo_partida;

    int comida;

    final int ENEMIGO = 1;

    int cd_disparo;

    int cd_escudo;

    int impactosAntes;

    int heridasRivalAntes;

    boolean spamEscudos;

    boolean caminar;

    boolean esquivando;

    private void evalua() throws Exception {

        temaEscudos();

        if (!hayChoque()) {

            if (!predecirRuta()) {

                bloqueo_radar = busqueda();
                if (!bloqueo_radar) {

                    if (!activarRadar()) {

                        if (!necesitoGirar()) {

                            movimiento();
                        }
                    }
                }
            }
        }

        endavant();
    }

    private boolean predecirRuta() throws Exception {

        if (estat.objecteVisor[0] == PARET && estat.distanciaVisors[0] < 50) {
            dreta();
            return true;
        }
        if (estat.objecteVisor[2] == PARET && estat.distanciaVisors[2] < 50) {
            esquerra();
            return true;
        }

        return false;
    }

    private boolean temaEscudos() throws Exception {

        if (!spamEscudos) {

            if (estat.impactesRebuts > 0) {
                activaEscut();
                spamEscudos = true;
                return true;
            } else if (estat.llançamentEnemicDetectat && estat.distanciaLlançamentEnemic < 30) {
                spamEscudos = true;
            }
            comprobarEscudoComidaEnemiga();
            fundirseEscudos();
        } else {
            activaEscut();
        }
        return false;
    }

    // Al colisionar habrá que girar 180º, a menos que choquemos con un enemigo frontalmente. En este caso buscaremos acribillarlo.
    private boolean hayChoque() {
        if (estat.enCollisio) {

            if (estat.objecteVisor[ESQUERRA] == ENEMIGO || estat.objecteVisor[CENTRAL] == ENEMIGO || estat.objecteVisor[DRETA] == ENEMIGO) {
                killMode(50);

            } else {
                gira(180);
            }

            return true;
        }
        return false;
    }

    // Dist disparo = 390
    // Dist visores = 600
    private boolean busqueda() throws Exception {

        if (killMode(DIST_CERCA)) {
            t = 0;
            esquivando = true;
            return true;
        }

        if (dispararComidaEnemiga(DIST_DISPARO)) {
            t = 0;
            return true;
        }

        if (buscarRecurso(comida, DIST_CERCA)) {
            esquivando = true;
            t = 0;
            return true;
        }

        if (buscarRecurso(Estat.ESCUT, DIST_CERCA)) {
            esquivando = true;
            t = 0;
            return true;
        }

        if (buscarRecurso(comida, 9999)) {
            esquivando = true;
            t = 0;
            return true;
        }

        if (buscarRecurso(Estat.ESCUT, 9999)) {
            esquivando = true;
            t = 0;
            return true;
        }
        
        if (killMode(9999)) {
            t = 0;
            esquivando = true;
            return true;
        }

        return false;
    }

    private boolean activarRadar() {
        if (t > (MAX_T - 6)) {  // 6 ITERACIONES PARA HACER UN GIRO COMPLETO
            gira(60);
            return true;
        }
        return false;
    }

    private void regularT() {
        t++;
        if (t == MAX_T) {
            t = 0;
        }
    }

    private boolean killMode(int dist) {

        Objecte objetivo = null;
        boolean ok = false;
        for (int i = 0; i < estat.numObjectes; i++) {
            Objecte rec = estat.objectes[i];

            if (rec.agafaTipus() == Estat.AGENT && rec.agafaDistancia() < dist) {

                ok = true;

                objetivo = rec;
                dist = rec.agafaDistancia();
            }
        }

        if (!ok) {
            return false;
        }

        if (objetivo.agafaSector() == 4) {
            gira(60);
        } else if (objetivo.agafaSector() == 1) {
            gira(-60);
        } else {

            mira(objetivo);

            if (!estat.llançant && cd_disparo <= 0) {
                llança();
                cd_disparo = 9;
            }
        }

        return true;
    }

    private boolean buscarRecurso(int recurso, int dist) {
        Objecte objetivo = null;
        boolean ok = false;
        for (int i = 0; i < estat.numObjectes; i++) {
            Objecte rec = estat.objectes[i];

            if (rec.agafaTipus() == recurso && rec.agafaDistancia() < dist) {

                ok = true;

                objetivo = rec;
                dist = rec.agafaDistancia();
            }
        }
        if (ok) {
            if (objetivo.agafaSector() == 4) {
                gira(60);
            } else if (objetivo.agafaSector() == 1) {
                gira(-60);
            } else {
                mira(objetivo);
            }
        }
        return ok;
    }

    private void esquivarParedComida() {
        if (estat.distanciaVisors[0] < 30 || estat.distanciaVisors[2] < 30) {
            if (estat.distanciaVisors[0] > estat.distanciaVisors[2]) {
                gira(20);
            } else {
                gira(-20);
            }
        }
    }

    private void comprobarEscudoComidaEnemiga() {

        for (int i = 0; !estat.escutActivat && i < estat.numObjectes; i++) {
            Objecte rec = estat.objectes[i];

            if (esRecursoEnemigo(rec) && rec.agafaDistancia() < 10) {
                //10 es la distancia a la que en teoria ya no podre evitar el recurso asi que lo placare

                activaEscut();
                return;
            }
        }
    }

    private boolean dispararComidaEnemiga(int dist) {

        if (estat.llançant) {
            return false;
        }

        Objecte objetivo = null;
        boolean ok = false;
        for (int i = 0; i < estat.numObjectes; i++) {
            Objecte rec = estat.objectes[i];

            if (esRecursoEnemigo(rec) && rec.agafaDistancia() <= dist) {

                ok = true;

                objetivo = rec;
                dist = rec.agafaDistancia();
            }
        }
        if (ok) {
            if (objetivo.agafaSector() == 4) {
                gira(60);
            } else if (objetivo.agafaSector() == 1) {
                gira(-60);
            } else {
                mira(objetivo);
                llança();
            }
            return true;
        }
        return false;
    }

    private boolean sectorOk(Objecte rec) {
        return rec.agafaSector() == 2 || rec.agafaSector() == 3;
    }

    private boolean esRecursoEnemigo(Objecte rec) {
        int id = rec.agafaTipus();
        return (id != Estat.AGENT) && (id != Estat.ESCUT) && (id != (100 + estat.id));
    }

    private boolean necesitoGirar() {

        if (verPared(20, 0, 20)) {
            return true;
        } else if (verPared(175, 80, 2)) {
            return true;
        }
        return false;
    }

    private boolean movimiento() {
        // Recorremos los tres visores
        for (int i = 0; i < estat.objecteVisor.length; i++) {
            if (estat.objecteVisor[i] == PARET && estat.distanciaVisors[i] > 80) { // Probar a cambiar esto
                int grados_giro = (int) sqrt(sqrt(sqrt(estat.distanciaVisors[i])));
                escogerLado(grados_giro);
                return true;
            }
        }
        return false;
    }

    private boolean verPared(int distanciaMenor, int distanciaMayor, int grados_giro) {
        for (int i = 0; i < estat.objecteVisor.length; i++) {
            if (estat.objecteVisor[i] == 0 && estat.distanciaVisors[i] < distanciaMenor && estat.distanciaVisors[i] >= distanciaMayor) {
                escogerLado(grados_giro);
                return true;
            }
        }
        return false;
    }

    //  Escoge un lado para girar
    private void escogerLado(int grados_giro) {

        if (estat.distanciaVisors[0] > estat.distanciaVisors[2]) {
            gira(grados_giro);
        } else {
            gira(-grados_giro);
        }
    }

    private void libreAlbedrio() throws Exception {
        Random rnd = new Random();
        int n = rnd.nextInt(100);
        if (n < 9) {
            if (n < 4) {
                gira(7);
            } else {
                gira(-7);
            }
        }
    }

    private void fundirseEscudos() {
        int frames_escudado = estat.escuts * 66;
        if (TIEMPO_MAX - tiempo_partida <= frames_escudado) {
            spamEscudos = true;
        }
    }
}
