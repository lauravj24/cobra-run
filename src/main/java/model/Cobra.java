package main.java.model;

/**
 * Clase que representa la cobra en el juego
 * Gestiona posición, vidas, puntaje, velocidad y efectos de power-ups
 *
 * @author Laura Jaramillo, Tomas Osorio, Tomas Varona
 */

public class Cobra extends Entity {
    //Posición inicial (col 1, fila 1 del laberinto celda vacia)
    /**
     * Posición X inicial de la cobra (col 1 x 24px)
     */
    public static final int X_INIT = 24;
    /**
     * Posición Y inicial de la cobra (fila 1 x 24px)
     */
    public static final int Y_INIT = 24;

    //Constantes de juego
    /**
     * Velocidad de movimiento de la cobra
     */
    private static final int BASE_SPEED = 3;
    /**
     * Velocidad aumentada al recoger power-ups de velocidad
     */
    private static final int BOOSTED_SPEED = 6;
    /**
     * Numero de vidas iniciales de la cobra
     */
    private static final int LIVES = 3;
    /**
     * Maximo de vidas que puede tener la cobra
     */
    private static final int MAX_LIVES = 5;

    //Coordenadas del túnel lateral (fila 9 x 24px = 216)
    private static final int TUNNEL_Y = 216;
    private static final int X_MAX = 504;

    //Estado del jugador
    /**
     * Nombre del jugador ingresado en la pantalla de inicio
     */
    private String playerName;
    /**
     * Puntaje acumulado durante la partida
     */
    private int score;
    /**
     * Numero de vidas restantes
     */
    private int lives;
    /**
     * Velocidad actual de movimiento
     */
    private int speed;
    /**
     * Estado de la animación de boca
     */
    private boolean mouthOpen;
    /**
     * Indica si el juego está pausado
     */
    private boolean paused;
    /**
     * Indica si se presionó Enter
     */
    private boolean enterPressed;

    //Efectos de power-up
    /**
     * Frames restantes del efecto de velocidad (0= inactivo)
     */
    private int speedTimer;
    /**
     * Frames restantes de invulnerabilidad (0= inactivo)
     */
    private int invicibleTimer;
    /**
     * Frames restantes del poder de matar enemigos (0= inactivo)
     */
    private int killTimer;

    /**
     * Dirección solicitada por el jugador pero aun no aplicada
     * Solo se aplica cuando la cobra está alineada a la cuadricula
     * garantizando movimiento cuadro a cuadro sin salirse de los pasillos
     */
    private int nextDirection;

    /**
     * Crea la cobra con sus valores iniciales
     */
    public Cobra() {
        super(X_INIT, Y_INIT, upload("cobra.png"));
        this.playerName = "Jugador";
        this.score = 0;
        this.lives = LIVES;
        this.speed = BASE_SPEED;
        this.mouthOpen = true;
        this.paused = false;
        this.enterPressed = false;
        this.speedTimer = 0;
        this.invicibleTimer = 0;
        this.killTimer = 0;
        this.nextDirection = DIRECTION_NONE;
        setDirection(DIRECTION_NONE); //quieta hasta que el jugador presione una tecla
    }

    /**
     * Actualiza unicamente la posición de la cobra
     * Los timers de los power-ups se actualizan por separado en updatePowerUps()
     */
    @Override
    public void update() {
        int nuevoX = getX() + calculateDx() * speed;
        int nuevoY = getY() + calculateDy() * speed;

        //Tunel lateral
        if (nuevoX > X_MAX && getY() == TUNNEL_Y) nuevoX = 0;
        if (nuevoX < 0 && getY() == TUNNEL_Y) nuevoX = X_MAX;

        setX(nuevoX);
        setY(nuevoY);
    }

    /**
     * Decrementa los temporizadores de power-up activos
     * se llama una vez por frame desde el controlador, independientemente
     * de si la cobra pudo moverse o no
     */
    public void updatePowerUps() {
        if (speedTimer > 0) {
            speedTimer--;
            if (speedTimer == 0) speed = BASE_SPEED;
        }
        if (invicibleTimer > 0) invicibleTimer--;
        if (killTimer > 0) killTimer--;
    }

    /**
     * Actualiza el sprite de la cobra segun la dirección y la animación de boca
     *
     * @param direction dirección actual de la cobra
     */
    public void updateSprite(int direction) {
        String file;
        if (mouthOpen) {
            file = "cobra_closed.png";
        } else {
            switch (direction) {
                case DIRECTION_LEFT:
                    file = "cobra_left.png";
                    break;
                case DIRECTION_RIGHT:
                    file = "cobra_right.png";
                    break;
                case DIRECTION_UP:
                    file = "cobra_up.png";
                    break;
                case DIRECTION_DOWN:
                    file = "cobra_down.png";
                    break;
                default:
                    file = "cobra_right.png";
                    break;
            }
        }
        setSprite(upload(file));
        mouthOpen = !mouthOpen;
    }

