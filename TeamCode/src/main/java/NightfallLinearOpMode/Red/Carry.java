package NightfallLinearOpMode.Red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import NightfallLinearOpMode.Intake;
import NightfallLinearOpMode.Drivetrain;
import NightfallLinearOpMode.Lift;
import NightfallLinearOpMode.Vision;

@Autonomous(name = "Red Carry", group = "red")
public class Carry extends LinearOpMode {

    private Drivetrain drivetrain;
    private Vision vision;
    private Lift lift;
    private Intake intake;
    private int pos;

    public static double kpTurn5 = 0.2386; //PID
    public static double kdTurn5 = 0.1; //PID
    public static double kpTurn45 = 0.2386;
    public static double kdTurn45 = 0.06;
    public static double timeoutTurn = 1.5;
    public static double kpTurn90 = 0.176;
    public static double kdTurn90 = 0.07;
    public static double kpTurn180 = 0.129;
    public static double kdTurn180 = 0.00;

    private int liftHeight = 3;

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
        liftHeight = pos;

        // PICK UP CAP AND SCORE PRE LOADED(red)
        drivetrain.gyroEncoderInch(1, 18, 2, 0);
        lift.capDown();
        //intake.intakeDown();
        sleep(300);
        lift.capUp();
        drivetrain.turnPD(50, kpTurn45, kdTurn45, 1.5);
        drivetrain.gyroEncoderInch(1, 16, 2, 50);
        lift.setLift(liftHeight, 1);
        // CAROUSEL, PICK UP DUCK AND SCORE
        drivetrain.gyroEncoderInch(-.5, 45.5, 3, 60);
        drivetrain.duckStart(-1);
        sleep(5000);
        drivetrain.duckStop();
        drivetrain.gyroEncoderInch(.5, 26, 3, 0);




    }
}
