package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
public class HelloWorld extends LinearOpMode {
    //private Gyroscope imu;
    private DcMotor motorTestFR;
    private DcMotor motorTestFL;
    private DcMotor motorTestBL;
    private DcMotor motorTestBR;
    //private DigitalChannel digitalTouch;
    //private DistanceSensor sensorColorRange;
    //private Servo servoTest;
    public double FL;
    public double FR;
    public double BL;
    public double BR;
    public void doMotorPowers(double Y, double X, double R) {
        // 计算前后左右的移动功率
        FL = 0.5*(Y + X); // 前左电机
        FR = 0.5*(Y - X); // 前右电机
        BL = 0.5*(Y - X); // 后左电机
        BR = 0.5*(Y + X); // 后右电机

        // 计算旋转控制
        double rotatePower = R; // 旋转控制
        FL -= 0.5*rotatePower; // 旋转影响前左电机
        FR += 0.5*rotatePower; // 旋转影响前右电机
        BL -= 0.5*rotatePower; // 旋转影响后左电机
        BR += 0.5*rotatePower; // 旋转影响后右电机

        // 如果需要，可以将结果标准化（确保功率在 -1 到 1 之间）
        double maxPower = Math.max(Math.abs(FL), Math.max(Math.abs(FR), Math.max(Math.abs(BL), Math.abs(BR))));

        if (maxPower > 1) {
            FL /= maxPower;
            FR /= maxPower;
            BL /= maxPower;
            BR /= maxPower;
        }

        // 打印电机功率输出（你可以把这些值传给电机控制系统）
        /*System.out.println("FL: " + FL);
        System.out.println("FR: " + FR);
        System.out.println("BL: " + BL);
        System.out.println("BR: " + BR);*/
    }

    @Override
    public void runOpMode() {
//        imu = hardwareMap.get(Gyroscope.class, "imu");
        motorTestFR = hardwareMap.get(DcMotor.class, "rightFront");
        motorTestFL = hardwareMap.get(DcMotor.class, "leftFront");
        motorTestBL = hardwareMap.get(DcMotor.class, "rightRear");
        motorTestBR = hardwareMap.get(DcMotor.class, "leftRear");
//        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
//        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        //servoTest = hardwareMap.get(Servo.class, "Servo_0");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        double tgtMotorPower = 0;
        while (opModeIsActive()) {
            /*telemetry.addData("Status", "Running");
            tgtMotorPower = -this.gamepad1.left_stick_y;
            motorTestFR.setPower(-tgtMotorPower);
            motorTestFL.setPower(tgtMotorPower);
            /*if(gamepad1.y) {
                servoTest.setPosition(0);
            }else if(gamepad1.b){
                servoTest.setPosition(0.5);
            }else if(gamepad1.a){
                servoTest.setPosition(1);
            }*/
            double rotationPower=this.gamepad1.left_trigger-this.gamepad1.right_trigger;
            doMotorPowers(this.gamepad1.left_stick_y,this.gamepad1.right_stick_x,rotationPower);
            motorTestFL.setPower(FL);
            motorTestFR.setPower(FR);
            motorTestBL.setPower(BL);
            motorTestBR.setPower(BR);
            telemetry.addData("Target Power", tgtMotorPower);
            telemetry.addData("Left Power", motorTestFL.getPower());
            telemetry.addData("Right Power", motorTestFR.getPower());
            //telemetry.addData("Servo Position", servoTest.getPosition());
            telemetry.addData("Status", "Running");
            telemetry.update();


        }
    }
}