    //Power-ups

    /**
     * Activa el efecto de velocidad durante los frames indicados
     *
     * @param frames duración del efecto en frames
     */
    public void activateSpeed(int frames) {
        speedTimer = frames;
        speed = BOOSTED_SPEED;
    }

    /**
     * Activa la invulnerabilidad durante los frames indicados
     *
     * @param frames duración del efecto en frames
     */
    public void activateInvicible(int frames) {
        invicibleTimer = frames;
    }

    /**
     * Activa el poder de matar enemigos durante los frames indicados
     *
     * @param frames duración del efecto en frames
     */
    public void activateKill(int frames) {
        killTimer = frames;
    }

    //Vida y puntaje

    /**
     * Suma puntos al puntaje acumulado
     *
     * @param points puntos a agregar (debe ser positivo)
     */
    public void addScore(int points) {
        if (points > 0) score += points;
    }

    /**
     * Descuenta una vida si el jugador no es invulnerable
     *
     * @return true si el jugador perdió la vida, false si era invulnerable
     */
    public boolean loseLife() {
        if (invicibleTimer > 0) return false;
        if (lives > 0) lives--;
        return true;
    }

    /**
     * Agrega una vida si no se ha alcanzado el máximo
     */
    public void addLife() {
        if (lives < MAX_LIVES) lives++;
    }

    /**
     * Reinicia la posición de la cobra sin alterar puntaje ni vidas
     *
     * @param startX posición X inicial del nivel
     * @param startY posición Y inicial del nivel
     */
    public void resetPosition(int startX, int startY) {
        setX(startX);
        setY(startY);
        setDirection(DIRECTION_NONE);
        nextDirection = DIRECTION_NONE;
        setSprite(upload("cobra_right.png"));
        speedTimer = 0;
        invicibleTimer = 0;
        killTimer = 0;
        speed = BASE_SPEED;
        mouthOpen = true;
    }

    /**
     * Reinicia completamente el estado del jugador para una nueva partida
     */
    public void resetAll() {
        resetPosition(X_INIT, Y_INIT);
        score = 0;
        lives = LIVES;
        enterPressed = false;
    }

    /**
     * Alterna el estado de pausa del juego
     */
    public void togglePause() {
        paused = !paused;
    }

    //Getters y Setters

    /**
     * @return nombre del jugador
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @param name nombre del jugador
     */
    public void setPlayerName(String name) {
        this.playerName = (name != null && !name.trim().isEmpty()) ? name.trim() : "Jugador";
    }

    /**
     * @return puntaje acumulado
     */
    public int getScore() {
        return score;
    }

    /**
     * @return numero de vidas restantes
     */
    public int getLives() {
        return lives;
    }

    /**
     * @return velocidad actual de movimiento
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @return true si el juego está en pausa
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @return true si Enter está presionado
     */
    public boolean isEnterPressed() {
        return enterPressed;
    }

    /**
     * @param v true cuando Enter está presionado, false cuando no lo está
     */
    public void setEnterPressed(boolean v) {
        this.enterPressed = v;
    }

    /**
     * @return true si el efecto de invulnerabilidad está activo
     */
    public boolean isInvincible() {
        return invicibleTimer > 0;
    }

    /**
     * @return true si el poder de matar enemigos está activo
     */
    public boolean canKiller() {
        return killTimer > 0;
    }

    /**
     * @return true si el efecto de velocidad está activo
     */
    public boolean hasSpeedBoost() {
        return speedTimer > 0;
    }

    /**
     * @return frames restantes de invulnerabilidad
     */
    public int getInvincibleTimer() {
        return invicibleTimer;
    }

    /**
     * @return frames restantes de poder de matar enemigos
     */
    public int getKillTimer() {
        return killTimer;
    }

    /**
     * Retorna la dirección solicitada por el jugador, pendiente de aplicar
     * El GameController la consume en el proximo borde de celda
     *
     * @return dirección pendiente (puede ser DIRECTION_NONE si no hay tecla presionada)
     */
    public int getNextDirection() {
        return nextDirection;
    }

    /**
     * Registra la dirección solicitada por el jugador
     * No se aplica inmediatamente solo en el proximo borde de celda
     *
     * @param dir dirección deseada (constantes DIR_ de Entity)
     */
    public void setNextDirection(int dir) {
        this.nextDirection = dir;
    }
}
