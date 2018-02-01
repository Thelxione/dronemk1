import org.jointheleague.jcodrone.*;
import org.jointheleague.jcodrone.protocol.light.LightEvent;
import org.jointheleague.jcodrone.protocol.light.LightMode;
import org.jointheleague.jcodrone.protocol.light.LightModeDrone;

public class DomDrone {

    public static final int TIME_UNIT = 1500;

    public static void main(String args[]) {
        try (CoDrone drone = new CoDrone()){
            drone.connect();
            LightMode mode= new LightModeBuilder().setColor("YELLOW").setMode(LightModeDrone.EYE_FLICKER).build();
            drone.lightMode(mode);
            mode= new LightModeBuilder().setColor("orange").setMode(LightModeDrone.ARM_FLICKER).build();
            drone.lightMode(mode);
            String pattern= MorseCode.X.getPattern();
            for(byte a:pattern.getBytes()) {
                int interval = a == '.'? TIME_UNIT :3*TIME_UNIT;
                System.out.format("%c %d", a, interval);
                LightMode mode2 = new LightModeBuilder()
                        .setColor("magenta")
                        .setMode(LightModeDrone.EYE_HOLD)
                        .build();
                drone.lightMode(mode2);
                Thread.sleep(interval);
                 mode2 = new LightModeBuilder()
                        .setColor("magenta")
                        .setMode(LightModeDrone.EYE_NONE)
                         .build();
                drone.lightMode(mode2);
                Thread.sleep(TIME_UNIT);
            }
            drone.takeoff();
            Thread.sleep(5000);
            drone.land();
            Thread.sleep(5000);
        } catch (CoDroneNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } catch (MessageNotSentException e) {
            e.printStackTrace();
        }
    }
}
