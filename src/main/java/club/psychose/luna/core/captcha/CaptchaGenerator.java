package club.psychose.luna.core.captcha;

import club.psychose.luna.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.stream.IntStream;

public final class CaptchaGenerator {
    private final SecureRandom secureRandom = new SecureRandom();

    private final Color[] characterColors = new Color[] {Color.PINK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.RED, Color.WHITE, Color.YELLOW};
    private final String[] imageFonts = new String[] {"Monospaced", "SansSerif", "Serif"};

    public File saveCaptchaFile (BufferedImage bufferedImage) throws IOException {
        if (!(Files.exists(Constants.getLunaFolderPath("\\temp\\captchas\\"))))
            Files.createDirectories(Constants.getLunaFolderPath("\\temp\\captchas\\"));

        while (true) {
            String fileName = this.secureRandom.nextInt(999999999) + ".png";

            if (!(Files.exists(Constants.getLunaFolderPath("\\temp\\captchas\\" + fileName)))) {
                ImageIO.write(bufferedImage, "png", Constants.getLunaFolderPath("\\temp\\captchas\\" + fileName).toFile());
                return Constants.getLunaFolderPath("\\temp\\captchas\\" + fileName).toFile();
            }
        }
    }

    public BufferedImage getCaptchaImage (String captchaCode) {
        BufferedImage captchaBufferedImage = new BufferedImage(800, 800, 3);
        Graphics graphics = captchaBufferedImage.createGraphics();
        Font font = new Font(this.imageFonts[this.secureRandom.nextInt(this.imageFonts.length)], Font.BOLD, 200);

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 800, 800);
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);

        int xPosition = 800 / captchaCode.length();

        IntStream.range(0, captchaCode.length()).forEach(characterPosition -> {
            char character = captchaCode.charAt(characterPosition);
            graphics.setColor(this.characterColors[this.secureRandom.nextInt(this.characterColors.length)]);
            graphics.drawString(String.valueOf(character), xPosition * characterPosition, 400);
            graphics.setColor(Color.BLACK);
        });

        graphics.dispose();
        return captchaBufferedImage;
    }

    public String generateCaptchaCode () {
        char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        char[] captchaCode = new char[6];
        IntStream.range(0, 6).forEach(captchaLength -> captchaCode[captchaLength] = characters[this.secureRandom.nextInt(characters.length)]);

        return new String(captchaCode);
    }
}