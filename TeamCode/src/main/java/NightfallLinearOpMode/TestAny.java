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
    private Intake intake;
    private int pos;

    public static double distance = 24;
    public static double kpForwards = 1;
    public static double timeout = 10;
    public static int heading = 0;
    
    public static double angle = 90;
    public static double kpTurn5 = 0.2386; //PID
    public static double kdTurn5 = 0.1; //PID
    public static double kpTurn45 = 0.15;
    public static double kdTurn45 = 0;
    public static double timeoutTurn = 1.5;
    public static double kpTurn90 = 0.176;
    public static double kdTurn90 = 0.07;
    public static double kpTurn180 = 0.129;
    public static double kdTurn180 = 0.00;


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

        waitForStart();

        if (isStopRequested()) return;
        while (!isStopRequested()) {
           drivetrain.turnPD(50, kpTurn45, kdTurn45, 1.5);
           sleep(1000);
           //telemetry.addData("angle", drivetrain.getGyroYaw());
          //  drivetrain.arcTurnPD(90, kpTurn90, kdTurn90,5);
        }






    }
}
