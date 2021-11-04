package NightfallLinearOpMode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.Roadrunner.trajectorysequence.TrajectorySequence;

import NightfallLinearOpMode.Intake;
import NightfallLinearOpMode.Drivetrain;
import NightfallLinearOpMode.Lift;
import NightfallLinearOpMode.Vision;

@Autonomous(name = "Test", group = "test")
@Config
public class TestAny extends LinearOpMode {

    private Drivetrain drivetrain;
    private Vision vision;
    private Lift lift;
  //  private Intake intake;
    private String pos;

    public static double distance = 24;
    public static double kpForwards = 0.4;
    public static double timeout = 10;
    public static int heading = 0;
    
    public static double angle = 90;
    public static double kpTurn = 0.5;
    public static double timeoutTurn = 1;
    public static double kdTurn = 1;


    @Override
    public void runOpMode() throws InterruptedException {

        drivetrain = new Drivetrain(this);
        vision = new Vision(this);
        lift = new Lift(this);
       // intake = new Intake(this);

        while (!isStarted()) {
            pos = vision.getPosNewMethod();
            telemetry.addData("team marker pos: ", pos);
            telemetry.update();
        }
        telemetry.addData("team marker pos: ", pos);
        telemetry.update();

        //trajectories/pose go here

        waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested()) {
            drivetrain.gyroEncoderInch(kpForwards, distance, timeout, heading);
            sleep(2000);
            drivetrain.gyroEncoderInch(-kpForwards, distance, timeout, heading);
            sleep(2000);
       //     drivetrain.turnPD(angle, kpTurn, kdTurn, timeoutTurn);
        }






    }
}
