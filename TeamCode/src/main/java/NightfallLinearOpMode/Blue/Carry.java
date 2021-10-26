package NightfallLinearOpMode.Blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import NightfallLinearOpMode.Intake;
import NightfallLinearOpMode.Drivetrain;
import NightfallLinearOpMode.Lift;
import NightfallLinearOpMode.Vision;

@Autonomous(name = "Blue Carry", group = "LinearOpMode")
public class Carry extends LinearOpMode {

    //private Drivetrain drivetrain;
    private Vision vision;
    //private Lift lift;
    //private Intake intake;
    private String pos;


    @Override
    public void runOpMode() throws InterruptedException {

        //drivetrain = new Drivetrain(this);
        vision = new Vision(this);
        //lift = new Lift(this);
        //intake = new Intake(this);

        while (!isStarted()) {
            pos = vision.getTeamMarkerPos();
            telemetry.addData("team marker pos: ", pos);
            telemetry.update();
        }
        telemetry.addData("team marker pos: ", pos);
        telemetry.update();

        //trajectories/pose go here

        idle();





    }
}
