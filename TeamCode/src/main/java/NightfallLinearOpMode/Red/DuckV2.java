package NightfallLinearOpMode.Red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import NightfallLinearOpMode.Intake;
import NightfallLinearOpMode.Drivetrain;
import NightfallLinearOpMode.Lift;
import NightfallLinearOpMode.Vision;

@Autonomous(name = "Red DuckV2", group = "blue")
public class DuckV2 extends LinearOpMode {

    private Drivetrain drivetrain;
    private Vision vision;
    private Lift lift;
    private Intake intake;
    private int pos;

    public static double kpTurn5 = .155; //PID
    public static double kdTurn5 = 0.435; //PID
    public static double kpTurn45 = 0.119;
    public static double kdTurn45 = 0.49;
    public static double timeoutTurn = 1.5;
    public static double kpTurn90 = 0.09;
    public static double kdTurn90 = 0.31;
    public static double kpTurn180 = 0.06;
    public static double kdTurn180 = 0.25;

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
    /*    drivetrain.gyroEncoderInch(.5, 38, 2, 0);
        drivetrain.gyroEncoderInch(-1, 8, 2,0);

     */
        drivetrain.gyroEncoderInch(1, 15, 1.5, 0);
        drivetrain.turnPD(-15, kpTurn5, kdTurn5, 2);
        drivetrain.gyroEncoderInch(.3, 39, 1.5, -15);
        drivetrain.gyroEncoderInch(-1, 21, 1.5, -15);
        //   lift.capDown();
        //intake.intakeDown();
        //    sleep(300);
        //     lift.capUp();
        drivetrain.turnPD(46, kpTurn45, kdTurn45, 1.5);
        drivetrain.gyroEncoderInch(1, 15, 2, 46);
        lift.setLift(liftHeight, 1);
        // CAROUSEL, PICK UP DUCK AND SCORE
        drivetrain.gyroEncoderInch(-.5, 34, 2.7, 55);
        drivetrain.turnPD(22.5, kpTurn45, kdTurn45, 2);
        drivetrain.gyroEncoderInch(-.5, 15.5, 1, 25);
        drivetrain.duckStart(-1);
        sleep(4500);
        intake.goatIntake(.7);
        //  drivetrain.duckStart(-.2);
        //sleep(800);
        drivetrain.duckStop();
        sleep(1000);
        intake.goatIntake(0);
        drivetrain.gyroEncoderInch(1, 8, 1.3, 20);
        //intake.goatIntake(0);
        drivetrain.turnPD(0, kpTurn5, kdTurn5, 1);
        intake.goatIntake(.7);
        drivetrain.gyroEncoderInch(-1, 9, 1, 0);
        drivetrain.gyroEncoderInch(1, 20, 2, 0);
        intake.goatIntake(0);
        drivetrain.turnPD(70, kpTurn45, kpTurn45, 2);
        drivetrain.gyroEncoderInch(1, 24, 1.5, 70);
        lift.setLift(4, 1);
        //drivetrain.turnPD(-90, kpTurn45, kdTurn45, 2);
        drivetrain.gyroEncoderInch(-1, 31, 3, 80);
        drivetrain.turnPD(90, kpTurn5, kdTurn5, 1);
    }
}
