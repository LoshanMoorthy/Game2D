package entities;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.PlayerConstant.*;
import static utils.HelpMethods.CanMoveHere;

public class Player extends Entity {

    private int animationTick, animationIndex, animationSpeed = 15;
    private int playerAction = IDLE;
    private int playerDirection = -1;
    private boolean moving = false, attacking = false;
    private BufferedImage[][] animations;
    private boolean left, up, right, down;
    private float playerSpeed = 2.0f;
    private int[][] levelData;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
    }

    public void update() {
        updatePosition();
        updateHitbox();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][animationIndex], (int) x, (int) y, width, height, null);
        drawHitbox(g);
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAnimation = playerAction;
        
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if (attacking)
            playerAction = ATTACK_1;
        
        if (startAnimation != playerAction)
            resetAnimationTick();
    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    private void updatePosition() {
        moving = false;
        if (!left && !right && !up && !down)
            return;

        float xSpeed = 0, ySpeed = 0;

        if (left && !right)
            xSpeed = -playerSpeed;
        else if (right && !left)
            xSpeed = playerSpeed;

        if (up && !down)
            ySpeed = -playerSpeed;
        else if (down && !up)
            ySpeed = playerSpeed;

        if (CanMoveHere(x + xSpeed, y + ySpeed, width, height, levelData)) {
            this.x += xSpeed;
            this.y += ySpeed;
            moving = true;
        }
    }

    private void loadAnimations() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[9][6];
        for (int i = 0; i < animations.length; i++)
            for (int j = 0; j < animations[i].length; j++)
                animations[i][j] = image.getSubimage(j * 64, i * 40, 64, 40);
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
    }

    public void resetDirectionBooleans() {
        left = false;
        right = false;
        down = false;
        up = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
