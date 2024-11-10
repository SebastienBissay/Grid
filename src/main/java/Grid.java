import processing.core.PApplet;
import processing.core.PImage;

import java.util.stream.IntStream;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class Grid extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Grid.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(COLOR.red(), COLOR.green(), COLOR.blue());
        frameRate(-1);
    }

    @Override
    public void draw() {
        float colorFactor = sq((float) frameCount / NUMBER_OF_FRAMES);
        stroke(BASE_COLOR.red() - APPLIED_COLOR.red() * colorFactor + RED_SHIFT_FACTOR * randomGaussian(),
                BASE_COLOR.green() - APPLIED_COLOR.green() * colorFactor,
                BASE_COLOR.blue() - APPLIED_COLOR.blue() * colorFactor);

        int x = floor(random(.5f - RANGE, .5f + RANGE) * width);
        int y = floor(random(.5f - RANGE, .5f + RANGE) * height);
        loadPixels();
        PImage northEast = createImage(x, y, RGB);
        updatePImage(northEast, 0, 0);
        PImage northWest = createImage(width - x, y, RGB);
        updatePImage(northWest, x, 0);
        PImage southEast = createImage(x, height - y, RGB);
        updatePImage(southEast, 0, y);
        PImage southWest = createImage(width - x, height - y, RGB);
        updatePImage(southWest, x, y);

        background(COLOR.red(), COLOR.green(), COLOR.blue());
        float offset = STARTING_OFFSET * sq(noise(frameCount * OFFSET_FACTOR));
        IntStream.range(0, height).forEach(j ->
                line(x - offset * width + offset * WIDTH * noise(j * NOISE_SCALE, frameCount * NOISE_SCALE), j,
                        x + offset * width - offset * WIDTH * noise(j * NOISE_SCALE, frameCount * NOISE_SCALE), j));
        IntStream.range(0, width).forEach(i ->
                line(i, y - offset * height + offset * HEIGHT * noise(i * NOISE_SCALE, frameCount * NOISE_SCALE),
                        i, y + offset * height - offset * HEIGHT * noise(i * NOISE_SCALE, frameCount * NOISE_SCALE)));
        image(northEast, floor(-width * offset), floor(-height * offset));
        image(northWest, x + floor(width * offset), floor(-height * offset));
        image(southEast, floor(-width * offset), y + floor(height * offset));
        image(southWest, x + floor(width * offset), y + floor(height * offset));
        if (frameCount >= NUMBER_OF_FRAMES) {
            fill(COLOR.red(), COLOR.green(), COLOR.blue());
            noStroke();
            rect(0, 0, WIDTH, MARGIN);
            rect(0, 0, MARGIN, HEIGHT);
            rect(0, HEIGHT - MARGIN, WIDTH, MARGIN);
            rect(WIDTH - MARGIN, 0, MARGIN, HEIGHT);

            noLoop();
            saveSketch(this);
        }
    }

    private void updatePImage(PImage pImage, int xOffset, int yOffset) {
        pImage.loadPixels();
        for (int i = 0; i < pImage.width; i++) {
            for (int j = 0; j < pImage.height; j++) {
                pImage.pixels[i + j * pImage.width] = pixels[i + xOffset + (j + yOffset) * width];
            }
        }
        pImage.updatePixels();
    }
}
