/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


import static javax.imageio.ImageIO.read;
import static tankrotationexample.GameConstants.size;

/**
 * @author anthony-pc
 */
public class TRE extends JPanel implements Runnable {

    private BufferedImage world;
    private final Launcher lf;
    static long tick = 0;
    private Graphics2D buffer, g2;
    private Image unbreak, breakable, powerUp1, powerUp2, powerUp3;
    private Rectangle2D tank1Rec, tank2Rec, wallRec, bulletRec;
    private boolean endGame = false;

    ArrayList<GameObject> gameObjects;
    ArrayList<Wall> walls = new ArrayList<>();

    public TRE(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            //this.resetGame();
            while (true && !this.isEndGame()) {
                tick++;
                this.gameObjects.forEach(gameObject -> gameObject.update());
                this.checkCollision();
                this.repaint();   // redraw game
                Thread.sleep(1000 / 144); //sleep for a few milliseconds
                /*
                 * simulate an end game event
                 * we will do this with by ending the game when drawn 2000 frames have been drawn
                 */
                /*
                if (tick > 2000) {
                    this.lf.setFrame("end");
                    return;
                }*/
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    /*public void resetGame() {
        tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
        this.t2.setX(960);
        this.t2.setY(300);
    } */

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        this.gameObjects = new ArrayList<>();

        try {
            InputStreamReader isr = new InputStreamReader(TRE.class.getClassLoader().getResourceAsStream("maps/map1"));
            BufferedReader mapReader = new BufferedReader(isr);

            Floor f1 = new Floor(0, 0, Resource.getResourceImage("floor"));
            this.gameObjects.add(f1);

            String row = mapReader.readLine();
            if (row == null) {
                throw new IOException("No data in file.");
            }
            String[] mapInfo = row.split("\t");
            int numCols = Integer.parseInt(mapInfo[0]);
            int numRows = Integer.parseInt(mapInfo[1]);

            for (int curRow = 0; curRow < numRows; curRow++) {
                row = mapReader.readLine();
                mapInfo = row.split("\t");
                for (int curCol = 0; curCol < numCols; curCol++) {
                    switch (mapInfo[curCol]) {
                        case "2":
                            BreakWall br = new BreakWall(curCol * 30, curRow * 30, Resource.getResourceImage("break"));
                            this.gameObjects.add(br);
                            break;
                        case "3":
                            PowerUpHealth health = new PowerUpHealth(curCol * 30, curRow * 30, Resource.getResourceImage("puHealth"));
                            this.gameObjects.add(health);
                            break;
                        case "4":
                            PowerUpRocket rocket = new PowerUpRocket(curCol * 30, curRow * 30, Resource.getResourceImage("puRocket"));
                            this.gameObjects.add(rocket);
                            break;
                        case "5":
                            PowerUpShield shield = new PowerUpShield(curCol * 30, curRow * 30, Resource.getResourceImage("puShield"));
                            this.gameObjects.add(shield);
                            break;
                        case "9":
                            UnBreakWall ubr = new UnBreakWall(curCol * 30, curRow * 30, Resource.getResourceImage("unbreak"));
                            this.gameObjects.add(ubr);
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        Tank t1 = new Tank(300, 300, 0, 0, 0, Resource.getResourceImage("tank1"));
        Tank t2 = new Tank(960, 300, 0, 0, 180, Resource.getResourceImage("tank2"));

        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W,
                KeyEvent.VK_S,
                KeyEvent.VK_A,
                KeyEvent.VK_D,
                KeyEvent.VK_SPACE);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_ENTER);

        this.gameObjects.add(t1);
        this.gameObjects.add(t2);

        this.setBackground(Color.BLACK);
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);
    }

    //X coordinate for split screen
    private int getXCoordinate(Tank t) {
        int x = t.getX();
        if (x < GameConstants.GAME_SCREEN_WIDTH / 4)
            x = GameConstants.GAME_SCREEN_WIDTH / 4;
        if (x > GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 4)
            x = GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 4;
        return x;
    }

    //Y coordinate for split screen
    public int getYCoordinate(Tank t) {
        int y = t.getY();
        if (y < GameConstants.GAME_SCREEN_HEIGHT / 2)
            y = GameConstants.GAME_SCREEN_HEIGHT / 2;
        if (y > GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT / 2)
            y = GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT / 2;
        return y;
    }

    //collisions
    public void checkCollision() {
        Tank t1 = (Tank) this.gameObjects.get(this.gameObjects.size() - 2);
        Tank t2 = (Tank) this.gameObjects.get(this.gameObjects.size() - 1);

        tank1Rec = t1.getRectangle();
        tank2Rec = t2.getRectangle();

        // Checks to see if player 1 collides with player 2
        if (tank1Rec.intersects(tank2Rec)) {
            t1.setCollides(true);
            t1.handleCollision();
        }
        if (tank2Rec.intersects(tank1Rec)) {
            t2.setCollides(true);
            t2.handleCollision();
        }

        //bullet hits another tank and subtract a life
        for (int i = 0; i < t1.getAmmo().size(); i++) {
            bulletRec = t1.getAmmo().get(i).getRectangle();

            if (tank2Rec.intersects(bulletRec)) {
                t1.getAmmo().remove(i);
                t2.setHealth(t2.getHealth() - 1);

                if (t2.getHealth() == 0) {
                    t2.setLives(t2.getLives() - 1);

                    if (!(t2.getLives() == -1)) {
                        t2.Respawn(5);
                        t2.setBulletDelay(false);
                        t1.setBulletDelay(false);
                        t1.Respawn(t1.getHealth());
                    }
                }
            }
        }
        for (int i = 0; i < t2.getAmmo().size(); i++) {
            bulletRec = t2.getAmmo().get(i).getRectangle();

            if (tank1Rec.intersects(bulletRec)) {
                t2.getAmmo().remove(i);
                t1.setHealth(t1.getHealth() - 1);

                if (t1.getHealth() == 0) {
                    t1.setLives(t1.getLives() - 1);

                    if (!(t1.getLives() == -1)) {
                        t1.Respawn(5);
                        t1.setBulletDelay(false);
                        t2.setBulletDelay(false);
                        t2.Respawn(t2.getHealth());
                    }
                }
            }
        }
    }

    public boolean isEndGame() {
        return endGame;
    }

    @Override
    public void paintComponent(Graphics g) {
        // base
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();

        //outline around tanks.. need to find a way to get rid of it
        //buffer.setColor(Color.BLACK);
        // gets rid of the trail the tanks make
        buffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);

        this.gameObjects.forEach(gameObject -> gameObject.drawImage(buffer));
        Tank t1 = (Tank) this.gameObjects.get(this.gameObjects.size() - 2);
        Tank t2 = (Tank) this.gameObjects.get(this.gameObjects.size() - 1);

        BufferedImage leftHalf = world.getSubimage(getXCoordinate(t1) - GameConstants.GAME_SCREEN_WIDTH / 4, getYCoordinate(t1) - GameConstants.GAME_SCREEN_HEIGHT / 2, GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rightHalf = world.getSubimage(getXCoordinate(t2) - GameConstants.GAME_SCREEN_WIDTH / 4, getYCoordinate(t1) - GameConstants.GAME_SCREEN_HEIGHT / 2, GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);

        //split screen
        g2.drawImage(leftHalf, 0, 0, null);
        g2.drawImage(rightHalf, GameConstants.GAME_SCREEN_WIDTH / 2, 0, null);

        //divider for minimap
        g2.setColor(Color.BLACK);
        g2.fillRect(GameConstants.GAME_SCREEN_WIDTH / 2, 0, 2, GameConstants.GAME_SCREEN_HEIGHT);
        g2.fillRect(GameConstants.GAME_SCREEN_WIDTH / 2 - (GameConstants.GAME_WORLD_WIDTH / (size * 2)) - 2, (GameConstants.GAME_SCREEN_HEIGHT - GameConstants.GAME_WORLD_HEIGHT / size) - 35,
                GameConstants.GAME_WORLD_WIDTH / size + 4, GameConstants.GAME_WORLD_HEIGHT / size + 4);

        //minimap
        Image mini = world.getScaledInstance(200, 200, 0);
        g2.drawImage(mini, GameConstants.GAME_SCREEN_WIDTH / 2 - (GameConstants.GAME_WORLD_WIDTH / (size * 2)), (GameConstants.GAME_SCREEN_HEIGHT - GameConstants.GAME_WORLD_HEIGHT / size) - 33,
                GameConstants.GAME_WORLD_WIDTH / size, GameConstants.GAME_WORLD_HEIGHT / size, this);

        //HEALTH BARS
        //  t1 health bars
        //  base
        g2.setFont(new Font("", Font.BOLD, 20));
        g2.setColor(Color.BLACK);
        g2.drawString("Tank 1 Lives Remaining: " + t1.getLivesRemaining(), GameConstants.GAME_SCREEN_WIDTH + 69 - GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT * 26 / 40);
        g2.drawString("Tank 1 Health: " + t1.getHealth(), GameConstants.GAME_SCREEN_WIDTH + 69 - GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT * 28 / 40);
        g2.drawRect(GameConstants.GAME_SCREEN_WIDTH + 69 - GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT * 29 / 40 - 1, 102, 12);
        //outline
        g2.setColor(Color.WHITE);
        g2.drawString("Tank 1 Lives Remaining: " + t1.getLivesRemaining(), GameConstants.GAME_SCREEN_WIDTH + 70 - GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT * 26 / 40);
        g2.drawString("Tank 1 Health: " + t1.getHealth(), GameConstants.GAME_SCREEN_WIDTH + 70 - GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT * 28 / 40);
        g2.drawRect(GameConstants.GAME_SCREEN_WIDTH + 70 - GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT * 29 / 40 - 1, 102, 12);
        g2.setColor(Color.GREEN);

        //  t2 health bars
        //  base
        g2.setColor(Color.BLACK);
        g2.drawString("Tank 2 Lives Remaining: " + t2.getLivesRemaining(), GameConstants.GAME_SCREEN_WIDTH - 310, GameConstants.GAME_SCREEN_HEIGHT * 26 / 40);
        g2.drawString("Tank 2 Health: " + t2.getHealth(), GameConstants.GAME_SCREEN_WIDTH - 310, GameConstants.GAME_SCREEN_HEIGHT * 28 / 40);
        g2.drawRect(GameConstants.GAME_SCREEN_WIDTH - 310, GameConstants.GAME_SCREEN_HEIGHT * 29 / 40 - 1, 102, 12);
        //outline
        g2.setColor(Color.WHITE);
        g2.drawString("Tank 2 Lives Remaining: " + t2.getLivesRemaining(), GameConstants.GAME_SCREEN_WIDTH - 309, GameConstants.GAME_SCREEN_HEIGHT * 26 / 40);
        g2.drawString("Tank 2 Health: " + t2.getHealth(), GameConstants.GAME_SCREEN_WIDTH - 309, GameConstants.GAME_SCREEN_HEIGHT * 28 / 40);
        g2.drawRect(GameConstants.GAME_SCREEN_WIDTH - 309, GameConstants.GAME_SCREEN_HEIGHT * 29 / 40 - 1, 102, 12);

        //actual health bars
        int decrease = 0;
        for (int i = 5; i >= 1; i--) {
            if (t1.getHealth() == i) {
                g2.setColor(Color.green);
                g2.fillRect(GameConstants.GAME_SCREEN_WIDTH + 72 - GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT - 192, 100 - decrease, 8);
            }
            if (t2.getHealth() == i) {
                g2.setColor(Color.green);
                g2.fillRect(GameConstants.GAME_SCREEN_WIDTH - 307, GameConstants.GAME_SCREEN_HEIGHT - 192, 100 - decrease, 8);
            }
            decrease += 20;
        }

        //POST GAME SCREEN
        //player1WINS
        if (t2.getLives() == -1) {
            g2.setFont(new Font("", Font.BOLD, 60));
            g2.setColor(Color.black);
            g2.drawString("Game Over: Player 1 Wins", GameConstants.GAME_WORLD_WIDTH / 2 - 393, GameConstants.GAME_SCREEN_HEIGHT / 2 + 2);
            endGame = true;
        }
        //player2WINS
        if (t1.getLives() == -1) {
            g2.setFont(new Font("", Font.BOLD, 60));
            g2.setColor(Color.black);
            g2.drawString("Game Over: Player 2 Wins", GameConstants.GAME_SCREEN_WIDTH / 2 - 393, GameConstants.GAME_SCREEN_HEIGHT / 2 + 2);
            endGame = true;
        }
    }
}
