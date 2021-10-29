package NightfallLinearOpMode.Blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import NightfallLinearOpMode.Intake;
import NightfallLinearOpMode.Drivetrain;
import NightfallLinearOpMode.Lift;
import NightfallLinearOpMode.Vision;

@Autonomous(name = "Blue Duck", group = "blue")
public class Duck extends LinearOpMode {

    private Drivetrain drivetrain;
    private Vision vision;
    private Lift lift;
    private Intake intake;
    private String pos;


    @Override
    public void runOpMode() throws InterruptedException {

        drivetrain = new Drivetrain(this);
        vision = new Vision(this);
        lift = new Lift(this);
        intake = new Intake(this);

        while (!isStarted()) {
            pos = vision.getPosNewMethod();
            telemetry.addData("team marker pos: ", pos);
            telemetry.update();
        }
        telemetry.addData("team marker pos: ", pos);
        telemetry.update();

        //trajectories/pose go here

        idle();

//======================= DEPOSIT MINERAL AND INTAKE TEAM MARKER =========================================
        //drivetrain.move(12);
        if (pos.equals("1")) {
            //lift.setheight(1);
            //lift.drop;
            //lift.setheight(0);
            //drivetrain.turn(90);
            //intake.intake(5);
            //drivetrain.turn(-90);
        }
        else if (pos.equals("2")) {
            //lift.setheight(2);
            //lift.drop;
            //lift.setheight(0);
            //drivetrain.turn(90);
            //drivetrain.move(6);
            //intake.intake(5);
            //drivetrain.move(-6);
            //drivetrain.turn(-90);
        }
        else {
            //lift.setheight(2);
            //lift.drop;
            //lift.setheight(0);
            //drivetrain.turn(90);
            //drivetrain.move(10);
            //intake.intake(5);
            //drivetrain.move(-10);
            //drivetrain.turn(-90);
        }
//======================= DUCK =========================================
        //drivetrain.move(-10);
        //drivetrain.turn(-90);
        //drivetrain.move(-18);
        //drivetrain.duck(4);
        //drivetrain.turn(45);
        //intake.intake(5);

//======================= PARK =========================================
        //drivetrain.turn(45);
        //drivetrain.move(60);
        //drivetrain.turn(-90);
        //drivetrain.move(90);
        //drivetrain.turn(-90);
        //drivetrain.move(52);
        //drivetrain.turn(90);
        //TODO: add odom retract
        //drivetrain.forwards(48);
        //drivetrain.turn(-90)
        //drivetrain.forwards(12);
//======================= COLLECT MINERAL AND TURN =========================================
        //intake.intake(5);
        //drivetrain.turn(-90);



    }
}
