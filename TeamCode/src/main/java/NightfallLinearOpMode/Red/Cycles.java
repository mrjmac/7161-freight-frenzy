package NightfallLinearOpMode.Red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import NightfallLinearOpMode.Intake;
import NightfallLinearOpMode.Drivetrain;
import NightfallLinearOpMode.Lift;
import NightfallLinearOpMode.Vision;

@Autonomous(name = "Red Cycles", group = "red")
public class Cycles extends LinearOpMode {

    private Drivetrain drivetrain;
    private Vision vision;
    private Lift lift;
    private Intake intake;
    private int pos;

    public static double kpTurn5 = .147; //PID
    public static double kdTurn5 = 0.48; //PID
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

        drivetrain.gyroEncoderInch(1, 15, 1, 0);
        drivetrain.turnPD(12.5, kpTurn5, kdTurn5, 1);
        drivetrain.gyroEncoderInch(1, 12, 1, 12.5);
        drivetrain.gyroEncoderInch(-1, 17.5, 1, 12.5);
        //  }
        drivetrain.turnPD(30, kpTurn45, kdTurn45, 2);
        drivetrain.gyroEncoderInch(1, 16, 1.5, -30);
        lift.setLift(liftHeight, 1);
        //   drivetrain.gyroEncoderInch(-1, 10, 1, 0);
        //   drivetrain.turnPD(90, kpTurn45, kdTurn45, 2);
        drivetrain.arcInchStraight(-1, 56, 2.5, -90);
        //intake.goatIntake(.85);
        drivetrain.turnPD(-52.5, kpTurn45, kdTurn45, 2);
        drivetrain.getElementDrive(-.95, 8, 1.5, -52.5, .85, 2);
        drivetrain.arcInch(.95, 62, 3, -90);
        drivetrain.turnPD(-25, kpTurn45, kdTurn45, 2);
        drivetrain.gyroEncoderInch(1, 6, 1, -25);
        lift.setLift(4, 1);
        drivetrain.turnPD(-90, kpTurn45, kdTurn90, 90);
        drivetrain.arcInchStraight(-1, 40, 2.5, -90);
        drivetrain.turnPD(-37.5, kpTurn45, kdTurn45, 2);
        drivetrain.getElementDrive(-.95, 10, 1.5, -37.5, .85, 2);
        drivetrain.arcInchStraight(.95, 62, 3, -90);
        drivetrain.turnPD(-25, kpTurn45, kdTurn45, 2);
        drivetrain.gyroEncoderInch(1,7, 1, -25);
        lift.setLift(4, 1);
        drivetrain.arcInch(-1, 54, 3.5, -100);
    }
}
