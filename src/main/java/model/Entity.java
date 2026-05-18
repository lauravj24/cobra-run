package main.java.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Clase abstracta base para todas las entidades del juego (cobra, enemigos, objetos)
 * Define posición, sprite, dirección y hitbox
 * Todas las clases deben implementar el método update()
 *
 * @author Laura Jaramillo, Tomas Osorio, Tomas Varona
 */
public abstract class Entity {
    /**
     * Dirección: ninguna(detenido)
     */
    public static final int DIRECTION_NONE = 0;
    /**
     * Dirección: izquierda
     */
    public static final int DIRECTION_LEFT = 1;
    /**
     * Dirección: derecha
     */
    public static final int DIRECTION_RIGHT = 3;
    /**
     * Dirección: arriba
     */
    public static final int DIRECTION_UP = 5;
    /**
     * Dirección: abajo
     */
    public static final int DIRECTION_DOWN = 2;

    /**
     * Posición X en pixeles
     */
    private int x;
    /**
     * Posición Y en pixeles
     */
    private int y;
    /**
     * Ancho de la entidad en pixeles
     */
    private int width;
    /**
     * Alto de la entidad en pixeles
     */
    private int height;
    /**
     * Imagen actual del sprite
     */
    private BufferedImage sprite;
    /**
     * Indica si la entidad está activa en el juego
     */
    private boolean active;
    /**
     * Dirección actual de movimiento
     */
    private int direction;

    /**
     * Constructor principal, asigna posición y sprite, e inicialización width y height
     * correctamente a partir del sprite cargado
     *
     * @param x      posición X inicial
     * @param y      posición Y inicial
     * @param sprite imagen del sprite
     */

    public Entity(int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.active = true;
        this.direction = DIRECTION_NONE;
        //setSprite asigna width y height
        setSprite(sprite);
    }

    /**
     * Constructor vacio para subclases
     */
    public Entity() {
    }

    /**
     * Actualiza el estado de la entidad cada frame
     * Debe ser implementado por cada subclase con su logica particular
     */
    public abstract void update();

    /**
     * Retorna la hitbox de la entidad en su posición actual
     *
     * @return Rectangle con la posición y dimensiones actuales
     */
    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Retorna una hitbox proyectada a una posición futura
     * Usado para detectar colisiones antes de moverse
     *
     * @param pX posición X futura
     * @param pY posición Y futura
     * @return Rectangle proyectado
     */

    public Rectangle getHitBox(int pX, int pY) {
        return new Rectangle(pX, pY, width, height);
    }

    /**
     * Calcula el desplazamiento horizontal segun la dirección actual
     *
     * @return -1 (izquierda), +1 (derecha), 0 (sin movimiento horizontal)
     */
    public int calculateDx() {
        if (direction == DIRECTION_LEFT) return -1;
        if (direction == DIRECTION_RIGHT) return 1;
        return 0;
    }

    /**
     * Calcula el desplazamiento vertical segun la dirección actual
     *
     * @return -1 (arriba), +1 (abajo), 0 (sin movimiento vertical)
     */
    public int calculateDy() {
        if (direction == DIRECTION_UP) return -1;
        if (direction == DIRECTION_DOWN) return 1;
        return 0;
    }

    /**
     * Carga una imagen desde la carpeta de recursos
     *
     * @param name nombre del archivo
     * @return BufferedImage cargada o null si no se encuentra
     */
    public static BufferedImage upload(String name) {
        try {
            InputStream is = Entity.class.getResourceAsStream("/resources/" + name);
            if (is == null) {
                System.err.println("Imagen no encontrada: " + name);
                return null;
            }
            return ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + name);
            return null;
        }
    }

    // Getters y Setters

    /**
     * @return posición X actual
     */
    public int getX() {
        return x;
    }

    /**
     * @param x nueva posición X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return posición Y actual
     */
    public int getY() {
        return y;
    }

    /**
     * @param y nueva posición Y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return ancho del sprite
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return alto del sprite
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return sprite actual
     */
    public BufferedImage getSprite() {
        return sprite;
    }

    /**
     * Asigna un nuevo sprite y actualiza width y heigh automaticamente
     *
     * @param sprite nueva imagen del sprite
     */
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
        if (sprite != null) {
            this.width = sprite.getWidth();
            this.height = sprite.getHeight();
        }
    }

    /**
     * @return true si la entidad está activa
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active nuevo estado de actividad
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return dirección actual de movimiento
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @param direction nueva dirección de movimiento
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }
}
