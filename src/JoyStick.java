import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.KillException;
import org.jointheleague.jcodrone.MessageNotSentException;


import java.util.Arrays;
import java.util.LinkedList;

import static java.awt.SystemColor.window;

public class JoyStick implements Runnable{

    private LinkedList<Controller> foundControllers=new LinkedList<>();



    CoDrone drone;
    String[] buttonNames = new String[]{
            "A",
            "B",
            "C",
            "X",
            "Y",
            "Z",
            "Left Thumb",
            "Right Thumb",
            "Left Thumb 2",
            "Right Thumb 2",
            "Select",
            "Unknown",
            "Mode"};

    public JoyStick() {
        searchForControllers();
    }

    /**
     * Search (and save) for controllers of type Controller.Type.STICK,
     * Controller.Type.GAMEPAD, Controller.Type.WHEEL and Controller.Type.FINGERSTICK.
     */
    private void searchForControllers() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(int i = 0; i < controllers.length; i++){
            Controller controller = controllers[i];

            if (
                    controller.getType() == Controller.Type.STICK ||
                            controller.getType() == Controller.Type.GAMEPAD ||
                            controller.getType() == Controller.Type.WHEEL ||
                            controller.getType() == Controller.Type.FINGERSTICK
                    )
            {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);


            }
        }
    }


    /**
     * Starts showing controller data on the window.
     */
    private void startShowingControllerData() {
        while (true) {
            // Currently selected controller.
            Controller controller = foundControllers.get(0);

            // Pull controller for current data, and break while loop if controller is disconnected.
            if (!controller.poll()) {
                break;
            }

            // X axis and Y axis
            int xAxisPercentage = 0;
            int yAxisPercentage = 0;

            // Go trough all components of the controller.
            Component[] components = controller.getComponents();
            for (int i = 0; i < components.length; i++) {
                Component component = components[i];
                Component.Identifier componentIdentifier = component.getIdentifier();
//                System.out.println(componentIdentifier);


                if (Arrays.asList(buttonNames).contains(componentIdentifier.getName())) { // If the component identifier name contains only numbers, then this is a button.

                    if(component.getPollData()!=0.0f){
                        System.out.println(componentIdentifier.getName()+" Pressed");
                        switch(componentIdentifier.getName()) {
                            case "R1":
                                try {
                                    drone.takeoff();
                                } catch (MessageNotSentException e) {

                                }
                                break;
                            case "L1":
                                try {
                                    drone.land();
                                } catch (MessageNotSentException e) {

                                }
                                break;
                            case "Mode":
                                try {
                                    drone.kill();
                                } catch (KillException e) {

                                }
                                break;
                        }
                    }
                }
                if (component.isAnalog() && component.getPollData()!=0.0f){
                    float axisValue=component.getPollData();
                    if (componentIdentifier==Component.Identifier.Axis.X){
                        System.out.format("Roll %f\n",component.getPollData());

                    }else if (componentIdentifier==Component.Identifier.Axis.Y){
                        System.out.format("Pitch %f\n",component.getPollData());

                    }else if (componentIdentifier==Component.Identifier.Axis.Z){
                        System.out.format("Rotate %f\n",component.getPollData());

                    }else if (componentIdentifier==Component.Identifier.Axis.RZ){
                        System.out.format("Altitude %f\n",component.getPollData());

                    }
                }
                //Give the processor a rest
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            }
        }
    }

    @Override
    public void run() {
        startShowingControllerData();

    }
    public void setDrone(CoDrone drone) {
        this.drone = drone;
    }
}
