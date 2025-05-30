package frc.robot.subsystems.leds;

import static frc.robot.subsystems.leds.LEDConstants.BREATHE_SPEED;
import static frc.robot.subsystems.leds.LEDConstants.LED_LENGTH;
import static frc.robot.subsystems.leds.LEDConstants.LED_PORT;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.util.Color;

/**
 * Contains the methods that dictate simulated behavior for LEDs. <br>
 * <br>
 * Interestingly, you can read AddressableLED data directly from the sim GUI, without a roboRIO – no
 * need for AddressableLEDSim or the byte-conversion nonsense it warrants. Therefore, this is a
 * direct copy of LEDIOPWM.
 */
public class LEDIOPWM implements LEDIO {

  private final AddressableLED led;
  private final AddressableLEDBuffer buffer;
  private final AddressableLEDBufferView[] views;

  private final LEDPattern[] patterns;

  public LEDIOPWM() {
    led = new AddressableLED(LED_PORT);
    buffer = new AddressableLEDBuffer(LED_LENGTH);

    views = new AddressableLEDBufferView[LEDConstants.SEGMENTS.length];
    patterns = new LEDPattern[LEDConstants.SEGMENTS.length];

    for (int i = 0; i < LEDConstants.SEGMENTS.length; i++) {
      LEDConstants.Segment segment = LEDConstants.SEGMENTS[i];
      views[i] = buffer.createView(segment.start(), segment.start() + segment.length() - 1);

      if (segment.reversed()) {
        views[i] = views[i].reversed();
      }

      patterns[i] = LEDPattern.solid(Color.kBlue).breathe(BREATHE_SPEED);
      patterns[i].applyTo(views[i]);
    }

    System.out.println(patterns.length);

    led.setLength(buffer.getLength());
    led.start();
  }

  public void setPattern(int idx, LEDPattern pattern) {
    pattern.applyTo(views[idx]);
  }

  public void setPatterns(LEDPattern[] patterns) {
    System.arraycopy(patterns, 0, this.patterns, 0, LEDConstants.SEGMENTS.length);
  }

  public void setAllPattern(LEDPattern pattern) {
    for (int i = 0; i < LEDConstants.SEGMENTS.length; i++) {
      this.patterns[i] = pattern;
    }
  }

  public void periodic() {
    for (int i = 0; i < LEDConstants.SEGMENTS.length; i++) {
      patterns[i].applyTo(views[i]);
    }
    led.setData(buffer);
  }
}
