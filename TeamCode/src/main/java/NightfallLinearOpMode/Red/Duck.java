package NightfallLinearOpMode.Red;

import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import NightfallLinearOpMode.Intake;
import NightfallLinearOpMode.Drivetrain;
import NightfallLinearOpMode.Lift;
import NightfallLinearOpMode.Vision;

@Autonomous(name = "Red Duck", group = "red")
public class Duck extends LinearOpMode {

    private Drivetrain drivetrain;
    private Vision vision;
    private Lift lift;
    private Intake intake;
    private String pos;

    public static double kpTurn5 = 0.2386; //PID
    public static double kdTurn5 = 0.1; //PID
    public static double kpTurn45 = 0.2386;
    public static double kdTurn45 = 0.1;
    public static double timeoutTurn = 1.5;
    public static double kpTurn90 = 0.176;
    public static double kdTurn90 = 0.07;
    public static double kpTurn180 = 0.129;
    public static double kdTurn180 = 0.00;

    private int liftHeight = 3;
    //state machine
    //start pose

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
        liftHeight = Integer.parseInt(pos);

        // PICK UP CAP AND SCORE PRE LOADED
        drivetrain.gyroEncoderInch(1, 24, 2, 0);
        lift.capDown();
        intake.intakeDown();
        sleep(300);
        lift.capUp();
        drivetrain.turnPD(45, kpTurn45, kdTurn45, 1.5);
        drivetrain.gyroEncoderInch(1, 8, 2, 45);
        lift.setLift(liftHeight, 1);
        // CAROUSEL, PICK UP DUCK AND SCORE
        drivetrain.gyroEncoderInch(-1, 35, 3, 45);
        drivetrain.duckStart(1);
        sleep(1500);
        drivetrain.duckStop();
        intake.intakePow(1);
        drivetrain.turnPD(0, kpTurn45, kdTurn45, 1.5);
        drivetrain.gyroEncoderInch(-.3, 5, 3, 0);
        drivetrain.gyroEncoderInch(.3, 5, 3, 0);
        intake.intakeStop();
        drivetrain.turnPD(45, kpTurn45, kdTurn45, 1.5);
        drivetrain.gyroEncoderInch(1, 68, 3, 45);
        lift.setLift(3, 1);
        // ARCTURN TOWARDS ALLIANCE PRELOAD AND SCORE
        drivetrain.gyroEncoderInch(-.5, 8, 1.5, 45);
        drivetrain.turnPD(0, kpTurn45, kdTurn45, 1.5);
        intake.intakePow(1);
        drivetrain.arcTurnPD(-90, kpTurn90, kdTurn90, 3);
        sleep(500);
        drivetrain.turnPD(-25, kpTurn90, kdTurn90, 1.5);
        intake.intakeStop();
        drivetrain.gyroEncoderInch(1, 35, 3, -25);
        lift.setLift(3, 1);
        // PARK INSIDE CRATER
        drivetrain.turnPD(90, kpTurn90, kdTurn90, 1.5);
        drivetrain.gyroEncoderInch(1, 50, 2.5, 90);





    }
}
